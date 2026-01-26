import type {OfficeDTO} from "@/data/office.ts";
import {Building2, MapPinIcon, SquarePen, Circle} from "lucide-react";
import {useNeighborhood} from "@/hooks/useNeighborhoods.ts";
import {Skeleton} from "@/components/ui/skeleton.tsx";
import {useTranslation} from "react-i18next";
import RemoveOfficeAlertDialog from "@/components/RemoveOfficeAlertDialog.tsx";
import {useUpdateOfficeMutation} from "@/hooks/useDoctors.ts";
import {toast} from "sonner";
import {useEffect, useState} from "react";

const baseStatusClassName = " rounded-xl font-normal border text-xs py-1 px-2 flex items-center justify-center gap-1 "
const activeClassName = "bg-green-100 border-green-400 text-black text-green-600" + baseStatusClassName
const inactiveClassName = "bg-gray-100 border-gray-300 text-(--text-light) " + baseStatusClassName

const fillClassName = " size-3"
const activeFillClassName = "fill-green-500 text-green-500 animate-pulse" + fillClassName
const inactiveFillClassName = "fill-gray-300 text-gray-300" + fillClassName

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
    onClick?: () => void,
    animateInDelay: number
}
export default function OfficeCard({office, onClick, animateInDelay}: OfficeCardProps) {
    const {data: neighborhood, isLoading: isLoadingNeighborhood} = useNeighborhood(office.neighborhood)
    const {t} = useTranslation()
    const [open, setOpen] = useState(false);
    const removeOfficeMutation = useUpdateOfficeMutation(office.self)
    const [mounted, setMounted] = useState(false);

    useEffect(() => {
        setMounted(false);
        const id = requestAnimationFrame(() => setMounted(true));
        return () => cancelAnimationFrame(id);
    }, [office, onClick]);

    return (
        <div id="container" style={{ transitionDelay: `${animateInDelay * 100}ms` }} onMouseLeave={() => setOpen(false)} className={`flex flex-col justify-center items-center transition-all w-auto ${mounted ? 'opacity-100 translate-y-0 scale-100' : 'opacity-0 translate-y-4 scale-95'}`}>
            <div onMouseEnter={() => setOpen(true)} onClick={onClick} className="relative peer flex transition-all overflow-hidden group hover:border-(--primary-color) hover:text-(--text-light) rounded-2xl cursor-pointer items-center hover:shadow  border-2">
                <div className=" group-hover:opacity-35  transition-opacity text-[1.3rem] flex justify-center-safe items-center overflow-hidden  flex-col font-semibold   rounded-2xl w-full px-8 py-7">
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
                <div className="absolute flex   justify-center transition-all opacity-0 hover:opacity-100 items-center top-0 left-0 w-full h-full bg-transparent hover:bg-[rgba(var(--primary-light-rgb),0.2)]">
                    <div className="flex flex-col  items-center justify-center gap-1 text-[1.1rem] font-bold rounded-md p-1">
                        <SquarePen className="size-10 text-(--primary-dark)" />
                        <p className="text-(--primary-dark) font-semibold text-xl ">{t("offices.dialog.edit.button")}</p>
                    </div>
                </div>
            </div>
            <RemoveOfficeAlertDialog setOpen={setOpen} open={open} officeName={office.name} onClick={() => removeOfficeMutation.mutate(
                {
                removed: true
                }, {
                    onSuccess: () => {
                        toast.success("Office Removed Successfully")
                    },
                    onError: () => {
                        toast.error("An Unexpected Error Occurred")
                    }
                })}/>
        </div>

    )
}