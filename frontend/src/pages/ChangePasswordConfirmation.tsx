import { useTranslation } from "react-i18next";
import { Link, Navigate, useLocation } from "react-router-dom";
import { CheckCircle2, ArrowLeft } from "lucide-react";
import { Button } from "@/components/ui/button";

const pageContainer = "min-h-screen bg-(--background-light) flex flex-col items-center pt-36 pb-6 px-4 sm:px-6 lg:px-8";
const cardContainer = "w-full max-w-lg bg-white py-10 px-16 rounded-xl border border-[var(--gray-300)] shadow-sm";
const headerContainer = "flex flex-col items-center text-center";
const successIconCircle = "h-16 w-16 bg-(--success-light) text-(--success) rounded-full flex items-center justify-center mb-6";
const titleText = "text-3xl font-bold tracking-tight text-[var(--text-color)]";
const subtitleText = "mt-3 text-base text-(--text-light) max-w-sm mx-auto";
const backLinkContainer = "flex items-center justify-center mt-6";
const backLink = "flex items-center gap-2 text-sm font-medium text-[var(--text-light)] hover:text-[var(--primary-color)] transition-colors";
const submitButton = "w-full text-base mt-8 font-semibold bg-[var(--primary-color)] hover:bg-[var(--primary-dark)] cursor-pointer";

export default function ChangePasswordConfirmation() {
    const { t } = useTranslation();
    const location = useLocation();

    // SEGURIDAD: Si no existe el estado "fromChangePassword", lo mandamos al home
    if (!location.state?.fromChangePassword) {
        return <Navigate to="/" replace />;
    }

    return (
        <div className={pageContainer}>
            <div className={cardContainer}>
                <div className={headerContainer}>
                    <div className={successIconCircle}>
                        <CheckCircle2 className="h-8 w-8" />
                    </div>
                    <h1 className={titleText}>{t("change_password.success_title")}</h1>
                    <p className={subtitleText}>{t("change_password.success_message")}</p>
                </div>

                <Link to="/login">
                    <Button className={submitButton} size="lg">
                        {t("change_password.back_to_login")}
                    </Button>
                </Link>

                <div className={backLinkContainer}>
                    <Link to="/" className={backLink}>
                        <ArrowLeft className="h-4 w-4" />
                        {t("common.back_to_home")}
                    </Link>
                </div>
            </div>
        </div>
    );
}