import {ChevronDown, ClipboardPenLine, Download, Eye, File, Paperclip, User, Info} from "lucide-react";
import {Button} from "@/components/ui/button.tsx";
import {Card} from "@/components/ui/card.tsx";
import {Badge} from "@/components/ui/badge.tsx";
import {useTranslation} from "react-i18next";
import {useState} from "react";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar.tsx";
import {useAppointmentFileHandlerMutation, useAppointmentFiles} from "@/hooks/useAppointments.ts";
import {userIdFromSelf} from "@/utils/IdUtils.ts";
import {useDoctor, useDoctorImageUrl} from "@/hooks/useDoctors.ts";
import {initialsFallback} from "@/utils/userUtils.ts";
import {useAuth} from "@/hooks/useAuth.ts";
import {usePatient} from "@/hooks/usePatients.ts";
import {useSpecialty} from "@/hooks/useSpecialties.ts";
import {useCoverage} from "@/hooks/useCoverages.ts";
import type {AppointmentDTO} from "@/data/appointments.ts";
import {Skeleton} from "@/components/ui/skeleton.tsx";
import {Spinner} from "@/components/ui/spinner.tsx";

const loadingSkeleton =
    "w-full h-102 sm:h-56";
const appointmentContainer =
    "p-0 border border-solid shadow gap-0";
const upperContainer =
    "w-full rounded-t-xl overflow-hidden flex flex-col sm:flex-row";
const leftSection =
    "bg-gray-100 w-full max-w-none min-w-0 space-y-3 flex flex-col justify-center items-center py-6 sm:py-8 sm:w-1/6 sm:max-w-44 sm:min-w-36 self-stretch rounded-t-md sm:rounded-t-none ";
const dateBlock =
    "text-sm flex flex-col gap-0 items-center justify-center text-center w-full";
const monthText =
    "text-(--text-light)";
const dayText =
    "text-3xl font-bold";
const yearText =
    "text-(--text-light)";
const statusRow =
    "w-full flex justify-center items-center opacity-80";
const badge =
    "bg-transparent text-[var(--primary-color)] border border-[var(--primary-color)] px-2 py-1 rounded-full font-medium";
const middleSection =
    "min-w-0 px-5 py-4 w-full flex flex-col items-center sm:items-start sm:h-full sm:w-5/6 sm:py-6";
const avatarContainer =
    "flex items-center w-12 h-12 border border-[var(--primary-light)] border-2 rounded-full sm:mb-0";
const doctorAvatar =
    "flex flex-row items-center gap-2";
const nameBlock =
    "flex flex-col min-w-0";
const fullNameText =
    "font-semibold";
const coverageRow =
    "text-(--text-light) text-xs flex flex-row items-center gap-2 min-w-0";
const coverageDot =
    "bg-[var(--primary-color)] w-2 h-2 rounded-full shrink-0";
const middleRow =
    "flex w-full py-4";
const reasonWrapper =
    "h-full w-full ml-0";
const reasonBar =
    "w-3 bg-blue-600 h-full rounded-sm shrink-0";
const reasonBox =
    "min-w-0 rounded-sm h-full w-full text-xs bg-gray-50 flex flex-col px-3 justify-center py-3";
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
    "flex gap-1 rounded-2xl font-[500] text-xs text-[var(--gray-600)] h-min py-1.5 px-2.5 text-sm flex items-center justify-center border border-[var(--gray-500)]";
const detailsButton =
    "flex gap-2 text-sm font-[400] hover:bg-(--primary-dark) cursor-pointer items-center text-white justify-center px-2 py-2 bg-(--primary-color)";
const filesIcon =
    "w-4 h-4";
const openContainer =
    "bg-(--gray-100) rounded-b-xl px-6 py-4";
const reportContainer =
    "gap-1 py-4 px-5 overflow-clip";
const reportTitle =
    "flex flex-row items-center gap-1 text-[var(--primary-color)] font-[600]";
const reportIcon =
    "w-5 h-5";
const filesContainer =
    "mt-4 grid gap-4 grid-cols-1 sm:grid-cols-2 lg:grid-cols-3";
const detailsAnim =
    "grid transition-[grid-template-rows] duration-400 ease-in-out";
const detailsClosed =
    "grid-rows-[0fr]";
const detailsOpen =
    "grid-rows-[1fr]";
