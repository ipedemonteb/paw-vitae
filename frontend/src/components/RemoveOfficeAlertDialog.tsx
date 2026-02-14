import {
    Dialog, DialogClose, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle
} from "@/components/ui/dialog.tsx";
import {Button} from "@/components/ui/button";
import type {Dispatch, SetStateAction} from "react";
import {useTranslation} from "react-i18next";
import {Spinner} from "@/components/ui/spinner.tsx";

type RemoveOfficeDialogProps = {
    officeName: string;
    onClick: () => void;
    open: boolean;
    setOpen: Dispatch<SetStateAction<boolean>>;
    mutationPending: boolean;
};

export default function RemoveOfficeDialog({officeName, onClick, open, setOpen, mutationPending}: RemoveOfficeDialogProps) {
    const {t} = useTranslation();

    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogContent>
                <DialogHeader className="font-bold text-xl text-(--text-color)">
                    <DialogTitle>{t("offices.dialog.remove.title")}</DialogTitle>
                    <DialogDescription className="text-(--text-light) text-base font-normal leading-snug mt-1">
                        {t("offices.dialog.remove.description")}
                    </DialogDescription>
                </DialogHeader>
                <div className="flex flex-col gap-4">
                    <div className="flex flex-col gap-1.5">
                        <p className="text-sm text-(--text-light)">{t("offices.dialog.remove.officeConfirmationLabel")}</p>
                        <p className="w-full rounded-md text-(--text-color) bg-gray-50 border py-2 px-3">{officeName}</p>
                    </div>
                </div>

                <DialogFooter className="pt-6">
                    <DialogClose asChild>
                        <Button variant="ghost" disabled={mutationPending} className="bg-white text-(--gray-600) border border-(--gray-400) hover:bg-(--gray-100) hover:border-(--gray-500-) hover:text-(--text-color) cursor-pointer transition-colors">
                            {t("offices.dialog.cancel")}
                        </Button>
                    </DialogClose>
                    <Button disabled={mutationPending} onClick={onClick} className="text-white bg-(--danger) border border-(--danger) hover:text-white hover:bg-(--danger-dark) hover:border hover:border-(--danger-dark) cursor-pointer">
                        {mutationPending ?
                            <>
                                <Spinner className="w-4 h-4" />
                                {t("common.deleting")}
                            </>
                        : t("offices.dialog.remove.confirm")}
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
}