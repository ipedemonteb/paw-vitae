import { format, isSameDay } from "date-fns";
import { X, Save, Loader2 } from "lucide-react";
import { Button } from "@/components/ui/button";

export type DateRange = {
    id: string;
    from: Date;
    to: Date;
};

interface SelectedPeriodsListProps {
    periods: DateRange[];
    onRemove: (id: string) => void;
    onSave: () => void;
    isSaving: boolean;
    locale: any;
    t: any;
}

export function SelectedPeriodsList({ periods, onRemove, onSave, isSaving, locale, t }: SelectedPeriodsListProps) {
    return (
        <div className="bg-white p-6 rounded-xl border shadow-sm space-y-4 w-full flex flex-col">
            <div className="flex items-center justify-between shrink-0">
                <h3 className="font-semibold text-lg text-gray-800">{t("unavailability.selectedDates")}</h3>
                <span className="text-sm text-gray-500 bg-gray-100 px-2 py-1 rounded-md">
                    {periods.length} {t("unavailability.items")}
                </span>
            </div>

            <div className="flex-1 overflow-y-auto pr-2 border border-gray-100 rounded-lg bg-gray-50/30 p-2">
                {periods.length === 0 ? (
                    <div className="h-full flex flex-col items-center justify-center text-gray-400">
                        <span className="italic">{t("unavailability.noDatesSelected")}</span>
                    </div>
                ) : (
                    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-3">
                        {periods.map((period) => (
                            <div key={period.id} className="flex items-center justify-between p-3 bg-white hover:shadow-sm hover:border-blue-300 transition-all rounded-lg text-sm border border-gray-200">
                                <div className="flex flex-col min-w-0">
                                    {isSameDay(period.from, period.to) ? (
                                        <>
                                            <span className="text-[10px] text-gray-400 font-bold uppercase tracking-wider">
                                                {t("unavailability.singleDay")}
                                            </span>
                                            <span className="font-medium text-gray-800 truncate">
                                                {format(period.from, "PPP", { locale })}
                                            </span>
                                        </>
                                    ) : (
                                        <>
                                            <span className="text-[10px] text-gray-400 font-bold uppercase tracking-wider">
                                                {t("unavailability.range")}
                                            </span>
                                            <div className="flex items-center gap-1 font-medium text-gray-800 truncate">
                                                <span>{format(period.from, "P", { locale })}</span>
                                                <span className="text-gray-400">→</span>
                                                <span>{format(period.to, "P", { locale })}</span>
                                            </div>
                                        </>
                                    )}
                                </div>
                                <Button variant="ghost" size="icon" className="h-8 w-8 text-gray-400 hover:text-red-500 hover:bg-red-50 rounded-full shrink-0" onClick={() => onRemove(period.id)}>
                                    <X className="h-4 w-4" />
                                </Button>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            <div className="pt-2 flex justify-end shrink-0">
                <Button
                    onClick={onSave}
                    className="bg-(--primary-color) hover:bg-(--primary-dark) text-white shadow-md px-8"
                    disabled={isSaving}
                >
                    {isSaving ? <Loader2 className="mr-2 h-4 w-4 animate-spin" /> : <Save className="mr-2 h-4 w-4" />}
                    {t("unavailability.saveChanges")}
                </Button>
            </div>
        </div>
    );
}