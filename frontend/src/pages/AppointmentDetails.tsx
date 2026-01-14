import { Card } from "@/components/ui/card.tsx"
import PatientProfileCard from "@/components/PatientProfileCard.tsx";
import {
    ClipboardCheck,
    Calendar,
    Stethoscope,
    MessageCircle,
    Hospital,
    FileCheckCorner,
    Paperclip,
    Download,
    Info,
    Cross,
    Asterisk,
    Star,
    CloudUpload,
} from "lucide-react";
import { Badge } from "@/components/ui/badge.tsx";
import { Button } from "@/components/ui/button.tsx";
import { RatingStars } from "@/components/RatingStars.tsx";
import { Separator } from "@/components/ui/separator.tsx";
import { UploadFiles } from "@/components/UploadFiles.tsx"
import { useTranslation } from "react-i18next";
import { useAppointment } from "@/hooks/useAppointments.ts";
import { formatLongDate, formatTimeHM } from "@/utils/dateUtils.ts";
import {useSpecialty} from "@/hooks/useSpecialties.ts";
import {useDoctorOffice} from "@/hooks/useDoctors.ts";
import {useNeighborhood} from "@/hooks/useNeighborhoods.ts";
import {userIdFromSelf} from "@/utils/IdUtils.ts";
import {useAuth} from "@/hooks/useAuth.ts";
import DoctorProfileCard from "@/components/DoctorProfileCard.tsx";

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
const appointmentData =
    "grid w-full gap-4 grid-cols-1 sm:grid-cols-2 lg:grid-cols-4";

function AppointmentDetails() {
    const appointmentId = "33";

    const { t } = useTranslation();
    const auth = useAuth();
    const role = auth.role;

    // TODO: handle isLoading
    const {data: appointment} = useAppointment(appointmentId);
    const {data: specialty} = useSpecialty(appointment?.specialty ?? null);
    const {data: office} = useDoctorOffice(appointment?.doctorOffice ?? null);
    const {data: neighborhood} = useNeighborhood(office?.neighborhood ?? null);

    const patientId = userIdFromSelf(appointment?.patient ?? "");
    const doctorId = userIdFromSelf(appointment?.doctor ?? "");

    if (!appointment) return <div>Loading...</div>;

    return (
            <div className={appointmentBackground}>
                <div className={cardContainer}>
                    <Card className={appointmentContainer}>
                        <div className={appointmentHeader}>
                            <h1 className={appointmentTitle}>{t("appointment.details.title")}</h1>
                            <p>{t("appointment.details.subtitle")}</p>
                        </div>
                        <div className={appointmentContent}>
                            {role === "ROLE_DOCTOR" ?
                                <PatientProfileCard patientId={patientId ?? ""}/> :
                                <DoctorProfileCard doctorId={doctorId ?? ""}/>
                            }
                            <div className={appointmentData}>
                                <StatusCard status={appointment.status} />
                                <DateCard date={appointment.date}/>
                                <SpecialtyCard specialty={specialty?.name}/>
                                <OfficeCard name={office?.name} neighborhood={neighborhood?.name}/>
                            </div>
                            <VisitCard reason={appointment.reason}/>
                            <PatientFileCard />
                            <PostVisitComponent />
                            <RatingComponent />
                            <UploadComponent />
                        </div>
                    </Card>
                </div>
            </div>
    )
}

const appointmentComponent =
    "w-full h-full flex flex-row items-center gap-3 px-6 py-6";
const iconContainer =
    "bg-[var(--primary-bg)] rounded-full p-4 flex items-center justify-center";
const icon =
    "w-7 h-7 text-[var(--primary-color)]";
const componentData =
    "flex flex-col";
const statusText =
    "text-[var(--text-color)] text-sm font-semibold";
const badgeConfirmed =
    "bg-[var(--success-light)] text-[var(--success)] border border-[var(--success)]";
const badgeCancelled =
    "bg-[var(--danger-light)] text-[var(--danger)] border border-[var(--error)]";
const badgeCompleted =
    "bg-[var(--landing-light)] text-[var(--primary-color)] border border-[var(--primary-color)]";

function StatusCard({ status }: { status: "completo" | "cancelado" | "confirmado" }) {

    const { t } = useTranslation();
    const translatedStatus = t(`appointment.details.status.${status}`);
    const badgeByStatus = {
        confirmado: badgeConfirmed,
        cancelado: badgeCancelled,
        completo: badgeCompleted,
    } as const;

    return (
        <Card className={appointmentComponent}>
            <div className={iconContainer}>
                <ClipboardCheck className={icon} />
            </div>
            <div className={componentData}>
                <p className={statusText + " mb-1"}>{t("appointment.details.status.title")}</p>
                <Badge className={badgeByStatus[status]}>{translatedStatus}</Badge>
            </div>
        </Card>
    );
}

const dateText =
    "text-[var(--text-light)] text-sm";

function DateCard({date}:{date: string}) {

    const { t } = useTranslation();

    const locale = typeof navigator === "undefined" ? "es-AR" : navigator.language || "es-AR";

    return (
        <Card className={appointmentComponent}>
            <div className={iconContainer}>
                <Calendar className={icon}/>
            </div>
            <div className={componentData}>
                <p className={statusText}>{t("appointment.details.date")}</p>
                <div>
                    <p className={dateText}>{formatLongDate(date, locale)}</p>
                    <p className={dateText}>{formatTimeHM(date, locale)}</p>
                </div>
            </div>
        </Card>
    );
}

