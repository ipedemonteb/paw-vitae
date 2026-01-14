import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar.tsx";
import { Mail, Phone } from "lucide-react";
import { Card } from "@/components/ui/card.tsx";
import {usePatientById} from "@/hooks/usePatients.ts";
import {Skeleton} from "@/components/ui/skeleton.tsx";
import {Spinner} from "@/components/ui/spinner.tsx";
import {initialsFallback} from "@/utils/userUtils.ts";

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

function PatientProfileCard({patientId}:{patientId: string}) {

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
            //TODO: handle in another way
            <Card className={profileCard}>
                <div className="px-6 py-4 text-[var(--danger)]">No se pudo cargar el paciente.</div>
            </Card>
        );
    }

    const avatarFallbackText = initialsFallback(patient.name, patient.lastName);

    return (
        <Card className={profileCard}>
            <Avatar className={avatarContainer}>
                <AvatarImage/>
                <AvatarFallback>{avatarFallbackText}</AvatarFallback>
            </Avatar>
            <div className={userDataContainer}>
                <h1 className={userName}>{patient.name + " " + patient.lastName}</h1>
                <div className={dataContainer}>
                    <div className={contactData}>
                        <Mail className={contactIcon} />
                        <p className="max-w-[150px] truncate sm:max-w-[300px] sm:truncate">{patient.email}</p>
                    </div>
                    <div className={contactData}>
                        <Phone className={contactIcon} />
                        <p>{patient.phone}</p>
                    </div>
                </div>
            </div>
        </Card>
    );
}

export default PatientProfileCard;