import {DialogClose, DialogContent, DialogFooter, DialogHeader, DialogTitle} from "@/components/ui/dialog.tsx";
import {Label} from "@/components/ui/label.tsx";
import {Input} from "@/components/ui/input.tsx";
import NeighborhoodCombobox from "@/components/NeighborhoodCombobox.tsx";
import {Switch} from "@/components/ui/switch.tsx";
import {Skeleton} from "@/components/ui/skeleton.tsx";
import SpecialtyToggleGroup from "@/components/SpecialtyToggleGroup.tsx";
import {Button} from "@/components/ui/button.tsx";
import type {DeepRequired, FieldErrorsImpl, GlobalError, UseFormReturn} from "react-hook-form";
import {useSpecialties} from "@/hooks/useSpecialties.ts";
import {useTranslation} from "react-i18next";
import {AlertTriangle} from "lucide-react";
import {Alert, AlertTitle} from "@/components/ui/alert.tsx";
import {useDoctorAvailability} from "@/hooks/useOffices.ts";
import {useAuth} from "@/hooks/useAuth.ts";
import {officeIdFromSelf} from "@/utils/IdUtils.ts";
import type {CreateOfficeForm, EditOfficeForm} from "@/lib/office-schema.ts";


type OfficeDialogComponentProps = {
    onSubmit: () => void,
    title: string,
    form:  UseFormReturn<any>,
    errors:  Partial<FieldErrorsImpl<DeepRequired<EditOfficeForm | CreateOfficeForm>>> & {root?: Record<string, GlobalError> & GlobalError},
    isLoading?: boolean,
    confirm: string,
    officeId?: string
}

export default function OfficeDialogComponent({onSubmit, title, form, isLoading = false, confirm, officeId, errors}: OfficeDialogComponentProps) {
    const {data: specialties, isLoading: isLoadingSpecialties} = useSpecialties()
    const {t} = useTranslation()
    const auth = useAuth()
    const id = officeId ? auth.userId : undefined
    const {data: availability} = useDoctorAvailability(id, officeId)
    const hasAvailability = !(availability?.filter(a => officeIdFromSelf(a.office) === officeId).length === 0);
    return (
        <DialogContent className="max-w-155! p-5 overflow-x-auto">
            <form onSubmit={(event) => {
                event.preventDefault()
                onSubmit()
            }}>
                <DialogHeader className="mb-3">
                    <DialogTitle className="border-b w-full relative pb-3">
                        {title}
                    </DialogTitle>
                </DialogHeader>
                <div className="flex flex-col items-center gap-2.5">
                    <div className="flex justify-between w-full items-center gap-4">
                        <div className="flex flex-col gap-1.5 text-(--text-light)  ">
                            <Label htmlFor="name" className="pl-1 text-[1rem]">{t("offices.dialog.nameLabel")}</Label>
                            <Input
                                id="name"
                                placeholder={t("offices.dialog.placeholderName")}
                                className={` w-70 placeholder:opacity-70 selection:bg-blue-300 selection:text-black" type="text ${form.formState.errors.name ? "border-(--danger)" : ""}`}
                                {...form.register("name")}
                            />
                            {errors.name && (
                                <p className="text-(--danger) text-sm">{errors.name.message}</p>
                            )}
                        </div>
                        <div className="flex flex-col gap-1.5 text-(--text-light) text-[1rem]">
                            <Label className="pl-1 text-[1rem]">{t("offices.dialog.neighborhoodLabel")}</Label>
                            <NeighborhoodCombobox
                                value={form.watch("neighborhood")}
                                onChange={(val) => form.setValue("neighborhood", val, { shouldDirty: true })}
                            />
                        </div>
                    </div>
                    {form.watch("active") !== undefined && (
                        <div className="flex w-full items-center justify-baseline gap-6">
                            <div className=" flex flex-col gap-1 ">
                                <Label htmlFor="active" className="text-[1rem] text-(--text-light)">{t("offices.dialog.activeLabel")}</Label>
                                <Switch disabled={!hasAvailability} onCheckedChange={(checked) => form.setValue("active", checked)}  defaultChecked={form.watch("active")} id="active" className="data-[state=checked]:bg-(--success) data-[state=checked]: transition-all " />
                            </div>
                            {!hasAvailability && (
                                <Alert className="w-fit bg-[rgba(var(--primary-light-rgb),0.15)] text-(--primary-color) border border-(--primary-color)">
                                    <AlertTriangle />
                                    <AlertTitle>{t("offices.dialog.edit.activeWarning")}</AlertTitle>
                                </Alert>
                            )}
                        </div>
                    )}
                    <div className="w-full flex flex-col gap-1.5">
                        <Label className="text-[1rem] text-(--text-light)">{t("offices.dialog.specialtiesLabel")}</Label>
                        {(isLoading || isLoadingSpecialties) ? (
                            <div className="w-full flex flex-wrap gap-y-1 gap-x-0.5 ">
                                {Array.from({length: 15}).map((_, i) => (
                                    <Skeleton key={i} className="w-28 h-8 rounded-full"/>
                                ))}
                            </div>
                        ) : (
                            <SpecialtyToggleGroup error={errors.specialties} onValueChange={(s: string[]) => form.setValue("specialties", s)} currentSpecialties={form.watch("specialties")} specialties={specialties} />
                        )}
                    </div>
                </div>
                <DialogFooter className="pt-4">
                    <DialogClose className="  hover:bg-gray-100 rounded-md py-1 px-3 text-sm bg-white text-(--text-light) border cursor-pointer">
                        {t("offices.dialog.cancel")}
                    </DialogClose>
                    <Button type="submit" className="border-none hover:bg-(--primary-dark)  bg-(--primary-color) text-white cursor-pointer">
                        {confirm}
                    </Button>
                </DialogFooter>
            </form>
        </DialogContent>
    )
}