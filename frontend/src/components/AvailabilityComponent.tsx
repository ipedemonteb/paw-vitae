import DashboardNavContainer from "@/components/DashboardNavContainer.tsx";
import DashboardNavHeader from "@/components/DashboardNavHeader.tsx";
import { useTranslation } from "react-i18next";
import { Button } from "../components/ui/button.tsx";
import { Form } from "../components/ui/form.tsx";
import { CalendarClock, Plus, Loader2, AlertCircle, RefreshCw } from "lucide-react";
import { useFieldArray, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import DashboardNavEmptyContent from "@/components/DashboardNavEmptyContent.tsx";
import { useDoctorOfficeAvailability, useDoctorOffices } from "@/hooks/useDoctors.ts";
import { useAuth } from "@/hooks/useAuth.ts";
import { useEffect, useState } from "react";
import { type DoctorAvailabilityFormDTO, putDoctorOfficeAvailability } from "@/data/doctors.ts";
import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import {type AvailabilityFormValues, formSchema, DB_DAY_TO_FORM, FORM_DAY_TO_DB, extractIdFromUrl } from "./availability-schema";
import { AvailabilityItem } from "./AvailabilityItem";

//TODO: Right now, if there is no slot belonging to an active office, the query goes kaboom because of backend managin. Decide how we handle that logic.

export default function AvailabilityComponent() {
    const { t } = useTranslation();
    const auth = useAuth();
    const [isSaving, setIsSaving] = useState(false);
    const [isDataReady, setIsDataReady] = useState(false);
    const queryClient = useQueryClient();

    const {
        data: offices,
        isLoading: loadingOffices,
        isError: isErrorOffices,
        error: errorOffices
    } = useDoctorOffices(`/doctors/${auth.userId}/offices`);

    const {
        data: availabilityRaw,
        isLoading: loadingAvailability,
        isError: isErrorAvailability
    } = useDoctorOfficeAvailability(offices);

    const isLoading = loadingOffices || loadingAvailability;
    const isError = isErrorOffices || isErrorAvailability;

    const dataSignature = JSON.stringify({ offices, availabilityRaw });

    const form = useForm<AvailabilityFormValues>({
        resolver: zodResolver(formSchema),
        defaultValues: { schedules: [] },
    });

    const { fields, append, remove } = useFieldArray({
        control: form.control,
        name: "schedules",
    });

    useEffect(() => {
        if (!isLoading && !isError && offices && availabilityRaw) {
            let databaseSchedules = availabilityRaw.flatMap((officeSlots, index) => {
                const currentOffice = offices[index];
                if (!currentOffice) return [];

                const officeIdReal = extractIdFromUrl(currentOffice.self);

                return officeSlots.map(slot => ({
                    officeId: officeIdReal,
                    dayOfWeek: DB_DAY_TO_FORM[slot.dayOfWeek],
                    startTime: slot.startTime.slice(0, 5),
                    endTime: slot.endTime.slice(0, 5)
                }));
            });

            databaseSchedules.sort((a, b) => {
                const officeDiff = Number(a.officeId) - Number(b.officeId);
                if (officeDiff !== 0) return officeDiff;

                const dayValA = FORM_DAY_TO_DB[a.dayOfWeek];
                const dayValB = FORM_DAY_TO_DB[b.dayOfWeek];

                const sortDayA = dayValA === 0 ? 7 : dayValA;
                const sortDayB = dayValB === 0 ? 7 : dayValB;

                if (sortDayA !== sortDayB) return sortDayA - sortDayB;

                return a.startTime.localeCompare(b.startTime);
            });

            if (!form.formState.isDirty) {
                const currentValues = JSON.stringify(form.getValues().schedules);
                const newValues = JSON.stringify(databaseSchedules);
                if (currentValues !== newValues) {
                    form.reset({ schedules: databaseSchedules });
                }
            }
            setIsDataReady(true);
        }
    }, [isLoading, isError, dataSignature, offices, availabilityRaw, form]);

    async function onSubmit(data: AvailabilityFormValues) {
        if (!offices) return;
        setIsSaving(true);
        try {
            const payload: DoctorAvailabilityFormDTO = {
                doctorOfficeAvailabilities: data.schedules.map(slot => ({
                    officeId: Number(slot.officeId),
                    dayOfWeek: FORM_DAY_TO_DB[slot.dayOfWeek],
                    startTime: `${slot.startTime}:00`,
                    endTime: `${slot.endTime}:00`
                }))
            };

            const url = `/doctors/${auth.userId}/availability`;
            await putDoctorOfficeAvailability(url, payload);
            await queryClient.invalidateQueries({ queryKey: ['doctor', 'office', 'availability'] });

            form.reset(data);

            toast.success(t("success", "Success"), {
                description: t("doctor.profile.update_success", "Availability updated successfully.")
            });

        } catch (error) {
            console.error("Error guardando", error);
            toast.error(t("error", "Error"), {
                description: t("doctor.profile.update_error", "Failed to update availability.")
            });
        } finally {
            setIsSaving(false);
        }
    }

    if (isError) {
        return (
            <DashboardNavContainer>
                <DashboardNavHeader title={t("availability.headerTitle")} children={undefined} />
                <div className="flex flex-col items-center justify-center h-[50vh] text-center p-6">
                    <div className="bg-red-50 p-4 rounded-full mb-4">
                        <AlertCircle className="h-10 w-10 text-red-500" />
                    </div>
                    <h3 className="text-lg font-semibold text-gray-900 mb-2">
                        {t("common.error_loading", "Error loading availability")}
                    </h3>
                    <p className="text-gray-500 max-w-sm mb-6">
                        {errorOffices?.message || t("common.error_generic_message", "There was a problem loading your office data. Please check your connection and try again.")}
                    </p>
                    <Button
                        variant="outline"
                        onClick={() => {
                            setIsDataReady(false);
                            queryClient.invalidateQueries({ queryKey: ['doctor'] });
                        }}
                    >
                        <RefreshCw className="mr-2 h-4 w-4" />
                        {t("common.retry", "Retry")}
                    </Button>
                </div>
            </DashboardNavContainer>
        );
    }

    if (isLoading || !isDataReady) {
        return (
            <DashboardNavContainer>
                <DashboardNavHeader title={t("availability.headerTitle")} children={undefined}/>
                <div className="flex flex-col items-center justify-center h-[50vh] text-gray-400">
                    <Loader2 className="h-10 w-10 animate-spin mb-4" />
                    <p>{t("Loading")}</p>
                </div>
            </DashboardNavContainer>
        );
    }

    return (
        <DashboardNavContainer>
            <DashboardNavHeader title={t("availability.headerTitle")} children={undefined}/>
            <div className="w-full max-w-5xl mx-auto">
                {fields.length === 0 ? (
                    <div className="flex flex-col items-center justify-center p-8 animate-in fade-in-50">
                        <DashboardNavEmptyContent
                            title={t("availability.emptyContent")}
                            text={t("availability.emptyContentText")}
                            Icon={CalendarClock}
                        />
                        <Button
                            size="lg"
                            className="mt-6 w-full max-w-sm shadow-md text-white bg-(--primary-color) hover:bg-(--primary-dark)"
                            onClick={() => {
                                const firstOfficeId = offices && offices.length > 0 ? extractIdFromUrl(offices[0].self) : "";
                                append({ officeId: firstOfficeId, dayOfWeek: "MONDAY", startTime: "09:00", endTime: "17:00" });
                            }}
                        >
                            <Plus className="mr-2 h-5 w-5" />
                            {t("availability.addSchedule")}
                        </Button>
                    </div>
                ) : (
                    <div className="p-6">
                        <Form {...form}>
                            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
                                <div className="space-y-4">
                                    {fields.map((field, index) => (
                                        <AvailabilityItem
                                            key={field.id}
                                            index={index}
                                            remove={remove}
                                            offices={offices}
                                            control={form.control}
                                        />
                                    ))}
                                </div>

                                <div className="flex flex-col sm:flex-row gap-4 pt-4 border-t items-center justify-between">
                                    <Button
                                        type="button"
                                        variant="outline"
                                        className="border-dashed text-gray-500 hover:text-primary hover:border-primary"
                                        onClick={() => {
                                            const firstOfficeId = offices && offices.length > 0 ? extractIdFromUrl(offices[0].self) : "";
                                            append({ officeId: firstOfficeId, dayOfWeek: "MONDAY", startTime: "09:00", endTime: "17:00" });
                                        }}
                                    >
                                        <Plus className="mr-2 h-4 w-4" />
                                        {t("availability.addSchedule")}
                                    </Button>

                                    <Button
                                        type="submit"
                                        disabled={isSaving}
                                        className="bg-(--primary-color) hover:bg-(--primary-dark) text-white font-medium shadow-sm px-8"
                                    >
                                        {isSaving && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                                        {t("availability.save")}
                                    </Button>
                                </div>
                            </form>
                        </Form>
                    </div>
                )}
            </div>
        </DashboardNavContainer>
    );
}