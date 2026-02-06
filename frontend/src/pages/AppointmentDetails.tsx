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
    Eye,
    ArrowLeft, X,
    ClipboardClock, ArrowRight
} from "lucide-react";
import { Badge } from "@/components/ui/badge.tsx";
import { Button } from "@/components/ui/button.tsx";
import { RatingStars } from "@/components/RatingStars.tsx";
import { Separator } from "@/components/ui/separator.tsx";
import { UploadFiles } from "@/components/UploadFiles.tsx"
import { useTranslation } from "react-i18next";
import {
    useAppointment,
    useAppointmentFileHandlerMutation,
    useAppointmentFiles, useCancelAppointmentMutation,
    useUpdateReportMutation,
    useUploadDoctorFilesMutation
} from "@/hooks/useAppointments.ts";
import { formatLongDate, formatTimeHM } from "@/utils/dateUtils.ts";
import {useSpecialty} from "@/hooks/useSpecialties.ts";
import {useDoctorOffice} from "@/hooks/useOffices.ts";
import {useNeighborhood} from "@/hooks/useNeighborhoods.ts";
import {userIdFromSelf} from "@/utils/IdUtils.ts";
import {useAuth} from "@/hooks/useAuth.ts";
import DoctorProfileCard from "@/components/DoctorProfileCard.tsx";
import type {AppointmentFileDTO} from "@/data/appointments.ts";
import React, {useEffect, useMemo, useState} from "react";
import {useRating, useCreateRating} from "@/hooks/useRatings.ts";
import {Textarea} from "@/components/ui/textarea.tsx";
import {useParams, useNavigate, Link} from "react-router-dom";
import { toast } from "sonner";
import GenericError from "@/pages/GenericError.tsx";
import {Spinner} from "@/components/ui/spinner.tsx";
import {
    Dialog, DialogClose,
    DialogContent,
    DialogDescription, DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger
} from "@/components/ui/dialog.tsx";
import {useDoctor} from "@/hooks/useDoctors.ts";

const appointmentBackground = "bg-[var(--background-light)] flex justify-center items-start min-h-screen";
const cardContainer = "mt-36 px-5 mx-auto max-w-6xl w-full mb-8";
const appointmentContainer = "p-0 pb-8";
const appointmentHeader = "flex flex-col items-center py-8 rounded-t-xl gap-3 bg-[linear-gradient(135deg,var(--background-light)_0%,var(--landing-light)_100%)]";
const appointmentTitle = "font-bold text-4xl text-center text-[var(--text-color)]";
const appointmentContent = "flex flex-col px-8 gap-4";
const appointmentData = "grid w-full gap-4 grid-cols-1 sm:grid-cols-2 lg:grid-cols-4";
const appointmentButtons = "flex flex-col sm:flex-row gap-4 justify-center items-center mt-4";
const goBackButton = "w-3xs bg-white text-(--primary-color) border border-(--primary-color) hover:bg-(--primary-dark) hover:border hover:border-(--primary-dark) hover:text-white cursor-pointer";
const cancelButton = "w-3xs bg-(--danger) text-white hover:bg-(--danger-dark) hover:text-white cursor-pointer";
const dialogHeader = "font-bold text-xl text-[var(--text-color)]";
const dialogText = "text-[var(--text-light)] text-lg font-normal";
const dialogFooter = "mt-2";
const dialogCancel =
    "bg-white text-[var(--primary-color)] border border-[var(--primary-color)] " +
    "hover:text-white hover:bg-[var(--primary-dark)] hover:border hover:border-[var(--primary-dark)] cursor-pointer";
const dialogConfirm =
    "text-white bg-[var(--danger)] border border-[var(--danger)] hover:text-white hover:bg-[var(--danger-dark)] hover:border hover:border-[var(--danger-dark)] cursor-pointer";


