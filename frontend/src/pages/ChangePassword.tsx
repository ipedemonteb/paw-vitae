import { useState, useEffect, type FormEvent } from "react"
import { useTranslation } from "react-i18next"
import {Link, useSearchParams} from "react-router-dom"
import {Lock, CheckCircle2, ArrowLeft, Check, X, AlertCircle} from "lucide-react"
import { PasswordInput } from "@/components/ui/passwordInput"
import { Button } from "@/components/ui/button"
import {changePatientPassword} from "@/data/patients.ts";
import { useAuth } from "@/hooks/useAuth"
import { changeDoctorPassword } from "@/data/doctors"
import { getAccessToken } from "@/context/auth-store"

function parseJwt(token: string) {
    try {
        return JSON.parse(atob(token.split('.')[1]));
    } catch (e) {
        return null;
    }
}


const pageContainer =
    "min-h-screen bg-gray-50/50 flex flex-col items-center pt-32 pb-12 px-4 sm:px-6 lg:px-8";
const cardContainer =
    "w-full max-w-lg space-y-8 bg-white p-8 sm:p-12 rounded-xl border border-gray-200 shadow-xl ";

const headerContainer =
    "flex flex-col items-center text-center";

const iconCircle =
    "h-16 w-16 bg-[var(--primary-color)]/10 text-[var(--primary-color)] rounded-full flex items-center justify-center mb-6";

const successIconCircle =
    "h-16 w-16 bg-green-100 text-green-600 rounded-full flex items-center justify-center mb-6";

const titleText =
    "text-3xl font-bold tracking-tight text-[var(--text-color)]";

const subtitleText =
    "mt-3 text-base text-[var(--text-light)] max-w-sm mx-auto";

const formContainer =
    "mt-8 space-y-6";

const inputGroup =
    "space-y-4";

const inputStyle =
    "text-base";

const validationMessage =
    "text-sm text-[var(--text-light)] flex items-center gap-2 mt-1.5 font-medium transition-colors duration-200";

const backLinkContainer =
    "flex items-center justify-center mt-8";

const backLink =
    "flex items-center gap-2 text-sm font-medium text-[var(--text-light)] hover:text-[var(--primary-color)] transition-colors";

const strengthBarContainer = "w-full h-2 bg-gray-100 rounded-full mt-4 overflow-hidden";
const strengthBarFill = "h-full transition-all duration-500 ease-out";

