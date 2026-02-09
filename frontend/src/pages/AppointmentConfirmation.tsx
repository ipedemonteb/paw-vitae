import { Card } from "@/components/ui/card.tsx";
import { Button } from "@/components/ui/button.tsx";
import {Check, Mail, Phone, Calendar, Clock, Stethoscope, Hospital, Info} from "lucide-react";
import {useDoctor, useDoctorImageUrl} from "@/hooks/useDoctors.ts";
import {useDoctorOffice} from "@/hooks/useOffices.ts";
import {initialsFallback} from "@/utils/userUtils.ts";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar.tsx";
import {useAppointment} from "@/hooks/useAppointments.ts";
import {useSpecialty} from "@/hooks/useSpecialties.ts";
import {useNeighborhood} from "@/hooks/useNeighborhoods.ts";
import GenericError from "@/pages/GenericError.tsx";
import {formatLongDate} from "@/utils/dateUtils.ts";
import {useTranslation} from "react-i18next";
import {Link, useParams} from "react-router-dom";
import {LoadingFullPageComponent} from "@/components/LoadingFullPageComponent.tsx";
import type {DoctorDTO} from "@/data/doctors.ts";
import {userIdFromSelf} from "@/utils/IdUtils.ts";
import {Skeleton} from "@/components/ui/skeleton.tsx";
import {Spinner} from "@/components/ui/spinner.tsx";
import {useDelayedBoolean} from "@/utils/queryUtils.ts";

const confirmationBackground =
    "bg-[var(--background-light)] flex justify-center items-start min-h-screen";
const cardContainer =
    "mt-36 px-5 mx-auto max-w-6xl w-full mb-8";
const confirmationContainer =
    "p-0 pb-8";
const checkContainer =
    "bg-[var(--success)] rounded-full p-4 text-white mb-4";
const checkIcon =
    "h-10 w-10";
const confirmationHeader =
    "flex flex-col items-center py-8 rounded-t-lg gap-3 px-5 " +
    "bg-[var(--success-light)]";
const confirmationTitle =
    "font-bold text-4xl text-center text-[var(--text-color)]";
const confirmationSubtitle =
    "text-center text-[var(--text-light)] max-w-3xl";
const confirmationContent =
    "flex flex-col px-8 gap-6";
const detailsCard =
    "px-6 gap-0";
const detailsTitle =
    "text-xl font-[600] text-[var(--text-color)] mb-4";
const rowDetails =
    "flex flex-col sm:flex-row gap-2";
const itemContainer =
    "flex flex-row items-center gap-4 sm:w-1/2 sm:justify-start";
const iconContainer =
    "bg-[var(--primary-bg)] p-2 rounded-lg text-[var(--primary-color)]";
const icon =
    "h-8 w-8";
const itemContent =
    "flex flex-col items-start text-left";
const itemTitle =
    "text-md font-[500] text-[var(--text-color)]";
const itemInfo =
    "text-md text-[var(--text-light)]";
const infoContainer =
    "flex flex-row items-center gap-2 text-sm text-[var(--primary-color)] justify-center border border-[var(--primary-color)] rounded-lg p-2 bg-[var(--primary-bg)] mt-1";
const buttonsContainer =
    "flex flex-col sm:flex-row items-center justify-center mt-3 sm:mt-2 gap-4";
const dashboardButton =
    "w-3xs cursor-pointer text-[var(--primary-color)] border border-[var(--primary-color)] bg-white hover:text-white hover:bg-[var(--primary-dark)] hover:border-[var(--primary-dark)]";
const detailsButton =
    "w-3xs cursor-pointer text-white bg-[var(--primary-color)] hover:bg-[var(--primary-dark)]";

