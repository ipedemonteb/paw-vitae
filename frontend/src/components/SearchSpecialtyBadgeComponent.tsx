import {HoverCard, HoverCardContent, HoverCardTrigger} from "@/components/ui/hover-card.tsx";
import {Badge} from "@/components/ui/badge.tsx";
import {Stethoscope} from "lucide-react";
import type {SpecialtyDTO} from "@/data/specialties.ts";
import {useTranslation} from "react-i18next";

const dataSpecialty =
    "flex flex-row text-sm items-center gap-1 text-[var(--primary-color)] mb-1";
const dataIcon =
    "h-4 w-4";

type SearchSpecialtyBadgeComponentProps = {
    specialties: SpecialtyDTO[],
    maxDisplay: number
}

export default function SearchSpecialtyBadgeComponent({specialties, maxDisplay}: SearchSpecialtyBadgeComponentProps) {
    const {t} = useTranslation()
    return (
       <div className="flex flex-row items-center text-ellipsis gap-2">
           {specialties.slice(0, maxDisplay).map(s => (
               <div className={dataSpecialty}>
                   <Stethoscope className={dataIcon} />
                   <p>{t(s.name)}</p>
               </div>
           ))}
           {specialties.length > 3 && (
               <HoverCard openDelay={100}>
                   <HoverCardTrigger className="cursor-default  flex items-center" asChild>
                       <Badge className="text-[0.55rem]  flex items-center justify-center bg-white text-(--primary-color)  border border-(--primary-color) hover:border hover:bg-(--primary-color) hover:text-white">
                           +{specialties.length - 3}
                       </Badge>
                   </HoverCardTrigger>
                   <HoverCardContent className="w-auto max-w-xs p-3">
                       <div className="flex flex-wrap gap-1">
                           {specialties.slice(3).map(s => (
                               <div className={dataSpecialty}>
                                   <Stethoscope className={dataIcon} />
                                   <p>{t(s.name)}</p>
                               </div>
                           ))}
                       </div>
                   </HoverCardContent>
               </HoverCard>
           )}
       </div>
    )
}