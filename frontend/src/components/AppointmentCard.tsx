import { Clock4, EyeIcon } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";
import type { AppointmentDTO } from "@/data/appointments.ts";
import { useTranslation } from "react-i18next";
import { usePatient } from "@/hooks/usePatients.ts";
import { useSpecialty } from "@/hooks/useSpecialties.ts";
import { useCoverage } from "@/hooks/useCoverages.ts";
import { useNavigate } from "react-router-dom";
import { useAuth } from "@/hooks/useAuth.ts";
import {appointmentIdFromSelf, userIdFromSelf} from "@/utils/IdUtils.ts";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar.tsx";
import { useDoctor, useDoctorImageUrl } from "@/hooks/useDoctors.ts";

const statusClassname =
    "h-7 font-medium border-solid border text-xs w-3/4 rounded-2xl flex items-center justify-center";
const confirmedStatusClassname = statusClassname + " bg-green-100 border-green-400";
const cancelledStatusClassname = statusClassname + " bg-red-100 border-red-400";
const completedStatusClassname = statusClassname + " bg-blue-100 border-blue-400";

const statusClassnameDictionary = {
    completo: completedStatusClassname,
    confirmado: confirmedStatusClassname,
    cancelado: cancelledStatusClassname,
};

const cardContainer = "w-full border border-solid rounded-md flex flex-col shadow sm:flex-row";
const leftSection =
    "bg-gray-100 w-full max-w-none min-w-0 space-y-3 flex flex-col justify-center items-center " +
    "py-6 sm:py-8 sm:h-full sm:w-1/6 sm:max-w-44 sm:min-w-36 self-stretch rounded-t-md md:rounded-l-md md:rounded-t-none";
const dateBlock = "text-sm flex flex-col gap-0 items-center justify-center text-center w-full";
const weekdayText = "text-(--text-light)";
const dayText = "text-3xl font-bold";
const monthText = "text-(--text-light)";
const timeRow = "w-full gap-x-1.5 flex flex-row justify-center items-center";
const timeIcon = "size-4";
const statusRow = "w-full flex justify-center items-center opacity-80";

const rightSection =
    "min-w-0 px-5 py-6 w-full flex flex-col items-center justify-center sm:h-full sm:w-5/6 sm:py-6";
const topRow = "flex gap-3 w-full items-center";
const nameBlock = "flex flex-col min-w-0";
const fullNameText = "font-semibold";
const coverageRow = "text-(--text-light) text-xs flex flex-row items-center gap-2 min-w-0";
const coverageDot = "bg-blue-500 w-2 h-2 rounded-full shrink-0";

const middleRow = "flex w-full py-4";
const reasonWrapper = "h-full flex w-full";
const reasonBar = "w-3 bg-blue-600 h-full rounded-sm shrink-0";
const reasonBox =
    "min-w-0 rounded-sm -ml-2 h-full w-full text-xs bg-gray-50 flex flex-col px-3 justify-center py-3";
const reasonLabel = "text-(--text-light)";
const reasonTextWrap = "relative w-full min-w-0";
const reasonText = "truncate block w-full";
const reasonFade = "pointer-events-none absolute top-0 right-0 h-full w-8 bg-linear-to-r from-transparent to-gray-50";

const bottomRow =
    "flex flex-col gap-3 items-stretch w-full sm:flex-row sm:justify-between sm:items-center";
const specialtyPill =
    "rounded-2xl font-medium text-(--primary-color) h-min py-1.5 px-2.5 text-sm flex items-center justify-center bg-gray-100 w-fit";
const detailsButton =
    "flex gap-2 hover:bg-(--primary-dark) cursor-pointer items-center rounded-lg text-white justify-center px-2 py-2 bg-(--primary-color)";

const avatarClass =
    "h-10 w-10 shrink-0 border border-solid border-blue-500 bg-blue-200";
const avatarFallbackClass =
    "bg-(--primary-bg) text-(--primary-color) font-semibold";

type AppointmentCardProps = {
    appointment: AppointmentDTO;
};

function transformStatus(status: string) {
    switch (status) {
        case "confirmado":
            return "confirmed";
        case "completo":
            return "completed";
        case "cancelado":
            return "cancelled";
    }
}

