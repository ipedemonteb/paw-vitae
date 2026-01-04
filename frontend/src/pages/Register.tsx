"use client"

import {Suspense, useState} from "react"
import { ChevronDown, User, Stethoscope } from "lucide-react"
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu.tsx"
import { Button } from "@/components/ui/button.tsx"
import { PatientForm } from "../components/PatientForm.tsx"
import { DoctorForm } from "../components/DoctorForm.tsx"
import {useTranslation} from "react-i18next";
import {RegistrationSuccess} from "@/components/RegistrationSuccess.tsx";
import {useSearchParams} from "react-router-dom";

type UserType = "patient" | "doctor"

export default function RegisterPage() {
    const { t } = useTranslation();
    return (
        <Suspense fallback={<div>{t("loading")}</div>}>
            <RegisterContent />
        </Suspense>
    )
}

function RegisterContent() {
    const [searchParams] = useSearchParams()
    const typeParam = searchParams.get('type')
    const [userType, setUserType] = useState<UserType>(
        typeParam === 'doctor' ? 'doctor' : 'patient'
    );
    const { t } = useTranslation();
    const [successEmail, setSuccessEmail] = useState<string | null>(null)

    if (successEmail) {
        return (
                <RegistrationSuccess email={successEmail} />
        )
    }

    return (
        <div className="mt-25 py-[30px] min-h-screen bg-gray-100 flex flex-col items-center pt-10 pb-10">
            <div className="w-full max-w-4xl bg-blue-600 rounded-t-xl p-8 text-center text-white shadow-lg relative">
                <h1 className="text-3xl font-bold mb-2">{t("register.title_register")}</h1>
                <p className="opacity-90">
                    {t("register.subtitle_register")}
                </p>

                <div className="absolute top-6 right-6">
                    <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                            <Button variant="secondary" className="bg-white/20 hover:bg-white/30 text-white border-0">
                                {userType === "patient" ? t('register.register_im_patient') : t('register.register_im_doctor')}
                                <ChevronDown className="ml-2 h-4 w-4" />
                            </Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent align="end" className="w-48">
                            <DropdownMenuItem onClick={() => setUserType("patient")}>
                                <User className="mr-2 h-4 w-4" />
                                {t("register.register_patient")}
                            </DropdownMenuItem>
                            <DropdownMenuItem onClick={() => setUserType("doctor")}>
                                <Stethoscope className="mr-2 h-4 w-4" />
                                {t("register.register_doctor")}
                            </DropdownMenuItem>
                        </DropdownMenuContent>
                    </DropdownMenu>
                </div>
            </div>


            <div className="w-full max-w-4xl bg-white shadow-xl rounded-b-xl p-8 border-x border-b">
                {userType === "patient" ? (
                    <PatientForm onSuccess={(email) => setSuccessEmail(email)} />
                ) : (
                    <DoctorForm onSuccess={(email) => setSuccessEmail(email)} />
                )}
            </div>
        </div>
    )
}