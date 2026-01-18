import type {OfficeDTO} from "@/data/office.ts";
import {Building2, Ellipsis, MapPinIcon, SquarePen, Circle} from "lucide-react";
import {useNeighborhood} from "@/hooks/useNeighborhoods.ts";
import {Skeleton} from "@/components/ui/skeleton.tsx";
import {useDoctorOfficeSpecialties} from "@/hooks/useDoctors.ts";
import {useSpecialtiesByUrl} from "@/hooks/useSpecialties.ts";
import {useTranslation} from "react-i18next";

const baseStatusClassName = " rounded-xl font-normal text-black border text-xs mt-7 py-1 px-4 flex items-center justify-center gap-1"
const activeClassName = "bg-green-100 border-green-400 text-green-600" + baseStatusClassName
const inactiveClassName = "bg-yellow-100 border-yellow-400" + baseStatusClassName

const fillClassName = " animate-pulse size-3.5"
const activeFillClassName = "fill-green-500 text-green-500" + fillClassName
const inactiveFillClassName = "fill-yellow-500 text-yellow-500" + fillClassName

const statusDictionary = {
    active: activeClassName,
    inactive: inactiveClassName
}

const fillStatusDictionary = {
    active: activeFillClassName,
    inactive: inactiveFillClassName
}


type OfficeCardProps = {
    office: OfficeDTO
}
export default function OfficeCard({office}: OfficeCardProps) {
    const {data: neighborhood, isLoading: isLoadingNeighborhood} = useNeighborhood(office.neighborhood)
    const {data: officeSpecialties} = useDoctorOfficeSpecialties(office.officeSpecialties)
    const {data: specialties, isLoading: isLoadingSpecialties} = useSpecialtiesByUrl(officeSpecialties?.map(o => o.specialty))
    const {t} = useTranslation()
    return (
        <div className="flex overflow-hidden hover:text-(--text-light)! relative flex-col text-xl font-semibold items-center border-2 hover:shadow hover:-translate-y-1 transition-all hover:border-(--primary-color) rounded-2xl h-70 w-65 py-2 px-3">
            <Building2 className="size-13"/>
            {office.name}
            <div className="flex text-xs items-center gap-1 pb-3 font-normal text-(--text-light) py-0.5">
                <MapPinIcon className="size-3  text-(--primary-color)" />
                {(neighborhood?.name && !isLoadingNeighborhood) ? neighborhood.name : (
                    <Skeleton className="w-24 h-3.5" />
                )}
            </div>
            <div className={isLoadingSpecialties ? "grid grid-cols-2 gap-y-1 gap-x-0.5" : ""}>
                {(isLoadingSpecialties) ? (
                    Array.from({length: 6}).map((_, i) => (
                        <Skeleton key={i} className="rounded-full h-5 w-24" />
                    ))
                ) : (
                    <div className="flex-wrap flex gap-x-0.5 gap-y-1">
                        {specialties.slice(0, 5).map(s => (
                            <div
                                className= "rounded-2xl font-medium text-(--primary-color) h-min py-1 px-2 text-xs flex items-center justify-center bg-gray-100 w-fit"
                                key={t(s.data?.name!)}
                            >
                                {t(s.data?.name!)}
                            </div>
                        ))}
                        <span className="font-normal cursor-default flex items-center justify-center  text-(--text-light) text-[0.65rem]">
                           <Ellipsis className="size-4" />
                        </span>
                    </div>
                )}

            </div>
            <div className={statusDictionary[office.status]}>
                <Circle className={fillStatusDictionary[office.status]} />
                {t("offices.status."+office.status)}
            </div>
            <div className="absolute flex  justify-center opacity-0 hover:opacity-100 items-center cursor-pointer top-0 left-0 w-full h-full bg-transparent hover:bg-[rgba(var(--primary-light-rgb),0.5)] transition-all">
                <div className="flex flex-col text-(--primary-dark)! items-center justify-center gap-1 text-[1.1rem] font-bold rounded-md p-1">
                    <SquarePen className="size-10 " />
                </div>
            </div>
        </div>
    )
}