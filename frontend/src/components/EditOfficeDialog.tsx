import OfficeCard from "@/components/OfficeCard.tsx";
import type {OfficeDTO} from "@/data/office.ts";
import {useDoctorOfficeSpecialties, useUpdateOfficeMutation} from "@/hooks/useDoctors.ts";
import {useSpecialtiesByUrl} from "@/hooks/useSpecialties.ts";
import {
    Dialog,
    DialogTrigger
} from "@/components/ui/dialog";
import {useForm} from "react-hook-form";
import {type EditOfficeForm, EditOfficeSchema} from "@/lib/office-schema.ts";
import {zodResolver} from "@hookform/resolvers/zod";
import {useEffect, useState} from "react";
import {specialtyIdFromSelf} from "@/utils/IdUtils.ts";
import {extractIdFromUrl} from "@/lib/utils.ts";
import {toast} from "sonner";
import OfficeDialogComponent from "@/components/OfficeDialogContent.tsx";
import {useTranslation} from "react-i18next";

export type OfficeDialogProps = {
    office: OfficeDTO
}

export default function EditOfficeDialog({office}: OfficeDialogProps) {
    const {data: officeSpecialties, isLoading} = useDoctorOfficeSpecialties(office.officeSpecialties)
    const {data: currentSpecialties, isLoading: isLoadingCurrentSpecialties} = useSpecialtiesByUrl(officeSpecialties?.map(s => s.specialty))
    const [open, setOpen] = useState(false)
    const {t} = useTranslation()

    const form = useForm<EditOfficeForm>({
        resolver: zodResolver(EditOfficeSchema),
        defaultValues: {
            name: office.name,
            neighborhood: office.neighborhood,
            active: office.status === "active",
            specialties: [] as string[],
        },
        mode: "onSubmit",
    })

    const updateOfficeMutation = useUpdateOfficeMutation(office.self)


    useEffect(() => {
        if (!isLoadingCurrentSpecialties && currentSpecialties) {
            form.reset({
                name: office.name,
                neighborhood: office.neighborhood,
                active: office.status === "active",
                specialties: currentSpecialties
                    .filter(s => s.data !== undefined)
                    .map(s => s.data.self),
            });
        }
    }, [
        isLoadingCurrentSpecialties,
        currentSpecialties,
        office,
        form
    ])

    const onSubmit = form.handleSubmit((values) => {
        const parsed = EditOfficeSchema.safeParse(values);
        if (!parsed.success) {
            return;
        }
        const specialties = values.specialties.map(s => specialtyIdFromSelf(s)).filter(s => s !== null)
        const neighborhood = extractIdFromUrl(values.neighborhood)
        updateOfficeMutation.mutate({
            officeName: values.name,
            specialtyIds: specialties,
            neighborhoodId: neighborhood,
            active: values.active,
            removed: false
        }, {
            onSuccess: () => {
                toast.success(t("offices.edit.toast.success"))
            },
            onError: () => {
                toast.error(t("offices.edit.toast.error"))
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
            <DialogTrigger asChild >
                <OfficeCard office={office}/>
            </DialogTrigger>
            <OfficeDialogComponent confirm={t("offices.dialog.edit.confirm")} title={t("offices.dialog.edit.title")} onSubmit={onSubmit} form={form} isLoading={isLoading || isLoadingCurrentSpecialties}/>
        </Dialog>
    )
}