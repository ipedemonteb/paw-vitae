import { Clock4, EyeIcon } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";
import type { AppointmentDTO } from "@/data/appointments.ts";
import { useTranslation } from "react-i18next";

const statusClassname =
    "h-7 font-medium border-solid border text-xs w-3/4 rounded-2xl flex items-center justify-center";
const confirmedStatusClassname =
    statusClassname + " bg-green-100 border-green-400";
const cancelledStatusClassname =
    statusClassname + " bg-red-100 border-red-400";
const completedStatusClassname =
    statusClassname + " bg-blue-100 border-blue-400";

const statusClassnameDictionary = {
    completed: completedStatusClassname,
    confirmed: confirmedStatusClassname,
    cancelled: cancelledStatusClassname
};

const cardContainer =
    "w-full border border-solid rounded-md flex flex-col shadow sm:flex-row";
const leftSection =
    "bg-gray-100 w-full max-w-none min-w-0 space-y-3 flex flex-col justify-center items-center py-6 sm:py-8 sm:h-full sm:w-1/6 sm:max-w-44 sm:min-w-36 self-stretch";
const dateBlock =
    "text-sm flex flex-col gap-0 items-center justify-center text-center w-full";
const weekdayText =
    "text-(--text-light)";
const dayText =
    "text-3xl font-bold";
const monthText =
    "text-(--text-light)";
const timeRow =
    "w-full gap-x-1.5 flex flex-row justify-center items-center";
const timeIcon =
    "size-4";
const statusRow =
    "w-full flex justify-center items-center opacity-80";

const rightSection =
    "min-w-0 px-5 py-6 w-full flex flex-col items-center justify-center sm:h-full sm:w-5/6 sm:py-6";
const topRow =
    "flex gap-3 w-full items-center";
const initialsCircle =
    "rounded-full border border-solid text-blue-700 border-blue-500 flex justify-center items-center bg-blue-200 h-10 w-10 shrink-0";
const nameBlock =
    "flex flex-col min-w-0";
const fullNameText =
    "font-semibold";
const coverageRow =
    "text-(--text-light) text-xs flex flex-row items-center gap-2 min-w-0";
const coverageDot =
    "bg-blue-500 w-2 h-2 rounded-full shrink-0";

const middleRow =
    "flex w-full py-4";
const reasonWrapper =
    "h-full flex w-full";
const reasonBar =
    "w-3 bg-blue-600 h-full rounded-sm shrink-0";
const reasonBox =
    "min-w-0 rounded-sm -ml-2 h-full w-full text-xs bg-gray-50 flex flex-col px-3 justify-center py-3";
const reasonLabel =
    "text-(--text-light)";
const reasonTextWrap =
    "relative w-full min-w-0";
const reasonText =
    "truncate block w-full";
const reasonFade =
    "pointer-events-none absolute top-0 right-0 h-full w-8 bg-linear-to-r from-transparent to-gray-50";

const bottomRow =
    "flex flex-col gap-3 items-stretch w-full sm:flex-row sm:justify-between sm:items-center";
const specialtyPill =
    "rounded-2xl font-medium text-(--primary-color) h-min py-1.5 px-2.5 text-sm flex items-center justify-center bg-gray-100 w-fit";
const detailsButton =
    "flex gap-2 hover:bg-(--primary-dark) cursor-pointer items-center rounded-lg text-white justify-center px-2 py-2 bg-(--primary-color)";

type AppointmentCardProps = {
    appointment: AppointmentDTO;
    patientName: string;
    patientLastname: string;
    patientCoverage: string;
};

export default function AppointmentCard({ appointment, patientName, patientLastname, patientCoverage }: AppointmentCardProps) {
    const { t, i18n } = useTranslation();
    const locale = i18n?.language || "en-US";

    const d = appointment.date;
    const weekday = new Intl.DateTimeFormat(locale, { weekday: "long" }).format(d).toUpperCase();
    const day = d.getDate();
    const month = new Intl.DateTimeFormat(locale, { month: "long" }).format(d).toUpperCase();
    const time = new Intl.DateTimeFormat(locale, { hour: "2-digit", minute: "2-digit", hour12: false }).format(d);
    const initials = patientName[0] + patientLastname[0];
    const fullName = patientName + " " + patientLastname;

    const status = "appointment.status." + appointment.status;

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
                    <div className={statusClassnameDictionary["confirmed"]}>
                        {t(status)}
                    </div>
                </div>
            </div>

            <div className={rightSection}>
                <div className={topRow}>
                    <div className={initialsCircle}>
                        {initials}
                    </div>
                    <div className={nameBlock}>
                        <span className={fullNameText}>
                            {fullName}
                        </span>
                        <span className={coverageRow}>
                            <span className={coverageDot} />
                            {t(patientCoverage)}
                        </span>
                    </div>
                </div>

                <div className={middleRow}>
                    <div className={reasonWrapper}>
                        <div className={reasonBar} />
                        <div className={reasonBox}>
                            <span className={reasonLabel}>
                                Reason for Visit:
                            </span>
                            <div className={reasonTextWrap}>
                                <span className={reasonText}>
                                    {appointment.reason}
                                </span>
                                <div className={reasonFade} />
                            </div>
                        </div>
                    </div>
                </div>

                <div className={bottomRow}>
                    <div className={specialtyPill}>
                        {t(appointment.specialty)}
                    </div>
                    <Button className={detailsButton}>
                        <EyeIcon />
                        View Details
                    </Button>
                </div>
            </div>
        </div>
    );
}
