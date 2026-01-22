import type {OfficeDTO} from "@/data/office.ts";
import {Building2, MapPinIcon, SquarePen, Circle, Trash2, AlertTriangleIcon} from "lucide-react";
import {useNeighborhood} from "@/hooks/useNeighborhoods.ts";
import {Skeleton} from "@/components/ui/skeleton.tsx";
import {useTranslation} from "react-i18next";
import {
    AlertDialog, AlertDialogAction, AlertDialogCancel,
    AlertDialogContent, AlertDialogDescription, AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogTrigger
} from "@/components/ui/alert-dialog.tsx";
import {Separator} from "@/components/ui/separator.tsx";
import {Alert, AlertTitle} from "@/components/ui/alert.tsx";

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
    onClick?: () => void
}
export default function OfficeCard({office, onClick}: OfficeCardProps) {
    const {data: neighborhood, isLoading: isLoadingNeighborhood} = useNeighborhood(office.neighborhood)
    const {t} = useTranslation()
    return (
        <div className="flex flex-col group justify-center items-center w-auto">
            <div onClick={onClick} className="relative flex transition-all overflow-hidden group rounded-2xl cursor-pointer hover:border-(--primary-color)  items-center hover:shadow hover:text-(--text-light)! border-2">
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
            <AlertDialog>
                <AlertDialogTrigger asChild>
                    <Trash2 className="rounded-full  group-hover:translate-y-1.5 transition-all opacity-0 group-hover:opacity-100 bg-red-500 p-1 hover:scale-105 cursor-pointer text-white size-7 text-xl" />
                </AlertDialogTrigger>
                <AlertDialogContent>
                    <AlertDialogHeader>
                        <AlertDialogTitle>
                            Confirm Office Removal
                        </AlertDialogTitle>
                        <Separator/>
                        <AlertDialogDescription>
                            <div className="flex flex-col gap-4">
                                <p className="text-black text-[1rem] pt-2">
                                    Are you sure you want to remove this office? This action cannot be undone.
                                </p>
                                <div className="flex flex-col gap-1">
                                    <p className="text-sm text-(--text-light)" >The following office will be removed:</p>
                                    <p className="w-full rounded-md font-semibold  bg-gray-50 border py-3 px-2">
                                        {office.name}
                                    </p>
                                </div>
                                <Alert className=" border-amber-500 bg-amber-100 text-amber-900 dark:border-amber-900 dark:bg-amber-950 dark:text-amber-50">
                                    <AlertTriangleIcon />
                                    <AlertTitle>Removing an office will permanently delete it.</AlertTitle>
                                </Alert>
                            </div>
                        </AlertDialogDescription>
                    </AlertDialogHeader>
                    <AlertDialogFooter>
                        <AlertDialogCancel className="cursor-pointer text-(--text-light)">
                            Cancel
                        </AlertDialogCancel>
                        <AlertDialogAction className="cursor-pointer bg-red-500 hover:bg-red-600" >
                            Confirm
                        </AlertDialogAction>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>
        </div>

    )
}