const detailsInner =
    "overflow-hidden";
const upperBorderWhenOpen =
    "border-b border-[var(--gray-300)]";

function PastAppointmentComponent({appointment} : {appointment: AppointmentDTO}) {
    const { t, i18n } = useTranslation();
    const locale = i18n?.language || "en-US";
    const auth = useAuth();
    const isDoctor = auth.role === "ROLE_DOCTOR";

    const doctorId = userIdFromSelf(appointment?.doctor);

    const { data: patient, isLoading: loadingPatient } = usePatient(appointment.patient);
    const { data: doctor, isLoading: loadingDoctor } = useDoctor(doctorId);
    const { url: doctorImgUrl, isLoading: loadingDoctorImg } = useDoctorImageUrl(doctorId);
    const { data: specialty, isLoading: loadingSpecialty } = useSpecialty(appointment.specialty);
    const { data: coverage, isLoading: loadingCoverage } = useCoverage(patient?.coverages);
    const { data: files, isLoading: loadingFiles } = useAppointmentFiles(appointment.appointmentFiles);

    const [open, setOpen] = useState(false);

    const dateObj = new Date(appointment.date);
    const isValidDate = !Number.isNaN(dateObj.getTime());

    const day = isValidDate ? dateObj.getDate() : "--";
    const month = isValidDate
        ? new Intl.DateTimeFormat(locale, { month: "long" }).format(dateObj).toUpperCase()
        : "--";
    const year = isValidDate ? dateObj.getFullYear() : "--";

    const displayName = isDoctor
        ? `${patient?.name ?? ""} ${patient?.lastName ?? ""}`.trim()
        : `${doctor?.name ?? ""} ${doctor?.lastName ?? ""}`.trim();

    const fallbackText = isDoctor
        ? initialsFallback(patient?.name, patient?.lastName)
        : initialsFallback(doctor?.name, doctor?.lastName);

    const avatarSrc = isDoctor ? undefined : (doctorImgUrl || undefined);

    const fileCount = files?.length ?? 0;

    const isLoading = loadingPatient || loadingDoctor || loadingSpecialty || loadingCoverage || loadingFiles;

    if(isLoading) return <Skeleton className={loadingSkeleton}/>

    return (
        <Card className={appointmentContainer}>
            <div className={`${upperContainer} ${open ? upperBorderWhenOpen : ""}`}>
                <div className={leftSection}>
                    <div className={dateBlock}>
                        <span className={monthText}>{month}</span>
                        <span className={dayText}>{day}</span>
                        <span className={yearText}>{year}</span>
                    </div>
                    <div className={statusRow}>
                        <Badge className={badge}>
                            {t(specialty?.name || "")}
                        </Badge>
                    </div>
                </div>
                <div className={middleSection}>
                    <div className={doctorAvatar}>
                        {loadingDoctorImg ?
                            <Skeleton className={`${avatarContainer} flex items-center justify-center`}>
                                <Spinner className="text-(--gray-400)" />
                            </Skeleton>
                            :
                            <Avatar className={avatarContainer}>
                                <AvatarImage src={avatarSrc} />
                                <AvatarFallback>{fallbackText}</AvatarFallback>
                            </Avatar>
                        }
                        <div className={nameBlock}>
                            <span className={fullNameText}>
                                {displayName}
                            </span>
                            <span className={coverageRow}>
                                <span className={coverageDot} />
                                {coverage?.name}
                            </span>
                        </div>
                    </div>
                    <div className={middleRow}>
                        <div className={reasonWrapper}>
                            <div className={reasonBar} />
                            <div className={reasonBox}>
                                <span className={reasonLabel}>
                                    {t("medical-history.component.reason")}
                                </span>
                                <div className={reasonTextWrap}>
                                    <span className={reasonText}>
                                        {appointment.reason.length > 0 ? appointment.reason : "-"}
                                    </span>
                                    <div className={reasonFade} />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className={bottomRow}>
                        <div className={specialtyPill}>
                            <Paperclip className={filesIcon}/>
                            {`${fileCount} ${fileCount === 1 ? t("medical-history.component.file") : t("medical-history.component.files")}`}
                        </div>
                        <Button
                            className={detailsButton}
                            type="button"
                            onClick={() => setOpen(v => !v)}
                            aria-expanded={open}
                        >
                            <ChevronDown className={open ? "rotate-180 transition-transform" : "transition-transform"} />
                            {t("medical-history.component.details")}
                        </Button>
                    </div>
                </div>
            </div>
            <div className={`${detailsAnim} ${open ? detailsOpen : detailsClosed}`}>
                <div className={detailsInner}>
                    <div className={openContainer}>
                        <Card className={reportContainer}>
                            <div className={reportTitle}>
                                <ClipboardPenLine className={reportIcon}/>
                                <h3>{t("medical-history.component.report")}</h3>
                            </div>
                            <p className={appointment.report.length > 0 ? "overflow-clip text-wrap wrap-break-word" : "text-(--text-light)"}>
                                {appointment.report.length > 0 ? appointment.report : t("medical-history.component.no-report")}
                            </p>
                        </Card>
                        {(files?.length ?? 0) > 0 ? (
                            <div className={filesContainer}>
                                {files!.map((f: any, idx: number) => (
                                    <FileComponent
                                        key={f.self ?? f.view ?? f.download ?? `${idx}`}
                                        name={f.fileName}
                                        uploader={f.uploaderRole}
                                        view={f.view}
                                        download={f.download}
                                    />
                                ))}
                            </div>
                        ) : (
                            <NoFilesComponent />
                        )}
                    </div>
                </div>
            </div>
        </Card>
    );
}

