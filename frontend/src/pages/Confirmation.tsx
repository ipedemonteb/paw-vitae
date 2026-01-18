import { Card } from "@/components/ui/card.tsx";
import { Button } from "@/components/ui/button.tsx";
import {Check, Mail, Phone, Calendar, Clock, Stethoscope, Hospital, Info} from "lucide-react";
import {useDoctor, useDoctorImageUrl} from "@/hooks/useDoctors.ts";
import {initialsFallback} from "@/utils/userUtils.ts";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar.tsx";

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
    "flex flex-col sm:flex-row items-center justify-center mt-3 sm:mt-6 gap-4";
const dashboardButton =
    "w-3xs cursor-pointer text-[var(--primary-color)] border border-[var(--primary-color)] bg-white hover:text-white hover:bg-[var(--primary-dark)] hover:border-[var(--primary-dark)]";
const detailsButton =
    "w-3xs cursor-pointer text-white bg-[var(--primary-color)] hover:bg-[var(--primary-dark)]";

function Confirmation() {

    const doctorId = "24";

    return (
        <div className={confirmationBackground}>
            <div className={cardContainer}>
                <Card className={confirmationContainer}>
                    <div className={confirmationHeader}>
                        <div className={checkContainer}>
                            <Check className={checkIcon}/>
                        </div>
                        <h1 className={confirmationTitle}>Appointment Confirmed!</h1>
                        <p className={confirmationSubtitle}>Your appointment has been confirmed. Here are the details.</p>
                    </div>
                    <div className={confirmationContent}>
                        <DoctorConfirmationCard doctorId={doctorId} />
                        <Card className={detailsCard}>
                            <h3 className={detailsTitle}>Appointment Details</h3>
                            <div className={rowDetails + " mb-2"}>
                                <div className={itemContainer}>
                                    <div className={iconContainer}>
                                        <Calendar className={icon}/>
                                    </div>
                                    <div className={itemContent}>
                                        <p className={itemTitle}>Date</p>
                                        <p className={itemInfo}>27/01/2026</p>
                                    </div>
                                </div>
                                <div className={itemContainer}>
                                    <div className={iconContainer}>
                                        <Clock className={icon} />
                                    </div>
                                    <div className={itemContent}>
                                        <p className={itemTitle}>Time</p>
                                        <p className={itemInfo}>14:00</p>
                                    </div>
                                </div>
                            </div>
                            <div className={rowDetails}>
                                <div className={itemContainer}>
                                    <div className={iconContainer}>
                                        <Stethoscope className={icon}/>
                                    </div>
                                    <div className={itemContent}>
                                        <p className={itemTitle}>Specialty</p>
                                        <p className={itemInfo}>Endocrinology</p>
                                    </div>
                                </div>
                                <div className={itemContainer}>
                                    <div className={iconContainer}>
                                        <Hospital className={icon} />
                                    </div>
                                    <div className={itemContent}>
                                        <p className={itemTitle}>Office</p>
                                        <p className={itemInfo}>Main - Almagro</p>
                                    </div>
                                </div>
                            </div>
                        </Card>
                        <div className={infoContainer}>
                            <Info />
                            <p>An email has been sent to your inbox with all appointment details.</p>
                        </div>
                        <div className={buttonsContainer}>
                            <Button className={dashboardButton}>
                                Go to Dashboard
                            </Button>
                            <Button className={detailsButton}>
                                View Details
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
    "flex flex-col items-center gap-1 md:items-start";
const userName =
    "text-[var(--text-color)] text-xl font-[700]";
const dataContainer =
    "flex flex-row gap-5 text-sm text-[var(--text-light)]";
const contactData =
    "flex flex-row items-center gap-1";
const contactIcon =
    "w-4 h-4";

function DoctorConfirmationCard( { doctorId } : { doctorId: string | undefined} ) {

    const { url: getDoctorImgUrl } = useDoctorImageUrl(doctorId);
    const { data: doctor } = useDoctor(doctorId);

    const avatarFallbackText = initialsFallback(doctor?.name, doctor?.lastName);

    return (
        <Card className={profileCard}>
            <Avatar className={avatarContainer}>
                <AvatarImage src={getDoctorImgUrl || undefined} />
                <AvatarFallback>{avatarFallbackText}</AvatarFallback>
            </Avatar>
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

export default Confirmation;