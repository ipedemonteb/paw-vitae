import {Clock4, EyeIcon, X} from "lucide-react";
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
import {initialsFallback} from "@/utils/userUtils.ts";
import { useCancelAppointmentMutation } from "@/hooks/useAppointments.ts";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
    DialogClose,
} from "@/components/ui/dialog.tsx";
import {Spinner, Spinner as Loader} from "@/components/ui/spinner.tsx";
import {useState} from "react";
import {toast} from "sonner";
import {Skeleton} from "@/components/ui/skeleton.tsx";

const statusClassname =
    "h-7 font-medium border-solid border text-xs w-3/4 rounded-2xl flex items-center justify-center";
const confirmedStatusClassname = statusClassname + " bg-(--success-light) border-(--success)";
const cancelledStatusClassname = statusClassname + " bg-(--danger-light) border-(--danger)";
const completedStatusClassname = statusClassname + " bg-(--primary-bg) border-(--primary-color)";

const statusClassnameDictionary = {
    completo: completedStatusClassname,
    confirmado: confirmedStatusClassname,
    cancelado: cancelledStatusClassname,
};

const loadingSkeleton =
    "w-full h-118 sm:h-56";
const leftSection =
    "bg-gray-100 w-full max-w-none min-w-0 space-y-3 flex flex-col justify-center items-center " +
    "py-6 sm:py-8 sm:w-1/6 sm:max-w-44 sm:min-w-36 self-stretch " +
    "rounded-t-md sm:rounded-t-none sm:rounded-l-md";
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
    "flex flex-col gap-3 items-stretch w-full sm:flex-row sm:justify-between sm:items-center mt-2";
const specialtyPill =
    "rounded-2xl font-medium text-(--primary-color) h-min py-1.5 px-2.5 text-sm flex items-center justify-center bg-gray-100 w-fit";
const detailsButton =
    "flex gap-2 hover:bg-(--primary-dark) cursor-pointer items-center rounded-lg text-white justify-center px-2 py-2 bg-(--primary-color)";
const cancelButton =
    "flex gap-2 cursor-pointer items-center rounded-lg justify-center px-2 py-2 " +
    "border border-(--danger) text-(--danger) bg-white hover:bg-(--danger) hover:text-white";
const avatarClass =
    "h-10 w-10 shrink-0 border border-solid border-blue-500 bg-blue-200";
const avatarFallbackClass =
    "bg-(--primary-bg) text-(--primary-color) font-semibold";
const dialogHeader = "font-bold text-xl text-[var(--text-color)]";
const dialogText = "text-[var(--text-light)] text-lg font-normal";
const dialogFooter = "mt-2";
const dialogCancel =
    "bg-white text-(--gray-600) border border-(--gray-400) hover:bg-(--gray-100) hover:border-(--gray-500-) hover:text-(--text-color) cursor-pointer transition-colors";
const dialogConfirm =
    "text-white bg-[var(--danger)] border border-[var(--danger)] hover:text-white hover:bg-[var(--danger-dark)] hover:border hover:border-[var(--danger-dark)] cursor-pointer";

