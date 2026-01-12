import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar.tsx";
import { Mail, Phone } from "lucide-react";
import { Card } from "@/components/ui/card.tsx";
import {usePatientById} from "@/hooks/usePatients.ts";
import {Skeleton} from "@/components/ui/skeleton.tsx";
import {Spinner} from "@/components/ui/spinner.tsx";

const profileCard =
    "flex flex-col gap-0 items-center sm:flex-row";
const avatarContainer =
    "flex items-center w-20 h-20 mx-6 mb-2 border border-[var(--primary-light)] border-4 rounded-full sm:mb-0";
const userDataContainer =
    "flex flex-col items-center md:items-start";
const userName =
    "text-[var(--text-color)] text-xl font-[700] mb-1";
const dataContainer =
    "flex flex-row gap-5 text-sm text-[var(--text-light)]";
const contactData =
    "flex flex-row items-center gap-1";
const contactIcon =
    "w-4 h-4";

function PatientProfileCard() {

    const patientId = "23";

    const { data: patient, isLoading, isError } = usePatientById(patientId);

    if (isLoading) {
        return (
            <Skeleton className={profileCard + "w-full h-32 justify-center items-center"}>
                <Spinner className="h-8 w-8 text-(--text-light)"/>
            </Skeleton>
        );
    }

    if (isError || !patient) {
        return (
            <Card className={profileCard}>
                <div className="px-6 py-4 text-[var(--danger)]">No se pudo cargar el paciente.</div>
            </Card>
        );
    }

    const patientName = patient.name;
    const patientLastName = patient.lastName;
    const patientEmail = patient.email;
    const patientPhone = patient.phone;
    const patientFullName = `${patientName} ${patientLastName}`;

    const avatarFallbackText = (() => {
        const a = patientName?.trim()?.[0] ?? "";
        const b = patientLastName?.trim()?.[0] ?? "";
        return (a + b).toUpperCase() || "U";
    })();

    return (
        <Card className={profileCard}>
            <Avatar className={avatarContainer}>
                <AvatarImage/>
                <AvatarFallback>{avatarFallbackText}</AvatarFallback>
            </Avatar>
            <div className={userDataContainer}>
                <h1 className={userName}>{patientFullName}</h1>
                <div className={dataContainer}>
                    <div className={contactData}>
                        <Mail className={contactIcon} />
                        <p className="max-w-[150px] truncate sm:max-w-[300px] sm:truncate">{patientEmail}</p>
                    </div>
                    <div className={contactData}>
                        <Phone className={contactIcon} />
                        <p>{patientPhone}</p>
                    </div>
                </div>
            </div>
        </Card>
    );
}

export default PatientProfileCard;