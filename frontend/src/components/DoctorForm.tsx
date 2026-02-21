import React, {useEffect, useState} from "react"
import {
    ArrowRight,
    ArrowLeft,
    Check,
    Calendar,
    AlertCircle
} from "lucide-react"
import { useTranslation } from "react-i18next"

import { useSpecialties } from "@/hooks/useSpecialties"
import { useCoverages } from "@/hooks/useCoverages"

import { FormInput } from "@/components/FormInput.tsx"
import { PasswordInput } from "@/components/PasswordInput.tsx"
import { FormStepper } from "@/components/FormStepper.tsx"
import {PasswordStrengthMeter} from "@/components/PasswordStrengthMeter.tsx";
import {useRegisterDoctorMutation} from "@/hooks/useDoctors.ts";
import type {AxiosError} from "axios";
import {Button} from "@/components/ui/button.tsx";
import {Spinner} from "@/components/ui/spinner.tsx";
import {RefetchComponent} from "@/components/ui/refetch.tsx";

interface DoctorFormProps {
    onSuccess: (email: string) => void;
}

const stepStyling = "w-full px-5 md:px-0 space-y-4";

export function DoctorForm({ onSuccess }: DoctorFormProps) {
    const { t } = useTranslation()

    const [step, setStep] = useState(1)

    const {mutate: registerDoctor, isPending: isSubmitting} = useRegisterDoctorMutation()

    const {data: specialties, isLoading: isLoadingSpecialties, isError: errorSpecialties, refetch: refetchSpecialties, isRefetching: refetchingSpecialties} = useSpecialties()
    const {data: coverages, isLoading: isLoadingCoverages, isError: errorCoverages, refetch: refetchCoverages, isRefetching: refetchingCoverages} = useCoverages()

    const [formData, setFormData] = useState({
        name: "",
        lastName: "",
        email: "",
        phone: "",
        password: "",
        repeatPassword: "",
        image: null as File | null,
        selectedSpecialties: [] as string[],
        selectedCoverages: [] as string[],
        agreeTerms: false
    })


    const [globalError, setGlobalError] = useState("");

    const [errors, setErrors] = useState<{[key : string] : string}>({})

    const [isPasswordValid, setIsPasswordValid] = useState(false)

    useEffect(() => {
        const pwd = formData.password;
        const hasMinLength = pwd.length >= 8;
        const hasUppercase = /[A-Z]/.test(pwd);
        const hasNumber = /[0-9]/.test(pwd);

        setIsPasswordValid(hasMinLength && hasUppercase && hasNumber);
    }, [formData.password]);

    const handleNextStep = () => {
        const newErrors = {} as {[key : string] : string}

        if (!formData.name) newErrors.name = t('register.errors.required');
        if (!formData.lastName) newErrors.lastName = t('register.errors.required');
        if (!formData.email) newErrors.email = t('register.errors.required');
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
        if (!emailRegex.test(formData.email)) newErrors.email = t("register.errors.invalid-email");
        if(!formData.phone) newErrors.phone = t('register.errors.required');

        if (formData.password !== formData.repeatPassword) {
            newErrors.repeatPassword = t("register.errors.passwords_mismatch");
        }

        if (!isPasswordValid) {
            newErrors.password = t("register.errors.password_requirements");
        }

        if(Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
            return;
        }

        setErrors({});
        setStep(2);
    }

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target
        setFormData(prev => ({ ...prev, [name]: value }))
        if(errors[name]) setErrors(prev => ({ ...prev, [name]: "" }))
    }

    const toggleSelection = (listName: 'selectedSpecialties' | 'selectedCoverages', url: string) => {
        setFormData(prev => {
            const list = prev[listName]
            if (list.includes(url)) {
                return { ...prev, [listName]: list.filter(item => item !== url) }
            } else {
                return { ...prev, [listName]: [...list, url] }
            }
        })
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()

        const newErrors = {} as {[key : string] : string}

        if (formData.selectedSpecialties.length === 0) {
            newErrors.selectedSpecialties = t('register.errors.select_specialty') || "Selecciona al menos una especialidad";
        }

        if (formData.selectedCoverages.length === 0) {
            newErrors.selectedCoverages = t('register.errors.select_coverage') || "Selecciona al menos una obra social";
        }

        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
            return;
        }

        setErrors({});


        registerDoctor(formData, {
            onSuccess: () => onSuccess(formData.email),
            onError: (error: AxiosError<any>) => {
                let isEmailError = false;
                let errorMessage = t('register.errors.generic');

                if (error.response && error.response.data) {

                    const backendMsg = error.response.data.message;

                    if (backendMsg && backendMsg.toLowerCase().includes("email")) {
                        isEmailError = true;
                    } else if (backendMsg) {
                        errorMessage = backendMsg;
                    }

                    if (error.response.data.errors) {
                        const apiErrors = error.response.data.errors;
                        const foundEmailError = apiErrors.find((e: any) =>
                            e.field === "email" || e.message?.toLowerCase().includes("email")
                        );
                        if (foundEmailError) isEmailError = true;
                    }
                }

                if (isEmailError) {
                    setStep(1);
                    setErrors(prev => ({ ...prev, email: t('register.errors.email_taken') }));
                } else {
                    setGlobalError(errorMessage);
                }
            }
        })

    }

    return (
        <form onSubmit={handleSubmit} className="w-animate-in fade-in slide-in-from-bottom-4 duration-500 flex flex-col items-center">

            <FormStepper
                currentStep={step}
                steps={[
                    { id: 1, label: t('register.step_personal') },
                    { id: 2, label: t('register.step_professional') }]}
                className="max-w-lg"
            />

            {step === 1 && (
                <div className={stepStyling}>
                    <div className="flex items-center gap-2 mb-4">
                        <h3 className="text-lg font-bold border-b-2 border-(--primary-color) text-(--primary-color)">{t('register.section_personal')}</h3>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <FormInput
                            id="name"
                            name="name"
                            label={t('register.label_name')}
                            value={formData.name}
                            onChange={handleInputChange}
                            placeholder={t('register.placeholder_name')}
                            required
                            error={errors.name}
                        />

                        <FormInput
                            id="lastName"
                            name="lastName"
                            label={t('register.label_lastname')}
                            value={formData.lastName}
                            onChange={handleInputChange}
                            placeholder={t('register.placeholder_lastname')}
                            required
                            error={errors.lastName}

                        />

                        <FormInput
                            id="email"
                            type="email"
                            name="email"
                            label={t('register.label_email')}
                            value={formData.email}
                            onChange={handleInputChange}
                            placeholder={t('register.placeholder_email')}
                            required
                            error={errors.email}

                        />

                        <FormInput
                            id="phone"
                            name="phone"
                            label={t('register.label_phone')}
                            value={formData.phone}
                            onChange={handleInputChange}
                            placeholder={t('register.placeholder_phone')}
                            required
                            error={errors.phone}
                        />

                        <div className="space-y-1">
                            <PasswordInput
                                id="password"
                                name="password"
                                label={t('register.label_password')}
                                value={formData.password}
                                onChange={handleInputChange}
                                required
                                error={errors.password}
                            />

                            <PasswordStrengthMeter password={formData.password} />
                        </div>

                        <PasswordInput
                            id="repeatPassword"
                            name="repeatPassword"
                            label={t('register.label_repeat_password')}
                            value={formData.repeatPassword}
                            onChange={handleInputChange}
                            required
                            error={errors.repeatPassword}
                        />
                    </div>


                    <div className="flex justify-center mt-4 pt-6">
                        <Button
                            type="button"
                            onClick={handleNextStep}
                            className="w-sm bg-(--primary-color) text-white text-base rounded-md hover:bg-b(--primary-dark) font-semibold flex items-center gap-2 shadow-sm transition-all hover:bg-(--primary-dark) cursor-pointer"
                        >
                            {t('register.btn_next')} <ArrowRight className="h-4 w-4" />
                        </Button>
                    </div>

                    <p className="text-center mt-4 text-sm text-(--gray-600)">
                        {t('register.have_account')} <a href="/login" className="text-(--primary-color) font-medium hover:underline hover:text-(--primary-dark)">{t('register.login_link')}</a>
                    </p>
                </div>
            )}

            {step === 2 && (
                <div className={stepStyling}>
                    <div className="flex items-center gap-2 mb-4">
                        <h3 className="text-lg font-bold border-(--primary-color) border-b-2 text-(--primary-color)">{t('register.section_coverage')}</h3>
                    </div>

                    <div className="space-y-3">
                        <label className="text-sm font-medium text-(--text-color)">
                            {t('register.label_specialties')} <span className="text-(--danger)">*</span>
                        </label>

                        {isLoadingSpecialties ? (
                            <div className="flex flex-col items-center gap-1 text-(--gray-500) text-md my-10">
                                <Spinner className="size-6"/>
                                {t('register.loader_specialties')}
                            </div>
                        ) : errorSpecialties ? (
                            <RefetchComponent
                                isFetching={refetchingSpecialties}
                                onRefetch={() => refetchSpecialties()}
                                className="flex justify-center h-32"
                                errorText={t("register.errors.no_specialties_available")}
                            />
                        ) : (specialties && specialties.length > 0) ? (
                            <div className="mt-1 grid grid-cols-2 md:grid-cols-3 gap-3">
                                {specialties.map((spec) => (
                                    <div
                                        key={spec.name}
                                        onClick={() => {
                                            toggleSelection('selectedSpecialties', spec.self)

                                            if (errors.selectedSpecialties) setErrors(prev => ({...prev, selectedSpecialties: ""}))
                                        }}
                                        className={`p-3 rounded-md border cursor-pointer flex items-center gap-2 transition-all relative overflow-hidden select-none
                                        ${formData.selectedSpecialties.includes(spec.self)
                                            ? 'bg-(--primary-bg) border-(--primary-color) text-(--primary-color) shadow-sm'
                                            : 'bg-white border-(--gray-200) hover:border-(--gray-300) hover:bg-(--gray-50) text-(--gray-600)'}`}
                                    >
                                        <div className={`w-5 h-5 rounded border shrink-0 flex items-center justify-center transition-colors
                                        ${formData.selectedSpecialties.includes(spec.self) ? 'bg-(--primary-color) border-(--primary-color)' : 'border-(--gray-300) bg-white'}`}>
                                            {formData.selectedSpecialties.includes(spec.self) && <Check className="h-3.5 w-3.5 text-white" />}
                                        </div>
                                        <span className="text-sm font-medium">{t(spec.name)}</span>
                                    </div>
                                ))}
                            </div>
                        ) : (
                            <div className="p-4 border border-(--danger-light) bg-(--danger-lightest) rounded-md text-(--danger) text-sm flex items-center gap-2">
                                <span className="font-medium">{t('register.errors.no_specialties_available')}</span>
                            </div>
                        )}

                        {errors.selectedSpecialties && (
                            <p className="text-sm text-(--danger) font-medium animate-in fade-in slide-in-from-top-1">
                                {errors.selectedSpecialties}
                            </p>
                        )}
                    </div>

                    <div className="space-y-3">
                        <label className="text-sm font-medium text-(--text-color)">
                            {t('register.label_coverages')} <span className="text-(--danger)">*</span>
                        </label>

                        {isLoadingCoverages ? (
                            <div className="flex flex-col items-center gap-1 text-(--gray-500) text-md my-10">
                                <Spinner className="size-6"/>
                                {t('register.loader_coverages')}
                            </div>
                        ) : errorCoverages ? (
                            <RefetchComponent
                                isFetching={refetchingCoverages}
                                onRefetch={() => refetchCoverages()}
                                className="flex justify-center h-32"
                                errorText={t("register.errors.no_coverages_available")}
                            />
                        ) : (coverages && coverages.length > 0) ? (
                            <div className="mt-1 grid grid-cols-2 md:grid-cols-3 gap-3">
                                {coverages.map((cov) => (
                                    <div
                                        key={cov.name}
                                        onClick={() => {
                                            toggleSelection('selectedCoverages', cov.self)
                                            if (errors.selectedCoverages) setErrors(prev => ({...prev, selectedCoverages: ""}))
                                        }}
                                        className={`p-3 rounded-md border cursor-pointer flex items-center gap-2 transition-all relative overflow-hidden select-none
                                        ${formData.selectedCoverages.includes(cov.self)
                                            ? 'bg-(--primary-bg) border-(--primary-color) text-(--primary-color) shadow-sm'
                                            : 'bg-white border-(--gray-200) hover:border-(--gray-300) hover:bg-(--gray-50) text-(--gray-600)'}`}
                                    >
                                        <div className={`w-5 h-5 rounded border shrink-0 flex items-center justify-center transition-colors
                                        ${formData.selectedCoverages.includes(cov.self) ? 'bg-(--primary-color) border-(--primary-color)' : 'border-(--gray-300) bg-white'}`}>
                                            {formData.selectedCoverages.includes(cov.self) && <Check className="h-3.5 w-3.5 text-white" />}
                                        </div>
                                        <span className="text-sm font-medium">{cov.name}</span>
                                    </div>
                                ))}
                            </div>
                        ) : (
                            <div className="p-4 border border-(--danger-light) bg-(--danger-lightest) rounded-md text-(--danger) text-sm flex items-center gap-2">
                                <span className="font-medium">{t('register.errors.no_coverages_available')}</span>
                            </div>
                        )}

                        {errors.selectedCoverages && (
                            <p className="text-sm text-(--danger) font-medium animate-in fade-in slide-in-from-top-1">
                                {errors.selectedCoverages}
                            </p>
                        )}
                    </div>

                    <div className="bg-(--warning-lighter) border border-(--warning) rounded-md p-4 text-sm text-(--warning-dark) flex gap-2 items-start mt-4">
                        <Calendar className="h-5 w-5 shrink-0" />
                        <p>{t('register.note_availability')}</p>
                    </div>

                    <div className="pt-4 border-t flex flex-col gap-4">
                        {globalError && (
                            <div className="bg-(--danger-lightest) border border-(--danger-light) text-(--danger) px-4 py-3 rounded-md text-sm flex items-center gap-2 animate-in fade-in slide-in-from-top-1">
                                <AlertCircle className="h-4 w-4 shrink-0" />
                                <span>{globalError}</span>
                            </div>
                        )}
                        <div className="flex items-center gap-2">
                            <input
                                type="checkbox" id="terms"
                                checked={formData.agreeTerms}
                                onChange={(e) => setFormData(prev => ({...prev, agreeTerms: e.target.checked}))}
                                className="h-4 w-4 text-(--primary-color) rounded border-(--gray-300) focus:ring-(--primary-color) cursor-pointer"
                            />
                            <label htmlFor="terms" className="text-sm text-(--gray-600) cursor-pointer select-none">
                                {t('register.terms_agree')} <a href="#" className="text-(--primary-color) hover:underline hover:text-(--primary-dark)">{t('register.terms_link')}</a>
                            </label>
                        </div>

                        <div className="flex items-center justify-between mt-4 mb-1 gap-4 w-full min-w-0">
                            <Button
                                type="button"
                                onClick={() => setStep(1)}
                                className="shrink-0 flex items-center bg-transparent border border-(--gray-400) gap-2 text-(--gray-500) hover:bg-(--gray-200) px-4 py-2 font-medium transition-colors cursor-pointer"
                            >
                                <ArrowLeft className="h-4 w-4" /> {t("register.btn_prev")}
                            </Button>

                            <Button
                                type="submit"
                                disabled={!formData.agreeTerms || isSubmitting}
                                className={`w-3xs max-w-full min-w-0 shrink font-semibold rounded-md shadow-md transition-all flex items-center justify-center gap-2 bg-(--primary-color)
                                  ${(!formData.agreeTerms || isSubmitting)
                                    ? "cursor-not-allowed text-white opacity-70"
                                    : "hover:bg-(--primary-dark) text-white cursor-pointer"
                                }`}
                            >
                                {isSubmitting ? (
                                    <>
                                        <Spinner className="h-4 w-4 shrink-0" />
                                        <span className="truncate">{t("register.btn_registering")}</span>
                                    </>
                                ) : (
                                    <>
                                        <span className="truncate">{t("register.btn_finish")}</span>
                                        <Check className="h-4 w-4 shrink-0" />
                                    </>
                                )}
                            </Button>
                        </div>
                    </div>
                </div>
            )}
        </form>
    )
}