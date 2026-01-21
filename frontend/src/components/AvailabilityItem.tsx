import { Button } from "../components/ui/button";
import { Card, CardContent } from "../components/ui/card";
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from "../components/ui/form";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Trash2 } from "lucide-react";
import { useTranslation } from "react-i18next";
import {type Control, useFormContext } from "react-hook-form";
import {type AvailabilityFormValues, DAYS, TIMES, extractIdFromUrl } from "../lib/availability-schema.ts";
import type {OfficeDTO} from "@/data/office.ts";

interface Props {
    index: number;
    remove: (index: number) => void;
    offices?: OfficeDTO[];
    control: Control<AvailabilityFormValues>;
}

export function AvailabilityItem({ index, remove, offices, control }: Props) {
    const { t } = useTranslation();
    const { formState } = useFormContext<AvailabilityFormValues>();

    return (
        <Card className="relative bg-white shadow-sm border border-gray-200">
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
                            control={control}
                            name={`schedules.${index}.officeId`}
                            render={({ field }) => (
                                <FormItem className="space-y-1 w-full">
                                    <FormLabel className="text-[10px] font-bold text-gray-500 tracking-wider">
                                        {t("availability.office")}
                                    </FormLabel>
                                    <Select onValueChange={field.onChange} value={field.value}>
                                        <FormControl>
                                            <SelectTrigger className="w-full max-w-[98%] h-9 bg-gray-50/50 text-sm overflow-hidden">
                                                <span className="flex-1 text-left truncate min-w-0 pr-2">
                                                    <SelectValue placeholder={t("availability.selectPlaceholder")} />
                                                </span>
                                            </SelectTrigger>
                                        </FormControl>
                                        <SelectContent>
                                            {offices?.map((office) => {
                                                const id = extractIdFromUrl(office.self);
                                                return (
                                                    <SelectItem key={id} value={id}>
                                                        {office.name}
                                                    </SelectItem>
                                                );
                                            })}
                                        </SelectContent>
                                    </Select>
                                    <FormMessage className="text-xs" />
                                </FormItem>
                            )}
                        />
                    </div>

                    <div className="col-span-1 md:col-span-3 min-w-0">
                        <FormField
                            control={control}
                            name={`schedules.${index}.dayOfWeek`}
                            render={({ field }) => (
                                <FormItem className="space-y-1 w-full">
                                    <FormLabel className="text-[10px] font-bold text-gray-500 tracking-wider">
                                        {t("availability.dayOfWeek")}
                                    </FormLabel>
                                    <Select onValueChange={field.onChange} value={field.value}>
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
                            control={control}
                            name={`schedules.${index}.startTime`}
                            render={({ field }) => (
                                <FormItem className="space-y-1 w-full">
                                    <FormLabel className="text-[10px] font-bold text-gray-500 tracking-wider">
                                        {t("availability.startTime")}
                                    </FormLabel>
                                    <Select onValueChange={field.onChange} value={field.value}>
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
                                    {formState.errors.schedules?.[index]?.startTime?.message && (
                                        <p className="text-xs font-medium text-red-500">
                                            {t(`availability.${formState.errors.schedules[index]?.startTime?.message}`)}
                                        </p>
                                    )}
                                </FormItem>
                            )}
                        />
                    </div>

                    <div className="col-span-1 md:col-span-3 min-w-0">
                        <FormField
                            control={control}
                            name={`schedules.${index}.endTime`}
                            render={({ field }) => (
                                <FormItem className="space-y-1 w-full">
                                    <FormLabel className="text-[10px] font-bold text-gray-500 tracking-wider">
                                        {t("availability.endTime")}
                                    </FormLabel>
                                    <Select onValueChange={field.onChange} value={field.value}>
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
                                    {formState.errors.schedules?.[index]?.endTime?.message && (
                                        <p className="text-xs font-medium text-red-500">
                                            {t(`availability.${formState.errors.schedules[index]?.endTime?.message}`)}
                                        </p>
                                    )}
                                </FormItem>
                            )}
                        />
                    </div>
                </div>
            </CardContent>
        </Card>
    );
}