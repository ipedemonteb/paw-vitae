import type {DoctorDTO} from "@/data/doctors.ts";
import {ArrowRightIcon} from 'lucide-react'
import {generatePath, Link} from "react-router-dom";
import {extractIdFromUrl} from "@/lib/utils.ts";
import {useDoctorImageUrl} from "@/hooks/useDoctors.ts";
import {userIdFromImageUrl} from "@/utils/IdUtils.ts";
import {Skeleton} from "@/components/ui/skeleton.tsx";
import {Spinner} from "@/components/ui/spinner.tsx";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar.tsx";
import {initialsFallback} from "@/utils/userUtils.ts";

type SearchResultsCardProps = {
    doctor: DoctorDTO
}

const avatarContainer =
    "flex items-center w-12 h-12 border border-[var(--primary-light)] border-4 rounded-full sm:mb-0";

export default function SearchResultsCard({doctor}: SearchResultsCardProps) {

    const doctorId = extractIdFromUrl(doctor.self);
    const profilePath = generatePath("/profile/:id", { id: String(doctorId) });
    const { url: getDoctorImgUrl, isLoading: isLoadingImage } = useDoctorImageUrl(userIdFromImageUrl(doctor?.image));
    const avatarFallbackText = initialsFallback(doctor?.name, doctor?.lastName);


    return (
        <Link to={profilePath} className="w-full cursor-pointer relative hover:border hover:border-(--primary-color) h-20 flex items-center justify-between px-4 rounded-md">
            <div className="flex gap-4 h-full items-center justify-baseline">
                {isLoadingImage && getDoctorImgUrl != undefined  ?
                    <Skeleton className={`${avatarContainer} flex justify-center items-center`}>
                        <Spinner className="h-6 w-6 text-(--gray-400)"/>
                    </Skeleton>
                : (
                    <Avatar className={avatarContainer}>
                        <AvatarImage src={getDoctorImgUrl || undefined} />
                        <AvatarFallback>{avatarFallbackText}</AvatarFallback>
                    </Avatar>
                )}
                <span className="text-lg font-normal">{doctor.name} {doctor.lastName}</span>
            </div>
            <ArrowRightIcon size={20} className="text-(--text-light)"/>
            <div className="absolute top-0 left-0 w-full h-full hover:bg-(--primary-light) hover:opacity-10" />
        </Link>
    )
}