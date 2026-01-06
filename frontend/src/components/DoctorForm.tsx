"use client"

import React, { useState } from "react"
import {
    ArrowRight,
    ArrowLeft,
    Upload,
    Check,
    Calendar,
    Loader2
} from "lucide-react"
import { useTranslation } from "react-i18next"

import { useSpecialties } from "@/hooks/useSpecialties"
import { useCoverages } from "@/hooks/useCoverages"
import { useUserService } from "@/hooks/userService"
import { api } from "@/data/Api"

import { FormInput } from "@/components/ui/FormInput"
import { PasswordInput } from "@/components/ui/passwordInput"
import { FormStepper } from "@/components/ui/FormStepper"

interface DoctorFormProps {
    onSuccess: (email: string) => void;
}

export function DoctorForm({ onSuccess }: DoctorFormProps) {
    const { t } = useTranslation()
    const userService = useUserService(api)

    const [step, setStep] = useState(1)
    const [isSubmitting, setIsSubmitting] = useState(false)

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

    const handleNextStep = () => {
        const requiredFields = ['name', 'lastName', 'email', 'phone', 'password', 'repeatPassword'];

        const missingField = requiredFields.find(field => !formData[field as keyof typeof formData]);

        if (missingField) {
            alert(t('register.errors.missing_fields'));
            return;
        }

        if (formData.password !== formData.repeatPassword) {
            alert(t('register.errors.passwords_mismatch'));
            return;
        }

        setStep(2);
    }

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target
        setFormData(prev => ({ ...prev, [name]: value }))
    }

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files[0]) {
            setFormData(prev => ({ ...prev, image: e.target.files![0] }))
        }
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

        setIsSubmitting(true)

        try {
            await userService.registerDoctor({
                ...formData,
                selectedSpecialties: formData.selectedSpecialties,
                selectedCoverages: formData.selectedCoverages
            });
            onSuccess(formData.email);
        } catch (error: any) {
            console.error("Error al registrar", error)

            if (error.response && error.response.data && error.response.data.errors) {
                const errors = error.response.data.errors;
                const emailError = errors.find((e: any) => e.field === "email" || e.message?.includes("email"));

                if (emailError) {
                    alert(t('register.errors.email_taken'));
                } else {
                    alert(`Error: ${errors[0].defaultMessage || t('register.errors.generic')}`);
                }
            } else {
                alert(t('register.errors.generic'))
            }
        } finally {
            setIsSubmitting(false)
        }
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
                    { id: 2, label: t('register.step_professional') },
                    { id: 3, label: t('register.step_availability') }
                ]}
            />

            {step === 1 && (
                <div className="space-y-6">
                    <div className="flex items-center gap-2 mb-4">
                        <h3 className="text-lg font-bold text-blue-600">{t('register.section_personal')}</h3>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <FormInput
                            id="name"
                            label={t('register.label_name')}
                            value={formData.name}
                            onChange={handleInputChange}
                            placeholder={t('register.placeholder_name')}
                            required
                        />

                        <FormInput
                            id="lastName"
                            label={t('register.label_lastname')}
                            value={formData.lastName}
                            onChange={handleInputChange}
                            placeholder={t('register.placeholder_lastname')}
                            required
                        />

                        <FormInput
                            id="email"
                            type="email"
                            label={t('register.label_email')}
                            value={formData.email}
                            onChange={handleInputChange}
                            placeholder={t('register.placeholder_email')}
                            required
                        />

                        <FormInput
                            id="phone"
                            label={t('register.label_phone')}
                            value={formData.phone}
                            onChange={handleInputChange}
                            placeholder={t('register.placeholder_phone')}
                            required
                        />

                        <PasswordInput
                            id="password"
                            label={t('register.label_password')}
                            value={formData.password}
                            onChange={handleInputChange}
                            tooltip={t('register.tooltip_password')}
                            required
                        />

                        <PasswordInput
                            id="repeatPassword"
                            label={t('register.label_repeat_password')}
                            value={formData.repeatPassword}
                            onChange={handleInputChange}
                            required
                        />
                    </div>

                    <div className="space-y-2 pt-2">
                        <label className="text-sm font-medium text-gray-700">{t('register.label_profile_image')}</label>
                        <div className="border-2 border-dashed border-gray-300 rounded-lg bg-gray-50 h-24 flex flex-col items-center justify-center text-gray-500 hover:bg-gray-100 transition-colors cursor-pointer relative group">
                            <input type="file" accept="image/*" onChange={handleFileChange} className="absolute inset-0 w-full h-full opacity-0 cursor-pointer z-10" />
                            <div className="flex flex-row items-center gap-2 group-hover:text-blue-600 transition-colors">
                                <Upload className="h-5 w-5" />
                                <span className="text-sm font-medium">
                                    {formData.image ? formData.image.name : t('register.label_profile_image')}
                                </span>
                            </div>
                        </div>
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
                        <h3 className="text-lg font-bold text-blue-600">{t('register.section_coverage')}</h3>
                    </div>

                    {/* Specialties Grid */}
                    <div className="space-y-3">
                        <label className="text-sm font-medium text-gray-700">{t('register.label_specialties')} <span className="text-red-500">*</span></label>

                        {isLoadingSpecialties ? (
                            <div className="flex items-center gap-2 text-gray-500 text-sm"><Loader2 className="h-4 w-4 animate-spin"/> {t('register.loader_specialties')}</div>
                        ) : (
                            <div className="grid grid-cols-2 md:grid-cols-3 gap-3">
                                {specialties?.map((spec) => (
                                    <div
                                        key={spec.name}
                                        onClick={() => toggleSelection('selectedSpecialties', spec.self)}
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
                        )}
                    </div>

                    <div className="space-y-3">
                        <label className="text-sm font-medium text-gray-700">{t('register.label_coverages')} <span className="text-red-500">*</span></label>

                        {isLoadingCoverages ? (
                            <div className="flex items-center gap-2 text-gray-500 text-sm"><Loader2 className="h-4 w-4 animate-spin"/> {t('register.loader_coverages')}</div>
                        ) : (
                            <div className="grid grid-cols-2 md:grid-cols-3 gap-3">
                                {coverages?.map((cov) => (
                                    <div
                                        key={cov.name}
                                        onClick={() => toggleSelection('selectedCoverages', cov.self)}
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
                        )}
                    </div>

                    <div className="bg-amber-50 border border-amber-200 rounded-md p-4 text-sm text-amber-800 flex gap-2 items-start mt-4">
                        <Calendar className="h-5 w-5 shrink-0" />
                        <p>{t('register.note_availability')}</p>
                    </div>

                    <div className="pt-6 border-t flex flex-col gap-4">
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