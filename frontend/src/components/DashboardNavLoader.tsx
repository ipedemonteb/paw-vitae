import {Spinner} from "@/components/ui/spinner.tsx";
import {useTranslation} from "react-i18next";
import {cn} from "@/lib/utils.ts";


export default function DashboardNavLoader({ className }: { className?: string }) {
    const { t } = useTranslation();

    return (
        <div
            className={cn(
                "w-full flex flex-col gap-2 items-center rounded-2xl justify-center h-52 m-2",
                className
            )}
        >
            <Spinner className="h-8 w-8 text-(--gray-400)" />
            <span className="text-(--gray-500)">{t("loading")}</span>
        </div>
    );
}