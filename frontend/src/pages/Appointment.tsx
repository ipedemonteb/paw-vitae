import React, { useMemo, useState, useEffect } from "react";
import { Card } from "@/components/ui/card.tsx"
import DoctorProfileCard from "@/components/DoctorProfileCard.tsx";
import { Stethoscope, Hospital, CalendarDays } from "lucide-react";
import { Select, SelectContent, SelectGroup, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select.tsx";
import { DatePicker } from "@/components/ui/date-picker.tsx";
import { Button } from "@/components/ui/button.tsx";
import {Textarea} from "@/components/ui/textarea.tsx";
import {UploadFiles} from "@/components/UploadFiles.tsx";
import {Label} from "@/components/ui/label.tsx";
import {Checkbox} from "@/components/ui/checkbox.tsx";
import {useTranslation} from "react-i18next";
import {useDoctor, useDoctorOffices, useDoctorSpecialties, useDoctorOfficeSpecialties} from "@/hooks/useDoctors.ts";
import type {OfficeDTO} from "@/data/office.ts";
import type {SpecialtyDTO} from "@/data/specialties.ts";
import type {OfficeSpecialtyDTO} from "@/data/doctors.ts";

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

const appointmentBackground =
    "bg-[var(--background-light)] flex justify-center items-start min-h-screen";
const cardContainer =
    "mt-36 px-5 mx-auto max-w-6xl w-full mb-8";
const appointmentContainer =
    "p-0 pb-8";
const appointmentHeader =
    "flex flex-col items-center py-8 rounded-t-xl gap-3 " +
    "bg-[linear-gradient(135deg,var(--background-light)_0%,var(--landing-light)_100%)]"
const appointmentTitle =
    "font-bold text-4xl text-center text-[var(--text-color)]";
const appointmentContent =
    "flex flex-col px-8 gap-4";
const selectorTitle =
    "text-[var(--text-color)] text-md font-[500]";
const selectorsContainer =
    "flex flex-col md:flex-row gap-6";
const dateCard =
    "flex flex-col";
const dateContainer =
    "flex flex-col md:items-center md:flex-row gap-5 md:gap-0";
const dateUpperContainer =
    "flex flex-row items-center";
const availableTimes =
    "flex flex-wrap items-center px-5 gap-2";
const timeButton =
    "bg-white text-[var(--primary-color)] border border-[var(--primary-color)] hover:bg-[var(--primary-color)] hover:text-white cursor-pointer " +
    "data-[selected=true]:bg-[var(--primary-color)] data-[selected=true]:text-white data-[selected=true]:border-[var(--primary-color)]";
const optionalsContainer =
    "flex flex-col gap-8 mt-4";
const bookContainer =
    "flex flex-col items-center w-full";
const bookButton =
    "mt-6 py-4 w-xs bg-[var(--primary-color)] hover:bg-[var(--primary-dark)] cursor-pointer";

function Appointment() {

    const doctorId = "24";

    const { t } = useTranslation();

    const [selectedDate, setSelectedDate] = useState<Date | undefined>(undefined);
    const [selectedTime, setSelectedTime] = useState<string | null>(null);
    const [selectedSpecialty, setSelectedSpecialty] = useState<string | null>(null);
    const [selectedOffice, setSelectedOffice] = useState<string | null>(null);

    const { data: doctor, isLoading, isError } = useDoctor(doctorId);
    // TODO: Handle isLoading and isError
    const { data: offices } = useDoctorOffices(doctor?.offices ?? null);
    const { data: officeSpecialties } = useDoctorOfficeSpecialties(offices ?? null);
    const { data: doctorSpecialties } = useDoctorSpecialties(doctor?.specialties ?? null);

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
        if (!isOfficeValid(filteredOffices, selectedOffice)) {
            setSelectedOffice(null);
        }
    }, [filteredOffices, selectedOffice]);

    if (isLoading) {
        return (
            <div>Loading...</div>
        );
    }

    if (!doctor || isError) {
        return (
            <div>Error</div>
        );
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
                        <DoctorProfileCard doctorId={doctorId} doctor={doctor}/>
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
                        />
                        <div className={optionalsContainer}>
                            <ReasonInput />
                            <FilesUpload />
                            <MedicalHistory />
                        </div>
                        <div className={bookContainer}>
                            <Button className={bookButton}>
                                {t("appointment.booking.book")}
                            </Button>
                        </div>
                    </div>
                </Card>
            </div>
        </div>
    )
}

