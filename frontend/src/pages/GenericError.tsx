import { useTranslation } from "react-i18next";
import { Link, useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import {
    ShieldAlert,
    FileQuestion,
    ServerCrash,
    Home,
    ArrowLeft,
    AlertTriangle
} from "lucide-react";

// --- Configuración de Variantes ---
// Definimos estilos e iconos según el código de error
const errorVariants: Record<number, { icon: any, color: string, bg: string }> = {
    403: {
        icon: ShieldAlert,
        color: "text-orange-600",
        bg: "bg-orange-100"
    },
    404: {
        icon: FileQuestion,
        color: "text-blue-600",
        bg: "bg-blue-100"
    },
    500: {
        icon: ServerCrash,
        color: "text-red-600",
        bg: "bg-red-100"
    },
    // Default
    0: {
        icon: AlertTriangle,
        color: "text-[var(--primary-color)]",
        bg: "bg-[var(--primary-color)]/10"
    }
};

interface GenericErrorProps {
    code?: number;
    title?: string;
    message?: string;
    showBackButton?: boolean;
}

// --- ESTILOS (Mismo patrón que ChangePassword/Verify) ---
const pageContainer = "min-h-screen bg-gray-50/50 flex flex-col items-center justify-center px-4 sm:px-6 lg:px-8";
const cardContainer = "w-full max-w-lg space-y-8 bg-white p-8 sm:p-10 rounded-xl border border-gray-200 shadow-xl text-center";
const iconContainerBase = "mx-auto h-24 w-24 rounded-full flex items-center justify-center mb-6";
const titleText = "text-3xl font-bold tracking-tight text-[var(--text-color)]";
const messageText = "mt-4 text-base text-[var(--text-light)] max-w-sm mx-auto";
const errorCodeText = "mt-2 text-sm font-semibold uppercase tracking-wide";
const buttonGroup = "mt-8 flex flex-col gap-3 sm:flex-row sm:justify-center";

export default function GenericError({
                                         code = 500,
                                         title,
                                         message,
                                         showBackButton = true
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

                {/* Icono Dinámico */}
                <div className={`${iconContainerBase} ${variant.bg} ${variant.color}`}>
                    <Icon className="h-12 w-12" />
                </div>

                {/* Contenido de Texto */}
                <div>
                    <h1 className={titleText}>{displayTitle}</h1>
                    <p className={`${errorCodeText} ${variant.color}`}>
                        Error {code}
                    </p>
                    <p className={messageText}>
                        {displayMessage}
                    </p>
                </div>

                {/* Botones de Acción */}
                <div className={buttonGroup}>
                    {showBackButton && (
                        <Button
                            variant="outline"
                            className="w-full sm:w-auto border-gray-300"
                            onClick={() => navigate(-1)}
                        >
                            <ArrowLeft className="mr-2 h-4 w-4" />
                            {t("common.go_back", "Go Back")}
                        </Button>
                    )}

                    <Link to="/" className="w-full sm:w-auto">
                        <Button className="w-full bg-[var(--primary-color)] hover:bg-[var(--primary-dark)]">
                            <Home className="mr-2 h-4 w-4" />
                            {t("common.return_home", "Return Home")}
                        </Button>
                    </Link>
                </div>
            </div>

            {/* Footer simple */}
            <div className="mt-8 text-sm text-gray-400">
                &copy; {new Date().getFullYear()} Vitae Medical
            </div>
        </div>
    );
}