type AppointmentCardProps = {
    appointment: AppointmentDTO;
    isUpcoming?: boolean;
    mounted: boolean;
    animationDelay: number
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

export default function AppointmentCard({ appointment, isUpcoming = false, mounted, animationDelay }: AppointmentCardProps) {
    const { t, i18n } = useTranslation();
    const navigate = useNavigate();
    const auth = useAuth();

    const cardContainer =
        `w-full border border-solid rounded-md overflow-hidden shadow flex flex-col sm:flex-row sm:items-stretch transition-all ${mounted ? 'opacity-100 translate-x-0' : 'opacity-0 -translate-x-4'}`;

    const isDoctor = auth.role === "ROLE_DOCTOR";
    const doctorId = userIdFromSelf(appointment.doctor);

    const { data: patient, isLoading: loadingPatient, isError: errorPatient } = usePatient(appointment.patient);
    const { data: doctor, isLoading: loadingDoctor, isError: errorDoctor } = useDoctor(doctorId);
    const { url, isLoading: isLoadingImg } = useDoctorImageUrl(isDoctor ? undefined : doctorId);
    const doctorImgUrl = isDoctor ? undefined : url;
    const loadingDoctorImg = isDoctor ? false : isLoadingImg;
    const isError = errorDoctor || errorPatient;
    const { data: specialty, isLoading: loadingSpecialty, isError: errorSpecialty } = useSpecialty(appointment.specialty);
    const { data: coverage, isLoading: loadingCoverage, isError: errorCoverage } = useCoverage(patient?.coverage);

    const locale = i18n?.language || "en-US";

    const d = new Date(appointment.date);
    const weekday = new Intl.DateTimeFormat(locale, { weekday: "long" }).format(d).toUpperCase();
    const day = d.getDate();
    const month = new Intl.DateTimeFormat(locale, { month: "long" }).format(d).toUpperCase();
    const time = new Intl.DateTimeFormat(locale, { hour: "2-digit", minute: "2-digit", hour12: false }).format(d);

    const displayName = isDoctor
        ? `${patient?.name ?? ""} ${patient?.lastName ?? ""}`.trim()
        : `${doctor?.name ?? ""} ${doctor?.lastName ?? ""}`.trim();

    const fallbackText = isDoctor
        ? initialsFallback(patient?.name, patient?.lastName)
        : initialsFallback(doctor?.name, doctor?.lastName);

    const status = "appointment.filters." + transformStatus(appointment.status);
    const base = isDoctor ? "/doctor/dashboard" : "/patient/dashboard";

    const cancelMutation = useCancelAppointmentMutation();
    const appointmentId = appointmentIdFromSelf(appointment.self);

    const [cancelOpen, setCancelOpen] = useState(false);

    const isLoading = loadingPatient || loadingDoctor || loadingSpecialty || loadingCoverage;

    if(isLoading) return <Skeleton className={loadingSkeleton}/>
    
    if(isError) return null;

    return (
        <div style={{ transitionDelay: `${animationDelay * 80}ms` }} className={cardContainer}>
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
                    { doctorImgUrl === undefined && loadingDoctorImg ?
                        <Skeleton className={`${avatarClass} flex items-center justify-center rounded-full`}>
                            <Spinner className="text-(--gray-400)"/>
                        </Skeleton>
                    :
                        <Avatar className={avatarClass}>
                            <AvatarImage src={doctorImgUrl || undefined} />
                            <AvatarFallback className={avatarFallbackClass}>{fallbackText}</AvatarFallback>
                        </Avatar>
                    }
                    <div className={nameBlock}>
                        <span className={fullNameText}>{displayName}</span>
                        {errorCoverage ? null :
                            <span className={coverageRow}>
                                <span className={coverageDot} />
                                {coverage?.name}
                            </span>
                        }
                    </div>
                </div>

                <div className={middleRow}>
                    <div className={reasonWrapper}>
                        <div className={reasonBar} />
                        <div className={reasonBox}>
                            <span className={reasonLabel}>{t("appointment.card.reason")}</span>
                            {appointment.reason.trim().length > 0 ? (
                                <div className={reasonTextWrap}>
                                    <span className={reasonText}>{appointment.reason}</span>
                                    <div className={reasonFade} />
                                </div>
                            ) : (
                                <p>-</p>
                            )}
                        </div>
                    </div>
                </div>

                <div className={bottomRow}>
                    {errorSpecialty ? <div/> : <div className={specialtyPill}>{t(specialty?.name || "")}</div>}
                    <div className="flex gap-2 w-full sm:w-auto">
                        {isUpcoming && appointment.cancellable && (
                            <Dialog open={cancelOpen} onOpenChange={setCancelOpen}>
                                <DialogTrigger asChild>
                                    <Button variant="outline" className={cancelButton}>
                                        <X className="w-4 h-4" />
                                        {t("appointment.cancel.cancel")}
                                    </Button>
                                </DialogTrigger>

                                <DialogContent>
                                    <DialogHeader className={dialogHeader}>
                                        <DialogTitle>
                                            {t("appointment.cancel.title")}
                                        </DialogTitle>

                                        <DialogDescription className={dialogText}>
                                            {t("appointment.cancel.subtitle")}
                                        </DialogDescription>
                                    </DialogHeader>

                                    <DialogFooter className={dialogFooter}>
                                        <DialogClose asChild>
                                            <Button
                                                type="button"
                                                className={dialogCancel}
                                                disabled={cancelMutation.isPending}
                                            >
                                                {t("appointment.cancel.back")}
                                            </Button>
                                        </DialogClose>
                                        <Button
                                            type="button"
                                            className={dialogConfirm}
                                            disabled={cancelMutation.isPending || !appointmentId}
                                            onClick={() => {
                                                if (!appointmentId) return;
                                                setCancelOpen(false);
                                                cancelMutation.mutate(
                                                    {
                                                        id: appointmentId,
                                                        userId: String(auth.userId),
                                                    },
                                                    {
                                                        onSuccess: () => {
                                                            toast.success(t("appointment.cancel.success"));
                                                        },
                                                        onError: (error: any) => {
                                                            toast.error(t("appointment.cancel.error"));
                                                        }
                                                    }
                                                );
                                            }}
                                        >
                                            {cancelMutation.isPending ? (
                                                <>
                                                    <Loader className="w-4 h-4 mr-2" />
                                                    {t("appointment.cancel.cancelling")}
                                                </>
                                            ) : (
                                                t("appointment.cancel.confirmation")
                                            )}
                                        </Button>
                                    </DialogFooter>
                                </DialogContent>
                            </Dialog>
                        )}
                        <Button
                            className={detailsButton}
                            onClick={() => navigate(`${base}/appointment-details/${appointmentId}`)}
                        >
                            <EyeIcon />
                            {t("appointment.card.details")}
                        </Button>
                    </div>
                </div>
            </div>
        </div>
    );
}
