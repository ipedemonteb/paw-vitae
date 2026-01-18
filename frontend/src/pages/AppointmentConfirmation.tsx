import React from "react";
import {Link, useParams} from "react-router-dom";
import { useTranslation } from "react-i18next";
import { Card } from "@/components/ui/card.tsx";
import { Button } from "@/components/ui/button.tsx";
import {
    CheckCircle,
    CalendarDays,
    MapPin,
    Stethoscope,
    Clock,
    Loader2
} from "lucide-react";
import DoctorProfileCard from "@/components/DoctorProfileCard.tsx";
import {useAppointment} from "@/hooks/useAppointments.ts";
import {useDoctor, useDoctorOffice} from "@/hooks/useDoctors.ts";
import {usePatient} from "@/hooks/usePatients.ts";
import GenericError from "@/pages/GenericError.tsx";
import {useSpecialty} from "@/hooks/useSpecialties.ts";
import {DateCard} from "@/pages/AppointmentDetails.tsx";
import {formatLongDate} from "@/utils/dateUtils.ts";
import {useNeighborhood} from "@/hooks/useNeighborhoods.ts";

const appointmentBackground =
    "bg-[var(--background-light)] flex justify-center items-start min-h-screen";
const cardContainer =
    "mt-36 px-5 mx-auto max-w-4xl w-full mb-8";
const appointmentContainer =
    "p-0 pb-8 overflow-hidden";
const appointmentHeader =
    "flex flex-col items-center py-8 gap-3 " +
    "bg-[var(--success-light)] border-b border-[var(--success)] text-[var(--success)]";
const appointmentTitle =
    "font-bold text-3xl text-center text-[var(--text-color)]";
const appointmentContent =
    "flex flex-col px-8 gap-6 pt-6";
const detailCard =
    "flex flex-col gap-4 border border-[var(--gray-300)] rounded-xl p-6 bg-white";
const sectionTitle =
    "text-[var(--text-color)] text-lg font-[600] mb-2";
const bookContainer =
    "flex flex-col items-center w-full mt-4";
const bookButton =
    "py-4 w-full sm:w-xs bg-[var(--primary-color)] hover:bg-[var(--primary-dark)] cursor-pointer text-white font-semibold";

function AppointmentConfirmation() {
    const { t } = useTranslation();
    const { appointmentId } = useParams();

    const { data: appointment, isError, isLoading } = useAppointment(appointmentId);

    const locale = typeof navigator === "undefined" ? "es-AR" : navigator.language || "es-AR";
    const {
        data: specialty,
        isLoading: isLoadingSpecialty,
        isError: isErrorSpecialty
    } = useSpecialty(appointment?.specialty);

    const {
        data: office,
        isLoading: isLoadingOffice,
        isError: isErrorOffice
    } = useDoctorOffice(appointment?.doctorOffice);

    const {
        data:neighborhood,
        isLoading: isLoadingNeighborhood,
        isError: isErrorNeighborhood
    } =useNeighborhood(office?.neighborhood);

    if (isLoading || isLoadingSpecialty || isLoadingOffice || isLoadingNeighborhood) {
        return <div className="flex justify-center mt-36"><Loader2 className="animate-spin h-8 w-8" /></div>;
    }

    if (isError || !appointment || isErrorSpecialty || isErrorOffice || isErrorNeighborhood) {
        return <GenericError code={404} />;
    }

    const formattedDate = appointment.date
        ? formatLongDate(new Date(appointment.date).toLocaleDateString(undefined, { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' }),locale)
        : "";

    const doctorId = appointment.doctor.split('/').pop() || "";

    return (
        <div className={appointmentBackground}>
            <div className={cardContainer}>
                <Card className={appointmentContainer}>
                    <div className={appointmentHeader}>
                        <CheckCircle className="w-16 h-16 text-green-600" />
                        <h1 className={appointmentTitle}>
                            {t("appointment.confirmation.success_title", "¡Turno Reservado con Éxito!")}
                        </h1>
                        <p className="text-green-800 font-medium">
                            {t("appointment.confirmation.success_subtitle", "Su cita ha sido confirmada.")}
                        </p>
                    </div>

                    <div className={appointmentContent}>

                        <div className="border rounded-xl overflow-hidden shadow-sm">
                            <DoctorProfileCard doctorId={doctorId} />
                        </div>

                        <div className={detailCard}>
                            <h3 className={sectionTitle}>
                                {t("appointment.details.title", "Detalles del Turno")}
                            </h3>

                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                <ConfirmationDetailRow
                                    icon={CalendarDays}
                                    label={t("appointment.booking.date", "Fecha")}
                                    value={formattedDate}
                                />
                                <ConfirmationDetailRow
                                    icon={Clock}
                                    label={t("appointment.booking.time", "Hora")}
                                    value={new Date(appointment.date).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}
                                />
                                <ConfirmationDetailRow
                                    icon={Stethoscope}
                                    label={t("search.specialty.title", "Especialidad")}
                                    value={specialty?.name ? t(specialty.name) : t("appointment.details.no-specialty")}
                                />
                                <ConfirmationDetailRow
                                    icon={MapPin}
                                    label={t("profile.office", "Consultorio")}
                                    value={`${office?.name},${neighborhood?.name}`|| "Consultorio"}
                                />
                            </div>
                        </div>

                        <div className="bg-blue-50 p-4 rounded-lg border border-blue-100 text-blue-800 text-sm text-center">
                            {t("appointment.confirmation.email_notice", "Hemos enviado un correo electrónico con los detalles de su cita.")}
                        </div>

                        <div className={bookContainer}>
                            <Button asChild className={bookButton}>
                                <Link to="/patient/dashboard/upcoming" replace>
                                    {t("appointment_confirmation.dashboard", "Volver a Mis Turnos")}
                                </Link>
                            </Button>
                        </div>
                    </div>
                </Card>
            </div>
        </div>
    );
}

const rowContainer = "flex items-start gap-3 p-2";
const rowIconContainer = "p-2 bg-[var(--primary-bg)] rounded-lg text-[var(--primary-color)] mt-1";
const rowTextContainer = "flex flex-col";
const rowLabel = "text-xs text-[var(--text-light)] uppercase font-semibold tracking-wider";
const rowValue = "text-[var(--text-color)] font-medium text-base";
const rowSubValue = "text-[var(--text-light)] text-sm";

function ConfirmationDetailRow({
                                   icon: Icon,
                                   label,
                                   value,
                                   subValue
                               }: {
    icon: React.ElementType,
    label: string,
    value: string,
    subValue?: string
}) {
    return (
        <div className={rowContainer}>
            <div className={rowIconContainer}>
                <Icon className="w-5 h-5" />
            </div>
            <div className={rowTextContainer}>
                <span className={rowLabel}>{label}</span>
                <span className={rowValue}>{value}</span>
                {subValue && <span className={rowSubValue}>{subValue}</span>}
            </div>
        </div>
    );
}

export default AppointmentConfirmation;