import { Card } from "@/components/ui/card"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar.tsx";
import { Calendar, Mail, Phone, UserRoundSearch } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";
import { RatingStars } from "@/components/RatingStars.tsx";
import type {SearchCardProps} from "@/components/SearchListCard.tsx";
import {useDoctorImageUrl, useDoctorSpecialties} from "@/hooks/useDoctors.ts";
import SearchSpecialtyBadgeComponent from "@/components/SearchSpecialtyBadgeComponent.tsx";
import {initialsFallback} from "@/utils/userUtils.ts";
import {generatePath, Link} from "react-router-dom";
import {extractIdFromUrl} from "@/lib/utils.ts";
import {useTranslation} from "react-i18next";
import {useDelayedBoolean} from "@/utils/queryUtils.ts";
import {Skeleton} from "@/components/ui/skeleton.tsx";
import {Spinner} from "@/components/ui/spinner.tsx";

const cardContainer =
    "p-0 gap-0 h-full flex flex-col justify-between ";
const iconContainer =
    "flex items-center justify-center px-8 pt-8 mb-4";
const icon =
    "w-16 h-16";
const dataContainer =
    "flex flex-col justify-center px-10 mb-2";
const dataName =
    "font-[600]";
const dataIcon =
    "h-4 w-4";
const dataContact =
    "flex flex-row items-center gap-1 text-[var(--text-light)] text-sm mb-1";
const ratingStars =
    "mt-[10px] flex items-center gap-[6px]";
const scheduleContainer =
    "flex flex-col justify-center items-stretch gap-2 px-8 py-6";
const scheduleButton =
    "w-full bg-[var(--primary-color)] border border-[var(--primary-color)] text-white py-2 px-4 hover:bg-[var(--primary-dark)] hover:border-[var(--primary-dark)] cursor-pointer";
const viewProfileButton =
    "w-full bg-white text-[var(--primary-color)] border border-[var(--primary-color)] py-2 px-4 hover:text-white hover:bg-[var(--primary-dark)] hover:border-[var(--primary-dark)] cursor-pointer";
const skeletonBadge =
    "h-4 w-20 rounded-md";

function SearchGridCard({doctor}: SearchCardProps) {
    const avatarFallbackText = initialsFallback(doctor?.name, doctor?.lastName);
    const {t} = useTranslation();
    const { data: specialties, isLoading: loadingSpecialties } = useDoctorSpecialties(doctor.specialties);
    const { url:imageUrl, isLoading: loadingDoctorImgUrl } = useDoctorImageUrl(doctor.self.split("/").pop());
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
                        <Skeleton className="h-4 w-8 rounded-md" />
                    </div>
                    :
                    <SearchSpecialtyBadgeComponent specialties={specialties || []} maxDisplay={2}/>
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
    )
}

export default SearchGridCard;