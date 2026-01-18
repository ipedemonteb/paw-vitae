"use client"

import React, {useEffect, useState} from "react"
import {
    ArrowRight,
    ArrowLeft,
    Check,
    Calendar,
    Loader2, AlertCircle
} from "lucide-react"
import { useTranslation } from "react-i18next"

import { useSpecialties } from "@/hooks/useSpecialties"
import { useCoverages } from "@/hooks/useCoverages"

import { FormInput } from "@/components/ui/FormInput"
import { PasswordInput } from "@/components/ui/passwordInput"
import { FormStepper } from "@/components/ui/FormStepper"
import {PasswordStrengthMeter} from "@/components/ui/PasswordStrengthMeter.tsx";
import {useRegisterDoctorMutation} from "@/hooks/useDoctors.ts";
import type {AxiosError} from "axios";

interface DoctorFormProps {
    onSuccess: (email: string) => void;
}

export function DoctorForm({ onSuccess }: DoctorFormProps) {
    const { t } = useTranslation()

    const [step, setStep] = useState(1)

    const {mutate: registerDoctor, isPending: isSubmitting} = useRegisterDoctorMutation()

    const {data: specialties, isLoading: isLoadingSpecialties} = useSpecialties()
    const {data: coverages, isLoading: isLoadingCoverages} = useCoverages()

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
        <form onSubmit={handleSubmit} className="animate-in fade-in slide-in-from-bottom-4 duration-500">

            <div className="text-center mb-8">
                <h2 className="text-2xl font-bold text-blue-600">{t('register.title_doctor')}</h2>
                <p className="text-gray-600 text-sm mt-1">{t('register.subtitle_doctor')}</p>
            </div>

            <FormStepper
                currentStep={step}
                steps={[
                    { id: 1, label: t('register.step_personal') },
                    { id: 2, label: t('register.step_professional') }]}
            />

            {step === 1 && (
                <div className="space-y-6">
                    <div className="flex items-center gap-2 mb-4">
                        <h3 className="text-lg font-bold border-b-2 border-blue-600 text-blue-600">{t('register.section_personal')}</h3>
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


                    <div className="flex justify-end pt-6">
                        <button
                            type="button"
                            onClick={handleNextStep}
                            className="bg-blue-600 text-white px-6 py-2.5 rounded-md hover:bg-blue-700 font-medium flex items-center gap-2 shadow-sm transition-all"
                        >
                            {t('register.btn_next')} <ArrowRight className="h-4 w-4" />
                        </button>
                    </div>
                </div>
            )}

            {step === 2 && (
                <div className="space-y-8">
                    <div className="flex items-center gap-2 mb-4">
                        <h3 className="text-lg font-bold border-blue-600 border-b-2 text-blue-600">{t('register.section_coverage')}</h3>
                    </div>

                    <div className="space-y-3">
                        <label className="text-sm font-medium text-gray-700">
                            {t('register.label_specialties')} <span className="text-red-500">*</span>
                        </label>

                        {isLoadingSpecialties ? (
                            <div className="flex items-center gap-2 text-gray-500 text-sm">
                                <Loader2 className="h-4 w-4 animate-spin"/> {t('register.loader_specialties')}
                            </div>
                        ) : (specialties && specialties.length > 0) ? (
                            <div className="grid grid-cols-2 md:grid-cols-3 gap-3">
                                {specialties.map((spec) => (
                                    <div
                                        key={spec.name}
                                        onClick={() => {
                                            toggleSelection('selectedSpecialties', spec.self)

                                            if (errors.selectedSpecialties) setErrors(prev => ({...prev, selectedSpecialties: ""}))
                                        }}
                                        className={`p-3 rounded-md border cursor-pointer flex items-center gap-2 transition-all relative overflow-hidden select-none
                                        ${formData.selectedSpecialties.includes(spec.self)
                                            ? 'bg-blue-50 border-blue-500 text-blue-700 shadow-sm'
                                            : 'bg-white border-gray-200 hover:border-gray-300 hover:bg-gray-50 text-gray-600'}`}
                                    >
                                        <div className={`w-5 h-5 rounded border flex-shrink-0 flex items-center justify-center transition-colors
                                        ${formData.selectedSpecialties.includes(spec.self) ? 'bg-blue-600 border-blue-600' : 'border-gray-300 bg-white'}`}>
                                            {formData.selectedSpecialties.includes(spec.self) && <Check className="h-3.5 w-3.5 text-white" />}
                                        </div>
                                        <span className="text-sm font-medium">{spec.name}</span>
                                    </div>
                                ))}
                            </div>
                        ) : (
                            <div className="p-4 border border-red-200 bg-red-50 rounded-md text-red-600 text-sm flex items-center gap-2">
                                <span className="font-medium">⚠️ {t('register.errors.no_specialties_available')}</span>
                            </div>
                        )}

                        {errors.selectedSpecialties && (
                            <p className="text-sm text-red-500 font-medium animate-in fade-in slide-in-from-top-1">
                                {errors.selectedSpecialties}
                            </p>
                        )}
                    </div>

                    <div className="space-y-3">
                        <label className="text-sm font-medium text-gray-700">
                            {t('register.label_coverages')} <span className="text-red-500">*</span>
                        </label>

                        {isLoadingCoverages ? (
                            <div className="flex items-center gap-2 text-gray-500 text-sm">
                                <Loader2 className="h-4 w-4 animate-spin"/> {t('register.loader_coverages')}
                            </div>
                        ) : (coverages && coverages.length > 0) ? (
                            <div className="grid grid-cols-2 md:grid-cols-3 gap-3">
                                {coverages.map((cov) => (
                                    <div
                                        key={cov.name}
                                        onClick={() => {
                                            toggleSelection('selectedCoverages', cov.self)
                                            if (errors.selectedCoverages) setErrors(prev => ({...prev, selectedCoverages: ""}))
                                        }}
                                        className={`p-3 rounded-md border cursor-pointer flex items-center gap-2 transition-all relative overflow-hidden select-none
                                        ${formData.selectedCoverages.includes(cov.self)
                                            ? 'bg-blue-50 border-blue-500 text-blue-700 shadow-sm'
                                            : 'bg-white border-gray-200 hover:border-gray-300 hover:bg-gray-50 text-gray-600'}`}
                                    >
                                        <div className={`w-5 h-5 rounded border flex-shrink-0 flex items-center justify-center transition-colors
                                        ${formData.selectedCoverages.includes(cov.self) ? 'bg-blue-600 border-blue-600' : 'border-gray-300 bg-white'}`}>
                                            {formData.selectedCoverages.includes(cov.self) && <Check className="h-3.5 w-3.5 text-white" />}
                                        </div>
                                        <span className="text-sm font-medium">{cov.name}</span>
                                    </div>
                                ))}
                            </div>
                        ) : (
                            <div className="p-4 border border-red-200 bg-red-50 rounded-md text-red-600 text-sm flex items-center gap-2">
                                <span className="font-medium">⚠️ {t('register.errors.no_coverages_available')}</span>
                            </div>
                        )}

                        {errors.selectedCoverages && (
                            <p className="text-sm text-red-500 font-medium animate-in fade-in slide-in-from-top-1">
                                {errors.selectedCoverages}
                            </p>
                        )}
                    </div>

                    <div className="bg-amber-50 border border-amber-200 rounded-md p-4 text-sm text-amber-800 flex gap-2 items-start mt-4">
                        <Calendar className="h-5 w-5 shrink-0" />
                        <p>{t('register.note_availability')}</p>
                    </div>

                    <div className="pt-6 border-t flex flex-col gap-4">
                        {globalError && (
                            <div className="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-md text-sm flex items-center gap-2 animate-in fade-in slide-in-from-top-1">
                                <AlertCircle className="h-4 w-4 shrink-0" />
                                <span>{globalError}</span>
                            </div>
                        )}
                        <div className="flex items-center gap-2">
                            <input
                                type="checkbox" id="terms"
                                checked={formData.agreeTerms}
                                onChange={(e) => setFormData(prev => ({...prev, agreeTerms: e.target.checked}))}
                                className="h-4 w-4 text-blue-600 rounded border-gray-300 focus:ring-blue-500 cursor-pointer"
                            />
                            <label htmlFor="terms" className="text-sm text-gray-600 cursor-pointer select-none">
                                {t('register.terms_agree')} <a href="#" className="text-blue-600 hover:underline">{t('register.terms_link')}</a>
                            </label>
                        </div>

                        <div className="flex justify-between">
                            <button type="button" onClick={() => setStep(1)} className="flex items-center gap-2 text-gray-600 hover:text-gray-900 px-4 py-2 font-medium transition-colors">
                                <ArrowLeft className="h-4 w-4" /> {t('register.btn_prev')}
                            </button>

                            <button
                                type="submit"
                                disabled={!formData.agreeTerms || isSubmitting}
                                className={`px-8 py-2.5 rounded-md font-medium shadow-md transition-all flex items-center gap-2
                                    ${(!formData.agreeTerms || isSubmitting)
                                    ? 'bg-blue-400 cursor-not-allowed text-white'
                                    : 'bg-blue-600 hover:bg-blue-700 text-white'
                                }`}
                            >
                                {isSubmitting ? (
                                    <>
                                        <Loader2 className="h-4 w-4 animate-spin" />
                                        {t('register.btn_registering')}
                                    </>
                                ) : (
                                    <>
                                        {t('register.btn_finish')} <Check className="h-4 w-4" />
                                    </>
                                )}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </form>
    )
}