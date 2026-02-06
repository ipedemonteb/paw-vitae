import {Spinner} from "@/components/ui/spinner.tsx";
import {useTranslation} from "react-i18next";

const loadingContainer = "flex flex-col gap-4 justify-center items-center h-screen";
const spinner = "h-12 w-12 text-(--gray-400)";
const loadingText = "text-xl text-(--gray-500)";

export function LoadingFullPageComponent() {
    const { t } = useTranslation();
    return (
        <div className={loadingContainer}>
            <Spinner className={spinner} />
            <p className={loadingText}>{t("loading")}</p>
        </div>
    );
}