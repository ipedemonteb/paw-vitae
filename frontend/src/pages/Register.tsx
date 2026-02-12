import { useEffect, useState } from "react"
import { Button } from "@/components/ui/button.tsx"
import { PatientForm } from "../components/PatientForm.tsx"
import { DoctorForm } from "../components/DoctorForm.tsx"
import { useTranslation } from "react-i18next"
import { RegistrationSuccess } from "@/components/RegistrationSuccess.tsx"
import { useSearchParams } from "react-router-dom"

type UserType = "patient" | "doctor"

export default function RegisterPage() {
    const [searchParams, setSearchParams] = useSearchParams()
    const typeParam = searchParams.get("type")

    const [userType, setUserType] = useState<UserType>(
        typeParam === "doctor" ? "doctor" : "patient"
    )

    useEffect(() => {
        if (typeParam === "doctor") setUserType("doctor")
        else if (typeParam === "patient") setUserType("patient")
    }, [typeParam])

    const toggleType = () => {
        const next: UserType = userType === "patient" ? "doctor" : "patient"

        setUserType(next)

        setSearchParams((prev) => {
            const p = new URLSearchParams(prev)
            p.set("type", next)
            return p
        })
    }

    const { t } = useTranslation()
    const [successEmail, setSuccessEmail] = useState<string | null>(null)

    if (successEmail) {
        return <RegistrationSuccess email={successEmail} />
    }

    const registerPage =
        "bg-(--background-light) px-5 flex justify-center items-start min-h-screen";
    const pageContainer =
        "mt-25 py-10 w-full min-h-screen bg-[var(--background-light)] flex flex-col items-center"
    const headerCard =
        "w-full max-w-4xl rounded-t-xl p-8 text-center relative " +
        "bg-[var(--primary-color)] text-white"
    const headerSubtitle = "opacity-90"
    const dropdownBtn =
        "bg-white/15 hover:bg-white/25 text-white border-0 cursor-pointer"
    const bodyCard =
        "w-full max-w-4xl bg-[var(--white)] shadow-[var(--shadow-xl)] rounded-b-xl p-8 " +
        "border-x border-b border-[var(--gray-200)]"

    return (
        <div className={registerPage}>
            <div className={pageContainer}>
                <div className={headerCard}>
                    <h1 className="text-3xl font-bold mb-2">{userType === "patient" ?
                        t("register.title.patient")
                        : t("register.title.doctor")}</h1>
                    <p className={headerSubtitle}>{userType === "patient" ? t("register.subtitle_register") : t('register.subtitle_doctor')}</p>

                    <div className="mt-4 md:mt-0 md:absolute md:top-6 md:right-6">
                        <Button variant="secondary" className={dropdownBtn} onClick={toggleType}>
                            {userType === "patient"
                                ? t("register.register_im_doctor")
                                : t("register.register_im_patient")}
                        </Button>
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
        </div>
    )
}