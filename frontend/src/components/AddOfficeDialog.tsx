import {
    Dialog,
    DialogTrigger
} from "@/components/ui/dialog.tsx";
import { Plus } from "lucide-react";
import { useMemo, useState } from "react";
import { useForm } from "react-hook-form";
import { type CreateOfficeForm, getCreateOfficeSchema } from "@/lib/office-schema.ts";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "sonner";
import { specialtyIdFromSelf } from "@/utils/IdUtils.ts";
import { extractIdFromUrl } from "@/lib/utils.ts";
import OfficeDialogComponent from "@/components/OfficeDialogContent.tsx";
import { useTranslation } from "react-i18next";
import { useCreateDoctorOfficeMutation } from "@/hooks/useOffices.ts";
import { useAuth } from "@/hooks/useAuth.ts";

export default function AddOfficeDialog() {
    const [open, setOpen] = useState(false);
    const { t } = useTranslation();

    const schema = useMemo(() => getCreateOfficeSchema(t), [t]);

    const form = useForm<CreateOfficeForm>({
        resolver: zodResolver(schema),
        mode: "onSubmit",
    });

    const auth = useAuth();
    const createOfficeMutation = useCreateDoctorOfficeMutation(auth.userId);

    const onSubmit = form.handleSubmit((values) => {
        const specialties = values.specialties.map((s) => specialtyIdFromSelf(s)).filter((s) => s !== null);
        const neighborhood = extractIdFromUrl(values.neighborhood)!;

        createOfficeMutation.mutate(
            {
                officeName: values.name,
                specialtyIds: specialties,
                neighborhoodId: neighborhood,
            },
            {
                onSuccess: () => {
                    toast.success(t("offices.dialog.add.toast.success"));
                },
                onError: () => {
                    toast.error(t("offices.dialog.add.toast.error"));
                },
                onSettled: () => {
                    setOpen(false);
                },
            }
        );
    });

    return (
        <Dialog
            open={open}
            onOpenChange={(open) => {
                if (open) form.reset();
                setOpen(open);
            }}
        >
            <DialogTrigger asChild>
                <button type="button" className="relative w-full h-56 sm:max-w-56 sm:mx-auto peer flex transition-all group rounded-2xl
                        cursor-pointer items-center border-2 border-dashed border-(--gray-400) bg-(--gray-50) hover:bg-(--gray-200)
                        hover:border-(--gray-500)"
                >
                    <div className="text-xl flex justify-center-safe items-center overflow-hidden flex-col font-semibold rounded-2xl w-full">
                        <Plus className="size-14 text-(--text-light)" />
                        <p className="w-full text-center min-w-0 text-ellipsis text-(--text-light)">{t("offices.dialog.add.title")}</p>
                    </div>
                </button>
            </DialogTrigger>

            <OfficeDialogComponent
                mutationPending={createOfficeMutation.isPending}
                errors={form.formState.errors}
                confirm={t("offices.dialog.add.confirm")}
                title={t("offices.dialog.add.title")}
                onSubmit={onSubmit}
                form={form}
            />
        </Dialog>
    );
}