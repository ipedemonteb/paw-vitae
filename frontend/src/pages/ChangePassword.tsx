import { useState, useEffect, type FormEvent } from "react"
import { useTranslation } from "react-i18next"
import { Link, useSearchParams } from "react-router-dom"
import {Lock, CheckCircle2, ArrowLeft, Check, X, AlertCircle, Loader2} from "lucide-react"
import { PasswordInput } from "@/components/ui/passwordInput"
import { Button } from "@/components/ui/button"
import {useAuth, useChangePasswordMutation} from "@/hooks/useAuth"

import { PasswordStrengthMeter } from "@/components/PasswordStrengthMeter.tsx"

const pageContainer = "min-h-screen bg-gray-50/50 flex flex-col items-center pt-32 pb-12 px-4 sm:px-6 lg:px-8";
const cardContainer = "w-full max-w-lg space-y-8 bg-white p-8 sm:p-12 rounded-xl border border-gray-200 shadow-xl ";
const headerContainer = "flex flex-col items-center text-center";
const iconCircle = "h-16 w-16 bg-[var(--primary-color)]/10 text-[var(--primary-color)] rounded-full flex items-center justify-center mb-6";
const successIconCircle = "h-16 w-16 bg-green-100 text-green-600 rounded-full flex items-center justify-center mb-6";
const titleText = "text-3xl font-bold tracking-tight text-[var(--text-color)]";
const subtitleText = "mt-3 text-base text-[var(--text-light)] max-w-sm mx-auto";
const formContainer = "mt-8 space-y-6";
const inputGroup = "space-y-4";
const inputStyle = "text-base";
const backLinkContainer = "flex items-center justify-center mt-8";
const backLink = "flex items-center gap-2 text-sm font-medium text-[var(--text-light)] hover:text-[var(--primary-color)] transition-colors";

export default function ChangePassword() {
    const { t } = useTranslation()
    const [searchParams] = useSearchParams()
    const { logout } = useAuth()
    const changePassword = useChangePasswordMutation()

    const token = searchParams.get("token")
    const email = searchParams.get("email")

    const [password, setPassword] = useState("")
    const [repeatPassword, setRepeatPassword] = useState("")
    const [isSubmitted, setIsSubmitted] = useState(false)
    const [isLoading, setIsLoading] = useState(false)
    const [apiError, setApiError] = useState<string | null>(null)

    const [isValid, setIsValid] = useState(false)
    const [passwordsMatch, setPasswordsMatch] = useState(false)

    useEffect(() => {
        if (!token || !email) {
            setApiError(t("change_password.error_invalid_link") || "Enlace inválido o expirado.");
        }
    }, [token, email, t]);

    useEffect(() => {
        const hasMinLength = password.length >= 8;
        const hasUppercase = /[A-Z]/.test(password);
        const hasNumber = /[0-9]/.test(password);
        const match = password === repeatPassword && repeatPassword !== "";

        setPasswordsMatch(match);
        setIsValid(hasMinLength && hasUppercase && hasNumber);

    }, [password, repeatPassword]);

    const isFormValid = isValid && passwordsMatch && !apiError;

    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();
        if (!isFormValid || !token || !email) return;

        setIsLoading(true);
        setApiError(null);

        changePassword.mutate({email, password, repeatPassword,token}, {
            onSuccess: () => setIsSubmitted(true),
            onError: () => setApiError( t("change_password.error_token_expired") || "Error al cambiar la contraseña."),
            onSettled: () => logout()
        })
    };

    if (isSubmitted) {
        return (
            <div className={pageContainer}>
                <div className={cardContainer}>
                    <div className={headerContainer}>
                        <div className={successIconCircle}>
                            <CheckCircle2 className="h-8 w-8" />
                        </div>
                        <h1 className={titleText}>
                            {t("change_password.success_title") || "Password Updated"}
                        </h1>
                        <p className={subtitleText}>
                            {t("change_password.success_message") || "Your password has been changed successfully."}
                        </p>
                    </div>
                    <div className={backLinkContainer}>
                        <Link to="/login" className={backLink}>
                            <ArrowLeft className="h-4 w-4" />
                            {t("change_password.back_to_login")}
                        </Link>
                    </div>
                </div>
            </div>
        )
    }

    return (
        <div className={pageContainer}>
            <div className={cardContainer}>

                <div className={headerContainer}>
                    <div className={iconCircle}>
                        <Lock className="h-8 w-8" />
                    </div>
                    <h1 className={titleText}>
                        {t("change_password.title")}
                    </h1>
                    <p className={subtitleText}>
                        {t("change_password.subtitle")}
                    </p>
                </div>

                <form className={formContainer} onSubmit={handleSubmit} noValidate>
                    {apiError && (
                        <div className="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-md flex items-center gap-2 text-sm">
                            <AlertCircle className="h-5 w-5 flex-shrink-0" />
                            <span>{apiError}</span>
                        </div>
                    )}

                    <div className={inputGroup}>
                        <PasswordInput
                            id="new-password"
                            label={t("change_password.new_password")}
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className={`${inputStyle} ${isValid ? "border-green-500 ring-green-500" : ""}`}
                            required
                        />

                        <PasswordStrengthMeter password={password} />
                    </div>

                    <div className={inputGroup}>
                        <PasswordInput
                            id="repeat-password"
                            label={t("change_password.confirm_password")}
                            value={repeatPassword}
                            onChange={(e) => setRepeatPassword(e.target.value)}
                            className={`${inputStyle} ${repeatPassword && !passwordsMatch ? "border-red-500 ring-red-500" : (passwordsMatch ? "border-green-500 ring-green-500" : "")}`}
                            required
                        />

                        {repeatPassword && !passwordsMatch && (
                            <p className="text-sm text-red-500 mt-2 flex items-center gap-1 animate-in fade-in slide-in-from-top-1 font-medium">
                                <X className="h-4 w-4" />
                                {t("change_password.error_mismatch")}
                            </p>
                        )}
                        {passwordsMatch && (
                            <p className="text-sm text-green-600 mt-2 flex items-center gap-1 animate-in fade-in slide-in-from-top-1 font-medium">
                                <Check className="h-4 w-4" />
                                {t("change_password.passwords_match")}
                            </p>
                        )}
                    </div>

                    <Button
                        type="submit"
                        className="w-full mt-6 bg-[var(--primary-color)] hover:bg-[var(--primary-dark)] h-12 text-base font-semibold shadow-md transition-all"
                        size="lg"
                        disabled={!isFormValid || isLoading}
                    >
                        {isLoading ? <Loader2 className="h-5 w-5 animate-spin"/> : t("change_password.submit")}
                    </Button>
                </form>

                <div className={backLinkContainer}>
                    <Link to="/login" className={backLink}>
                        <ArrowLeft className="h-4 w-4" />
                        {t("change_password.back_to_login")}
                    </Link>
                </div>
            </div>
        </div>
    )
}
