import { format, isSameMonth, isBefore, isSameDay, isWithinInterval, startOfMonth, endOfMonth, startOfWeek, endOfWeek, eachDayOfInterval, startOfToday } from "date-fns";
import { ChevronLeft, ChevronRight } from "lucide-react";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";

export type DateRange = {
    id: string;
    from: Date;
    to: Date;
};

interface CalendarGridProps {
    currentMonth: Date;
    unavailablePeriods: DateRange[];
    selectionStart: Date | undefined;
    onDayClick: (day: Date) => void;
    onNextMonth: () => void;
    onPrevMonth: () => void;
    locale: any;
}

export function CalendarGrid({
                                 currentMonth,
                                 unavailablePeriods,
                                 selectionStart,
                                 onDayClick,
                                 onNextMonth,
                                 onPrevMonth,
                                 locale
                             }: CalendarGridProps) {
    const today = startOfToday();
    const monthStart = startOfMonth(currentMonth);
    const monthEnd = endOfMonth(monthStart);
    const startDate = startOfWeek(monthStart, { weekStartsOn: 1 });
    const endDate = endOfWeek(monthEnd, { weekStartsOn: 1 });
    const calendarDays = eachDayOfInterval({ start: startDate, end: endDate });

    const weekDays = eachDayOfInterval({
        start: startOfWeek(new Date(), { weekStartsOn: 1 }),
        end: endOfWeek(new Date(), { weekStartsOn: 1 })
    });

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
            return isEdge ? `${baseStyle} text-white` : `${baseStyle} text-red-700`;
        }
        if (selectionStart && isSameDay(day, selectionStart)) return `${baseStyle} text-white`;
        return `${baseStyle} text-gray-700`;
    };

    return (
        <div className="flex flex-col shadow-sm rounded-xl overflow-hidden border border-gray-200 bg-white w-full">
            <div className="bg-blue-100/50 p-4 flex items-center justify-between border-b border-blue-200 h-16 shrink-0">
                <Button variant="ghost" size="icon" onClick={onPrevMonth} className="hover:bg-blue-200 text-blue-700">
                    <ChevronLeft className="h-5 w-5" />
                </Button>
                <h2 className="text-xl font-bold text-blue-900 capitalize">
                    {format(currentMonth, "MMMM yyyy", { locale })}
                </h2>
                <Button variant="ghost" size="icon" onClick={onNextMonth} className="hover:bg-blue-200 text-blue-700">
                    <ChevronRight className="h-5 w-5" />
                </Button>
            </div>

            <div className="shrink-0 bg-gray-100">
                <div className="grid grid-cols-7 border-b border-gray-100 bg-white">
                    {weekDays.map((day) => (
                        <div key={day.toString()} className="py-3 text-center text-sm font-semibold text-gray-500 uppercase tracking-wide">
                            {format(day, "EEE", { locale })}
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
                                onClick={() => !isPast && onDayClick(day)}
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
                    <span>Inicio/Fin</span>
                </div>
                <div className="flex items-center gap-2">
                    <div className="w-3 h-3 bg-red-100 border border-red-200 rounded-sm"></div>
                    <span>Rango</span>
                </div>
                <div className="flex items-center gap-2">
                    <div className="w-3 h-3 bg-gray-200 border border-gray-300 rounded-sm"></div>
                    <span>Pasado</span>
                </div>
            </div>
        </div>
    );
}