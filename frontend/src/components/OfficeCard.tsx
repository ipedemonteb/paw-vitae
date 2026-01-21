import type {OfficeDTO} from "@/data/office.ts";
import {Building2, MapPinIcon, SquarePen, Circle} from "lucide-react";
import {useNeighborhood} from "@/hooks/useNeighborhoods.ts";
import {Skeleton} from "@/components/ui/skeleton.tsx";
import {useTranslation} from "react-i18next";

const baseStatusClassName = " rounded-xl font-normal text-black border text-xs py-1 px-2 flex items-center justify-center gap-1 "
const activeClassName = "bg-green-100 border-green-400 text-green-600" + baseStatusClassName
const inactiveClassName = "bg-yellow-100 border-yellow-400" + baseStatusClassName

const fillClassName = " animate-pulse size-3"
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
    office: OfficeDTO,
    onClick?: () => void
}
export default function OfficeCard({office, onClick}: OfficeCardProps) {
    const {data: neighborhood, isLoading: isLoadingNeighborhood} = useNeighborhood(office.neighborhood)
    const {t} = useTranslation()
    return (
        <div onClick={onClick} className="relative transition-all h-fit overflow-hidden group rounded-2xl cursor-pointer hover:border-(--primary-color) hover:shadow hover:text-(--text-light)! min-w-56 max-w-56 border-2 hover:-translate-y-1">
            <div className=" group-hover:opacity-35 transition-opacity text-[1.3rem] flex justify-center-safe items-center overflow-hidden  flex-col font-semibold   rounded-2xl w-full px-8 py-7">
                <Building2 className="size-14 stroke-[1.5]"/>
                {office.name}
                <div className="flex text-sm items-center gap-1 pb-3 font-normal text-(--text-light)">
                    <MapPinIcon className="size-4  text-(--primary-color)" />
                    {(neighborhood?.name && !isLoadingNeighborhood) ? neighborhood.name : (
                        <Skeleton className="w-24 h-3.5" />
                    )}
                </div>
                <div className={statusDictionary[office.status]}>
                    <Circle className={fillStatusDictionary[office.status]} />
                    {t("offices.status."+office.status)}
                </div>
            </div>
            <div className="absolute flex  justify-center transition-all opacity-0 hover:opacity-100 items-center top-0 left-0 w-full h-full bg-transparent hover:bg-[rgba(var(--primary-light-rgb),0.2)]">
                <div className="flex flex-col  items-center justify-center gap-1 text-[1.1rem] font-bold rounded-md p-1">
                    <SquarePen className="size-10 text-(--primary-dark)" />
                    <p className="text-(--primary-dark) font-semibold text-xl ">Edit</p>
                </div>
            </div>
        </div>

    )
}