const selectorCard =
    "flex flex-row items-center w-full gap-0";
const iconContainer =
    "flex items-center bg-[var(--primary-bg)] rounded-full p-5 text-[var(--primary-color)] mx-5";
const icon =
    "w-8 h-8";
const selectorContent =
    "flex flex-col gap-1 min-w-56";
const selectorButton =
    "cursor-pointer";

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
                                    {o.name}
                                </SelectItem>
                            ))}
                        </SelectGroup>
                    </SelectContent>
                </Select>
            </div>
        </Card>
    );
}

function DateSelector({selectedDate, setSelectedDate, selectedTime, setSelectedTime}:{
    selectedDate: Date | undefined
    setSelectedDate: (date: Date | undefined) => void
    selectedTime: string | null
    setSelectedTime: React.Dispatch<React.SetStateAction<string | null>>;
}) {
    const { t } = useTranslation();

    useEffect(() => {
        if (!selectedDate) setSelectedTime(null);
    }, [selectedDate]);

    const locale = useMemo(() => (typeof navigator === "undefined" ? "en-US" : navigator.language || "en-US"), []);

    const formattedConfirmation = useMemo(() => {
        if (!selectedDate || !selectedTime) return null;

        const [hh, mm] = selectedTime.split(":").map(Number);
        const d = new Date(selectedDate);
        d.setHours(hh, mm, 0, 0);

        let datePart = new Intl.DateTimeFormat(locale, {
            weekday: "long",
            year: "numeric",
            month: "long",
            day: "numeric",
        }).format(d);

        datePart = datePart.charAt(0).toUpperCase() + datePart.slice(1);

        const timePart = new Intl.DateTimeFormat(locale, {
            hour: "2-digit",
            minute: "2-digit",
        }).format(d);

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
                        <DatePicker value={selectedDate} onChange={setSelectedDate} placeholder={t("appointment.booking.select-date")}/>
                    </div>
                </div>
                {selectedDate ? (
                    <div className={availableTimes}>
                        {[
                            "10:00","11:00","12:00","13:00","14:00","15:00","16:00",
                            "17:00","18:00","19:00","20:00"
                        ].map((t) => (
                            <Button
                                key={t}
                                className={timeButton}
                                data-selected={selectedTime === t}
                                onClick={() => setSelectedTime((prev) => (prev === t ? null : t))}
                            >
                                {t}
                            </Button>
                        ))}
                    </div>
                ) : null}
            </div>
            {formattedConfirmation ? <Confirmation text={formattedConfirmation} /> : null}
        </Card>
    );
}

const confirmation =
    "mx-6 py-2 px-5 bg-[var(--success-light)] rounded-lg border border-[var(--success)]";
const confirmationTitle =
    "text-[var(--text-color)] text-md font-[500]";
const confirmationText =
    "text-[var(--text-color)] text-sm font-[300]";

function Confirmation({ text }: { text: string }) {
    const { t } = useTranslation();

    return (
        <div className={confirmation}>
            <h3 className={confirmationTitle}>{t("appointment.booking.your-appointment")}</h3>
            <p className={confirmationText}>{text}</p>
        </div>
    );
}

const reasonCard =
    "flex flex-col gap-2";

function ReasonInput() {
    const { t } = useTranslation();

    return (
        <div className={reasonCard}>
            <h3 className={selectorTitle}>{t("appointment.booking.reason")}</h3>
            <Textarea placeholder={t("appointment.booking.enter-reason")}/>
        </div>
    );
}

function FilesUpload() {
    const { t } = useTranslation();

    return (
        <div>
            <h3 className={selectorTitle + " mb-2"}>{t("appointment.booking.upload")}</h3>
            <UploadFiles />
        </div>
    );
}

const medicalCointainer =
    "flex flex-col";

function MedicalHistory() {
    const { t } = useTranslation();

    return (
        <div className={medicalCointainer}>
            <Label className="hover:bg-accent/50 flex items-start gap-3 rounded-lg border p-3 has-[[aria-checked=true]]:border-[var(--primary-color)] has-[[aria-checked=true]]:bg-[var(--primary-bg)] dark:has-[[aria-checked=true]]:border-[var(--primary-light)] dark:has-[[aria-checked=true]]:bg-[var(--primary-dark)]">
                <Checkbox
                    id="toggle-2"
                    defaultChecked
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