import React, { useMemo, useState, useEffect } from "react";
import { Card } from "@/components/ui/card.tsx"
import DoctorProfileCard from "@/components/DoctorProfileCard.tsx";
import { Stethoscope, Hospital, CalendarDays } from "lucide-react";
import { Select, SelectContent, SelectGroup, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select.tsx";
import { DatePicker } from "@/components/ui/date-picker.tsx";
import { Button } from "@/components/ui/button.tsx";

const appointmentBackground =
    "bg-[var(--background-light)] flex justify-center items-start min-h-screen";
const cardContainer =
    "mt-36 px-5 mx-auto max-w-6xl w-full mb-8";
const appointmentContainer =
    "p-0 pb-8";
const appointmentHeader =
    "flex flex-col items-center py-8 rounded-t-lg gap-3 " +
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

function Appointment() {
    const selectedSpecialty = "General";

    const [selectedDate, setSelectedDate] = useState<Date | undefined>(undefined);
    const [selectedTime, setSelectedTime] = useState<string | null>(null);

    return (
        <div className={appointmentBackground}>
            <div className={cardContainer}>
                <Card className={appointmentContainer}>
                    <div className={appointmentHeader}>
                        <h1 className={appointmentTitle}>Book an Appointment</h1>
                        <p>Fill out the form below to schedule your appointment</p>
                    </div>
                    <div className={appointmentContent}>
                        <DoctorProfileCard />
                        <div className={selectorsContainer}>
                            <SpecialtySelector selectedSpecialty={selectedSpecialty} />
                            <OfficeSelector />
                        </div>
                        <DateSelector
                            selectedDate={selectedDate}
                            setSelectedDate={setSelectedDate}
                            selectedTime={selectedTime}
                            setSelectedTime={setSelectedTime}
                        />
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

function SpecialtySelector({selectedSpecialty}: {selectedSpecialty: string}) {
    return (
        <Card className={selectorCard}>
            <div className={iconContainer}>
                <Stethoscope className={icon}/>
            </div>
            <div className={selectorContent}>
                <p className={selectorTitle}>Select Specialty</p>
                <Select>
                    <SelectTrigger className={selectorButton}>
                        <SelectValue placeholder={selectedSpecialty}/>
                    </SelectTrigger>
                    <SelectContent>
                        <SelectGroup>
                            <SelectItem value="general">General</SelectItem>
                            <SelectItem value="cardiology">Cardiology</SelectItem>
                            <SelectItem value="endocrinology">Endocrinology</SelectItem>
                            <SelectItem value="dermatolofy">Dermatology</SelectItem>
                        </SelectGroup>
                    </SelectContent>
                </Select>
            </div>
        </Card>
    );
}

function OfficeSelector() {
    return (
        <Card className={selectorCard}>
            <div className={iconContainer}>
                <Hospital className={icon}/>
            </div>
            <div className={selectorContent}>
                <p className={selectorTitle}>Select Office</p>
                <Select>
                    <SelectTrigger className={selectorButton}>
                        <SelectValue placeholder="Select Office"/>
                    </SelectTrigger>
                    <SelectContent>
                        <SelectGroup>
                            <SelectItem value="main">Main Office</SelectItem>
                            <SelectItem value="hospital">Hospital</SelectItem>
                            <SelectItem value="oficina2">Oficina Mataderos</SelectItem>
                            <SelectItem value="oficina3">Oficina Parque Chas</SelectItem>
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

    useEffect(() => {
        if (!selectedDate) setSelectedTime(null);
    }, [selectedDate]);

    const locale = useMemo(() => (typeof navigator === "undefined" ? "en-US" : navigator.language || "en-US"), []);

    const formattedConfirmation = useMemo(() => {
        if (!selectedDate || !selectedTime) return null;

        const [hh, mm] = selectedTime.split(":").map(Number);
        const d = new Date(selectedDate);
        d.setHours(hh, mm, 0, 0);

        const datePart = new Intl.DateTimeFormat(locale, {
            weekday: "long",
            year: "numeric",
            month: "long",
            day: "numeric",
        }).format(d);

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
                        <p className={selectorTitle}>Date and Time</p>
                        <DatePicker value={selectedDate} onChange={setSelectedDate} />
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
    return (
        <div className={confirmation}>
            <h3 className={confirmationTitle}>Your Appointment</h3>
            <p className={confirmationText}>{text}</p>
        </div>
    );
}

export default Appointment;