function initials(name?: string, lastName?: string) {
    const a = (name?.trim()?.[0] ?? "").toUpperCase();
    const b = (lastName?.trim()?.[0] ?? "").toUpperCase();
    return (a + b) || "U";
}

export default function AppointmentCard({ appointment }: AppointmentCardProps) {
    const { t, i18n } = useTranslation();
    const navigate = useNavigate();
    const auth = useAuth();

    const isDoctor = auth.role === "ROLE_DOCTOR";

    const doctorId = userIdFromSelf(appointment.doctor);

    const { data: patient } = usePatient(appointment.patient);
    const { data: doctor } = useDoctor(doctorId);
    const { url: doctorImgUrl } = useDoctorImageUrl(doctorId);

    const { data: specialty } = useSpecialty(appointment.specialty);
    const { data: coverage } = useCoverage(patient?.coverages);

    const locale = i18n?.language || "en-US";

    const d = new Date(appointment.date);
    const weekday = new Intl.DateTimeFormat(locale, { weekday: "long" }).format(d).toUpperCase();
    const day = d.getDate();
    const month = new Intl.DateTimeFormat(locale, { month: "long" }).format(d).toUpperCase();
    const time = new Intl.DateTimeFormat(locale, {
        hour: "2-digit",
        minute: "2-digit",
        hour12: false,
    }).format(d);

    const displayName = isDoctor
        ? `${patient?.name ?? ""} ${patient?.lastName ?? ""}`.trim()
        : `${doctor?.name ?? ""} ${doctor?.lastName ?? ""}`.trim();

    const fallbackText = isDoctor
        ? initials(patient?.name, patient?.lastName)
        : initials(doctor?.name, doctor?.lastName);

    const avatarSrc = isDoctor ? undefined : (doctorImgUrl || undefined);

    const status = "appointment.filters." + transformStatus(appointment.status);
    const base = isDoctor ? "/doctor/dashboard" : "/patient/dashboard";

    return (
        <div className={cardContainer}>
            <div className={leftSection}>
                <div className={dateBlock}>
                    <span className={weekdayText}>{weekday}</span>
                    <span className={dayText}>{day}</span>
                    <span className={monthText}>{month}</span>
                </div>
                <div className={timeRow}>
                    <Clock4 color="var(--primary-color)" className={timeIcon} />
                    {time}
                </div>
                <div className={statusRow}>
                    <div className={statusClassnameDictionary[appointment.status]}>
                        {t(status)}
                    </div>
                </div>
            </div>

            <div className={rightSection}>
                <div className={topRow}>
                    <Avatar className={avatarClass}>
                        <AvatarImage src={avatarSrc} />
                        <AvatarFallback className={avatarFallbackClass}>{fallbackText}</AvatarFallback>
                    </Avatar>

                    <div className={nameBlock}>
                        <span className={fullNameText}>{displayName}</span>
                        <span className={coverageRow}>
              <span className={coverageDot} />
                            {t(coverage?.name || "")}
            </span>
                    </div>
                </div>

                <div className={middleRow}>
                    <div className={reasonWrapper}>
                        <div className={reasonBar} />
                        <div className={reasonBox}>
                            {appointment.reason.trim().length > 0 ? (
                                <>
                                    <span className={reasonLabel}>{t("appointment.card.reason")}</span>
                                    <div className={reasonTextWrap}>
                                        <span className={reasonText}>{appointment.reason}</span>
                                        <div className={reasonFade} />
                                    </div>
                                </>
                            ) : (
                                <span className="text-(--text-light)">{t("medical-history.component.no-reason")}</span>
                            )}
                        </div>
                    </div>
                </div>

                <div className={bottomRow}>
                    <div className={specialtyPill}>{t(specialty?.name || "")}</div>
                    <Button
                        className={detailsButton}
                        onClick={() =>
                            navigate(`${base}/appointment-details/${appointmentIdFromSelf(appointment.self)}`)
                        }
                    >
                        <EyeIcon />
                        {t("appointment.card.details")}
                    </Button>
                </div>
            </div>
        </div>
    );
}