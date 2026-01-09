import { useState, FormEvent } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { useTranslation } from "react-i18next"
import { KeyRound, ArrowLeft, Mail, AlertCircle, Loader2, CheckCircle2 } from "lucide-react"
import { Link } from "react-router-dom"
import { useUserMutation } from "@/hooks/useUser"


const pageContainer =
    "min-h-screen flex items-center justify-center bg-gray-50/50 py-12 px-4 sm:px-6 lg:px-8";

const cardContainer =
    "w-full max-w-lg space-y-8 bg-white p-10 rounded-xl border border-[var(--gray-300)] shadow-sm";

const headerContainer =
    "flex flex-col items-center text-center";

const iconCircle =
    "h-14 w-14 bg-[var(--primary-color)]/10 text-[var(--primary-color)] rounded-full flex items-center justify-center mb-6";

// Icono de Éxito (Check Verde)
const successIconCircle =
    "h-14 w-14 bg-green-100 text-green-600 rounded-full flex items-center justify-center mb-6";

const titleText =
    "text-3xl font-bold tracking-tight text-[var(--text-color)]";

const subtitleText =
    "mt-3 text-base text-[var(--text-light)] max-w-sm mx-auto";

const formContainer =
    "mt-10 space-y-6";

const inputWrapper =
    "relative";

const inputIcon =
    "absolute left-3 top-3 h-5 w-5 text-gray-400";

const inputField =
    "pl-10 h-11";

const submitButton =
    "w-full flex justify-center py-3 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-[var(--primary-color)] hover:bg-[var(--primary-dark)] focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-[var(--primary-color)] transition-colors disabled:opacity-50 disabled:cursor-not-allowed";

const backLinkContainer =
    "flex items-center justify-center mt-8";

const backLink =
    "flex items-center gap-2 text-sm font-medium text-[var(--text-light)] hover:text-[var(--primary-color)] transition-colors";

const errorMessage =
    "flex items-center gap-2 text-sm text-red-500 mt-2 animate-in fade-in slide-in-from-top-1";


export default function RecoverPassword() {
    const { t } = useTranslation()

    const [email, setEmail] = useState("")
    const [validationError, setValidationError] = useState<string | null>(null)

    const { mutate, isPending, isSuccess, isError } = useUserMutation();

    const handleSubmit = (e: FormEvent) => {
        e.preventDefault()

        setValidationError(null)

        if (!email.trim()) {
            setValidationError(t("recover.error_required"))
            return
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
        if (!emailRegex.test(email)) {
            setValidationError(t("recover.error_invalid"))
            return
        }
        mutate({ email })
    }

    if (isSuccess) {
        return (
            <div className={pageContainer}>
                <div className={cardContainer}>
                    <div className={headerContainer}>
                        <div className={successIconCircle}>
                            <CheckCircle2 className="h-7 w-7" />
                        </div>
                        <h1 className={titleText}>
                            {t("recover.success_title") || "Check your email"}
                        </h1>
                        <p className={subtitleText}>
                            {t("recover.success_message", { email }) || `We have sent a confirmation link to ${email}.`}
                        </p>
                    </div>
                    <div className={backLinkContainer}>
                        <Link to="/login" className={backLink}>
                            <ArrowLeft className="h-4 w-4" />
                            {t("recover.back_to_login")}
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
                        <KeyRound className="h-7 w-7" />
                    </div>
                    <h1 className={titleText}>
                        {t("recover.title")}
                    </h1>
                    <p className={subtitleText}>
                        {t("recover.subtitle")}
                    </p>
                </div>

                <form className={formContainer} onSubmit={handleSubmit} noValidate>
                    <div className="space-y-2">
                        <div className={inputWrapper}>
                            <Mail className={`absolute left-3 top-3 h-5 w-5 ${validationError || isError ? "text-red-500" : "text-gray-400"}`} />
                            <Input
                                type="email"
                                className={`${inputField} ${validationError || isError ? "border-red-500 focus-visible:ring-red-500" : ""}`}
                                placeholder={t("recover.placeholder_email")}
                                aria-label={t("login.aria_email")}
                                value={email}
                                onChange={(e) => {
                                    setEmail(e.target.value)
                                    if (validationError) setValidationError(null)
                                }}
                                disabled={isPending}
                                autoFocus
                            />
                        </div>

                        {validationError && (
                            <p className={errorMessage}>
                                <AlertCircle className="h-4 w-4" />
                                {validationError}
                            </p>
                        )}

                        {isError && !validationError && (
                            <p className={errorMessage}>
                                <AlertCircle className="h-4 w-4" />
                                {t("recover.error_generic") || "An error occurred. Please try again."}
                            </p>
                        )}
                    </div>

                    <Button type="submit" className={submitButton} disabled={isPending}>
                        {isPending ? (
                            <>
                                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                                {t("loading") || "Sending..."}
                            </>
                        ) : (
                            t("recover.button_send")
                        )}
                    </Button>
                </form>

                <div className={backLinkContainer}>
                    <Link to="/login" className={backLink}>
                        <ArrowLeft className="h-4 w-4" />
                        {t("recover.back_to_login")}
                    </Link>
                </div>
            </div>
        </div>
    )
}