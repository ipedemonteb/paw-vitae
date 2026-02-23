import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.tsx";
import {useTranslation} from "react-i18next";

type DashboardNavSelectProps = {
    value: string;
    onValueChange: (val: string) => void;
    content: string[];
    display: (s: string) => string;
    all?: string
}

export default function DashboardNavSelect({value, onValueChange, content, display, all}: DashboardNavSelectProps) {
    const {t} = useTranslation()
    return (
        <Select
            value={value}
            onValueChange={onValueChange}
        >
            <SelectTrigger className="bg-white text-black hover:bg-(--gray-100) font-light text-sm border rounded-md cursor-pointer">
                <SelectValue/>
            </SelectTrigger>
            <SelectContent position="popper" className="min-w-fit max-w-28 p-0">
                {all && (
                    <SelectItem value="all">{t("appointment.filters.all")}</SelectItem>
                )}
                {content.map(o => (
                    <SelectItem key={o} value={o}>
                        {display(o)}
                    </SelectItem>
                ))}
            </SelectContent>
        </Select>
    )
}