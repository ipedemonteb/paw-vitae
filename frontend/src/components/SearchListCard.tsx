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

const cardContainer =
    "flex flex-col items-stretch p-0 sm:flex-row";
const iconContainer =
    "flex items-center justify-center px-8 py-6 border-b sm:py-0 sm:border-b-0 sm:border-r";
const icon =
    "w-16 h-16";
const dataContainer =
    "flex flex-col items-center justify-center px-6 py-2 sm:py-6 sm:px-1 sm:items-start";
const dataName =
    "font-[600]";
const dataIcon =
    "h-4 w-4";
const dataContact =
    "flex flex-row items-center gap-1 text-[var(--text-light)] text-sm mb-1";
const scheduleContainer =
    "flex flex-col justify-center items-stretch gap-2 px-8 py-6 border-t sm:items-center sm:py-4 sm:ml-auto sm:border-t-0 sm:border-l";
const scheduleButton =
    "w-full bg-[var(--primary-color)] border border-[var(--primary-color)] text-white py-2 px-4 hover:bg-[var(--primary-dark)] hover:border-[var(--primary-dark)] cursor-pointer";
const viewProfileButton =
    "w-full bg-white text-[var(--primary-color)] border border-[var(--primary-color)] py-2 px-4 hover:text-white hover:bg-[var(--primary-dark)] hover:border-[var(--primary-dark)] cursor-pointer";
const ratingStars =
    "mt-[10px]";

export type SearchCardProps = {
    doctor: DoctorDTO
}

function SearchListCard({doctor}: SearchCardProps) {
    const avatarFallbackText = initialsFallback(doctor?.name, doctor?.lastName);

    const {t} = useTranslation();

    const specialties = useDoctorSpecialties(doctor.specialties);
    const {url:imageUrl} = useDoctorImageUrl(doctor.self.split("/").pop());
    const doctorId = extractIdFromUrl(doctor.self)
    const profilePath = generatePath("/profile/:id", { id: doctorId })
    const schedulePath = generatePath("/appointment/:id", { id: doctorId })
    return (
        <Card className={cardContainer}>
            <div className={iconContainer}>
                <Avatar className={icon}>
                    <AvatarImage src={imageUrl || undefined} />
                    <AvatarFallback>{avatarFallbackText}</AvatarFallback>
                </Avatar>
            </div>
            <div className={dataContainer}>
                <h3 className={dataName}>{doctor.name} {doctor.lastName}</h3>
                <SearchSpecialtyBadgeComponent specialties={specialties.data || []} maxDisplay={3}/>
                <div className={dataContact}>
                    <Mail className={dataIcon} />
                    <p>{doctor.email}</p>
                </div>
                <div className={dataContact}>
                    <Phone className={dataIcon} />
                    <p>{doctor.phone}</p>
                </div>
                <RatingStars rating={doctor.rating} className={ratingStars} sizeClassName="h-4 w-4"/>
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
