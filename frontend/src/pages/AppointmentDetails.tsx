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
import {useAppointment, useAppointmentFiles} from "@/hooks/useAppointments.ts";
import { formatLongDate, formatTimeHM } from "@/utils/dateUtils.ts";
import {useSpecialty} from "@/hooks/useSpecialties.ts";
import {useDoctorOffice} from "@/hooks/useDoctors.ts";
import {useNeighborhood} from "@/hooks/useNeighborhoods.ts";
import {userIdFromSelf} from "@/utils/IdUtils.ts";
import {useAuth} from "@/hooks/useAuth.ts";
import DoctorProfileCard from "@/components/DoctorProfileCard.tsx";
import type {AppointmentFileDTO} from "@/data/appointments.ts";
import React, {useEffect, useMemo, useState} from "react";
import {useRating} from "@/hooks/useRatings.ts";
import {Textarea} from "@/components/ui/textarea.tsx";
import { useParams } from "react-router-dom";

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
    const { id } = useParams<{ id: string }>();
    if (!id) return null;
    const appointmentId = id;

    const { t } = useTranslation();
    const auth = useAuth();
    const role = auth.role;

    //TODO: handle isLoading and isError
    const { data: appointment } = useAppointment(appointmentId);
    const { data: specialty } = useSpecialty(appointment?.specialty ?? null);
    const { data: office } = useDoctorOffice(appointment?.doctorOffice ?? null);
    const { data: neighborhood } = useNeighborhood(office?.neighborhood ?? null);
    const { data: files } = useAppointmentFiles(appointmentId);
    const { data: rating } = useRating(appointment?.rating ?? null);

    const patientId = userIdFromSelf(appointment?.patient ?? "");
    const doctorId = userIdFromSelf(appointment?.doctor ?? "");

    const { patientFiles, doctorFiles } = useMemo(() => {
        const all = files ?? [];
        return {
            patientFiles: all.filter((f) => f.uploaderRole === "patient"),
            doctorFiles: all.filter((f) => f.uploaderRole === "doctor"),
        };
    }, [files]);

    const [newRating, setNewRating] = useState<number>(0);

    if (!appointment) return <div>Loading...</div>;

    const hasRating = typeof rating?.rating === "number" && !Number.isNaN(rating.rating);

    const isDoctor = role === "ROLE_DOCTOR";
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
                            <DoctorProfileCard doctorId={doctorId ?? ""} />
                        )}

                        <div className={appointmentData}>
                            <StatusCard status={appointment.status} />
                            <DateCard date={appointment.date} />
                            <SpecialtyCard specialty={specialty?.name} />
                            <OfficeCard name={office?.name} neighborhood={neighborhood?.name} />
                        </div>

                        <VisitCard reason={appointment.reason} />
                        <PatientFileCard files={patientFiles} />

                        {showPostVisit ? (
                            <PostVisitComponent files={doctorFiles} report={appointment.report} isDoctor={role === "ROLE_DOCTOR"}/>
                        ) : null}

                        {showRatingBlock ? (
                            hasRating ? (
                                <RatingComponent rating={rating?.rating} comment={rating?.comment} />
                            ) : isDoctor ? (
                                <RatingComponent rating={undefined} comment={undefined} />
                            ) : isCompleted ? (
                                <RateComponent rating={newRating} setRating={setNewRating} />
                            ) : (
                                <LockedRateComponent rating={undefined} setRating={undefined} />
                            )
                        ) : null}

                        {showUpload ? <UploadComponent /> : null}
                    </div>
                </Card>
            </div>
        </div>
    );
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
    "bg-[var(--danger-light)] text-[var(--danger)] border border-[var(--danger)]";
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

const cardIconContainer =
    "flex flex-row items-center text-[var(--primary-color)] gap-1 mb-2 mt-1";
const cardIcon =
    "w-5 h-5";
const cardTitle =
    "text-lg font-semibold";
const cardContent =
    "px-5 gap-4";

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
function FileComponent({ file }: { file: AppointmentFileDTO }) {
    const href = file.download || file.view;

    return (
        <Card className={fileComponent}>
            <Paperclip className={fileIcon} />
            <h3 className={fileTitle}>{file.fileName}</h3>

            <Button className={fileDownload} asChild disabled={!href}>
                <a href={href} target="_blank" rel="noreferrer">
                    <Download />
                </a>
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
const noReport =
    "text-sm text-[var(--text-light)]";
const reportTitle =
    "text-md font-[500]";
const submitReportButton =
    "bg-[var(--primary-color)] hover:bg-[var(--primary-dark)] text-white cursor-pointer";

function PostVisitComponent({ files, report, isDoctor }: { files: AppointmentFileDTO[], report:string, isDoctor: boolean; }) {
    const { t } = useTranslation();

    const hasReport = (report ?? "").trim().length > 0;

    const [draft, setDraft] = useState<string>(report ?? "");

    useEffect(() => {
        setDraft(report ?? "");
    }, [report]);

    const canSubmit = isDoctor && !hasReport && draft.trim().length > 0;

    const handleSubmit = () => {
        if (!canSubmit) return;
        // TODO: llamar a tu mutation/endpoint para guardar el report
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
                        <div className="flex flex-col w-full gap-3">
                            <p className={reportTitle}>{t("appointment.details.report")}</p>
                            <Textarea
                                value={draft}
                                onChange={(e) => setDraft(e.target.value)}
                                placeholder={t("appointment.details.write-report")}
                            />
                            <div className="flex justify-end">
                                <Button className={submitReportButton} onClick={handleSubmit} disabled={!canSubmit}>
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

const ratingContainer =
    "flex flex-row items-center gap-3";
const ratingNumber =
    "text-base text-[var(--primary-text)] font-[700] mt-1";

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
                        <p className={hasComment ? "" : noReport}>
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

const rateText =
    "text-md font-[500]";

function RateComponent({
    rating,
    setRating,
}: {
    rating: number;
    setRating: React.Dispatch<React.SetStateAction<number>>;
}) {
    const { t } = useTranslation();

    return (
        <div>
            <div className={cardIconContainer}>
                <Star className={cardIcon} />
                <h1 className={cardTitle}>{t("appointment.details.rate")}</h1>
            </div>
            <Card className={cardContent}>
                <p className={rateText}>{t("appointment.details.rating")}</p>
                <SelectStars value={rating} onChange={setRating} />
                <p className={rateText}>{t("appointment.details.review")}</p>
                <Textarea placeholder={t("appointment.details.write-review")} />
                <div className={submitContainer}>
                    <Button className={submitButton}>
                        {t("appointment.details.submit-rating")}
                    </Button>
                </div>
            </Card>
        </div>
    );
}

const lockedText =
    " text-[var(--gray-500)]";
const lockedHover =
    " cursor-not-allowed bg-[var(--gray-100)]";
const lockedButton =
    " bg-[var(--gray-400)] hover:bg-[var(--gray-200)]";

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

const starsContainer =
    "flex flex-row items-center gap-1 mb-2";
const starButton =
    "rounded-md cursor-pointer transition-transform hover:scale-110 outline-none focus:outline-none focus:ring-0";
const starIconBase =
    "w-6 h-6";
const starFilled =
    "fill-[var(--primary-color)] text-[var(--primary-color)]";
const starEmpty =
    "fill-transparent text-[var(--gray-400)]";

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