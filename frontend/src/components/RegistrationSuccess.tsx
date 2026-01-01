"use client"

import { CheckCircle } from "lucide-react"
import { useTranslation } from "react-i18next"

interface RegistrationSuccessProps {
    email: string;
}

export function RegistrationSuccess({ email }: RegistrationSuccessProps) {
    const { t } = useTranslation();

    return (
        <div className="w-full h-[calc(100vh-100px)] flex items-center justify-center overflow-hidden p-4">

            <div className="w-full max-w-lg shadow-2xl rounded-xl animate-in fade-in zoom-in-95 duration-500 flex flex-col">

                <div className="bg-blue-600 rounded-t-xl p-8 text-center text-white">
                </div>

                <div className="bg-white rounded-b-xl p-8 flex flex-col items-center text-center">

                    <div className="mb-6 bg-green-100 p-3 rounded-full">
                        <CheckCircle className="h-12 w-12 text-green-600" />
                    </div>

                    <h2 className="text-2xl font-bold text-gray-800 mb-2">
                        {t('register.success_title')}
                    </h2>

                    <p className="text-gray-600 mb-6 text-sm">
                        {t('register.success_message')} <br/>
                        <strong className="text-gray-900 font-medium">{email}</strong>.
                    </p>

                    <div className="w-full bg-blue-50 text-blue-800 rounded-lg p-4 text-sm border border-blue-100 mb-6">
                        {t('register.success_check_email')}
                    </div>

                    <button
                        onClick={() => window.location.href = '/login'}
                        className="text-blue-600 font-semibold hover:text-blue-800 hover:underline transition-colors"
                    >
                        {t('register.btn_go_login')}
                    </button>
                </div>
            </div>
        </div>
    )
}