function SpecialtyCard({specialty}:{specialty: string | undefined}) {
    const { t } = useTranslation();

    return (
        <Card className={appointmentComponent}>
            <div className={iconContainer}>
                <Stethoscope className={icon}/>
            </div>
            <div className={componentData}>
                <p className={statusText}>{t("appointment.details.specialty")}</p>
                <p className={dateText}>{t(specialty || "appointment.details.no-specialty")}</p>
            </div>
        </Card>
    );
}

function OfficeCard({name, neighborhood}:{name: string | undefined, neighborhood: string | undefined}) {
    const { t } = useTranslation();

    return (
        <Card className={appointmentComponent}>
            <div className={iconContainer}>
                <Hospital className={icon}/>
            </div>
            <div className={componentData}>
                <p className={statusText}>{t("appointment.details.office")}</p>
                <p className={dateText}>{name + " - " + neighborhood}</p>
            </div>
        </Card>
    );
}

function VisitCard({reason}:{reason?: string}) {
    const { t } = useTranslation();

    return (
        <Card className={appointmentComponent}>
            <div className={iconContainer}>
                <MessageCircle className={icon}/>
            </div>
            <div className={componentData}>
                <p className={statusText}>{t("appointment.details.reason")}</p>
                <p className={dateText}>{reason ?? "-"}</p>
            </div>
        </Card>
    );
}

const cardIconContainer =
    "flex flex-row items-center text-[var(--primary-color)] gap-1 mb-2 mt-1";
const cardIcon =
    "w-5 h-5";
const cardTitle =
    "text-lg font-semibold";
const cardContent =
    "px-5 gap-4";

function PatientFileCard() {
    const { t } = useTranslation();

    return (
        <div>
            <div className={cardIconContainer}>
                <FileCheckCorner className={cardIcon}/>
                <h1 className={cardTitle}>{t("appointment.details.patient-files")}</h1>
            </div>
            <Card className={cardContent}>
                <FileComponent />
                <FileComponent />
                <FileComponent />
                {/*<FileEmptyComponent />*/}
            </Card>
        </div>
    );
}

const fileComponent =
    "flex flex-row items-center p-4 rounded-lg gap-3";
const fileIcon =
    "text-[var(--primary-color)] h-6 w-6 mx-1";
const fileTitle =
    "font-[500] text-sm text-[var(--text-color)]";
const fileDownload =
    "ml-auto rounded-full p-5 flex items-center justify-center " +
    "text-white bg-[var(--primary-color)] hover:bg-[var(--primary-dark)] cursor-pointer";

// TODO: add file view page?
function FileComponent() {
    return (
        <Card className={fileComponent}>
            <Paperclip className={fileIcon} />
            <h3 className={fileTitle}>Estudio de Sangre - Paciente Rodriguez</h3>
            <Button className={fileDownload}>
                <Download />
            </Button>
        </Card>
    );
}

const emptyFileContainer =
    "flex flex-row items-center justify-center p-4 text-[var(--gray-500)] " +
    " bg-[var(--gray-100)] rounded-lg gap-2 border border-dashed border-[var(--gray-400)]";

function FileEmptyComponent() {
    const { t } = useTranslation();

    return (
        <div className={emptyFileContainer}>
            <Info />
            <p>{t("appointment.details.no-upload")}</p>
        </div>
    );
}

const doctorCommentContainer =
    "flex flex-row items-center px-5 gap-0";
const asteriskIcon =
    "w-8 h-8 text-[var(--primary-color)] shrink-0 mr-3";
const doctorComment =
    "text-md text-[var(--text-color)]";

function PostVisitComponent() {
    const { t } = useTranslation();

    return (
        <div>
            <div className={cardIconContainer}>
                <Cross className={cardIcon}/>
                <h1 className={cardTitle}>{t("appointment.details.post-visit")}</h1>
            </div>
            <Card className={cardContent}>
                <Card className={doctorCommentContainer}>
                    <Asterisk className={asteriskIcon} />
                    <p className={doctorComment}>El paciente efectivamente, tiene dolor de cabeza, adjunto archivos de estudios.</p>
                </Card>
                <FileEmptyComponent />
            </Card>
        </div>
    );
}

const ratingContainer =
    "flex flex-row items-center gap-3";
const ratingNumber =
    "text-base text-[var(--primary-text)] font-[700] mt-1";

function RatingComponent() {
    const { t } = useTranslation();

    const rating = 3;

    return (
        <div>
            <div className={cardIconContainer}>
                <Star className={cardIcon}/>
                <h1 className={cardTitle}>{t("appointment.details.rating")}</h1>
            </div>
            <Card className={cardContent}>
                <p>Me atendió demasiado rapido, no me prestó atención</p>
                <Separator />
                <div className={ratingContainer}>
                    <RatingStars rating={rating} />
                    <p className={ratingNumber}>{rating}</p>
                </div>
            </Card>
        </div>
    );
}

const uploadTitle =
    "text-[var(--text-color)] font-[600]";
const submitContainer =
    "flex w-full justify-center";
const submitButton =
    "mt-6 w-3xs bg-[var(--primary-color)] text-white hover:bg-[var(--primary-dark)] cursor-pointer";

function UploadComponent() {
    const { t } = useTranslation();

    return (
        <div>
            <div className={cardIconContainer + " mt-4"}>
                <CloudUpload className={cardIcon}/>
                <h1 className={uploadTitle}>{t("appointment.details.upload")}</h1>
            </div>
            <UploadFiles />
            <div className={submitContainer}>
                <Button className={submitButton}>
                    {t("appointment.details.submit")}
                </Button>
            </div>
        </div>
    );
}

export default AppointmentDetails;