const fileCard =
    "w-full p-4 gap-4 max-h-32 overflow-hidden";
const fileUpperContainer =
    "flex flex-row items-center gap-2 overflow-clip";
const fileIconContainer =
    "bg-[var(--primary-bg)] text-[var(--primary-color)] p-2 rounded-md";
const reportFileTitle =
    "text-md font-[500] leading-tight mb-1 text-ellipsis text-nowrap overflow-clip";
const uploadedByContainer =
    "flex flex-row items-center gap-1 text-[var(--text-light)] text-sm";
const userIcon =
    "w-4 h-4";
const fileLowerContainer =
    "flex flex-row items-center justify-end gap-2";
const viewButton =
    "bg-transparent text-[var(--primary-color)] hover:bg-[var(--primary-dark)] hover:text-white gap-1 " +
    "border border-[var(--primary-color)] hover:border-[var(--primary-dark)] cursor-pointer font-[500] hover:font-[500]";
const downloadButton =
    "bg-[var(--primary-color)] text-white hover:bg-[var(--primary-dark)] " +
    "border border-[var(--primary-color)] hover:border-[var(--primary-dark)] cursor-pointer";

function FileComponent({name, uploader, view, download}:{
        name: string,
        uploader: string,
        view: string,
        download: string
}) {
    const { t } = useTranslation();
    const { mutate: handleFile, isPending } = useAppointmentFileHandlerMutation();

    return (
        <Card className={fileCard}>
            <div className={fileUpperContainer}>
                <div className={fileIconContainer}>
                    <File/>
                </div>
                <div className="overflow-hidden">
                    <h3 className={reportFileTitle}>{name}</h3>
                    <div className={uploadedByContainer}>
                        <User className={userIcon}/>
                        <p>{t("medical-history.component.uploaded")} {t(`${uploader}.${uploader}`)}</p>
                    </div>
                </div>
            </div>
            <div className={fileLowerContainer}>
                <Button
                    className={viewButton}
                    disabled={isPending}
                    onClick={() => handleFile({
                        url: view,
                        action: 'view',
                        fileName: name
                    })}
                >
                    <Eye/>
                    {t("medical-history.component.view")}
                </Button>
                <Button
                    className={downloadButton}
                    disabled={isPending}
                    onClick={() => handleFile({
                        url: download,
                        action: 'download',
                        fileName: name
                    })}
                >
                    <Download/>
                </Button>
            </div>
        </Card>
    );
}

const noFiles =
    "flex flex-row items-center justify-center px-4 py-10 text-[var(--gray-500)] " +
    " bg-[var(--gray-100)] rounded-lg gap-2 border border-dashed border-[var(--gray-400)] mt-4";

function NoFilesComponent() {
    const { t } = useTranslation();

    return (
        <div className={noFiles}>
            <Info />
            <p>{t("medical-history.component.no-files")}</p>
        </div>
    );
}

export default PastAppointmentComponent;