function AppointmentConfirmation() {

    const { t } = useTranslation();
    const { id } = useParams();

    const { data: appointment, isError, isLoading } = useAppointment(id);

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
    } = useNeighborhood(office?.neighborhood);

    const doctorId = appointment?.doctor.split('/').pop() || "";

    const {
        data: doctor,
        isLoading: isLoadingDoctor
    } = useDoctor(doctorId)

    const loading = isLoading || isLoadingSpecialty || isLoadingOffice || isLoadingNeighborhood || isLoadingDoctor

    if (useDelayedBoolean(loading)) {
        return <LoadingFullPageComponent/>
    }

    if (isError || !appointment || isErrorSpecialty || isErrorOffice || isErrorNeighborhood) {
        return <GenericError code={404} />;
    }

    const formattedDate = appointment.date
        ? formatLongDate(new Date(appointment.date).toLocaleDateString(undefined, { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' }),locale)
        : "";

    return (
        <div className={confirmationBackground}>
            <div className={cardContainer}>
                <Card className={confirmationContainer}>
                    <div className={confirmationHeader}>
                        <div className={checkContainer}>
                            <Check className={checkIcon}/>
                        </div>
                        <h1 className={confirmationTitle}>{t("appointment.confirmation.title")}</h1>
                        <p className={confirmationSubtitle}>{t("appointment.confirmation.subtitle")}</p>
                    </div>
                    <div className={confirmationContent}>
                        <DoctorConfirmationCard doctor={doctor} />
                        <Card className={detailsCard}>
                            <h3 className={detailsTitle}>{t("appointment.confirmation.details")}</h3>
                            <div className={rowDetails + " mb-2"}>
                                <div className={itemContainer}>
                                    <div className={iconContainer}>
                                        <Calendar className={icon}/>
                                    </div>
                                    <div className={itemContent}>
                                        <p className={itemTitle}>{t("appointment.confirmation.date")}</p>
                                        <p className={itemInfo}>{formattedDate}</p>
                                    </div>
                                </div>
                                <div className={itemContainer}>
                                    <div className={iconContainer}>
                                        <Clock className={icon} />
                                    </div>
                                    <div className={itemContent}>
                                        <p className={itemTitle}>{t("appointment.confirmation.time")}</p>
                                        <p className={itemInfo}>{new Date(appointment.date).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}</p>
                                    </div>
                                </div>
                            </div>
                            <div className={rowDetails}>
                                <div className={itemContainer}>
                                    <div className={iconContainer}>
                                        <Stethoscope className={icon}/>
                                    </div>
                                    <div className={itemContent}>
                                        <p className={itemTitle}>{t("appointment.confirmation.specialty")}</p>
                                        <p className={itemInfo}>{specialty?.name ? t(specialty.name) : "No specialty"}</p>
                                    </div>
                                </div>
                                <div className={itemContainer}>
                                    <div className={iconContainer}>
                                        <Hospital className={icon} />
                                    </div>
                                    <div className={itemContent}>
                                        <p className={itemTitle}>{t("appointment.confirmation.office")}</p>
                                        <p className={itemInfo}>{`${office?.name}, ${neighborhood?.name}`|| "Consultorio"}</p>
                                    </div>
                                </div>
                            </div>
                        </Card>
                        <div className={infoContainer}>
                            <Info />
                            <p>{t("appointment.confirmation.info")}</p>
                        </div>
                        <div className={buttonsContainer}>
                            <Button className={dashboardButton}>
                                <Link to="/patient/dashboard">
                                    {t("appointment.confirmation.dashboard")}
                                </Link>
                            </Button>
                            <Button className={detailsButton}>
                                <Link to={`/patient/dashboard/appointment-details/${id}`}>
                                    {t("appointment.confirmation.view-details")}
                                </Link>
                            </Button>
                        </div>
                    </div>
                </Card>
            </div>
        </div>
    );
}

const profileCard =
    "flex flex-col gap-0 items-center sm:flex-row";
const avatarContainer =
    "flex items-center w-20 h-20 mx-6 mb-2 border border-[var(--primary-light)] border-4 rounded-full sm:mb-0";
const userDataContainer =
    "flex flex-col items-center gap-1 sm:items-start";
const userName =
    "text-[var(--text-color)] text-xl font-[700]";
const dataContainer =
    "flex flex-row gap-5 text-sm text-[var(--text-light)]";
const contactData =
    "flex flex-row items-center gap-1";
const contactIcon =
    "w-4 h-4";

function DoctorConfirmationCard( { doctor } : { doctor: DoctorDTO | undefined} ) {

    const { url: getDoctorImgUrl, isLoading: isLoadingImgUrl } = useDoctorImageUrl(userIdFromSelf(doctor?.self));

    const avatarFallbackText = initialsFallback(doctor?.name, doctor?.lastName);

    return (
        <Card className={profileCard}>
            {isLoadingImgUrl ?
                <Skeleton className={`${avatarContainer} flex justify-center items-center`}>
                    <Spinner className="h-6 w-6 text-(--gray-300)"/>
                </Skeleton>
                : (
                    <Avatar className={avatarContainer}>
                        <AvatarImage src={getDoctorImgUrl || undefined} />
                        <AvatarFallback>{avatarFallbackText}</AvatarFallback>
                    </Avatar>
                )}
            <div className={userDataContainer}>
                <h1 className={userName}>{doctor?.name + " " + doctor?.lastName}</h1>
                <div className={dataContainer}>
                    <div className={contactData}>
                        <Mail className={contactIcon} />
                        <p className="max-w-[150px] truncate sm:max-w-[300px] sm:truncate">{doctor?.email}</p>
                    </div>
                    <div className={contactData}>
                        <Phone className={contactIcon} />
                        <p>{doctor?.phone}</p>
                    </div>
                </div>
            </div>
        </Card>
    );
}

export default AppointmentConfirmation;