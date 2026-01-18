import DashboardNavContainer from "@/components/DashboardNavContainer.tsx";
import DashboardNavHeader from "@/components/DashboardNavHeader.tsx";
import { useTranslation } from "react-i18next";
import { Button } from "../components/ui/button.tsx";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "../components/ui/form.tsx";
import { Card, CardContent } from "../components/ui/card.tsx";
import { CalendarClock, Plus, Trash2 } from "lucide-react";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { z } from "zod";
import { useFieldArray, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import DashboardNavEmptyContent from "@/components/DashboardNavEmptyContent.tsx";

const DAYS = [
    "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"
];

const TIMES = [
    "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"
];

const timeToMinutes = (time: string) => {
    const [hours, minutes] = time.split(":").map(Number);
    return hours * 60 + minutes;
};

const scheduleItemSchema = z.object({
    officeId: z.string().min(1, "Requerido"),
    dayOfWeek: z.string().min(1, "Requerido"),
    startTime: z.string().min(1, "Requerido"),
    endTime: z.string().min(1, "Requerido"),
}).refine((data) => {
    if (!data.startTime || !data.endTime) return true;
    return timeToMinutes(data.endTime) > timeToMinutes(data.startTime);
}, {
    message: "errorTimeOrder",
    path: ["endTime"],
});

const formSchema = z.object({
    schedules: z.array(scheduleItemSchema)
        .superRefine((items, ctx) => {
            items.forEach((item, index) => {
                const currentStart = timeToMinutes(item.startTime);
                const currentEnd = timeToMinutes(item.endTime);

                items.forEach((otherItem, otherIndex) => {
                    if (index === otherIndex) return;

                    if (item.officeId === otherItem.officeId && item.dayOfWeek === otherItem.dayOfWeek) {
                        const otherStart = timeToMinutes(otherItem.startTime);
                        const otherEnd = timeToMinutes(otherItem.endTime);

                        const hasOverlap = (currentStart < otherEnd) && (currentEnd > otherStart);

                        if (hasOverlap) {
                            ctx.addIssue({
                                code: z.ZodIssueCode.custom,
                                message: "errorOverlap",
                                path: [index, "startTime"],
                            });
                        }
                    }
                });
            });
        }),
});

type AvailabilityFormValues = z.infer<typeof formSchema>;

export default function AvailabilityComponent() {
    const { t } = useTranslation();

    const form = useForm<AvailabilityFormValues>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            schedules: [],
        },
    });

    const { fields, append, remove } = useFieldArray({
        control: form.control,
        name: "schedules",
    });

    function onSubmit(data: AvailabilityFormValues) {
        console.log("Payload:", data);
    }

    return (
        <DashboardNavContainer>
            <DashboardNavHeader title={t("availability.headerTitle")} children={undefined} />

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
                        onClick={() => append({
                            officeId: "",
                            dayOfWeek: "",
                            startTime: "09:00",
                            endTime: "17:00"
                        })}
                    >
                        <Plus className="mr-2 h-5 w-5" />
                        {t("availability.addSchedule")}
                    </Button>
                </div>
            ) : (
                <div className="p-6 max-w-5xl mx-auto">
                    <Form {...form}>
                        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">

                            {form.formState.errors.schedules?.root && (
                                <div className="p-3 bg-red-50 text-red-600 text-sm rounded-md border border-red-200">
                                    {t(`availability.${form.formState.errors.schedules.root.message}`)}
                                </div>
                            )}

                            <div className="space-y-4">
                                {fields.map((field, index) => (
                                    <Card key={field.id} className="relative bg-white shadow-sm border border-gray-200">
                                        <div className="absolute left-0 top-0 bottom-0 w-1 bg-blue-500/20" />

                                        <CardContent className="p-4 pl-6">
                                            <Button
                                                type="button"
                                                variant="ghost"
                                                size="icon"
                                                className="absolute right-2 top-2 h-7 w-7 text-gray-400 hover:text-red-500 hover:bg-red-50 z-10"
                                                onClick={() => remove(index)}
                                            >
                                                <Trash2 className="h-4 w-4" />
                                            </Button>

                                            <div className="grid grid-cols-1 md:grid-cols-13 gap-4 items-start pr-8">

                                                <div className="col-span-1 md:col-span-4 min-w-0">
                                                    <FormField
                                                        control={form.control}
                                                        name={`schedules.${index}.officeId`}
                                                        render={({ field }) => (
                                                            <FormItem className="space-y-1 w-full">
                                                                <FormLabel className="text-[10px] font-bold text-gray-500 tracking-wider">
                                                                    {t("availability.office")}
                                                                </FormLabel>
                                                                <Select onValueChange={field.onChange} defaultValue={field.value}>
                                                                    <FormControl>

                                                                        <SelectTrigger className="w-full max-w-[98%] h-9 bg-gray-50/50 text-sm overflow-hidden">
                                                                            <span className="flex-1 text-left truncate min-w-0 pr-2">
                                                                                <SelectValue placeholder={t("availability.selectPlaceholder")} />
                                                                            </span>
                                                                        </SelectTrigger>
                                                                    </FormControl>
                                                                    <SelectContent>
                                                                        <SelectItem value="1">Consultorio Central con Nombre Extremadamente Largo para Probar el Corte de Texto</SelectItem>
                                                                        <SelectItem value="2">Clínica Norte</SelectItem>
                                                                    </SelectContent>
                                                                </Select>
                                                                <FormMessage className="text-xs" />
                                                            </FormItem>
                                                        )}
                                                    />
                                                </div>

                                                <div className="col-span-1 md:col-span-3 min-w-0">
                                                    <FormField
                                                        control={form.control}
                                                        name={`schedules.${index}.dayOfWeek`}
                                                        render={({ field }) => (
                                                            <FormItem className="space-y-1 w-full">
                                                                <FormLabel className="text-[10px] font-bold text-gray-500  tracking-wider">
                                                                    {t("availability.dayOfWeek")}
                                                                </FormLabel>
                                                                <Select onValueChange={field.onChange} defaultValue={field.value}>
                                                                    <FormControl>
                                                                        <SelectTrigger className="w-full max-w-[98%] h-9 bg-gray-50/50 text-sm">
                                                                            <span className="truncate text-left block w-full">
                                                                                <SelectValue placeholder={t("availability.selectPlaceholder")} />
                                                                            </span>
                                                                        </SelectTrigger>
                                                                    </FormControl>
                                                                    <SelectContent>
                                                                        {DAYS.map((day) => (
                                                                            <SelectItem key={day} value={day}>
                                                                                {t(`days.${day}`)}
                                                                            </SelectItem>
                                                                        ))}
                                                                    </SelectContent>
                                                                </Select>
                                                                <FormMessage className="text-xs" />
                                                            </FormItem>
                                                        )}
                                                    />
                                                </div>

                                                <div className="col-span-1 md:col-span-3 min-w-0">
                                                    <FormField
                                                        control={form.control}
                                                        name={`schedules.${index}.startTime`}
                                                        render={({ field }) => (
                                                            <FormItem className="space-y-1 w-full">
                                                                <FormLabel className="text-[10px] font-bold text-gray-500  tracking-wider">
                                                                    {t("availability.startTime")}
                                                                </FormLabel>
                                                                <Select onValueChange={field.onChange} defaultValue={field.value}>
                                                                    <FormControl>
                                                                        <SelectTrigger className="w-full max-w-[98%] h-9 bg-gray-50/50 text-sm">
                                                                            <span className="truncate text-left block w-full">
                                                                                <SelectValue placeholder="--:--" />
                                                                            </span>
                                                                        </SelectTrigger>
                                                                    </FormControl>
                                                                    <SelectContent>
                                                                        <div className="h-48 overflow-y-auto">
                                                                            {TIMES.map((time) => (
                                                                                <SelectItem key={time} value={time}>{time}</SelectItem>
                                                                            ))}
                                                                        </div>
                                                                    </SelectContent>
                                                                </Select>
                                                                    {form.formState.errors.schedules?.[index]?.startTime?.message &&
                                                                        <p className="text-xs font-medium text-red-500">
                                                                            {t(`availability.${form.formState.errors.schedules[index]?.startTime?.message}`)}
                                                                        </p>                                                                    }
                                                            </FormItem>
                                                        )}
                                                    />
                                                </div>

                                                <div className="col-span-1 md:col-span-3 min-w-0">
                                                    <FormField
                                                        control={form.control}
                                                        name={`schedules.${index}.endTime`}
                                                        render={({ field }) => (
                                                            <FormItem className="space-y-1 w-full">
                                                                <FormLabel className="text-[10px] font-bold text-gray-500  tracking-wider">
                                                                    {t("availability.endTime")}
                                                                </FormLabel>
                                                                <Select onValueChange={field.onChange} defaultValue={field.value}>
                                                                    <FormControl>
                                                                        <SelectTrigger className="w-full max-w-[98%] h-9 bg-gray-50/50 text-sm">
                                                                            <span className="truncate text-left block w-full">
                                                                                <SelectValue placeholder="--:--" />
                                                                            </span>
                                                                        </SelectTrigger>
                                                                    </FormControl>
                                                                    <SelectContent>
                                                                        <div className="h-48 overflow-y-auto">
                                                                            {TIMES.map((time) => (
                                                                                <SelectItem key={`end-${time}`} value={time}>{time}</SelectItem>
                                                                            ))}
                                                                        </div>
                                                                    </SelectContent>
                                                                </Select>
                                                                    {form.formState.errors.schedules?.[index]?.endTime?.message &&
                                                                        <p className="text-xs font-medium text-red-500">
                                                                            {t(`availability.${form.formState.errors.schedules[index]?.endTime?.message}`)}
                                                                        </p>                                                                    }
                                                            </FormItem>
                                                        )}
                                                    />
                                                </div>

                                            </div>
                                        </CardContent>
                                    </Card>
                                ))}
                            </div>

                            <div className="flex flex-col sm:flex-row gap-4 pt-4 border-t items-center justify-between">
                                <Button
                                    type="button"
                                    variant="outline"
                                    className="border-dashed text-gray-500 hover:text-primary hover:border-primary"
                                    onClick={() => append({ officeId: "", dayOfWeek: "", startTime: "09:00", endTime: "17:00" })}
                                >
                                    <Plus className="mr-2 h-4 w-4" />
                                    {t("availability.addSchedule")}
                                </Button>

                                <Button
                                    type="submit"
                                    className="bg-(--primary-color) hover:bg-(--primary-dark) text-white font-medium shadow-sm px-8"
                                >
                                    {t("availability.save")}
                                </Button>
                            </div>
                        </form>
                    </Form>
                </div>
            )}
        </DashboardNavContainer>
    );
}