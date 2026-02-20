import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Avatar, AvatarImage, AvatarFallback } from "@/components/ui/avatar";
import { Calendar, UserRoundSearch, Mail, Phone} from "lucide-react";
import { RatingStars } from "@/components/RatingStars.tsx";
import type {DoctorDTO} from "@/data/doctors.ts";
import {useDoctorImageUrl, useDoctorSpecialties} from "@/hooks/useDoctors.ts";
import SearchSpecialtyBadgeComponent from "@/components/SearchSpecialtyBadgeComponent.tsx";
import {initialsFallback} from "@/utils/userUtils.ts";
import {generatePath, Link} from "react-router-dom";
import {extractIdFromUrl} from "@/lib/utils.ts";
import {useTranslation} from "react-i18next";
import {useDelayedBoolean} from "@/utils/queryUtils.ts";
import {Skeleton} from "@/components/ui/skeleton.tsx";
import {Spinner} from "@/components/ui/spinner.tsx";
import {userIdFromImageUrl} from "@/utils/IdUtils.ts";

const cardContainer =
    "flex flex-col items-stretch p-0 md:flex-row";
const iconContainer =
    "flex items-center justify-center px-8 py-6 border-b md:py-0 md:border-b-0 md:border-r";
const icon =
    "w-16 h-16";
const dataContainer =
    "flex flex-col items-center justify-center px-6 py-2 md:py-6 md:px-1 md:items-start";
const dataName =
    "font-[600]";
const dataIcon =
    "h-4 w-4";
const dataContact =
    "flex flex-row items-center gap-1 text-[var(--text-light)] text-sm mb-1";
const scheduleContainer =
    "flex flex-col justify-center items-stretch gap-2 px-8 py-6 border-t md:items-center md:py-4 md:ml-auto md:border-t-0 md:border-l";
const scheduleButton =
    "w-full bg-[var(--primary-color)] border border-[var(--primary-color)] text-white py-2 px-4 hover:bg-[var(--primary-dark)] hover:border-[var(--primary-dark)] cursor-pointer";
const viewProfileButton =
    "w-full bg-white text-[var(--primary-color)] border border-[var(--primary-color)] py-2 px-4 hover:text-white hover:bg-[var(--primary-dark)] hover:border-[var(--primary-dark)] cursor-pointer";
const ratingStars =
    "mt-[10px]";
const skeletonBadge =
    "h-4 w-20 rounded-md";

export type SearchCardProps = {
    doctor: DoctorDTO
}

function SearchListCard({doctor}: SearchCardProps) {
    const avatarFallbackText = initialsFallback(doctor?.name, doctor?.lastName);

    const {t} = useTranslation();

    const { data: specialties, isLoading: loadingSpecialties, isError: errorSpecialties } = useDoctorSpecialties(doctor.specialties);
    const { url:imageUrl, isLoading: loadingDoctorImgUrl } = useDoctorImageUrl(userIdFromImageUrl(doctor?.image));
    const doctorId = extractIdFromUrl(doctor.self)
    const profilePath = generatePath("/profile/:id", { id: String(doctorId) })
    const schedulePath = generatePath("/appointment/:id", { id: String(doctorId) })

    return (
        <Card className={cardContainer}>
            <div className={iconContainer}>
                {loadingDoctorImgUrl ?
                    <Skeleton className={`${icon} flex justify-center items-center rounded-full`}>
                        <Spinner className="h-6 w-6 text-(--gray-300)"/>
                    </Skeleton>
                    :
                    <Avatar className={icon}>
                        <AvatarImage src={imageUrl || undefined} />
                        <AvatarFallback>{avatarFallbackText}</AvatarFallback>
                    </Avatar>
                }
            </div>
            <div className={dataContainer}>
                <h3 className={dataName}>{doctor.name} {doctor.lastName}</h3>
                {useDelayedBoolean(loadingSpecialties) ?
                    <div className="flex gap-2 my-1">
                        <Skeleton className={skeletonBadge} />
                        <Skeleton className={skeletonBadge} />
                        <Skeleton className={skeletonBadge} />
                        <Skeleton className="h-4 w-8 rounded-md" />
                    </div>
                    :
                    errorSpecialties ? null
                    :
                    <SearchSpecialtyBadgeComponent specialties={specialties || []} maxDisplay={3}/>
                }
                <div className={dataContact}>
                    <Mail className={dataIcon} />
                    <p>{doctor.email}</p>
                </div>
                <div className={dataContact}>
                    <Phone className={dataIcon} />
                    <p>{doctor.phone}</p>
                </div>
               {doctor.ratingCount > 0 && (
                    <RatingStars rating={doctor.rating} className={ratingStars} sizeClassName="h-4 w-4" />
                )}
            </div>
            <div className={scheduleContainer}>
                <Button asChild className={scheduleButton}>
                    <Link to={schedulePath}>
                        <Calendar className={dataIcon} />
                        {t("search.schedule")}
                    </Link>
                </Button>
                <Button asChild className={viewProfileButton}>
                    <Link to={profilePath}>
                        <UserRoundSearch className={dataIcon} />
                        {t("search.view_profile")}
                    </Link>
                </Button>
            </div>
        </Card>
    );
}

export default SearchListCard;
