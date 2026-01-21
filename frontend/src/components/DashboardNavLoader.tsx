import {Spinner} from "@/components/ui/spinner.tsx";
import {useTranslation} from "react-i18next";

export default function DashboardNavLoader() {
    const { t } = useTranslation();
    return (
        <div className="w-full flex flex-col gap-2 items-center rounded-2xl justify-center h-52 m-2 ">
            <Spinner className="h-8 w-8 text-(--text-light)"/>
            <span className="text-(--text-light)">
                {t("loading")}
            </span>
        </div>
    )
}