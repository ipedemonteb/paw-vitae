import OfficeCard from "@/components/OfficeCard.tsx";
import type {OfficeDTO} from "@/data/offices.ts";
import {useDoctorOfficeSpecialties, useDeleteOfficeMutation, useUpdateOfficeMutation} from "@/hooks/useOffices.ts";
import {useSpecialtiesByUrl} from "@/hooks/useSpecialties.ts";
import {Dialog} from "@/components/ui/dialog";
import {DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger} from "@/components/ui/dropdown-menu";
import {Button} from "@/components/ui/button";
import {useForm} from "react-hook-form";
import {type EditOfficeForm, getEditOfficeSchema} from "@/lib/office-schema.ts";
import {zodResolver} from "@hookform/resolvers/zod";
import {useEffect, useMemo, useState} from "react";
import {officeIdFromSelf, specialtyIdFromSelf} from "@/utils/IdUtils.ts";
import {cn, extractIdFromUrl} from "@/lib/utils.ts";
import {toast} from "sonner";
import OfficeDialogComponent from "@/components/OfficeDialogContent.tsx";
import {useTranslation} from "react-i18next";
import RemoveOfficeAlertDialog from "@/components/RemoveOfficeAlertDialog.tsx";
import {MoreVertical, Pencil, Trash2} from "lucide-react";

export type OfficeDialogProps = {
    office: OfficeDTO;
    animateInDelay: number;
};

const anim =
    "transition-all duration-500 ease-out will-change-transform will-change-opacity";
const animFrom =
    "opacity-0 translate-y-2 scale-[0.99]";
const animTo =
    "opacity-100 translate-y-0 scale-100";

export default function EditOfficeDialog({office, animateInDelay}: OfficeDialogProps) {
    const {data: officeSpecialties, isLoading} = useDoctorOfficeSpecialties(office.officeSpecialties);
    const {data: currentSpecialties, isLoading: isLoadingCurrentSpecialties} = useSpecialtiesByUrl(officeSpecialties?.map(s => s.specialty));

    const [editOpen, setEditOpen] = useState(false);
    const [deleteOpen, setDeleteOpen] = useState(false);
    const [mounted, setMounted] = useState(false);

    useEffect(() => {
        setMounted(false);
        const id = requestAnimationFrame(() => setMounted(true));
        return () => cancelAnimationFrame(id);
    }, []);

    const {t} = useTranslation();
    const schema = useMemo(() => getEditOfficeSchema(t), [t]);

    const form = useForm<EditOfficeForm>({
        resolver: zodResolver(schema),
        defaultValues: {
            name: office.name,
            neighborhood: office.neighborhood,
            active: office.status === "active",
            specialties: [] as string[],
        },
        mode: "onSubmit",
    });

    const updateOfficeMutation = useUpdateOfficeMutation(office.self);
    const removeOfficeMutation = useDeleteOfficeMutation(office.self);

    useEffect(() => {
        if (!isLoadingCurrentSpecialties && currentSpecialties) {
            form.reset({
                name: office.name,
                neighborhood: office.neighborhood,
                active: office.status === "active",
                specialties: currentSpecialties.filter(s => s.data !== undefined).map(s => s.data.self),
            });
        }
    }, [isLoadingCurrentSpecialties, currentSpecialties, office, form]);

    const onSubmit = form.handleSubmit((values) => {
        const specialties = values.specialties.map(s => specialtyIdFromSelf(s)).filter(s => s !== null);
        const neighborhood = extractIdFromUrl(values.neighborhood);
        updateOfficeMutation.mutate(
            {officeName: values.name, specialtyIds: specialties, neighborhoodId: neighborhood, active: values.active, removed: false},
            {
                onSuccess: () => toast.success(t("offices.dialog.edit.toast.success")),
                onError: () => toast.error(t("offices.dialog.edit.toast.error")),
                onSettled: () => setEditOpen(false),
            }
        );
    });

    return (
        <div className={cn("relative", anim, mounted ? animTo : animFrom)} style={{ transitionDelay: `${animateInDelay}ms` }}>
            <div className="absolute top-2 right-2 z-10">
                <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                        <Button variant="ghost" size="icon" className="h-10 w-10 rounded-full cursor-pointer">
                            <MoreVertical className="size-5 text-(--text-light)" />
                        </Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent align="end" className="min-w-36">
                        <DropdownMenuItem onSelect={() => { form.reset(); setEditOpen(true); }} className="cursor-pointer">
                            <Pencil className="size-4" />
                            {t("offices.dialog.edit.button")}
                        </DropdownMenuItem>
                        <DropdownMenuItem onSelect={() => setDeleteOpen(true)} className="cursor-pointer text-(--danger) focus:text-(--danger) data-highlighted:text-(--danger-dark) data-highlighted:bg-(--danger-light)">
                            <Trash2 className="size-4 text-(--danger)" />
                            {t("offices.dialog.remove.button")}
                        </DropdownMenuItem>
                    </DropdownMenuContent>
                </DropdownMenu>
            </div>

            <OfficeCard mounted={mounted} animateInDelay={animateInDelay} office={office} />

            <Dialog open={editOpen} onOpenChange={(open) => { if (open) form.reset(); setEditOpen(open); }}>
                <OfficeDialogComponent mutationPending={updateOfficeMutation.isPending} errors={form.formState.errors} officeId={officeIdFromSelf(office.self)} confirm={t("offices.dialog.edit.confirm")} title={t("offices.dialog.edit.title")} onSubmit={onSubmit} form={form} isLoading={isLoading || isLoadingCurrentSpecialties}/>
            </Dialog>

            <RemoveOfficeAlertDialog mutationPending={removeOfficeMutation.isPending} open={deleteOpen} setOpen={setDeleteOpen} officeName={office.name} onClick={() => removeOfficeMutation.mutate(undefined, {
                onSuccess: () => toast.success(t("offices.dialog.remove.toast.success")),
                onError: () => toast.error(t("offices.dialog.remove.toast.error")),
                onSettled: () => setDeleteOpen(false),
            })}/>
        </div>
    );
}
