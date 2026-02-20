import {DialogClose, DialogContent, DialogFooter, DialogHeader, DialogTitle} from "@/components/ui/dialog.tsx";
import {Label} from "@/components/ui/label.tsx";
import {Input} from "@/components/ui/input.tsx";
import NeighborhoodCombobox from "@/components/ui/neighborhood-combobox.tsx";
import {Switch} from "@/components/ui/switch.tsx";
import {Skeleton} from "@/components/ui/skeleton.tsx";
import SpecialtyToggleGroup from "@/components/SpecialtyToggleGroup.tsx";
import {Button} from "@/components/ui/button.tsx";
import type {DeepRequired, FieldErrorsImpl, GlobalError, UseFormReturn} from "react-hook-form";
import {useTranslation} from "react-i18next";
import {AlertTriangle} from "lucide-react";
import {Alert, AlertTitle} from "@/components/ui/alert.tsx";
import {useDoctorAvailability} from "@/hooks/useOffices.ts";
import {useAuth} from "@/hooks/useAuth.ts";
import {officeIdFromSelf} from "@/utils/IdUtils.ts";
import type {CreateOfficeForm, EditOfficeForm} from "@/lib/office-schema.ts";
import {Spinner} from "@/components/ui/spinner.tsx";
import {useDoctor, useDoctorSpecialties} from "@/hooks/useDoctors.ts";
import {RefetchComponent} from "@/components/ui/refetch.tsx";

type OfficeDialogComponentProps = {
    onSubmit: () => void,
    title: string,
    form:  UseFormReturn<any>,
    errors:  Partial<FieldErrorsImpl<DeepRequired<EditOfficeForm | CreateOfficeForm>>> & {root?: Record<string, GlobalError> & GlobalError},
    isLoading?: boolean,
    confirm: string,
    officeId?: string,
    mutationPending: boolean
}

export default function OfficeDialogComponent({onSubmit, title, form, isLoading = false, confirm, officeId, errors, mutationPending}: OfficeDialogComponentProps) {
    const {t} = useTranslation()
    const auth = useAuth()

    const {data: doctor, isError: errorDoctor, refetch: refetchDoctor, isFetching: fetchingDoctor} = useDoctor(auth.userId)
    const {data: specialties, isLoading: isLoadingSpecialties, isError: errorSpecialties, refetch: refetchSpecialties, isFetching: fetchingSpecialties} = useDoctorSpecialties(doctor?.specialties)
    const id = officeId ? auth.userId : undefined
    const {data: availability, isError: errorAvailability, refetch: refetchAvailability, isFetching: fetchingAvailability} = useDoctorAvailability(id, officeId)
    const hasAvailability = !(availability?.filter(a => officeIdFromSelf(a.office) === officeId).length === 0);
    const isError = errorDoctor || errorSpecialties || errorAvailability;
    const onRefetch = () => {
        refetchDoctor();
        refetchSpecialties();
        refetchAvailability();
    }
    const isFetching = fetchingDoctor || fetchingSpecialties || fetchingAvailability;

    return (
        <DialogContent className="overflow-x-auto w-[calc(100%-2rem)] mx-auto sm:max-w-155!">
            <form onSubmit={(event) => {
                event.preventDefault()
                onSubmit()
            }}>
                <DialogHeader className="font-bold text-xl text-(--text-color)">
                    <DialogTitle>
                        {title}
                    </DialogTitle>
                </DialogHeader>
                <div className="flex mt-4 flex-col items-center gap-3">
                    {isError || true ?
                        <RefetchComponent
                            onRefetch={onRefetch}
                            isFetching={isFetching}
                            errorText={t("offices.dialog.edit.error")}
                            className={"my-10"}
                        />
                    :
                        <>
                            <div className="flex flex-col sm:flex-row w-full items-start gap-4 sm:gap-4">
                                <div className="flex flex-col gap-1 text-(--text-light) w-full sm:flex-1 min-w-0">
                                    <Label htmlFor="name" className="text-md">{t("offices.dialog.nameLabel")}</Label>
                                    <Input
                                        disabled={mutationPending}
                                        id="name"
                                        placeholder={t("offices.dialog.placeholderName")}
                                        className={` w-full sm:w-70 placeholder:opacity-70  selection:text-black" type="text ${form.formState.errors.name ? "border-(--danger)" : ""}`}
                                        {...form.register("name")}
                                    />
                                    {errors.name && (
                                        <p className="text-(--danger) text-sm">{errors.name?.message}</p>
                                    )}
                                </div>
                                <div className="flex flex-col gap-1 text-(--text-light) w-full sm:flex-1 min-w-0">
                                    <Label className="text-[1rem]">{t("offices.dialog.neighborhoodLabel")}</Label>
                                    <div className="w-full min-w-0">
                                        <NeighborhoodCombobox
                                            error={errors.neighborhood}
                                            mutationPending={mutationPending}
                                            value={form.watch("neighborhood")}
                                            onChange={(val) => form.setValue("neighborhood", val, { shouldDirty: true })}
                                        />
                                    </div>
                                </div>
                            </div>
                            {form.watch("active") !== undefined  && (
                                <div className="flex w-full items-center justify-baseline gap-6">
                                    <div className=" flex flex-col gap-1">
                                        <Label htmlFor="active" className="text-md text-(--text-light)">{t("offices.dialog.activeLabel")}</Label>
                                        <Switch disabled={!hasAvailability || mutationPending} onCheckedChange={(checked) => form.setValue("active", checked)}  defaultChecked={form.watch("active")} id="active" className="cursor-pointer data-[state=checked]:bg-(--success) data-[state=checked]: transition-all " />
                                    </div>
                                </div>
                            )}
                            <div className="w-full flex flex-col gap-2">
                                <Label className="text-md text-(--text-light)">{t("offices.dialog.specialtiesLabel")}</Label>
                                {(isLoading || isLoadingSpecialties) ? (
                                    <div className="w-full flex flex-wrap gap-y-1 gap-x-0.5 ">
                                        {Array.from({length: 15}).map((_, i) => (
                                            <Skeleton key={i} className="w-28 h-8 rounded-full"/>
                                        ))}
                                    </div>
                                ) : (
                                    <SpecialtyToggleGroup mutationPending={mutationPending} error={errors.specialties} onValueChange={(s: string[]) => form.setValue("specialties", s)} currentSpecialties={form.watch("specialties")} specialties={specialties} />
                                )}
                            </div>
                            {!hasAvailability && (
                                <Alert className="w-full mt-4 mb-2 bg-[rgba(var(--primary-light-rgb),0.15)] text-(--primary-color) border border-(--primary-color)">
                                    <AlertTriangle />
                                    <AlertTitle>{t("offices.dialog.edit.activeWarning")}</AlertTitle>
                                </Alert>
                            )}
                        </>
                    }
                </div>
                {isError || true ? null :
                    <DialogFooter className="pt-6">
                        <DialogClose disabled={mutationPending} className="py-1 px-3 text-sm rounded-md bg-white text-(--gray-600) border border-(--gray-400) hover:bg-(--gray-100) hover:border-(--gray-500-) hover:text-(--text-color) cursor-pointer transition-colors">
                            {t("offices.dialog.cancel")}
                        </DialogClose>
                        <Button disabled={mutationPending} type="submit" className="border-none hover:bg-(--primary-dark)  bg-(--primary-color) text-white cursor-pointer">
                            {mutationPending ? (
                                <Spinner/>
                            ) : confirm}
                        </Button>
                    </DialogFooter>
                }
            </form>
        </DialogContent>
    )
}