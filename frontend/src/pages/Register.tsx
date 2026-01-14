"use client"

import { Suspense, useEffect, useState } from "react"
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
import { useTranslation } from "react-i18next"
import { RegistrationSuccess } from "@/components/RegistrationSuccess.tsx"
import { useSearchParams } from "react-router-dom"

type UserType = "patient" | "doctor"

export default function RegisterPage() {
    const { t } = useTranslation()
    return (
        <Suspense fallback={<div>{t("loading")}</div>}>
            <RegisterContent />
        </Suspense>
    )
}

function RegisterContent() {
    const [searchParams] = useSearchParams()
    const typeParam = searchParams.get("type")
    const [userType, setUserType] = useState<UserType>(
        typeParam === "doctor" ? "doctor" : "patient"
    )

    useEffect(() => {
        if (typeParam === "doctor") setUserType("doctor")
        else if (typeParam === "patient") setUserType("patient")
    }, [typeParam])

    const { t } = useTranslation()
    const [successEmail, setSuccessEmail] = useState<string | null>(null)

    if (successEmail) {
        return <RegistrationSuccess email={successEmail} />
    }

    const pageContainer =
        "mt-25 py-[30px] min-h-screen bg-[var(--background-light)] flex flex-col items-center pt-10 pb-10"

    const headerCard =
        "w-full max-w-4xl rounded-t-xl p-8 text-center shadow-[var(--shadow-lg)] relative " +
        "bg-[var(--primary-color)] text-white"

    const headerSubtitle = "opacity-90"

    const dropdownBtn =
        "bg-white/15 hover:bg-white/25 text-white border-0 cursor-pointer"

    const dropdownContent =
        "w-48 bg-[var(--white)] border border-[var(--gray-200)] shadow-[var(--shadow-md)]"

    const dropdownItem =
        "cursor-pointer text-[var(--text-color)] focus:bg-[var(--primary-bg)] focus:text-[var(--text-color)]"

    const bodyCard =
        "w-full max-w-4xl bg-[var(--white)] shadow-[var(--shadow-xl)] rounded-b-xl p-8 " +
        "border-x border-b border-[var(--gray-200)]"

    return (
        <div className={pageContainer}>
            <div className={headerCard}>
                <h1 className="text-3xl font-bold mb-2">{t("register.title_register")}</h1>
                <p className={headerSubtitle}>{t("register.subtitle_register")}</p>

                <div className="absolute top-6 right-6">
                    <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                            <Button variant="secondary" className={dropdownBtn}>
                                {userType === "patient"
                                    ? t("register.register_im_patient")
                                    : t("register.register_im_doctor")}
                                <ChevronDown className="ml-2 h-4 w-4" />
                            </Button>
                        </DropdownMenuTrigger>

                        <DropdownMenuContent align="end" className={dropdownContent}>
                            <DropdownMenuItem className={dropdownItem} onClick={() => setUserType("patient")}>
                                <User className="mr-2 h-4 w-4 text-[var(--primary-color)]" />
                                {t("register.register_patient")}
                            </DropdownMenuItem>
                            <DropdownMenuItem className={dropdownItem} onClick={() => setUserType("doctor")}>
                                <Stethoscope className="mr-2 h-4 w-4 text-[var(--primary-color)]" />
                                {t("register.register_doctor")}
                            </DropdownMenuItem>
                        </DropdownMenuContent>
                    </DropdownMenu>
                </div>
            </div>

            <div className={bodyCard}>
                {userType === "patient" ? (
                    <PatientForm onSuccess={(email) => setSuccessEmail(email)} />
                ) : (
                    <DoctorForm onSuccess={(email) => setSuccessEmail(email)} />
                )}
            </div>
        </div>
    )
}