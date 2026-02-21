import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar.tsx";
import { Mail, Phone } from "lucide-react";
import { RatingStars } from "@/components/RatingStars.tsx";
import BadgeComponent from "@/components/BadgeComponent.tsx";
import { Card } from "@/components/ui/card.tsx";
import { useDoctorImageUrl, useDoctorSpecialties } from "@/hooks/useDoctors.ts";
import { useSpecialtiesByUrl } from "@/hooks/useSpecialties.ts";
import { initialsFallback } from "@/utils/userUtils.ts";
import type { DoctorDTO } from "@/data/doctors.ts";
import { userIdFromImageUrl } from "@/utils/IdUtils.ts";
import { Skeleton } from "@/components/ui/skeleton.tsx";
import { Spinner } from "@/components/ui/spinner.tsx";
import { LoadingSpecialties } from "@/components/ui/loading-badges.tsx";

const profileCard = "flex flex-col gap-0 items-center sm:flex-row";
const avatarContainer = "flex items-center w-20 h-20 mx-6 mb-2 border border-[var(--primary-light)] border-4 rounded-full sm:mb-0";
const userDataContainer = "flex flex-col items-center gap-1 sm:items-start";
const userName = "text-[var(--text-color)] text-xl font-[700]";
const dataContainer = "flex flex-row gap-5 text-sm text-[var(--text-light)]";
const contactData = "flex flex-row items-center gap-1";
const contactIcon = "w-4 h-4";
const ratingContent = "flex flex-row items-center gap-2 font-bold";
const ratingText = "font-medium text-[var(--text-light)]";

function DoctorProfileCard({ doctor }: { doctor: DoctorDTO | undefined }) {

    const { url: getDoctorImgUrl, isLoading: isLoadingImage } = useDoctorImageUrl(userIdFromImageUrl(doctor?.image));

    const { data: specialtyRefs, isLoading: isLoadingRefs, isError: isErrorRefs } = useDoctorSpecialties(doctor?.specialties);

    const specialtyUrls = specialtyRefs?.map(ref => ref.self);

    const { data: specialtiesQueries, isLoading: isLoadingDetails, isError: isErrorDetails } = useSpecialtiesByUrl(specialtyUrls);

    const isLoadingSpecialties = isLoadingRefs || isLoadingDetails;
    const isErrorSpecialties = isErrorRefs || isErrorDetails;

    const avatarFallbackText = initialsFallback(doctor?.name, doctor?.lastName);

    const specialtiesList: string[] = (specialtiesQueries ?? [])
        .map(q => q.data?.name)
        .filter((name): name is string => !!name);

    const maxBadges = 4;

    return (
        <Card className={profileCard}>
            {isLoadingImage && getDoctorImgUrl != undefined ?
                <Skeleton className={`${avatarContainer} flex justify-center items-center`}>
                    <Spinner className="h-6 w-6 text-(--gray-400)" />
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
                        <p className="max-w-37.5 truncate sm:max-w-75 sm:truncate">{doctor?.email}</p>
                    </div>
                    <div className={contactData}>
                        <Phone className={contactIcon} />
                        <p>{doctor?.phone}</p>
                    </div>
                </div>
                {(doctor?.ratingCount ?? 0) > 0 && (
                    <div className={ratingContent}>
                        <RatingStars rating={doctor?.rating || 0} sizeClassName="h-4 w-4" />
                        <p>{doctor?.rating.toPrecision(2)}</p>
                        <p className={ratingText}>({doctor?.ratingCount})</p>
                    </div>
                )}
                {isErrorSpecialties ? null :
                    isLoadingSpecialties ? (
                            <LoadingSpecialties badgesCount={4} />
                        ) :
                        <BadgeComponent specialties={specialtiesList} maxBadges={maxBadges} />
                }
            </div>
        </Card>
    );
}

export default DoctorProfileCard;