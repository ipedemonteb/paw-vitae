import type {DoctorDTO} from "@/data/doctors.ts";
import {ArrowRightIcon, User} from 'lucide-react'
import {useState} from "react";

type SearchResultsCardProps = {
    doctor: DoctorDTO
}
export default function SearchResultsCard({doctor}: SearchResultsCardProps) {
    const [image, setImage] = useState(true)
    return (
        <div className="w-full cursor-pointer relative hover:border hover:border-(--primary-color) h-20 flex items-center justify-between px-4  rounded-md">
            <div className="flex gap-4 h-full items-center justify-baseline">
                {image ? (
                    <img className="w-12 h-12 rounded-full" onError={() => setImage(false)}  onLoad={() => setImage(true)} src={doctor.image} alt="jeje"/>
                ) : (
                    <div className="w-12 h-12 rounded-full flex items-center justify-center">
                        <User size={35}/>
                    </div>
                )}
                <span className="text-lg font-normal">{doctor.name} {doctor.lastName}</span>
            </div>
            <ArrowRightIcon size={20} className="text-(--text-light)"/>
            <div className="absolute top-0 left-0 w-full h-full hover:bg-(--primary-light) hover:opacity-10" />
        </div>
    )
}