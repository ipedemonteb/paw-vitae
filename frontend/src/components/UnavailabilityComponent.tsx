import { useState, useEffect } from "react";
import DashboardNavContainer from "@/components/DashboardNavContainer.tsx";
import DashboardNavHeader from "@/components/DashboardNavHeader.tsx";
import { useTranslation } from "react-i18next";
import { Button } from "@/components/ui/button";
import { AlertCircle, RefreshCw, Loader2 } from "lucide-react";
import { addMonths, subMonths, isBefore, isSameDay, compareAsc, max, startOfToday, parseISO, format } from "date-fns";
import { es, enUS } from "date-fns/locale";
import { useAuth } from "@/hooks/useAuth.ts";
import { useDoctorUnavailability, usePutDoctorUnavailability } from "@/hooks/useDoctors.ts";
import { toast } from "sonner";
import { type DoctorUnavailabilityFormDTO } from "@/data/doctors.ts";
import { useQueryClient } from "@tanstack/react-query";
import { CalendarGrid } from "./CalendarGrid";
import { SelectedPeriodsList } from "./SelectedPeriodsList";

export type DateRange = {
    id: string;
    from: Date;
    to: Date;
};

const scrollbarFix = {
    scrollbarGutter: 'stable'
};

const locales: Record<string, any> = {
    es: es,
    en: enUS,
};

export default function UnavailabilityComponent() {
    const { t, i18n } = useTranslation();
    const currentLocale = locales[i18n.language?.split('-')[0]] || enUS;
    const today = startOfToday();
    const auth = useAuth();
    const queryClient = useQueryClient();

    const {
        data: serverUnavailability,
        isLoading,
        isError,
        error,
        isSuccess,
        isRefetching
    } = useDoctorUnavailability(`/doctors/${auth.userId}/unavailability`);

    const { mutateAsync: saveUnavailability, isPending: isSaving } = usePutDoctorUnavailability(`/doctors/${auth.userId}/unavailability`);

    const [currentMonth, setCurrentMonth] = useState(new Date());
    const [unavailablePeriods, setUnavailablePeriods] = useState<DateRange[]>([]);
    const [selectionStart, setSelectionStart] = useState<Date | undefined>(undefined);
    const [isDataSynchronized, setIsDataSynchronized] = useState(false);

    useEffect(() => {
        if (isSuccess && serverUnavailability && !isDataSynchronized && !isRefetching) {
            const mappedPeriods = serverUnavailability.map((dto) => ({
                id: crypto.randomUUID(),
                from: parseISO(dto.startDate),
                to: parseISO(dto.endDate)
            }));
            setUnavailablePeriods(mappedPeriods);
            setIsDataSynchronized(true);
        }
    }, [isSuccess, serverUnavailability, isDataSynchronized, isRefetching]);



    const nextMonth = () => setCurrentMonth(addMonths(currentMonth, 1));
    const prevMonth = () => setCurrentMonth(subMonths(currentMonth, 1));

    const addAndMergePeriods = (newPeriod: DateRange) => {
        const allPeriods = [...unavailablePeriods, newPeriod];
        allPeriods.sort((a, b) => compareAsc(a.from, b.from));

        const merged: DateRange[] = [];
        if (allPeriods.length === 0) return;

        let current = allPeriods[0];

        for (let i = 1; i < allPeriods.length; i++) {
            const next = allPeriods[i];
            if (isBefore(next.from, current.to) || isSameDay(next.from, current.to)) {
                current = {
                    ...current,
                    to: max([current.to, next.to]),
                };
            } else {
                merged.push(current);
                current = next;
            }
        }
        merged.push(current);
        setUnavailablePeriods(merged);
    };

    const handleDayClick = (day: Date) => {
        if (isBefore(day, today)) return;

        if (!selectionStart) {
            setSelectionStart(day);
            return;
        }

        let newRange: DateRange;
        if (isBefore(day, selectionStart)) {
            newRange = { id: crypto.randomUUID(), from: day, to: selectionStart };
        } else {
            newRange = { id: crypto.randomUUID(), from: selectionStart, to: day };
        }

        addAndMergePeriods(newRange);
        setSelectionStart(undefined);
    };

    const removePeriod = (id: string) => {
        setUnavailablePeriods(unavailablePeriods.filter(p => p.id !== id));
    };

    const handleSave = async () => {
        try {
            const payload: DoctorUnavailabilityFormDTO = {
                unavailabilitySlots: unavailablePeriods.map(p => ({
                    startDate: format(p.from, 'yyyy-MM-dd'),
                    endDate: format(p.to, 'yyyy-MM-dd')
                }))
            };

            await saveUnavailability(payload);
            setIsDataSynchronized(false);

            toast.success(t("success"), {
                description: t("unavailability.success")
            });

        } catch (err) {
            console.error(err);
            toast.error(t("Error"), {
                description: t("unavailability.error")
            });
        }
    };

    if (isError) {
        return (
            <DashboardNavContainer>
                <DashboardNavHeader title={t("unavailability.headerTitle")} children={undefined} />
                <div className="flex flex-col items-center justify-center h-[50vh] text-center p-6">
                    <div className="bg-red-50 p-4 rounded-full mb-4">
                        <AlertCircle className="h-10 w-10 text-red-500" />
                    </div>
                    <h3 className="text-lg font-semibold text-gray-900 mb-2">
                        {t("common.error_loading", "Error loading data")}
                    </h3>
                    <p className="text-gray-500 max-w-sm mb-6">
                        {error?.message || t("common.error_generic_message", "There was a problem loading your unavailability data.")}
                    </p>
                    <Button
                        variant="outline"
                        onClick={() => {
                            setIsDataSynchronized(false);
                            queryClient.invalidateQueries({ queryKey: ['doctor', 'unavailability'] });
                        }}
                    >
                        <RefreshCw className="mr-2 h-4 w-4" />
                        {t("common.retry", "Retry")}
                    </Button>
                </div>
            </DashboardNavContainer>
        );
    }

    if (isLoading) {
        return (
            <DashboardNavContainer>
                <DashboardNavHeader title={t("unavailability.headerTitle")} children={undefined} />
                <div className="flex flex-col items-center justify-center h-[50vh] text-gray-400">
                    <Loader2 className="h-10 w-10 animate-spin mb-4" />
                    <p>{t("Loading")}</p>
                </div>
            </DashboardNavContainer>
        );
    }

    return (
        <DashboardNavContainer>
            <div style={scrollbarFix} className="w-full">
                <DashboardNavHeader title={t("unavailability.headerTitle")} children={undefined} />
                <div className="p-6 max-w-6xl mx-auto flex flex-col gap-6 animate-in fade-in-50">

                    <CalendarGrid
                        currentMonth={currentMonth}
                        unavailablePeriods={unavailablePeriods}
                        selectionStart={selectionStart}
                        onDayClick={handleDayClick}
                        onNextMonth={nextMonth}
                        onPrevMonth={prevMonth}
                        locale={currentLocale}
                    />

                    <SelectedPeriodsList
                        periods={unavailablePeriods}
                        onRemove={removePeriod}
                        onSave={handleSave}
                        isSaving={isSaving}
                        locale={currentLocale}
                        t={t}
                    />
                </div>
            </div>
        </DashboardNavContainer>
    );
}