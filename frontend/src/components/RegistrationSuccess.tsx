"use client"

import {ArrowLeft, CheckCircle2} from "lucide-react"
import { useTranslation } from "react-i18next"
import {Link} from "react-router-dom";

interface RegistrationSuccessProps {
    email: string;
}

const pageContainer =
    "min-h-screen bg-(--background-light) flex flex-col items-center pt-36 pb-6 px-4 sm:px-6 lg:px-8";
const cardContainer =
    "w-full max-w-lg bg-white py-10 px-16 rounded-xl border border-[var(--gray-300)] shadow-sm";
const headerContainer =
    "flex flex-col items-center text-center";
const successIconCircle =
    "h-16 w-16 bg-(--success-light) text-(--success) rounded-full flex items-center justify-center mb-6";
const titleText =
    "text-3xl font-bold tracking-tight text-[var(--text-color)]";
const subtitleText =
    "mt-3 text-base text-(--text-light) max-w-sm mx-auto";
const backLinkContainer =
    "flex items-center justify-center mt-6";
const backLink =
    "flex items-center gap-2 text-sm font-medium text-[var(--text-light)] hover:text-[var(--primary-color)] transition-colors";

export function RegistrationSuccess({ email }: RegistrationSuccessProps) {
    const { t } = useTranslation();

    return (
        <div className={pageContainer}>
            <div className={cardContainer}>
                <div className={headerContainer}>
                    <div className={successIconCircle}>
                        <CheckCircle2 className="h-8 w-8 text-(--success)" />
                    </div>

                    <h1 className={titleText}>
                        {t('register.success_title')}
                    </h1>

                    <p className={subtitleText}>
                        {t('register.success_message')} <br/>
                        <strong className="text-(--text-color) font-medium">{email}</strong>.
                    </p>

                    <div className="w-full bg-(--primary-bg) text-(--primary-color) rounded-lg p-4 text-sm border border-(--primary-color) mt-4">
                        {t('register.success_check_email')}
                    </div>

                    <div className={backLinkContainer}>
                        <Link to="/login" className={backLink}>
                            <ArrowLeft className="h-4 w-4" />
                            {t("change_password.back_to_login")}
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    )
}