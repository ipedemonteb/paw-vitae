import React, { useMemo, useState, useEffect, useCallback } from "react";
import { Card } from "@/components/ui/card.tsx"
import DoctorProfileCard from "@/components/DoctorProfileCard.tsx";
import { Stethoscope, Hospital, CalendarDays } from "lucide-react";
import { Select, SelectContent, SelectGroup, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select.tsx";
import { DatePicker } from "@/components/ui/date-picker.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Textarea } from "@/components/ui/textarea.tsx";
import { UploadFiles } from "@/components/UploadFiles.tsx";
import { Label } from "@/components/ui/label.tsx";
import { Checkbox } from "@/components/ui/checkbox.tsx";
import { useTranslation } from "react-i18next";
import {
    useDoctor,
    useDoctorSpecialties,
    useDoctorUnavailability
} from "@/hooks/useDoctors.ts";
import {
    useDoctorAvailability,
    useDoctorOffices,
    useDoctorOfficesSpecialties
} from "@/hooks/useOffices.ts";
import { useSpecialtiesByUrl } from "@/hooks/useSpecialties.ts";
import {useAppointments, useBookAppointmentMutation} from "@/hooks/useAppointments.ts";
import { useOccupiedSlots } from "@/hooks/useSlots.ts";
import { useAuth } from "@/hooks/useAuth.ts";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "sonner";
import type { OfficeDTO, OfficeSpecialtyDTO } from "@/data/offices.ts";
import type { SpecialtyDTO } from "@/data/specialties.ts";
import {
    startOfDay,
    parseISO,
    isSameDay,
    addDays,
    format,
    isBefore,
    addMinutes,
    isWithinInterval,
    endOfDay
} from "date-fns";
import { useNeighborhood } from "@/hooks/useNeighborhoods.ts";
import GenericError from "@/pages/GenericError.tsx";
import {LoadingFullPageComponent} from "@/components/LoadingFullPageComponent.tsx";
import {useDelayedBoolean} from "@/utils/queryUtils.ts";
import {Spinner} from "@/components/ui/spinner.tsx";
import {RefetchComponent} from "@/components/ui/refetch.tsx";
import {cn} from "@/lib/utils.ts";

function buildSpecialtyToOfficesMapFromLinks(
    offices: OfficeDTO[],
    officeSpecialtyLinks: OfficeSpecialtyDTO[][],
    specialtyBySelf: Map<string, SpecialtyDTO>
) {
    const map = new Map<string, { specialty: SpecialtyDTO; offices: OfficeDTO[] }>();
    offices.forEach((office, i) => {
        const links = officeSpecialtyLinks[i] ?? [];
        links.forEach((link) => {
            const self = link.specialty;
            const specialty = specialtyBySelf.get(self) ?? { self, name: self };
            const current = map.get(self);
            if (!current) map.set(self, { specialty, offices: [office] });
            else current.offices.push(office);
        });
    });
    return map;
}

function getFilteredOffices(
    offices: OfficeDTO[] | undefined,
    selectedSpecialty: string | null,
    specialtyToOffices: Map<string, { specialty: SpecialtyDTO; offices: OfficeDTO[] }>
): OfficeDTO[] {
    if (!offices) return [];
    if (!selectedSpecialty) return offices;
    return specialtyToOffices.get(selectedSpecialty)?.offices ?? [];
}

function isOfficeValid(offices: OfficeDTO[], selectedOffice: string | null) {
    if (!selectedOffice) return true;
    return offices.some((o) => o.self === selectedOffice);
}

const appointmentBackground = "bg-[var(--background-light)] flex justify-center items-start min-h-screen";
const cardContainer = "mt-36 px-5 mx-auto max-w-6xl w-full mb-8";
const appointmentContainer = "p-0 pb-8";
const appointmentHeader = "flex flex-col items-center px-8 text-center py-8 rounded-t-xl gap-3 bg-[linear-gradient(135deg,var(--background-light)_0%,var(--landing-light)_100%)]";
const appointmentTitle = "font-bold text-4xl text-center text-[var(--text-color)]";
const appointmentContent = "flex flex-col px-8 gap-4";
const selectorTitle = "text-[var(--text-color)] text-md font-[500]";
const selectorsContainer = "flex flex-col md:flex-row gap-6";
const dateCard = "flex flex-col";
const dateContainer = "flex flex-col md:items-center md:flex-row gap-5 md:gap-0";
const dateUpperContainer = "flex flex-row items-center";
const availableTimesFormat = "flex flex-wrap items-center px-5 gap-2";
const timeButton = "bg-white text-[var(--primary-color)] border border-[var(--primary-color)] hover:bg-[var(--primary-color)] hover:text-white cursor-pointer data-[selected=true]:bg-[var(--primary-color)] data-[selected=true]:text-white data-[selected=true]:border-[var(--primary-color)]";
const optionalsContainer = "flex flex-col gap-8 mt-4";
const bookContainer = "flex flex-col items-center w-full";
const bookButton = "mt-6 py-4 w-xs bg-[var(--primary-color)] hover:bg-[var(--primary-dark)] cursor-pointer";

function Appointment() {
    const { id: doctorId } = useParams<{ id: string }>();
    const { t } = useTranslation();
    const navigate = useNavigate();
    const auth = useAuth();
    const patientId = auth.userId

    const { mutate: bookAppointment, isPending: isBooking } = useBookAppointmentMutation();

    const [selectedDate, setSelectedDate] = useState<Date | undefined>(undefined);
    const [selectedTime, setSelectedTime] = useState<string | null>(null);
    const [selectedSpecialty, setSelectedSpecialty] = useState<string | null>(null);
    const [selectedOffice, setSelectedOffice] = useState<string | null>(null);

    const [reason, setReason] = useState("");
    const [allowFullHistory, setAllowFullHistory] = useState(true);
    const [files, setFiles] = useState<File[]>([]);

    const today = useMemo(() => startOfDay(new Date()), []);
    const maxDate = useMemo(() => addDays(today, 30), [today]);
    const fromStr = useMemo(() => format(today, 'yyyy-MM-dd'), [today]);
    const toStr = useMemo(() => format(maxDate, 'yyyy-MM-dd'), [maxDate]);

    const { data: doctor, isLoading: loadingDoctor, isError: errorDoctor, error: doctorError } = useDoctor(doctorId);
    const { data: offices, isLoading: loadingOffices, isFetching: fetchingOffices, isError: errorOffices, refetch: refetchOffices } = useDoctorOffices(doctor?.offices, { status: "active" });
    const { data: officeSpecialties, isLoading: isLoadingOfficeSpecialties, isFetching: fetchingOfficeSpecialties, isError: errorOfficeSpecialties, refetch: refetchOfficeSpecialties } = useDoctorOfficesSpecialties(offices);
    const {
        data: specialtyRefs,
        isLoading: loadingSpecialtyRefs,
        isFetching: fetchingSpecialtyRefs,
        isError: errorSpecialtyRefs,
        refetch: refetchSpecialtyRefs
    } = useDoctorSpecialties(doctor?.specialties);

    const specialtyUrls = specialtyRefs?.map(ref => ref.self);

    const {
        data: specialtyQueries,
        isLoading: loadingSpecialtyDetails,
        isFetching: fetchingSpecialtyDetails,
        isError: errorSpecialtyDetails,
        refetch: refetchSpecialtyDetails
    } = useSpecialtiesByUrl(specialtyUrls);

    const isLoadingDoctorSpecialties = loadingSpecialtyRefs || loadingSpecialtyDetails;
    const fetchingDoctorSpecialties = fetchingSpecialtyRefs || fetchingSpecialtyDetails;
    const errorDoctorSpecialties = errorSpecialtyRefs || errorSpecialtyDetails;
    const refetchDoctorSpecialties = () => { refetchSpecialtyRefs(); refetchSpecialtyDetails(); };

    const doctorSpecialties = (specialtyQueries ?? []).map(q => q.data).filter((d): d is SpecialtyDTO => !!d);

    const { data: appointments, isLoading: loadingAppointments, isFetching: fetchingAppointments, isError: errorAppointments, refetch: refetchAppointments } = useAppointments({ userId: patientId, collection: "upcoming", pageSize: 400, page: 1 });
    const { data: allAvailability, isLoading: loadingAvailability, isFetching: fetchingAvailability, isError: errorAvailability, refetch: refetchAvailability } = useDoctorAvailability(doctorId);
    const { data: occupiedSlots, isLoading: loadingSlots, isFetching: fetchingSlots, isError: errorSlots, refetch: refetchSlots } = useOccupiedSlots(fromStr, toStr, doctorId);
    const { data: unavailabilityPage, isLoading: loadingUnavailability, isFetching: fetchingUnavailability, isError: errorUnavailability, refetch: refetchUnavailability } = useDoctorUnavailability(doctor?.unavailability, { from: fromStr, to: toStr });

    const unavailableRanges = unavailabilityPage?.data || [];
    const isLoading = loadingDoctor || loadingOffices || loadingSlots || loadingUnavailability || isLoadingOfficeSpecialties || isLoadingDoctorSpecialties || loadingAvailability || loadingAppointments;
    const isBookingError = errorOffices || errorOfficeSpecialties || errorDoctorSpecialties || errorAvailability || errorSlots || errorUnavailability || errorAppointments;
    const isBookingFetching = fetchingOffices || fetchingOfficeSpecialties || fetchingDoctorSpecialties || fetchingAvailability || fetchingSlots || fetchingUnavailability || fetchingAppointments;

    const refetchBookingData = () => { refetchOffices(); refetchOfficeSpecialties(); refetchDoctorSpecialties(); refetchAvailability(); refetchSlots(); refetchUnavailability(); refetchAppointments(); };


    const specialtyBySelf = useMemo(() => {
        const m = new Map<string, SpecialtyDTO>();
        (doctorSpecialties ?? []).forEach((s) => m.set(s.self, s));
        return m;
    }, [doctorSpecialties]);

    const specialtyToOffices = useMemo(() => {
        if (!offices) return new Map<string, { specialty: SpecialtyDTO; offices: OfficeDTO[] }>();
        return buildSpecialtyToOfficesMapFromLinks(offices, officeSpecialties, specialtyBySelf);
    }, [offices, officeSpecialties, specialtyBySelf]);

    const specialtyOptions = useMemo(
        () => Array.from(specialtyToOffices.values()).map((x) => x.specialty),
        [specialtyToOffices]
    );

    const filteredOffices = useMemo(
        () => getFilteredOffices(offices, selectedSpecialty, specialtyToOffices),
        [offices, selectedSpecialty, specialtyToOffices]
    );

    useEffect(() => {
        if (!isOfficeValid(filteredOffices, selectedOffice)) setSelectedOffice(null);
    }, [filteredOffices, selectedOffice]);


    const slotsForSelectedOffice = useMemo(() => {
        if (!selectedOffice || !allAvailability || !selectedDate || !occupiedSlots) return [];

        const dateStr = format(selectedDate, 'yyyy-MM-dd');

        const dayOfWeek = selectedDate.getDay();
        const dailyRules = allAvailability.filter(r =>
            r.office === selectedOffice && r.dayOfWeek === dayOfWeek
        );

        const generatedSlots: { startTime: string }[] = [];

        dailyRules.forEach(rule => {
            let current = parseISO(`${dateStr}T${rule.startTime}`);
            const end = parseISO(`${dateStr}T${rule.endTime}`);

            while (isBefore(current, end)) {
                const currentTimeShort = format(current, 'HH:mm');
                const currentTimeFull = format(current, 'HH:mm:00');

                const isOccupied = occupiedSlots.some(occ => {
                    const occTimeShort = occ.startTime.substring(0, 5);
                    return occ.date === dateStr && occTimeShort === currentTimeShort;
                });

                const priorCommitment = appointments?.data?.some(a => {
                    if (a.status === 'cancelado') return false;

                    const aDateObj = parseISO(a.date);
                    const aDateStr = format(aDateObj, 'yyyy-MM-dd');
                    const aTimeShort = format(aDateObj, 'HH:mm');
                    return aDateStr === dateStr && aTimeShort === currentTimeShort;
                }) ?? false;

                if (!isOccupied && !priorCommitment) {
                    generatedSlots.push({ startTime: currentTimeFull });
                }

                current = addMinutes(current, 60);
            }
        });

        return generatedSlots.sort((a, b) => a.startTime.localeCompare(b.startTime));

    }, [selectedOffice, allAvailability, selectedDate, occupiedSlots, appointments]);


    const slotsForDay = useMemo(() => {
        if (!selectedDate) return [];
        let slots = slotsForSelectedOffice;

        if (isSameDay(selectedDate, new Date())) {
            const now = new Date();
            const currentTime = format(now, 'HH:mm:00');
            slots = slots.filter(slot => slot.startTime > currentTime);
        }
        return slots;
    }, [selectedDate, slotsForSelectedOffice]);


    const isDateSelectable = useCallback((d: Date) => {
        if (d < startOfDay(new Date()) || d > maxDate) return false;

        if (!selectedOffice || !allAvailability || !unavailableRanges || !occupiedSlots) return false;
        const dayOfWeek = d.getDay();

        const isOnLeave = unavailableRanges.some(range => {
            return isWithinInterval(d, {
                start: startOfDay(parseISO(range.startDate)),
                end: endOfDay(parseISO(range.endDate))
            })
        })

        const dateStr = format(d, 'yyyy-MM-dd')

        const isFullyBooked = occupiedSlots
            .filter(o => startOfDay(parseISO(o.date)).getTime() === startOfDay(d).getTime())
            .length === allAvailability.filter(a => a.dayOfWeek === d.getDay() && a.office === selectedOffice).reduce((sum, rule) => {
            const start = parseISO(`${dateStr}T${rule.startTime}`);
            const end = parseISO(`${dateStr}T${rule.endTime}`);
            const slots = end.getHours() - start.getHours();
            return sum + slots;
        }, 0)

        return allAvailability.some(r => r.office === selectedOffice && r.dayOfWeek === dayOfWeek && !isOnLeave && !isFullyBooked);
    }, [maxDate, selectedOffice, allAvailability, unavailableRanges]);


    useEffect(() => {
        setSelectedDate(undefined);
        setSelectedTime(null);
    }, [selectedOffice]);

    useEffect(() => {
        setSelectedTime(null);
    }, [selectedDate]);


    const handleBook = () => {
        if (!patientId) {
            toast.error(t("error.error"), { description: "Debe iniciar sesión para reservar." });
            navigate("/login");
            return;
        }

        if (!selectedTime || !selectedSpecialty || !selectedOffice || !selectedDate) {
            toast.error(t("error.error"), { description: t("register.errors.missing_fields", "Complete todos los campos") });
            return;
        }
        if (reason.length > 250) {
            toast.error(t("error.error"), { description: t("appointment.validation.reason_too_long", "El motivo no puede superar los 250 caracteres") });
            return;
        }
        const [hours] = selectedTime.split(":").map(Number);

        const appointmentForm = {
            appointmentDate: format(selectedDate, 'yyyy-MM-dd'),
            appointmentHour: hours,
            patientId: patientId!,
            doctorId: doctorId!,
            specialtyId: selectedSpecialty.split("/").pop()!,
            officeId: selectedOffice.split("/").pop()!,
            reason: reason,
            allowFullHistory: allowFullHistory
        };

        bookAppointment({ form: appointmentForm, files: files}, {
            onSuccess: (newId) => {
                toast.success(t("success.appointment_created", "Turno reservado exitosamente"));
                navigate(`/appointment/${newId}/confirmation`);
            },
            onError: (err) => {
                if (err.response?.status === 409) {
                    toast.error(t("error.slot_taken", "El turno acaba de ser ocupado. Por favor elija otro horario."));
                    setSelectedTime(null);
                } else {
                    toast.error(t("appointment.booking.error.failed"), {
                        description: t("appointment.booking.error.try-again")
                    });
                }
            }
        });
    };

    if (useDelayedBoolean(isLoading)) {
        return (
            <LoadingFullPageComponent/>
        );
    }

    if (errorDoctor || !doctor) {
        const status = doctorError ? (doctorError as any).response?.status : 404;
        return <GenericError code={status} />;
    }

    return (
        <div className={appointmentBackground}>
            <div className={cardContainer}>
                <Card className={appointmentContainer}>
                    <div className={appointmentHeader}>
                        <h1 className={appointmentTitle}>{t("appointment.booking.header.title")}</h1>
                        <p>{t("appointment.booking.header.subtitle")}</p>
                    </div>
                    <div className={appointmentContent}>
                        <DoctorProfileCard doctor={doctor}/>
                        {isBookingError ?
                            <RefetchData isFetching={isBookingFetching} onRefetch={refetchBookingData} errorText={t("appointment.booking.error.refetch")}/>
                            :
                            <>
                                <div className={selectorsContainer}>
                                    <SpecialtySelector
                                        options={specialtyOptions}
                                        selectedSpecialty={selectedSpecialty}
                                        setSelectedSpecialty={setSelectedSpecialty}/>
                                    <OfficeSelector
                                        options={filteredOffices}
                                        selectedOffice={selectedOffice}
                                        setSelectedOffice={setSelectedOffice}
                                        disabled={!selectedSpecialty}
                                    />
                                </div>
                                <DateSelector
                                    selectedDate={selectedDate}
                                    setSelectedDate={setSelectedDate}
                                    selectedTime={selectedTime}
                                    setSelectedTime={setSelectedTime}
                                    availableSlots={slotsForDay}
                                    disabled={!selectedOffice}
                                    isDateDisabled={(d) => !isDateSelectable(d)}
                                    maxDate={maxDate}
                                />

                            </>
                        }
                        <div className={optionalsContainer}>
                            <ReasonInput value={reason} onChange={setReason} />
                            <FilesUpload onFilesChange={setFiles} />
                            <MedicalHistory checked={allowFullHistory} onCheckedChange={setAllowFullHistory} />
                        </div>
                        <div className={bookContainer}>
                            <Button
                                className={bookButton}
                                onClick={handleBook}
                                disabled={isBooking || !selectedTime}
                            >
                                {isBooking ? (
                                    <>
                                        <Spinner className="mr-2 h-4 w-4" />
                                        {t("saving")}
                                    </>
                                ) : (
                                    t("appointment.booking.book")
                                )}
                            </Button>
                        </div>
                    </div>
                </Card>
            </div>
        </div>
    )
}

function DateSelector({
                          selectedDate,
                          setSelectedDate,
                          selectedTime,
                          setSelectedTime,
                          availableSlots,
                          disabled,
                          isDateDisabled,
                          maxDate
                      }:{
    selectedDate: Date | undefined
    setSelectedDate: (date: Date | undefined) => void
    selectedTime: string | null
    setSelectedTime: React.Dispatch<React.SetStateAction<string | null>>;
    availableSlots: { startTime: string }[];
    disabled?: boolean;
    isDateDisabled?: (date: Date) => boolean;
    maxDate: Date;
}) {
    const { t } = useTranslation();
    const locale = useMemo(() => (typeof navigator === "undefined" ? "en-US" : navigator.language || "en-US"), []);

    const formattedConfirmation = useMemo(() => {
        if (!selectedDate || !selectedTime) return null;

        const [hh, mm] = selectedTime.split(":").map(Number);
        const d = new Date(selectedDate);
        d.setHours(hh, mm, 0, 0);

        let datePart = new Intl.DateTimeFormat(locale, {
            weekday: "long", year: "numeric", month: "long", day: "numeric",
        }).format(d);
        datePart = datePart.charAt(0).toUpperCase() + datePart.slice(1);

        const timePart = new Intl.DateTimeFormat(locale, { hour: "2-digit", minute: "2-digit" }).format(d);
        return `${datePart} at ${timePart}`;
    }, [selectedDate, selectedTime, locale]);

    return (
        <Card className={dateCard}>
            <div className={dateContainer}>
                <div className={dateUpperContainer}>
                    <div className={iconContainer}>
                        <CalendarDays className={icon}/>
                    </div>
                    <div className={selectorContent}>
                        <p className={selectorTitle}>{t("appointment.booking.date")}</p>
                        <DatePicker
                            value={selectedDate}
                            onChange={setSelectedDate}
                            disabled={disabled}
                            isDateDisabled={isDateDisabled}
                            fromDate={new Date()}
                            toDate={maxDate}
                        />
                    </div>
                </div>
                {selectedDate ? (
                    <div className={availableTimesFormat}>
                        {availableSlots.length === 0 ? (
                            <p className="px-5 text-sm text-(--text-light)">
                                {t("appointment.booking.no-times")}
                            </p>
                        ) : (
                            availableSlots.map((slot) => {
                                const displayTime = slot.startTime.substring(0, 5);
                                return (
                                    <Button
                                        key={slot.startTime}
                                        className={timeButton}
                                        data-selected={selectedTime === slot.startTime}
                                        onClick={() => setSelectedTime((prev) => (prev === slot.startTime ? null : slot.startTime))}
                                        disabled={disabled}
                                    >
                                        {displayTime}
                                    </Button>
                                )
                            })
                        )}
                    </div>
                ) : null}
            </div>
            {formattedConfirmation ? <Confirmation text={formattedConfirmation} /> : null}
        </Card>
    );
}

const selectorCard = "flex flex-row items-center w-full gap-0";
const iconContainer = "flex items-center bg-[var(--primary-bg)] rounded-full p-5 text-[var(--primary-color)] mx-5";
const icon = "w-8 h-8";
const selectorContent = "flex flex-col gap-1 min-w-56";
const selectorButton = "cursor-pointer";

function OfficeItem({ office }: { office: OfficeDTO }) {
    const { data: neighborhood } = useNeighborhood(office.neighborhood);

    return (
        <span className="flex items-center gap-2">
            <span>{office.name}</span>
            {neighborhood && (
                <span className="text-gray-500 text-xs">({neighborhood.name})</span>
            )}
        </span>
    );
}

function OfficeSelector({
                            options,
                            selectedOffice,
                            setSelectedOffice,
                            disabled,
                        }: {
    options: OfficeDTO[];
    selectedOffice: string | null;
    setSelectedOffice: React.Dispatch<React.SetStateAction<string | null>>;
    disabled?: boolean;
}) {
    const { t } = useTranslation();
    return (
        <Card className={selectorCard}>
            <div className={iconContainer}>
                <Hospital className={icon} />
            </div>
            <div className={selectorContent}>
                <p className={selectorTitle}>{t("appointment.booking.office")}</p>

                <Select
                    value={selectedOffice ?? ""}
                    onValueChange={(v) => setSelectedOffice(v)}
                    disabled={disabled || options.length === 0}
                >
                    <SelectTrigger className={selectorButton}>
                        <SelectValue placeholder={t("appointment.booking.office")} />
                    </SelectTrigger>

                    <SelectContent
                        position="popper"
                        side="bottom"
                        sideOffset={2}
                        avoidCollisions={false}
                        className="max-h-56 overflow-y-auto"
                    >
                        <SelectGroup>
                            {options.map((o) => (
                                <SelectItem key={o.self} value={o.self}>
                                    <OfficeItem office={o} />
                                </SelectItem>
                            ))}
                        </SelectGroup>
                    </SelectContent>
                </Select>
            </div>
        </Card>
    );
}

function SpecialtySelector({options, selectedSpecialty, setSelectedSpecialty}: {
    options: SpecialtyDTO[];
    selectedSpecialty: string | null;
    setSelectedSpecialty: React.Dispatch<React.SetStateAction<string | null>>;
}) {
    const { t } = useTranslation();

    return (
        <Card className={selectorCard}>
            <div className={iconContainer}>
                <Stethoscope className={icon}/>
            </div>
            <div className={selectorContent}>
                <p className={selectorTitle}>{t("appointment.booking.specialty")}</p>
                <Select value={selectedSpecialty ?? ""} onValueChange={(v) => setSelectedSpecialty(v)}>
                    <SelectTrigger className={selectorButton}>
                        <SelectValue placeholder={t("appointment.booking.specialty")}/>
                    </SelectTrigger>
                    <SelectContent
                        position="popper"
                        side="bottom"
                        sideOffset={2}
                        avoidCollisions={false}
                        className="max-h-56 overflow-y-auto"
                    >
                        <SelectGroup>
                            {options.map((s) => (
                                <SelectItem key={s.self} value={s.self}>
                                    {t(s.name)}
                                </SelectItem>
                            ))}
                        </SelectGroup>
                    </SelectContent>
                </Select>
            </div>
        </Card>
    );
}

const confirmation = "mx-6 py-2 px-5 bg-(--success-lighter) rounded-lg border border-(--success)";
const confirmationTitle = "text-(--success-dark) text-md font-[500]";
const confirmationText = "text-(--success-dark) text-sm font-[300]";

function Confirmation({ text }: { text: string }) {
    const { t } = useTranslation();

    return (
        <div className={confirmation}>
            <h3 className={confirmationTitle}>{t("appointment.booking.your-appointment")}</h3>
            <p className={confirmationText}>{text}</p>
        </div>
    );
}

const reasonCard = "flex flex-col gap-2";

function ReasonInput({ value, onChange }: { value: string, onChange: (v: string) => void }) {
    const { t } = useTranslation();

    return (
        <div className={reasonCard}>
            <h3 className={selectorTitle}>{t("appointment.booking.reason")}</h3>
            <Textarea
                placeholder={t("appointment.booking.enter-reason")}
                value={value}
                onChange={(e) => onChange(e.target.value)}
            />
        </div>
    );
}

function FilesUpload({ onFilesChange }: { onFilesChange: (files: File[]) => void }) {
    const { t } = useTranslation();

    return (
        <div>
            <h3 className={selectorTitle + " mb-2"}>{t("appointment.booking.upload")}</h3>
            <UploadFiles onChange={onFilesChange} />
        </div>
    );
}

const medicalCointainer = "flex flex-col";

function MedicalHistory({ checked, onCheckedChange }: { checked: boolean, onCheckedChange: (v: boolean) => void }) {
    const { t } = useTranslation();

    return (
        <div className={medicalCointainer}>
            <Label className="hover:bg-accent/50 flex items-start gap-3 rounded-lg border p-3 has-aria-checked:border-(--primary-color) has-aria-checked:bg-(--primary-bg) dark:has-aria-checked:border-(--primary-light) dark:has-aria-checked:bg-(--primary-dark)">
                <Checkbox
                    id="toggle-2"
                    checked={checked}
                    onCheckedChange={onCheckedChange}
                    className="data-[state=checked]:border-(--primary-color) data-[state=checked]:bg-(--primary-color) data-[state=checked]:text-white dark:data-[state=checked]:border-(--primary-light) dark:data-[state=checked]:bg-(--primary-light) dark:data-[state=checked]:text-(--text-color) cursor-pointer"
                />
                <div className="grid gap-1.5 font-normal">
                    <p className="text-sm leading-none font-medium">{t("appointment.booking.allow.title")}</p>
                    <p className="text-muted-foreground text-sm">
                        {t("appointment.booking.allow.subtitle")}
                    </p>
                </div>
            </Label>
        </div>
    );
}

function RefetchData({
                         isFetching = false,
                         onRefetch,
                         errorText,
                         tryAgainText,
                         className,
                         disabled,
                     }: {
    isFetching?: boolean;
    onRefetch: () => void;
    errorText?: string;
    tryAgainText?: string;
    className?: string;
    disabled?: boolean;
}) {
    return (
        <Card className={cn("w-full p-8", className)}>
            <RefetchComponent
                isFetching={isFetching}
                onRefetch={onRefetch}
                errorText={errorText}
                tryAgainText={tryAgainText}
                disabled={disabled}
            />
        </Card>
    );
}

export default Appointment;