export default function ChangePassword() {
    const { t } = useTranslation()
    const [searchParams] = useSearchParams()

    const { login, logout } = useAuth()

    const token = searchParams.get("token")
    const email = searchParams.get("email")

    const [password, setPassword] = useState("")
    const [repeatPassword, setRepeatPassword] = useState("")

    const [isSubmitted, setIsSubmitted] = useState(false)
    const [ isLoading,setIsLoading] = useState(false)
    const [apiError, setApiError] = useState<string | null>(null)

    const [strength, setStrength] = useState(0)
    const [hasMinLength, setHasMinLength] = useState(false)
    const [hasUppercase, setHasUppercase] = useState(false)
    const [hasNumber, setHasNumber] = useState(false)
    const [passwordsMatch, setPasswordsMatch] = useState(false)

    useEffect(() => {
        if (!token || !email) {
            setApiError(t("change_password.error_invalid_link") || "Enlace inválido o expirado.");
        }
    }, [token, email, t]);

    useEffect(() => {
        let score = 0;
        if (password.length >= 8) score++;
        if (password.length >= 12) score++;
        if (/[A-Z]/.test(password)) score++;
        if (/[0-9]/.test(password)) score++;
        if (/[^A-Za-z0-9]/.test(password)) score++;

        setStrength(score);
        setHasMinLength(password.length >= 8);
        setHasUppercase(/[A-Z]/.test(password));
        setHasNumber(/[0-9]/.test(password));

        setPasswordsMatch(password === repeatPassword && repeatPassword !== "");
    }, [password, repeatPassword]);

    const getStrengthStyles = () => {
        if (!password) return { width: "0%", color: "bg-gray-200", label: "" };
        if (strength < 2) return { width: "20%", color: "bg-red-500", label: t("change_password.strength.weak") };
        if (strength < 4) return { width: "60%", color: "bg-yellow-500", label: t("change_password.strength.medium") };
        return { width: "100%", color: "bg-green-500", label: t("change_password.strength.strong") };
    };

    const strengthStyle = getStrengthStyles();
    const isFormValid = hasMinLength && hasUppercase && hasNumber && passwordsMatch && !apiError;
    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();
        if (!isFormValid || !token || !email) return;

        setIsLoading(true);
        setApiError(null);

        try {
            const loginResult = await login(email, token);

            if (!loginResult.success) {
                throw new Error(t("change_password.error_token_expired") || "Enlace expirado.");
            }

            const accessToken = getAccessToken();
            const claims = parseJwt(accessToken || "");

            if (!claims || !claims.role || !claims.userId) {
                throw new Error("No se pudo verificar la identidad del usuario.");
            }

            const userId = claims.userId;
            const userRole = claims.role;

            const formPayload = {
                password: password,
                repeatPassword: repeatPassword
            };

            const roleUpper = String(userRole).toUpperCase();

            if (roleUpper.includes('DOCTOR')) {
                await changeDoctorPassword(`/doctors/${userId}`, formPayload);
            }
            else if (roleUpper.includes('PATIENT')) {
                await changePatientPassword(`/patients/${userId}/`, formPayload);
            }
            else {
                throw new Error("Rol de usuario desconocido.");
            }

            setIsSubmitted(true);
            logout();

        } catch (error: any) {
            setApiError(error.message || t("change_password.error_generic") || "Error al cambiar la contraseña.");
            logout();
        } finally {
            setIsLoading(false);
        }
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
                            className={`${inputStyle} ${hasMinLength && hasUppercase && hasNumber ? "border-green-500 ring-green-500" : ""}`}
                            required
                            tooltip={t("register.tooltip_password")}
                        />

                        <div className={`transition-all duration-300 ${password ? 'opacity-100 max-h-24' : 'opacity-0 max-h-0 overflow-hidden'}`}>
                            <div className="flex justify-between text-xs mt-3 mb-1">
                                <span className="text-[var(--text-light)] font-medium">Strength</span>
                                <span className="text-[var(--text-color)] font-bold">{strengthStyle.label}</span>
                            </div>
                            <div className={strengthBarContainer}>
                                <div
                                    className={`${strengthBarFill} ${strengthStyle.color}`}
                                    style={{ width: strengthStyle.width }}
                                />
                            </div>
                        </div>


                        <div className="mt-3 space-y-1 pl-1">
                            <RequirementItem isValid={hasMinLength} text={t("change_password.req_length")} />
                            <RequirementItem isValid={hasUppercase} text={t("change_password.req_uppercase")} />
                            <RequirementItem isValid={hasNumber} text={t("change_password.req_number") || "Includes a number"} />
                        </div>
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
                        className="w-full mt-6 bg-[var(--primary-color)] hover:bg-[var(--primary-dark)] h-12 text-base font-semibold shadow-md"
                        size="lg"
                        disabled={!isFormValid}
                    >
                        {t("change_password.submit")}
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

function RequirementItem({ isValid, text }: { isValid: boolean; text: string }) {
    return (
        <p className={`${validationMessage} ${isValid ? "text-green-600" : ""}`}>
            {isValid ? (
                <Check className="h-4 w-4 text-green-600 flex-shrink-0" />
            ) : (
                <div className="h-1.5 w-1.5 rounded-full bg-gray-300 mx-1.5 flex-shrink-0" />
            )}
            {text}
        </p>
    );
}