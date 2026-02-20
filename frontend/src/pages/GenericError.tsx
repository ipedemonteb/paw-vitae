import { useTranslation } from "react-i18next";
import { Link, useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { ShieldAlert, FileQuestion, ServerCrash, Home, ArrowLeft, AlertTriangle } from "lucide-react";

const errorVariants: Record<number, { icon: any; color: string; bg: string }> = {
    403: {
        icon: ShieldAlert,
        color: "text-[var(--warning-dark)]",
        bg: "bg-[var(--warning-light)]",
    },
    404: {
        icon: FileQuestion,
        color: "text-[var(--primary-color)]",
        bg: "bg-[var(--primary-bg)]",
    },
    500: {
        icon: ServerCrash,
        color: "text-[var(--danger-dark)]",
        bg: "bg-[var(--danger-light)]",
    },
    0: {
        icon: AlertTriangle,
        color: "text-[var(--primary-color)]",
        bg: "bg-[var(--primary-bg)]",
    },
};

interface GenericErrorProps {
    code?: number;
    title?: string;
    message?: string;
    showBackButton?: boolean;
}

const pageContainer =
    "min-h-screen bg-[var(--background-light)] flex flex-col items-center justify-center px-4 sm:px-6 lg:px-8";
const cardContainer =
    "w-full max-w-lg space-y-8 bg-[var(--white)] p-8 sm:p-10 rounded-xl border border-[var(--gray-200)] shadow-[var(--shadow-xl)] text-center";
const iconContainerBase = "mx-auto h-24 w-24 rounded-full flex items-center justify-center mb-6";
const titleText = "text-3xl font-bold tracking-tight text-[var(--text-color)]";
const messageText = "mt-4 text-base text-[var(--text-light)] max-w-sm mx-auto";
const errorCodeText = "mt-2 text-sm font-semibold uppercase tracking-wide";
const buttonGroup = "mt-8 flex flex-col gap-3 sm:flex-row sm:justify-center";

export default function GenericError({
                                         code = 500,
                                         title,
                                         message,
                                         showBackButton = true,
                                     }: GenericErrorProps) {
    const { t } = useTranslation();
    const navigate = useNavigate();

    const variant = errorVariants[code] || errorVariants[0];
    const Icon = variant.icon;

    const displayTitle = title || t(`error.${code}.title`, "Error");
    const displayMessage = message || t(`error.${code}.message`, "An unexpected error occurred.");

    return (
        <div className={pageContainer}>
            <div className={cardContainer}>
                <div className={`${iconContainerBase} ${variant.bg} ${variant.color}`}>
                    <Icon className="h-12 w-12" />
                </div>

                <div>
                    <h1 className={titleText}>{displayTitle}</h1>
                    <p className={`${errorCodeText} ${variant.color}`}>Error {code}</p>
                    <p className={messageText}>{displayMessage}</p>
                </div>

                <div className={buttonGroup}>
                    {showBackButton && (
                        <Button
                            type="button"
                            variant="outline"
                            className="w-full sm:w-auto border border-(--gray-300) text-(--text-color) hover:bg-(--gray-100) cursor-pointer"
                            onClick={() => navigate(-1)}
                        >
                            <ArrowLeft className="mr-2 h-4 w-4" />
                            {t("common.go_back", "Go Back")}
                        </Button>
                    )}

                    <Link to="/" className="w-full sm:w-auto">
                        <Button className="w-full bg-(--primary-color) hover:bg-(--primary-dark) text-white cursor-pointer">
                            <Home className="mr-2 h-4 w-4" />
                            {t("common.return_home", "Return Home")}
                        </Button>
                    </Link>
                </div>
            </div>
        </div>
    );
}
