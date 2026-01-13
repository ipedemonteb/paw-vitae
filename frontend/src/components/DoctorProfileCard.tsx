import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar.tsx";
import { Mail, Phone } from "lucide-react";
import { RatingStars } from "@/components/RatingStars.tsx";
import BadgeComponent from "@/components/BadgeComponent.tsx";
import { Card } from "@/components/ui/card.tsx";
import {useDoctor, useDoctorImageUrl, useDoctorSpecialties} from "@/hooks/useDoctors.ts";
import {Skeleton} from "@/components/ui/skeleton.tsx";
import {Spinner} from "@/components/ui/spinner.tsx";
import {initialsFallback} from "@/utils/userUtils.ts";

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
const ratingContent =
    "flex flex-row items-center gap-2 font-bold";
const ratingText =
    "font-medium text-[var(--text-light)]";

function DoctorProfileCard( { doctorId } : { doctorId: string } ) {

    const { data: doctor, isLoading, isError  } = useDoctor(doctorId);
    const { url: getDoctorImgUrl } = useDoctorImageUrl(doctorId);
    const shouldFetchSpecialties = !!doctor?.specialties;
    const { data: specialties } = useDoctorSpecialties(doctor?.specialties, {
        enabled: shouldFetchSpecialties,
    });

    if (isLoading) {
        return (
            <Skeleton className={profileCard + "w-full h-42 justify-center items-center"}>
                <Spinner className="h-8 w-8 text-(--text-light)"/>
            </Skeleton>
        );
    }

    if (isError || !doctor) {
        return (
            //TODO: handle in another way
            <Card className={profileCard}>
                <div className="px-6 py-4 text-[var(--danger)]">No se pudo cargar el paciente.</div>
            </Card>
        );
    }
    const avatarFallbackText = initialsFallback(doctor.name, doctor.lastName);
    const specialtiesList: string[] = (specialties ?? []).map((s) => s.name);

    const maxBadges = 4;

    return (
        <Card className={profileCard}>
            <Avatar className={avatarContainer}>
                <AvatarImage src={getDoctorImgUrl || undefined} />
                <AvatarFallback>{avatarFallbackText}</AvatarFallback>
            </Avatar>
            <div className={userDataContainer}>
                <h1 className={userName}>{doctor.name + " " + doctor.lastName}</h1>
                <div className={dataContainer}>
                    <div className={contactData}>
                        <Mail className={contactIcon} />
                        <p className="max-w-[150px] truncate sm:max-w-[300px] sm:truncate">{doctor.email}</p>
                    </div>
                    <div className={contactData}>
                        <Phone className={contactIcon} />
                        <p>{doctor.phone}</p>
                    </div>
                </div>
                <div className={ratingContent}>
                    <RatingStars rating={doctor.rating} sizeClassName="h-4 w-4" />
                    <p>{doctor.rating}</p>
                    <p className={ratingText}>({doctor.ratingCount})</p>
                </div>
                <BadgeComponent specialties={specialtiesList} maxBadges={maxBadges} />
            </div>
        </Card>
    );
}

export default DoctorProfileCard;