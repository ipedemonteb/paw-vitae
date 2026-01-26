import {
    AlertDialog, AlertDialogAction, AlertDialogCancel,
    AlertDialogContent, AlertDialogDescription, AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogTrigger
} from "@/components/ui/alert-dialog";
import {AlertTriangleIcon, Trash2} from "lucide-react";
import {Separator} from "@/components/ui/separator";
import {Alert, AlertTitle} from "@/components/ui/alert.tsx";
import type {Dispatch, SetStateAction} from "react";
import {useTranslation} from "react-i18next";

type RemoveOfficeAlertDialogProps = {
    officeName: string,
    onClick: () => void,
    open: boolean,
    setOpen: Dispatch<SetStateAction<boolean>>
}

export default function RemoveOfficeAlertDialog({officeName, onClick, open, setOpen}: RemoveOfficeAlertDialogProps) {
    const baseClass = " bg-red-500 rounded-full cursor-pointer transition-all delay-75  gap-1 p-2 flex items-center justify-center  text-white text-xs "
    const openClass = baseClass + "scale-100 opacity-100 translate-y-1.5"
    const closeClass = baseClass + "scale-0 opacity-0 -translate-y-4"
    const {t} = useTranslation()
    return (
        <AlertDialog onOpenChange={() => setOpen(false)}>
            <AlertDialogTrigger asChild>
                <div className={open ? openClass : closeClass}>
                    <Trash2 className="size-4" />
                    {t("offices.dialog.remove.button")}
                </div>
                {/*<Trash2 className="rounded-full  group-hover:translate-y-1.5 transition-all opacity-0 group-hover:opacity-100 bg-red-500 p-1 hover:scale-105 cursor-pointer text-white size-7 text-xl" />*/}
            </AlertDialogTrigger>
            <AlertDialogContent>
                <AlertDialogHeader>
                    <AlertDialogTitle>
                        {t("offices.dialog.remove.title")}
                    </AlertDialogTitle>
                    <Separator/>
                    <AlertDialogDescription>
                        <div className="flex flex-col gap-4">
                            <p className="text-black text-[1rem] pt-2">
                                {t("offices.dialog.remove.description")}
                            </p>
                            <div className="flex flex-col gap-1">
                                <p className="text-sm text-(--text-light)" >{t("offices.dialog.remove.officeConfirmationLabel")}</p>
                                <p className="w-full rounded-md font-semibold text-black  bg-gray-50 border py-3 px-2">
                                    {officeName}
                                </p>
                            </div>
                            <Alert className=" border-amber-500 bg-amber-100 text-amber-900 dark:border-amber-900 dark:bg-amber-950 dark:text-amber-50">
                                <AlertTriangleIcon />
                                <AlertTitle>{t("offices.dialog.remove.alert")}</AlertTitle>
                            </Alert>
                        </div>
                    </AlertDialogDescription>
                </AlertDialogHeader>
                <AlertDialogFooter>
                    <AlertDialogCancel className="cursor-pointer text-(--text-light)">
                        {t("offices.dialog.cancel")}
                    </AlertDialogCancel>
                    <AlertDialogAction onClick={onClick} className="cursor-pointer bg-red-500 hover:bg-red-600" >
                        {t("offices.dialog.remove.confirm")}
                    </AlertDialogAction>
                </AlertDialogFooter>
            </AlertDialogContent>
        </AlertDialog>
    )
}