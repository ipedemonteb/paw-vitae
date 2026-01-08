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
    Asterisk
} from "lucide-react";
import { Badge } from "@/components/ui/badge.tsx";
import { Button } from "@/components/ui/button.tsx";

const appointmentBackground =
    "bg-[var(--background-light)] flex justify-center items-start min-h-screen";
const cardContainer =
    "mt-36 px-5 mx-auto max-w-6xl w-full";
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
    return (
            <div className={appointmentBackground}>
                <div className={cardContainer}>
                    <Card className={appointmentContainer}>
                        <div className={appointmentHeader}>
                            <h1 className={appointmentTitle}>Appointment Details</h1>
                            <p>View and manage your appointment information</p>
                        </div>
                        <div className={appointmentContent}>
                            <PatientProfileCard />
                            <div className={appointmentData}>
                                <StatusCard />
                                <DateCard />
                                <SpecialtyCard />
                                <OfficeCard />
                            </div>
                            <VisitCard />
                            <PatientFileCard />
                            <PostVisitComponent />
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
const badge =
    "bg-[var(--success-light)] text-[var(--success)] border border-[var(--success)]";

function StatusCard() {
    return (
        <Card className={appointmentComponent}>
            <div className={iconContainer}>
                <ClipboardCheck className={icon}/>
            </div>
            <div className={componentData}>
                <p className={statusText + " mb-1"}>Status</p>
                <Badge className={badge}>Confirmed</Badge>
            </div>
        </Card>
    );
}

const dateText =
    "text-[var(--text-light)] text-sm";

function DateCard() {
    return (
        <Card className={appointmentComponent}>
            <div className={iconContainer}>
                <Calendar className={icon}/>
            </div>
            <div className={componentData}>
                <p className={statusText}>Date</p>
                <div>
                    <p className={dateText}>2025-12-30</p>
                    <p className={dateText}>14:00</p>
                </div>
            </div>
        </Card>
    );
}

function SpecialtyCard() {
    return (
        <Card className={appointmentComponent}>
            <div className={iconContainer}>
                <Stethoscope className={icon}/>
            </div>
            <div className={componentData}>
                <p className={statusText}>Specialty</p>
                <p className={dateText}>Dermatology</p>
            </div>
        </Card>
    );
}

function OfficeCard() {
    return (
        <Card className={appointmentComponent}>
            <div className={iconContainer}>
                <Hospital className={icon}/>
            </div>
            <div className={componentData}>
                <p className={statusText}>Office</p>
                <p className={dateText}>Parque Avellaneda</p>
            </div>
        </Card>
    );
}

function VisitCard() {
    return (
        <Card className={appointmentComponent}>
            <div className={iconContainer}>
                <MessageCircle className={icon}/>
            </div>
            <div className={componentData}>
                <p className={statusText}>Reason of Visit (Optional)</p>
                <p className={dateText}>Voy porque me duele la cabeza.</p>
            </div>
        </Card>
    );
}

const patientFileIconContainer =
    "flex flex-row items-center text-[var(--primary-color)] gap-1 mb-2 mt-1";
const patientFileIcon =
    "w-5 h-5";
const patientFileTitle =
    "text-lg font-semibold";
const patientFileContent =
    "px-5 gap-4";

function PatientFileCard() {
    return (
        <div>
            <div className={patientFileIconContainer}>
                <FileCheckCorner className={patientFileIcon}/>
                <h1 className={patientFileTitle}>Patient Files</h1>
            </div>
            <Card className={patientFileContent}>
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
    return (
        <div className={emptyFileContainer}>
            <Info />
            <p>No uploaded files.</p>
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
    return (
        <div>
            <div className={patientFileIconContainer}>
                <Cross className={patientFileIcon}/>
                <h1 className={patientFileTitle}>Post Visit Information</h1>
            </div>
            <Card className={patientFileContent}>
                <Card className={doctorCommentContainer}>
                    <Asterisk className={asteriskIcon} />
                    <p className={doctorComment}>El paciente efectivamente, tiene dolor de cabeza, adjunto archivos de estudios.</p>
                </Card>
                <FileEmptyComponent />
            </Card>
        </div>
    );
}

export default AppointmentDetails;