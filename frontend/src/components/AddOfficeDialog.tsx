import {
    Dialog,
    DialogTrigger
} from "@/components/ui/dialog.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Plus} from 'lucide-react'
import {useState} from "react";
import {useForm} from "react-hook-form";
import {type CreateOfficeForm, CreateOfficeSchema} from "@/lib/office-schema.ts";
import {zodResolver} from "@hookform/resolvers/zod";
import {toast} from "sonner";
import {specialtyIdFromSelf} from "@/utils/IdUtils.ts";
import {extractIdFromUrl} from "@/lib/utils.ts";
import OfficeDialogComponent from "@/components/OfficeDialogContent.tsx";
import {useTranslation} from "react-i18next";
import {useCreateDoctorOfficeMutation} from "@/hooks/useOffices.ts";
import {useAuth} from "@/hooks/useAuth.ts";

export default function AddOfficeDialog() {
    const [open, setOpen] = useState(false)
    const {t} = useTranslation()

    const form = useForm<CreateOfficeForm>({
        resolver: zodResolver(CreateOfficeSchema),
        mode: "onSubmit",
    })

    const auth = useAuth()
    const createOfficeMutation = useCreateDoctorOfficeMutation(auth.userId!)

    const onSubmit = form.handleSubmit((values) => {
        const parsed = CreateOfficeSchema.safeParse(values);
        if (!parsed.success) {
            return;
        }
        const specialties = values.specialties.map(s => specialtyIdFromSelf(s)).filter(s => s !== null)
        const neighborhood = extractIdFromUrl(values.neighborhood)!
        createOfficeMutation.mutate({
            officeName: values.name,
            specialtyIds: specialties,
            neighborhoodId: neighborhood
        }, {
            onSuccess: () => {
                toast.success(t("offices.dialog.add.toast.success"))
            },
            onError: () => {
                toast.error(t("offices.dialog.add.toast.error"))
            },
            onSettled: () => {
                setOpen(false)
            }
        })
    })

    return (
        <Dialog open={open} onOpenChange={(open) => {
            if (open) form.reset()
            setOpen(open)
        }}>
            <DialogTrigger asChild className="cursor-pointer">
                <Button className="text-(--primary-color) hover:bg-(--primary-bg) bg-transparent">
                    <Plus/>
                    {t("offices.dialog.add.title")}
                </Button>
            </DialogTrigger>
            <OfficeDialogComponent confirm={t("offices.dialog.add.confirm")} title={t("offices.dialog.add.title")} onSubmit={onSubmit} form={form}/>
        </Dialog>
    )
}