function AppointmentDetails() {
    const { id } = useParams<{ id: string }>();
    const appointmentId = id;

    if (!appointmentId) return null;

    const { t } = useTranslation();
    const auth = useAuth();
    const role = auth.role;
    const isDoctor = role === "ROLE_DOCTOR";
    const navigate = useNavigate();
    const base = isDoctor ? "/doctor/dashboard" : "/patient/dashboard";
    const handleBackToAppointments = () => {
        navigate(base);
    };
    const cancelMutation = useCancelAppointmentMutation();
    const [cancelOpen, setCancelOpen] = useState(false);

    const {
        data: appointment,
        isLoading: loadingAppointment,
        isError: errorAppointment,
        error: appointmentError,
    } = useAppointment(appointmentId);
    const patientId = userIdFromSelf(appointment?.patient);
    const doctorId = userIdFromSelf(appointment?.doctor);
    const { data: doctor, isLoading: loadingDoctor } = useDoctor(doctorId);
    const { data: specialty, isLoading: loadingSpecialty } = useSpecialty(appointment?.specialty);
    const { data: office, isLoading: loadingOffice } = useDoctorOffice(appointment?.doctorOffice);
    const { data: neighborhood } = useNeighborhood(office?.neighborhood);
    const { data: files, isLoading: loadingFiles } = useAppointmentFiles(appointment?.appointmentFiles);
    const { data: rating } = useRating(appointment?.rating);

    const isLoading = loadingAppointment || loadingSpecialty || loadingOffice || loadingFiles || loadingDoctor;
    const isError = errorAppointment;

    const { patientFiles, doctorFiles } = useMemo(() => {
        const all = files ?? [];
        return {
            patientFiles: all.filter((f) => f.uploaderRole === "patient"),
            doctorFiles: all.filter((f) => f.uploaderRole === "doctor"),
        };
    }, [files]);

    const [newRating, setNewRating] = useState<number>(0);

    if (isLoading) {
        return (
            <div className={appointmentBackground}>
                <div className="flex flex-col items-center justify-center h-screen gap-4">
                    <Spinner className="h-12 w-12" />
                    <p className="text-[var(--text-light)] font-medium">{t("common.loading", "Cargando detalles...")}</p>
                </div>
            </div>
        );
    }

    if (isError || !appointment) {
        const status = appointmentError ? (appointmentError as any).response?.status : 404;
        return <GenericError code={status} />;
    }

    const hasRating = typeof rating?.rating === "number" && !Number.isNaN(rating.rating);
    const isCompleted = appointment.status === "completo";
    const isCancelled = appointment.status === "cancelado";
    const showPostVisit = !isCancelled;
    const showRatingBlock = !isCancelled;
    const showUpload = isDoctor && isCompleted;

    return (
        <div className={appointmentBackground}>
            <div className={cardContainer}>
                <Card className={appointmentContainer}>
                    <div className={appointmentHeader}>
                        <h1 className={appointmentTitle}>{t("appointment.details.title")}</h1>
                        <p>{t("appointment.details.subtitle")}</p>
                    </div>
                    <div className={appointmentContent}>
                        {isDoctor ? (
                            <PatientProfileCard patientId={patientId ?? ""} />
                        ) : (
                            <DoctorProfileCard doctor={doctor} />
                        )}

                        <div className={appointmentData}>
                            <StatusCard status={appointment.status} />
                            <DateCard date={appointment.date} />
                            <SpecialtyCard specialty={specialty?.name} />
                            <OfficeCard name={office?.name} neighborhood={neighborhood?.name} />
                        </div>

                        <VisitCard reason={appointment.reason} />

                        <div className="flex flex-col gap-6">
                            <PatientFileCard files={patientFiles} />
                            <MedicalHistoryCard isDoctor={isDoctor} canAccessMedicalHistory={appointment.allowFullHistory} patientId={patientId}/>


                            {showPostVisit ? (
                                isDoctor && !isCompleted && (appointment.report ?? "").trim().length === 0 ? (
                                    <PostVisitLockedComponent/>
                                ) : (
                                    <PostVisitComponent
                                        appointmentId={appointmentId}
                                        files={doctorFiles}
                                        report={appointment.report}
                                        isDoctor={role === "ROLE_DOCTOR"}
                                    />
                                )
                            ) : null}

                            {showRatingBlock ? (
                                hasRating ? (
                                    <RatingComponent rating={rating?.rating} comment={rating?.comment} />
                                ) : isDoctor ? (
                                    <RatingComponent rating={undefined} comment={undefined} />
                                ) : isCompleted ? (
                                    <RateComponent
                                        rating={newRating}
                                        setRating={setNewRating}
                                        appointmentId={appointmentId}
                                    />
                                ) : (
                                    <LockedRateComponent rating={undefined} setRating={undefined} />
                                )
                            ) : null}

                            {showUpload ? <UploadComponent appointmentId={appointmentId} /> : null}
                        </div>

                        <div className={appointmentButtons}>
                            <Button className={goBackButton} onClick={handleBackToAppointments} type="button">
                                <ArrowLeft className="h-5 w-5" />
                                <p>{t("appointment.details.back")}</p>
                            </Button>
                            {!isCancelled && !isCompleted && (
                                <Dialog open={cancelOpen} onOpenChange={setCancelOpen}>
                                    <DialogTrigger asChild>
                                        <Button className={cancelButton} type="button">
                                            <X className="h-5 w-5" />
                                            <p>{t("appointment.cancel.cancel")}</p>
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
                                                        { id: appointmentId, userId: String(auth.userId) },
                                                        {
                                                            onSuccess: () => navigate(base),
                                                        }
                                                    );
                                                }}
                                            >
                                                {cancelMutation.isPending ? (
                                                    <>
                                                        <Spinner className="w-4 h-4 mr-2" />
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
                        </div>
                    </div>
                </Card>
            </div>
        </div>
    );
}


const appointmentComponent = "w-full h-full flex flex-row items-center gap-3 px-6 py-6";
const iconContainer = "bg-[var(--primary-bg)] rounded-full p-4 flex items-center justify-center";
const icon = "w-7 h-7 text-[var(--primary-color)]";
const componentData = "flex flex-col";
const statusText = "text-[var(--text-color)] text-sm font-semibold";
const badgeConfirmed = "bg-[var(--success-light)] text-[var(--success)] border border-[var(--success)]";
const badgeCancelled = "bg-[var(--danger-light)] text-[var(--danger)] border border-[var(--danger)]";
const badgeCompleted = "bg-[var(--landing-light)] text-[var(--primary-color)] border border-[var(--primary-color)]";

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

const dateText = "text-[var(--text-light)] text-sm";

export function DateCard({date}:{date: string}) {
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

const cardIconContainer = "flex flex-row items-center text-[var(--primary-color)] gap-1 mb-2 mt-1";
const cardIcon = "w-5 h-5";
const cardTitle = "text-lg font-semibold";
const cardContent = "px-5 gap-4";

function PatientFileCard({ files }: { files: AppointmentFileDTO[] }) {
    const { t } = useTranslation();

    return (
        <div>
            <div className={cardIconContainer}>
                <FileCheckCorner className={cardIcon}/>
                <h1 className={cardTitle}>{t("appointment.details.patient-files")}</h1>
            </div>

            <Card className={cardContent}>
                {files.length === 0 ? (
                    <FileEmptyComponent />
                ) : (
                    files.map((f) => (
                        <FileComponent key={f.id} file={f} />
                    ))
                )}
            </Card>
        </div>
    );
}

const fileComponent = "flex flex-row items-center p-4 rounded-lg gap-3";
const fileIcon = "text-[var(--primary-color)] h-6 w-6 mx-1";
const fileTitle = "font-[500] text-sm text-[var(--text-color)] truncate max-w-[150px] sm:max-w-xs";
const actionsContainer = "ml-auto flex flex-row gap-2";
const actionButton = "rounded-full p-2 h-9 w-9 flex items-center justify-center transition-colors cursor-pointer";
const viewButton = actionButton + " text-(--primary-color) border border-(--primary-color) hover:bg-(--primary-dark) hover:text-white hover:border-(--primary-dark)";

function FileComponent({ file }: { file: AppointmentFileDTO }) {
    const { t } = useTranslation();
    const { mutate: handleFile, isPending } = useAppointmentFileHandlerMutation();

    return (
        <Card className={fileComponent}>
            <Paperclip className={fileIcon} />
            <h3 className={fileTitle} title={file.fileName}>{file.fileName}</h3>

            <div className={actionsContainer}>
                {file.view && (
                    <Button
                        variant="ghost"
                        size="icon"
                        className={viewButton}
                        title={t("common.view", "Ver")}
                        disabled={isPending}
                        onClick={() => handleFile({
                            url: file.view,
                            action: 'view',
                            fileName: file.fileName
                        })}
                    >
                        {isPending ? <Spinner className="h-5 w-5" /> : <Eye className="h-5 w-5" />}
                    </Button>
                )}

                {file.download && (
                    <Button
                        size="icon"
                        className={actionButton + " text-white bg-[var(--primary-color)] hover:bg-[var(--primary-dark)]"}
                        title={t("common.download", "Descargar")}
                        disabled={isPending}
                        onClick={() => handleFile({
                            url: file.download,
                            action: 'download',
                            fileName: file.fileName
                        })}
                    >
                        {isPending ? <Spinner className="h-5 w-5" /> : <Download className="h-5 w-5" />}
                    </Button>
                )}
            </div>
        </Card>
    );
}

const emptyFileContainer = "flex flex-row items-center justify-center p-4 text-[var(--gray-500)] bg-[var(--gray-100)] rounded-lg gap-2 border border-dashed border-[var(--gray-400)]";

function FileEmptyComponent() {
    const { t } = useTranslation();

    return (
        <div className={emptyFileContainer}>
            <Info />
            <p>{t("appointment.details.no-upload")}</p>
        </div>
    );
}

const medicalHistoryContainer = "px-6";
const medicalHistoryUpper = "flex flex-row items-center gap-2 font-[400] text-(--warning-dark)";
const medicalHistoryLower = "w-full flex flex-row justify-center";
const medicalHistoryButton = "w-3xs cursor-pointer bg-(--primary-color) hover:bg-[var(--primary-dark)] text-white";
const noMedicalHistoryContainer = "flex flex-row items-center gap-2 border border-dashed rounded-xl p-4 bg-(--gray-100) border-(--gray-400)";
const infoIcon = "h-8 w-8 text-[var(--text-light)]";
const noMedicalHistoryText = "text-[var(--text-light)] leading-normal";

function MedicalHistoryCard({canAccessMedicalHistory = false, isDoctor, patientId}:{canAccessMedicalHistory?: boolean, isDoctor?: boolean, patientId?: string}) {
    const { t } = useTranslation();

    return (
        <div>
            <div className={cardIconContainer}>
                <ClipboardClock className={cardIcon}/>
                <h1 className={cardTitle}>{t("appointment.details.medical-history")}</h1>
            </div>
            {canAccessMedicalHistory ? (
                <Card className={medicalHistoryContainer}>
                    <div className={medicalHistoryUpper}>
                        <Info/>
                        <h3>{isDoctor ? t("appointment.details.access.doctor") : t("appointment.details.access.patient")}</h3>
                    </div>
                    {isDoctor && (
                        <div className={medicalHistoryLower}>
                            <Button className={medicalHistoryButton} asChild>
                                <Link to={`/medical-history/${patientId}`}>
                                    {t("appointment.details.access-button")}
                                    <ArrowRight className="ml-2 h-4 w-4" />
                                </Link>
                            </Button>
                        </div>
                    )}
                </Card>
            ) :
            <div className={noMedicalHistoryContainer}>
                <Info className={infoIcon}/>
                <p className={noMedicalHistoryText}>{t("appointment.details.no-access")}</p>
            </div>
            }
        </div>
    );
}

const doctorCommentContainer = "flex flex-row items-center px-5 gap-0 overflow-hidden";
const asteriskIcon = "w-8 h-8 text-[var(--primary-color)] shrink-0 mr-3";
const doctorComment = "text-md text-[var(--text-color)] overflow-hidden text-wrap wrap-break-word";
const noReport = "text-sm text-[var(--text-light)]";
const reportTitle = "text-md font-[500]";
const submitReportButton = "bg-[var(--primary-color)] hover:bg-[var(--primary-dark)] text-white cursor-pointer";

function PostVisitComponent({
                                appointmentId,
                                files,
                                report,
                                isDoctor
                            }: {
    appointmentId: string,
    files: AppointmentFileDTO[],
    report:string,
    isDoctor: boolean;
}) {
    const { t } = useTranslation();
    const { mutate: updateReport, isPending } = useUpdateReportMutation();

    const hasReport = (report ?? "").trim().length > 0;
    const [draft, setDraft] = useState<string>(report ?? "");

    useEffect(() => {
        setDraft(report ?? "");
    }, [report]);

    const canSubmit = isDoctor && !hasReport && draft.trim().length > 0;

    const handleSubmit = () => {
        if (!canSubmit) return;

        updateReport({ id: appointmentId, report: draft }, {
            onSuccess: () => {
                toast.success(t("success.report_saved", "Reporte médico guardado"));
            },
            onError: () => {
                toast.error(t("error.report_failed", "Error al guardar el reporte"));
            }
        });
    };

    return (
        <div>
            <div className={cardIconContainer}>
                <Cross className={cardIcon} />
                <h1 className={cardTitle}>{t("appointment.details.post-visit")}</h1>
            </div>

            <Card className={cardContent}>
                <Card className={doctorCommentContainer}>
                    <Asterisk className={asteriskIcon} />
                    {hasReport ? (
                        <p className={doctorComment}>{report}</p>
                    ) : isDoctor ? (
                        <div className="flex flex-col w-full gap-3 py-3">
                            <p className={reportTitle}>{t("appointment.details.report")}</p>
                            <Textarea
                                value={draft}
                                onChange={(e) => setDraft(e.target.value)}
                                placeholder={t("appointment.details.write-report")}
                                disabled={isPending}
                            />
                            <div className="flex justify-end">
                                <Button
                                    className={submitReportButton}
                                    onClick={handleSubmit}
                                    disabled={!canSubmit || isPending}
                                >
                                    {isPending && <Spinner className="mr-2 h-4 w-4" />}
                                    {t("appointment.details.submit-report")}
                                </Button>
                            </div>
                        </div>
                    ) : (
                        <p className={noReport}>{t("appointment.details.no-report")}</p>
                    )}
                </Card>

                {files.length === 0 ? <FileEmptyComponent /> : files.map((f) => (
                    <FileComponent key={f.id} file={f} />
                ))}
            </Card>
        </div>
    );
}

const postVisitLockedText = " text-[var(--gray-500)]";
const postVisitLockedHover = " cursor-not-allowed bg-[var(--gray-100)]";
const asteriskLockedIcon = "w-8 h-8 text-(--gray-500) shrink-0 mr-3";

function PostVisitLockedComponent() {
    const { t } = useTranslation();

    return (
        <div>
            <div className={cardIconContainer}>
                <Cross className={cardIcon + postVisitLockedText} />
                <h1 className={cardTitle + postVisitLockedText}>
                    {t("appointment.details.post-visit-locked")}
                </h1>
            </div>

            <Card className={cardContent + postVisitLockedHover}>
                <Card className={doctorCommentContainer + " bg-(--gray-100)"}>
                    <Asterisk className={asteriskLockedIcon} />

                    <div className="flex flex-col w-full gap-3 py-3">
                        <p className={reportTitle + postVisitLockedText}>
                            {t("appointment.details.report")}
                        </p>

                        <p className={noReport + postVisitLockedText + " -mt-2"}>
                            {t(
                                "appointment.details.report-locked",
                            )}
                        </p>

                        <Textarea
                            placeholder={t("appointment.details.write-report")}
                            disabled
                        />

                        <div className="flex justify-end">
                            <Button className={submitReportButton + lockedButton} disabled>
                                {t("appointment.details.submit-report")}
                            </Button>
                        </div>
                    </div>
                </Card>
            </Card>
        </div>
    );
}

const ratingContainer = "flex flex-row items-center gap-3";
const ratingNumber = "text-base text-[var(--primary-text)] font-[700] mt-1";

function RatingComponent({ rating, comment }: { rating: number | undefined; comment: string | undefined }) {
    const { t } = useTranslation();

    const hasRating = typeof rating === "number" && !Number.isNaN(rating);
    const hasComment = (comment ?? "").trim().length > 0;

    return (
        <div>
            <div className={cardIconContainer}>
                <Star className={cardIcon} />
                <h1 className={cardTitle}>{t("appointment.details.rating")}</h1>
            </div>
            <Card className={cardContent}>
                {!hasRating ? (
                    <p className={noReport}>{t("appointment.details.no-rating")}</p>
                ) : (
                    <>
                        <p className={hasComment ? "overflow-hidden text-wrap wrap-break-word" : noReport}>
                            {hasComment ? comment : t("appointment.details.no-comment")}
                        </p>
                        <Separator />
                        <div className={ratingContainer}>
                            <RatingStars rating={rating} />
                            <p className={ratingNumber}>{rating}</p>
                        </div>
                    </>
                )}
            </Card>
        </div>
    );
}

const rateText = "text-md font-[500]";

function RateComponent({
                           rating,
                           setRating,
                           appointmentId,
                       }: {
    rating: number;
    setRating: React.Dispatch<React.SetStateAction<number>>;
    appointmentId: string;
}) {
    const { t } = useTranslation();
    const [comment, setComment] = useState("");
    const { mutate: submitRating, isPending } = useCreateRating();

    const handleSubmit = () => {
        if (rating === 0) {
            toast.error(t("error.rating_required", "Debe seleccionar una calificación"));
            return;
        }

        submitRating({
            rating: rating,
            comment: comment,
            appointmentId: appointmentId
        }, {
            onSuccess: () => {
                toast.success(t("appointment.created", "Calificación enviada"));
            },
            onError: () => {
                toast.error(t("error.rating_failed", "Error al enviar la calificación"));
            }
        });
    };

    return (
        <div>
            <div className={cardIconContainer}>
                <Star className={cardIcon} />
                <h1 className={cardTitle}>{t("appointment.details.rate")}</h1>
            </div>
            <Card className={cardContent}>
                <p className={rateText}>{t("appointment.details.rating")}</p>

                <SelectStars
                    value={rating}
                    onChange={setRating}
                    disabled={isPending}
                />

                <p className={rateText}>{t("appointment.details.review")}</p>
                <Textarea
                    placeholder={t("appointment.details.write-review")}
                    value={comment}
                    onChange={(e) => setComment(e.target.value)}
                    disabled={isPending}
                />

                <div className={submitContainer}>
                    <Button
                        className={submitButton}
                        onClick={handleSubmit}
                        disabled={isPending}
                    >
                        {isPending && <Spinner className="mr-2 h-4 w-4" />}
                        {t("appointment.details.submit-rating")}
                    </Button>
                </div>
            </Card>
        </div>
    );
}

const lockedText = " text-[var(--gray-500)]";
const lockedHover = " cursor-not-allowed bg-[var(--gray-100)]";
const lockedButton = " bg-[var(--gray-400)] hover:bg-[var(--gray-200)]";

function LockedRateComponent({
                                 rating,
                                 setRating,
                             }: {
    rating: undefined;
    setRating: undefined;
}) {
    const { t } = useTranslation();

    return (
        <div>
            <div className={cardIconContainer}>
                <Star className={cardIcon + lockedText} />
                <h1 className={cardTitle + lockedText}>{t("appointment.details.rate-locked")}</h1>
            </div>
            <Card className={cardContent + lockedHover}>
                <p className={rateText + lockedText}>{t("appointment.details.rating")}</p>
                <SelectStars value={rating} onChange={setRating} disabled />
                <p className={rateText + lockedText}>{t("appointment.details.review")}</p>
                <Textarea placeholder={t("appointment.details.write-review")} disabled />
                <div className={submitContainer}>
                    <Button className={submitButton + lockedButton} disabled>
                        {t("appointment.details.submit-rating")}
                    </Button>
                </div>
            </Card>
        </div>
    );
}

const starsContainer = "flex flex-row items-center gap-1 mb-2";
const starButton = "rounded-md cursor-pointer transition-transform hover:scale-110 outline-none focus:outline-none focus:ring-0";
const starIconBase = "w-6 h-6";
const starFilled = "fill-[var(--primary-color)] text-[var(--primary-color)]";
const starEmpty = "fill-transparent text-[var(--gray-400)]";

export function SelectStars({
                                value,
                                onChange,
                                disabled = false,
                            }: {
    value?: number;
    onChange?: (value: number) => void;
    disabled?: boolean;
}) {
    const [hover, setHover] = useState<number | null>(null);

    const selected = value ?? 0;
    const active = hover ?? selected;

    return (
        <div
            className={starsContainer}
            onMouseLeave={() => setHover(null)}
            aria-label="Rating selector"
            role="radiogroup"
        >
            {Array.from({ length: 5 }).map((_, i) => {
                const v = i + 1;
                const filled = v <= active;

                return (
                    <button
                        key={v}
                        type="button"
                        className={starButton}
                        disabled={disabled}
                        onMouseEnter={() => !disabled && setHover(v)}
                        onClick={() => !disabled && onChange?.(v)}
                        aria-label={`Rate ${v} star${v === 1 ? "" : "s"}`}
                        aria-checked={v === selected}
                        role="radio"
                    >
                        <Star className={`${starIconBase} ${filled ? starFilled : starEmpty}`} />
                    </button>
                );
            })}
        </div>
    );
}

const uploadTitle = "text-[var(--text-color)] font-[600]";
const submitContainer = "flex w-full justify-center";
const submitButton = "mt-6 w-3xs bg-[var(--primary-color)] text-white hover:bg-[var(--primary-dark)] cursor-pointer";

function UploadComponent({ appointmentId }: { appointmentId: string }) {
    const { t } = useTranslation();
    const [files, setFiles] = useState<File[]>([]);
    const { mutate: uploadFiles, isPending } = useUploadDoctorFilesMutation();

    const handleSubmit = () => {
        if (files.length === 0) return;

        uploadFiles({ id: appointmentId, files }, {
            onSuccess: () => {
                toast.success(t("success.files_uploaded", "Archivos subidos correctamente"));
                setFiles([]);
            },
            onError: () => {
                toast.error(t("error.upload_failed", "Error al subir los archivos"));
            }
        });
    };

    return (
        <div>
            <div className={cardIconContainer + " mt-4"}>
                <CloudUpload className={cardIcon}/>
                <h1 className={uploadTitle}>{t("appointment.details.upload")}</h1>
            </div>

            <UploadFiles onChange={setFiles} />

            <div className={submitContainer}>
                <Button
                    className={submitButton}
                    onClick={handleSubmit}
                    disabled={files.length === 0 || isPending}
                >
                    {isPending && <Spinner className="mr-2 h-4 w-4" />}
                    {t("appointment.details.submit")}
                </Button>
            </div>
        </div>
    );
}

export default AppointmentDetails;