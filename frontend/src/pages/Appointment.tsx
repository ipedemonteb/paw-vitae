import React, { useMemo, useState, useEffect, useCallback } from "react";
import { Card } from "@/components/ui/card.tsx"
import DoctorProfileCard from "@/components/DoctorProfileCard.tsx";
import { Stethoscope, Hospital, CalendarDays, Loader2 } from "lucide-react";
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
} from "@/hooks/useDoctors.ts";
import {useDoctorAvailability, useDoctorOffices, useDoctorOfficesSpecialties} from "@/hooks/useOffices.ts";

import { useBookAppointment } from "@/hooks/useAppointments.ts";
import { useDoctorSlots } from "@/hooks/useSlots.ts";
import { useAuth } from "@/hooks/useAuth.ts";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "sonner";
import { useQueryClient } from "@tanstack/react-query";
import type { OfficeDTO, OfficeSpecialtyDTO } from "@/data/offices.ts";
import type { SpecialtyDTO } from "@/data/specialties.ts";
import type { AvailabilitySlotDTO } from "@/data/slots.ts";
import { startOfDay, parseISO, isSameDay, addMonths } from "date-fns";
import { useNeighborhood } from "@/hooks/useNeighborhoods.ts";
import GenericError from "@/pages/GenericError.tsx";

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
const appointmentHeader = "flex flex-col items-center py-8 rounded-t-xl gap-3 bg-[linear-gradient(135deg,var(--background-light)_0%,var(--landing-light)_100%)]";
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
    const queryClient = useQueryClient();

    const { mutate: bookAppointment, isPending: isBooking } = useBookAppointment();

    const [selectedDate, setSelectedDate] = useState<Date | undefined>(undefined);
    const [selectedSlotId, setSelectedSlotId] = useState<number | null>(null);
    const [selectedSpecialty, setSelectedSpecialty] = useState<string | null>(null);
    const [selectedOffice, setSelectedOffice] = useState<string | null>(null);

    const [reason, setReason] = useState("");
    const [allowFullHistory, setAllowFullHistory] = useState(true);
    const [files, setFiles] = useState<File[]>([]);

    const { data: doctor, isLoading: loadingDoctor, isError: errorDoctor, error: doctorError } = useDoctor(doctorId);
    const { data: offices, isLoading: loadingOffices } = useDoctorOffices(doctor?.offices, { status: "active" });
    const { data: officeSpecialties } = useDoctorOfficesSpecialties(offices);
    const { data: doctorSpecialties } = useDoctorSpecialties(doctor?.specialties);
    const { data: allAvailability } = useDoctorAvailability(doctorId);

    const { data: allSlots, isLoading: loadingSlots } = useDoctorSlots(doctorId);

    const isLoading = loadingDoctor || loadingOffices || loadingSlots;
    const today = useMemo(() => startOfDay(new Date()), []);

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

    const availableSlots = useMemo(() => {
        if (!allSlots) return [];
        return allSlots.filter(s => s.status === 'AVAILABLE');
    }, [allSlots]);

    const selectedOfficeRules = useMemo(() => {
        if (!selectedOffice || !allAvailability) return [];
        return allAvailability.filter(rule => rule.office === selectedOffice);
    }, [selectedOffice, allAvailability]);

    const slotsForSelectedOffice = useMemo(() => {
        if (!selectedOfficeRules) return [];

        return availableSlots.filter(slot => {
            const slotDate = parseISO(slot.date);
            const dayOfWeek = slotDate.getDay();

            return selectedOfficeRules.some((rule) => {
                if (rule.dayOfWeek !== dayOfWeek) return false;
                return slot.startTime >= rule.startTime && slot.startTime < rule.endTime;
            });
        });
    }, [availableSlots, selectedOfficeRules]);

    const isDateSelectable = useCallback((d: Date) => {
        if (d < today) return false;
        return slotsForSelectedOffice.some(slot => isSameDay(parseISO(slot.date), d));
    }, [today, slotsForSelectedOffice]);

    const slotsForDay = useMemo(() => {
        if (!selectedDate) return [];
        let slots = slotsForSelectedOffice
            .filter(slot => isSameDay(parseISO(slot.date), selectedDate))
            .sort((a, b) => a.startTime.localeCompare(b.startTime));
        if (isSameDay(selectedDate, new Date())) {
            const now = new Date();
            const currentTime = `${now.getHours().toString().padStart(2, '0')}:${now.getMinutes().toString().padStart(2, '0')}`;
            slots = slots.filter(slot => slot.startTime > currentTime);
        }
        return slots;
    }, [selectedDate, slotsForSelectedOffice]);


    useEffect(() => {
        setSelectedDate(undefined);
        setSelectedSlotId(null);
    }, [selectedOffice]);

    useEffect(() => {
        setSelectedSlotId(null);
    }, [selectedDate]);


    const handleBook = () => {
        if (!auth.userId) {
            toast.error(t("error"), { description: "Debe iniciar sesión para reservar." });
            navigate("/login");
            return;
        }

        if (!selectedSlotId || !selectedSpecialty || !selectedOffice) {
            toast.error(t("error"), { description: t("register.errors.missing_fields", "Complete todos los campos") });
            return;
        }

        const appointmentForm = {
            slotId: selectedSlotId,
            patientId: auth.userId!,
            doctorId: doctorId!,
            specialtyId: selectedSpecialty.split("/").pop()!,
            officeId: selectedOffice.split("/").pop()!,
            reason: reason,
            allowFullHistory: allowFullHistory
        };

        bookAppointment({ form: appointmentForm, files: files}, {
            onSuccess: (newId) => {
                queryClient.invalidateQueries({ queryKey: ['auth', 'appointments'] });
                queryClient.invalidateQueries({ queryKey: ['doctors', doctorId, 'slots'] });

                toast.success(t("success.appointment_created", "Turno reservado exitosamente"));
                navigate(`/appointment/${newId}/confirmation`);
            },
            onError: () => {
                toast.error(t("appointment.booking.error.failed"), {
                    description: t("appointment.booking.error.try-again")
                });
            }
        });
    };

    if (isLoading) {
        return (
            <div className={appointmentBackground}>
                <div className="flex flex-col items-center justify-center h-screen gap-4">
                    <Loader2 className="h-12 w-12 animate-spin text-[var(--primary-color)]" />
                    <p className="text-[var(--text-light)] font-medium">{t("common.loading", "Cargando...")}</p>
                </div>
            </div>
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
                        <DoctorProfileCard doctorId={doctorId}/>
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
                            selectedSlotId={selectedSlotId}
                            setSelectedSlotId={setSelectedSlotId}
                            availableSlots={slotsForDay}
                            disabled={!selectedOffice}
                            isDateDisabled={(d) => !isDateSelectable(d)}
                        />

                        <div className={optionalsContainer}>
                            <ReasonInput value={reason} onChange={setReason} />
                            <FilesUpload onFilesChange={setFiles} />
                            <MedicalHistory checked={allowFullHistory} onCheckedChange={setAllowFullHistory} />
                        </div>
                        <div className={bookContainer}>
                            <Button
                                className={bookButton}
                                onClick={handleBook}
                                disabled={isBooking || !selectedSlotId}
                            >
                                {isBooking ? (
                                    <>
                                        <Loader2 className="mr-2 h-4 w-4 animate-spin" />
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
                          selectedSlotId,
                          setSelectedSlotId,
                          availableSlots,
                          disabled,
                          isDateDisabled,
                      }:{
    selectedDate: Date | undefined
    setSelectedDate: (date: Date | undefined) => void
    selectedSlotId: number | null
    setSelectedSlotId: React.Dispatch<React.SetStateAction<number | null>>;
    availableSlots: AvailabilitySlotDTO[];
    disabled?: boolean;
    isDateDisabled?: (date: Date) => boolean;
}) {
    const { t } = useTranslation();
    const locale = useMemo(() => (typeof navigator === "undefined" ? "en-US" : navigator.language || "en-US"), []);

    const formattedConfirmation = useMemo(() => {
        if (!selectedDate || !selectedSlotId) return null;

        const slot = availableSlots.find(s => s.id === selectedSlotId);
        if (!slot) return null;

        const [hh, mm] = slot.startTime.split(":").map(Number);
        const d = new Date(selectedDate);
        d.setHours(hh, mm, 0, 0);

        let datePart = new Intl.DateTimeFormat(locale, {
            weekday: "long", year: "numeric", month: "long", day: "numeric",
        }).format(d);
        datePart = datePart.charAt(0).toUpperCase() + datePart.slice(1);

        const timePart = new Intl.DateTimeFormat(locale, { hour: "2-digit", minute: "2-digit" }).format(d);
        return `${datePart} at ${timePart}`;
    }, [selectedDate, selectedSlotId, availableSlots, locale]);

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
                            toDate={addMonths(new Date(), 1)}
                        />
                    </div>
                </div>
                {selectedDate ? (
                    <div className={availableTimesFormat}>
                        {availableSlots.length === 0 ? (
                            <p className="px-5 text-sm text-[var(--text-light)]">
                                {t("appointment.booking.no-times")}
                            </p>
                        ) : (
                            availableSlots.map((slot) => {
                                const displayTime = slot.startTime.substring(0, 5);
                                return (
                                    <Button
                                        key={slot.id}
                                        className={timeButton}
                                        data-selected={selectedSlotId === slot.id}
                                        onClick={() => setSelectedSlotId((prev) => (prev === slot.id ? null : slot.id))}
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

                    <SelectContent>
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
                    <SelectContent>
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

const confirmation = "mx-6 py-2 px-5 bg-[var(--success-light)] rounded-lg border border-[var(--success)]";
const confirmationTitle = "text-[var(--text-color)] text-md font-[500]";
const confirmationText = "text-[var(--text-color)] text-sm font-[300]";

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
            <Label className="hover:bg-accent/50 flex items-start gap-3 rounded-lg border p-3 has-[[aria-checked=true]]:border-[var(--primary-color)] has-[[aria-checked=true]]:bg-[var(--primary-bg)] dark:has-[[aria-checked=true]]:border-[var(--primary-light)] dark:has-[[aria-checked=true]]:bg-[var(--primary-dark)]">
                <Checkbox
                    id="toggle-2"
                    checked={checked}
                    onCheckedChange={onCheckedChange}
                    className="data-[state=checked]:border-[var(--primary-color)] data-[state=checked]:bg-[var(--primary-color)] data-[state=checked]:text-white dark:data-[state=checked]:border-[var(--primary-light)] dark:data-[state=checked]:bg-[var(--primary-light)] dark:data-[state=checked]:text-[var(--text-color)] cursor-pointer"
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

export default Appointment;