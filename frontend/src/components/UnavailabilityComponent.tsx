import { useState, useMemo } from "react";
import DashboardNavContainer from "@/components/DashboardNavContainer.tsx";
import DashboardNavHeader from "@/components/DashboardNavHeader.tsx";
import { useTranslation } from "react-i18next";
import { Button } from "@/components/ui/button";
import { ChevronLeft, ChevronRight, Save, X } from "lucide-react";
import {
    format,
    addMonths,
    subMonths,
    startOfMonth,
    endOfMonth,
    startOfWeek,
    endOfWeek,
    eachDayOfInterval,
    isSameMonth,
    isSameDay,
    isWithinInterval,
    isBefore,
    compareAsc,
    max,
    startOfToday
} from "date-fns";
import { es, enUS } from "date-fns/locale";
import { cn } from "@/lib/utils";

const scrollbarFix = {
    scrollbarGutter: 'stable'
};

type DateRange = {
    id: string;
    from: Date;
    to: Date;
};

const locales: Record<string, any> = {
    es: es,
    en: enUS,
};

export default function UnavailabilityComponent() {
    const { t, i18n } = useTranslation();
    const currentLocale = locales[i18n.language?.split('-')[0]] || enUS;
    const today = startOfToday();

    const [currentMonth, setCurrentMonth] = useState(new Date());
    const [unavailablePeriods, setUnavailablePeriods] = useState<DateRange[]>([]);
    const [selectionStart, setSelectionStart] = useState<Date | undefined>(undefined);

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

    const monthStart = startOfMonth(currentMonth);
    const monthEnd = endOfMonth(monthStart);
    const startDate = startOfWeek(monthStart, { weekStartsOn: 1 });
    const endDate = endOfWeek(monthEnd, { weekStartsOn: 1 });
    const calendarDays = eachDayOfInterval({ start: startDate, end: endDate });

    const weekDays = useMemo(() => {
        const firstWeekStart = startOfWeek(new Date(), { weekStartsOn: 1 });
        const firstWeekEnd = endOfWeek(new Date(), { weekStartsOn: 1 });
        return eachDayOfInterval({ start: firstWeekStart, end: firstWeekEnd });
    }, []);

    const renderDayBackground = (day: Date) => {
        if (selectionStart && isSameDay(day, selectionStart)) {
            return <div className="absolute inset-1 bg-blue-500 rounded-md animate-pulse z-0" />;
        }

        const period = unavailablePeriods.find(p => isWithinInterval(day, { start: p.from, end: p.to }));
        if (!period) return null;

        const isStart = isSameDay(day, period.from);
        const isEnd = isSameDay(day, period.to);
        const isSingleDay = isStart && isEnd;

        if (isSingleDay) return <div className="absolute inset-1 bg-red-500 rounded-md z-0" />;
        if (isStart) return <div className="absolute top-1 bottom-1 left-1 right-0 bg-red-500 rounded-l-md z-0" />;
        if (isEnd) return <div className="absolute top-1 bottom-1 left-0 right-1 bg-red-500 rounded-r-md z-0" />;

        return <div className="absolute inset-y-1 inset-x-0 bg-red-100 z-0" />;
    };

    const getDayTextStyle = (day: Date) => {
        const baseStyle = "font-semibold text-sm transition-colors duration-200";

        const period = unavailablePeriods.find(p => isWithinInterval(day, { start: p.from, end: p.to }));
        if (period) {
            const isEdge = isSameDay(day, period.from) || isSameDay(day, period.to);
            if (isEdge) return `${baseStyle} text-white`;
            return `${baseStyle} text-red-700`;
        }
        if (selectionStart && isSameDay(day, selectionStart)) return `${baseStyle} text-white`;
        return `${baseStyle} text-gray-700`;
    };

    const handleSave = () => {
    };

    return (
        <DashboardNavContainer>
            <div style={scrollbarFix} className="w-full">
                <DashboardNavHeader title={t("unavailability.headerTitle")} children={undefined} />

                <div className="p-6 max-w-6xl mx-auto flex flex-col gap-6 animate-in fade-in-50">
                    <div className="flex flex-col shadow-sm rounded-xl overflow-hidden border border-gray-200 bg-white w-full">
                        <div className="bg-blue-100/50 p-4 flex items-center justify-between border-b border-blue-200 h-16 shrink-0">
                            <Button variant="ghost" size="icon" onClick={prevMonth} className="hover:bg-blue-200 text-blue-700">
                                <ChevronLeft className="h-5 w-5" />
                            </Button>
                            <h2 className="text-xl font-bold text-blue-900 capitalize">
                                {format(currentMonth, "MMMM yyyy", { locale: currentLocale })}
                            </h2>
                            <Button variant="ghost" size="icon" onClick={nextMonth} className="hover:bg-blue-200 text-blue-700">
                                <ChevronRight className="h-5 w-5" />
                            </Button>
                        </div>

                        <div className="shrink-0 bg-gray-100">
                            <div className="grid grid-cols-7 border-b border-gray-100 bg-white">
                                {weekDays.map((day) => (
                                    <div key={day.toString()} className="py-3 text-center text-sm font-semibold text-gray-500 uppercase tracking-wide">
                                        {format(day, "EEE", { locale: currentLocale })}
                                    </div>
                                ))}
                            </div>

                            <div className="grid grid-cols-7 gap-px bg-gray-100 border-b border-gray-100">
                                {calendarDays.map((day) => {
                                    const isCurrentMonth = isSameMonth(day, monthStart);
                                    const isPast = isBefore(day, today);

                                    return (
                                        <div
                                            key={day.toString()}
                                            onClick={() => !isPast && handleDayClick(day)}
                                            className={cn(
                                                "relative h-24 transition-colors",
                                                isPast ? "bg-gray-50/80 cursor-not-allowed" : "bg-white cursor-pointer hover:bg-gray-50",
                                                !isCurrentMonth && !isPast && "bg-gray-50/30"
                                            )}
                                        >
                                            {!isPast && renderDayBackground(day)}

                                            <div className="absolute inset-0 flex items-center justify-center pointer-events-none z-20">
                                                <span className={cn(
                                                    "flex items-center justify-center w-8 h-8 rounded-full tabular-nums",
                                                    isPast && "text-gray-300 line-through",
                                                    !isPast && getDayTextStyle(day)
                                                )}>
                                                    {format(day, "d")}
                                                </span>
                                            </div>
                                        </div>
                                    );
                                })}
                            </div>
                        </div>

                        <div className="bg-gray-50 p-3 flex gap-6 text-xs font-medium text-gray-600 border-t border-gray-100 justify-center h-12 items-center shrink-0">
                            <div className="flex items-center gap-2">
                                <div className="w-3 h-3 bg-red-500 rounded-sm"></div>
                                <span>{t("unavailability.startEndOrSingle")}</span>
                            </div>
                            <div className="flex items-center gap-2">
                                <div className="w-3 h-3 bg-red-100 border border-red-200 rounded-sm"></div>
                                <span>{t("unavailability.inRange")}</span>
                            </div>
                            <div className="flex items-center gap-2">
                                <div className="w-3 h-3 bg-gray-200 border border-gray-300 rounded-sm"></div>
                                <span>{t("unavailability.pastDates")}</span>
                            </div>
                        </div>
                    </div>

                    <div className="bg-white p-6 rounded-xl border shadow-sm space-y-4 w-full flex flex-col ">
                        <div className="flex items-center justify-between shrink-0">
                            <h3 className="font-semibold text-lg text-gray-800">{t("unavailability.selectedDates")}</h3>
                            <span className="text-sm text-gray-500 bg-gray-100 px-2 py-1 rounded-md">
                                {unavailablePeriods.length} {t("unavailability.items")}
                            </span>
                        </div>

                        <div className="flex-1 overflow-y-auto pr-2 border border-gray-100 rounded-lg bg-gray-50/30 p-2">
                            {unavailablePeriods.length === 0 ? (
                                <div className="h-full flex flex-col items-center justify-center text-gray-400">
                                    <span className="italic">{t("unavailability.noDatesSelected")}</span>
                                </div>
                            ) : (
                                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-3">
                                    {unavailablePeriods.map((period) => (
                                        <div key={period.id} className="flex items-center justify-between p-3 bg-white hover:shadow-sm hover:border-blue-300 transition-all rounded-lg text-sm border border-gray-200">
                                            <div className="flex flex-col min-w-0">
                                                {isSameDay(period.from, period.to) ? (
                                                    <>
                                                        <span className="text-[10px] text-gray-400 font-bold uppercase tracking-wider">
                                                            {t("unavailability.singleDay")}
                                                        </span>
                                                        <span className="font-medium text-gray-800 truncate">
                                                            {format(period.from, "PPP", { locale: currentLocale })}
                                                        </span>
                                                    </>
                                                ) : (
                                                    <>
                                                        <span className="text-[10px] text-gray-400 font-bold uppercase tracking-wider">
                                                            {t("unavailability.range")}
                                                        </span>
                                                        <div className="flex items-center gap-1 font-medium text-gray-800 truncate">
                                                            <span>{format(period.from, "P", { locale: currentLocale })}</span>
                                                            <span className="text-gray-400">→</span>
                                                            <span>{format(period.to, "P", { locale: currentLocale })}</span>
                                                        </div>
                                                    </>
                                                )}
                                            </div>
                                            <Button variant="ghost" size="icon" className="h-8 w-8 text-gray-400 hover:text-red-500 hover:bg-red-50 rounded-full shrink-0" onClick={() => removePeriod(period.id)}>
                                                <X className="h-4 w-4" />
                                            </Button>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>

                        <div className="pt-2 flex justify-end shrink-0">
                            <Button
                                onClick={handleSave}
                                className="bg-(--primary-color) hover:bg-(--primary-dark) text-white shadow-md px-8"
                                disabled={unavailablePeriods.length === 0}
                            >
                                <Save className="mr-2 h-4 w-4" />
                                {t("unavailability.saveChanges")}
                            </Button>
                        </div>
                    </div>
                </div>
            </div>
        </DashboardNavContainer>
    );
}