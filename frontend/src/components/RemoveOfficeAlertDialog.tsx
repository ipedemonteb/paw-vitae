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
                <DialogHeader>
                    <DialogTitle>{t("offices.dialog.remove.title")}</DialogTitle>
                    <DialogDescription>
                        <div className="flex flex-col gap-4 pt-2">
                            <p className="text-(--text-light) text-lg">{t("offices.dialog.remove.description")}</p>
                            <div className="flex flex-col gap-1">
                                <p className="text-sm text-(--text-light)">{t("offices.dialog.remove.officeConfirmationLabel")}</p>
                                <p className="w-full rounded-md text-(--text-color) bg-gray-50 border py-3 px-2">{officeName}</p>
                            </div>
                        </div>
                    </DialogDescription>
                </DialogHeader>

                <DialogFooter className="pt-6">
                    <DialogClose asChild>
                        <Button variant="ghost" disabled={mutationPending} className="bg-white text-(--gray-600) border border-(--gray-400) hover:bg-(--gray-100) hover:border-(--gray-500-) hover:text-(--text-color) cursor-pointer transition-colors">
                            {t("offices.dialog.cancel")}
                        </Button>
                    </DialogClose>
                    <Button disabled={mutationPending} onClick={onClick} className="bg-(--danger) hover:bg-(--danger-dark) text-white cursor-pointer">
                        {mutationPending ? <Spinner/> : t("offices.dialog.remove.confirm")}
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
}