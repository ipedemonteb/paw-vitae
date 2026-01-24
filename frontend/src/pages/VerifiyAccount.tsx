import { useEffect, useState, useRef } from "react"
import { useTranslation } from "react-i18next"
import { Link, useSearchParams } from "react-router-dom"
import { CheckCircle2, XCircle, Loader2, Home, ArrowLeft } from "lucide-react"
import { Button } from "@/components/ui/button"
import { useAuth } from "@/hooks/useAuth"

const pageContainer =
    "min-h-screen bg-gray-50/50 flex flex-col items-center pt-32 pb-12 px-4 sm:px-6 lg:px-8";

const cardContainer =
    "w-full max-w-lg space-y-8 bg-white p-8 sm:p-12 rounded-xl border border-gray-200 shadow-xl text-center mt-30";

const headerContainer =
    "flex flex-col items-center text-center space-y-4";

const loadingIconCircle =
    "h-20 w-20 bg-blue-50 text-blue-600 rounded-full flex items-center justify-center mb-4 animate-pulse";

const successIconCircle =
    "h-20 w-20 bg-green-100 text-green-600 rounded-full flex items-center justify-center mb-4";

const errorIconCircle =
    "h-20 w-20 bg-red-100 text-red-600 rounded-full flex items-center justify-center mb-4";

const titleText =
    "text-3xl font-bold tracking-tight text-[var(--text-color)]";

const subtitleText =
    "mt-2 text-base text-[var(--text-light)] max-w-sm mx-auto";

const actionContainer =
    "mt-8 flex flex-col gap-3";


export default function VerifyAccount() {
    const { t } = useTranslation()
    const [searchParams] = useSearchParams()
    const { login } = useAuth()

    const hasAttemptedVerify = useRef(false)

    const [status, setStatus] = useState<'loading' | 'success' | 'error'>('loading')

    useEffect(() => {
        if (hasAttemptedVerify.current) return;

        const token = searchParams.get("token")
        const email = searchParams.get("email")

        if (!token || !email) {
            setStatus('error')
            return
        }

        hasAttemptedVerify.current = true;

        login.mutate({email, password: token,rememberMe:false}, {
            onSuccess: () => {
                setStatus('success')
            },
            onError: () => {
                setStatus('error')
            }
        })

    }, [])


    if (status === 'loading') {
        return (
            <div className={pageContainer}>
                <div className={cardContainer}>
                    <div className={headerContainer}>
                        <div className={loadingIconCircle}>
                            <Loader2 className="h-10 w-10 animate-spin" />
                        </div>
                        <h1 className={titleText}>{t("verify.loading_title")}</h1>
                        <p className={subtitleText}>{t("verify.loading_message")}</p>
                    </div>
                </div>
            </div>
        )
    }

    if (status === 'success') {
        return (
            <div className={pageContainer}>
                <div className={cardContainer}>
                    <div className={headerContainer}>
                        <div className={successIconCircle}>
                            <CheckCircle2 className="h-10 w-10" />
                        </div>
                        <h1 className={titleText}>{t("verify.success_title")}</h1>
                        <p className={subtitleText}>{t("verify.success_message")}</p>
                    </div>

                    <div className={actionContainer}>
                        <Link to="/">
                            <Button className="w-full h-12 text-base bg-[var(--primary-color)] hover:bg-[var(--primary-dark)]">
                                <Home className="mr-2 h-5 w-5" />
                                {t("verify.go_home")}
                            </Button>
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
                    <div className={errorIconCircle}>
                        <XCircle className="h-10 w-10" />
                    </div>
                    <h1 className={titleText}>{t("verify.error_title")}</h1>
                    <p className={subtitleText}>{t("verify.error_message")}</p>
                </div>

                <div className={actionContainer}>
                    <Link to="/login">
                        <Button variant="outline" className="w-full h-12 text-base border-gray-300">
                            <ArrowLeft className="mr-2 h-5 w-5" />
                            {t("verify.back_to_login")}
                        </Button>
                    </Link>
                    <Link to="/">
                        <Button variant="ghost" className="w-full text-[var(--text-light)]">
                            {t("verify.go_home")}
                        </Button>
                    </Link>
                </div>
            </div>
        </div>
    )
}