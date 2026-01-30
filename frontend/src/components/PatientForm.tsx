"use client"

import React, {useEffect, useState} from "react"
import {MapPin, Loader2, User, AlertCircle} from "lucide-react"
import { useTranslation } from "react-i18next"

import { useCoverages } from "@/hooks/useCoverages"
import { useNeighborhoods } from "@/hooks/useNeighborhoods"


import { FormInput } from "@/components/ui/FormInput"
import { PasswordInput } from "@/components/ui/passwordInput"
import { PasswordStrengthMeter } from "@/components/ui/PasswordStrengthMeter"
import {useRegisterPatientMutation} from "@/hooks/usePatients.ts";

interface PatientFormProps {
    onSuccess: (email: string) => void;
}

export function PatientForm({ onSuccess }: PatientFormProps) {
    const { t } = useTranslation()

    const {mutate: registerPatient, isPending: isSubmitting} = useRegisterPatientMutation()

    const {data: coverages, isLoading: isLoadingCoverages} = useCoverages()
    const {data: neighborhoods, isLoading: isLoadingNeighborhoods} = useNeighborhoods()

    const [formData, setFormData] = useState({
        name: "",
        lastName: "",
        email: "",
        phone: "",
        password: "",
        repeatPassword: "",
        coverageUrl: "",
        neighborhoodUrl: "",
        neighborhoodDisplayName: "",
        agreeTerms: false
    })

    const [neighborhoodQuery, setNeighborhoodQuery] = useState("")
    const [showNeighborhoods, setShowNeighborhoods] = useState(false)
    const [errors, setErrors] = useState<{[key : string] : string}>({})
    const [isPasswordValid, setIsPasswordValid] = useState(false)

    const [globalError, setGlobalError] = useState("");


    useEffect(() => {
        const pwd = formData.password;
        const hasMinLength = pwd.length >= 8;
        const hasUppercase = /[A-Z]/.test(pwd);
        const hasNumber = /[0-9]/.test(pwd);
        setIsPasswordValid(hasMinLength && hasUppercase && hasNumber);
    }, [formData.password]);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { id, value } = e.target
        setFormData(prev => ({ ...prev, [id]: value }))
        if (errors[id]) {
            setErrors(prev => ({ ...prev, [id]: "" }))
        }
    }

    const handleNeighborhoodSelect = (neighborhood: { name: string, self: string }) => {
        setFormData(prev => ({
            ...prev,
            neighborhoodUrl: neighborhood.self,
            neighborhoodDisplayName: neighborhood.name
        }))
        setNeighborhoodQuery(neighborhood.name)
        setShowNeighborhoods(false)
        if (errors.neighborhoodUrl) {
            setErrors(prev => ({ ...prev, neighborhoodUrl: "" }))
        }
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()

        const newErrors = {} as {[key : string] : string}

        if (!formData.name) newErrors.name = t('register.errors.required');
        if (!formData.lastName) newErrors.lastName = t('register.errors.required');
        if (!formData.email) newErrors.email = t('register.errors.required');
        if (!formData.phone) newErrors.phone = t('register.errors.required');

        if (!formData.neighborhoodUrl) {
            newErrors.neighborhoodUrl = t('register.errors.select_neighborhood') || "Selecciona un barrio";
        }
        if (!formData.coverageUrl) {
            newErrors.coverageUrl = t('register.errors.select_coverage') || "Selecciona una obra social";
        }

        if (formData.password !== formData.repeatPassword) {
            newErrors.repeatPassword = t("register.errors.passwords_mismatch");
        }
        if (!isPasswordValid) {
            newErrors.password = t("register.errors.password_requirements");
        }

        if (!formData.agreeTerms) {
            newErrors.agreeTerms = t("register.errors.terms_required") || "Debes aceptar los términos";
        }

        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
            const firstErrorField = document.getElementById(Object.keys(newErrors)[0]);
            if (firstErrorField) firstErrorField.scrollIntoView({ behavior: 'smooth', block: 'center' });
            return;
        }

        setErrors({});

        registerPatient(formData, {
                onSuccess: () => {
                    onSuccess(formData.email);
                },
                onError: (error: any) => {
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
                        setErrors(prev => ({ ...prev, email: t('register.errors.email_taken') }));
                    } else {
                        setGlobalError(errorMessage);
                    }                }
            }
            );

    }

    const filteredNeighborhoods = neighborhoods?.filter(n =>
        n.name.toLowerCase().includes(neighborhoodQuery.toLowerCase())
    ) || []

    return (
        <form onSubmit={handleSubmit} noValidate className="space-y-8 animate-in fade-in slide-in-from-bottom-4 duration-500">

            <section>
                <h2 className="text-xl font-bold text-blue-600 mb-6 border-b-2 border-blue-600 w-fit pb-1">
                    {t('register.section_personal')}
                </h2>
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
                        name="email"
                        type="email"
                        label={t('register.label_email')}
                        value={formData.email}
                        onChange={handleInputChange}
                        placeholder={t('register.placeholder_email')}
                        required
                        error={errors.email}
                    />


                    <div className="space-y-2 relative" id="neighborhoodUrl">
                        <label htmlFor="neighborhood" className="text-sm font-medium text-gray-700">
                            {t('register.label_neighborhood')} <span className="text-red-500">*</span>
                        </label>
                        <div className="relative">
                            <MapPin className={`absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 ${errors.neighborhoodUrl ? 'text-red-500' : 'text-gray-400'}`} />
                            <input
                                id="neighborhood"
                                type="text"
                                placeholder={t('register.placeholder_neighborhood')}
                                value={formData.neighborhoodDisplayName || neighborhoodQuery}
                                onChange={(e) => {
                                    setNeighborhoodQuery(e.target.value)
                                    setFormData(prev => ({...prev, neighborhoodDisplayName: ""}))
                                    setShowNeighborhoods(true)
                                }}
                                onFocus={() => setShowNeighborhoods(true)}
                                className={`w-full pl-9 pr-3 py-2 border rounded-md focus:outline-none focus:ring-2 transition-all
                                    ${errors.neighborhoodUrl
                                    ? 'border-red-500 focus:ring-red-200 '
                                    : 'border-gray-300 focus:ring-blue-500'}`}
                                autoComplete="off"
                            />
                        </div>

                        {errors.neighborhoodUrl && (
                            <p className="text-sm text-red-500 font-medium animate-in fade-in slide-in-from-top-1">
                                {errors.neighborhoodUrl}
                            </p>
                        )}

                        {showNeighborhoods && !isLoadingNeighborhoods ? (
                            <div className="absolute z-10 w-full bg-white border rounded-md shadow-lg mt-1 max-h-40 overflow-y-auto">
                                {filteredNeighborhoods.length > 0 ? (
                                    filteredNeighborhoods.map(n => (
                                        <div
                                            key={n.name}
                                            className="px-4 py-2 hover:bg-gray-100 cursor-pointer text-sm"
                                            onClick={() => handleNeighborhoodSelect(n)}
                                        >
                                            {n.name}
                                        </div>
                                    ))
                                ) : (
                                    <div className="px-4 py-2 text-gray-500 text-sm">{t('register.no_neighborhoods')}</div>
                                )}
                            </div>
                        ) : isLoadingNeighborhoods ? (
                            <div className="flex items-center gap-2 text-gray-500 text-sm"><Loader2 className="h-4 w-4 animate-spin"/> {t('register.loader_neighborhoods')}</div>
                        ): (<></>)}
                    </div>

                    <div className="md:col-span-2">
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
                    </div>
                </div>
            </section>

            <section>
                <h2 className="text-xl font-bold text-blue-600 mb-6 border-b-2 border-blue-600 w-fit pb-1">
                    {t('register.section_security')}
                </h2>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
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
            </section>

            <section id="coverageUrl">
                <h2 className="text-xl font-bold text-blue-600 mb-6 border-b-2 border-blue-600 w-fit pb-1">
                    {t('register.section_coverage')} <span className="text-red-500 text-sm">*</span>
                </h2>

                {isLoadingCoverages ? (
                    <div className="flex items-center gap-2 text-gray-500 text-sm"><Loader2 className="h-4 w-4 animate-spin"/> {t('register.loader_coverages')}</div>
                ) : (
                    <div className="space-y-2">
                        <div className="flex flex-wrap gap-4">
                            {coverages?.map((cov: any) => (
                                <div
                                    key={cov.self}
                                    onClick={() => {
                                        setFormData(prev => ({...prev, coverageUrl: cov.self}))
                                        if(errors.coverageUrl) setErrors(prev => ({...prev, coverageUrl: ""}))
                                    }}
                                    className={`flex items-center space-x-2 border p-4 rounded-lg cursor-pointer w-full md:w-auto transition-colors select-none
                                        ${formData.coverageUrl === cov.self
                                        ? 'bg-blue-50 border-blue-500 shadow-sm'
                                        : errors.coverageUrl
                                            ? ''
                                            : 'hover:bg-gray-50 border-gray-200'}`}
                                >
                                    <input
                                        type="radio"
                                        id={`cov-${cov.name}`}
                                        name="coverage"
                                        value={cov.self}
                                        checked={formData.coverageUrl === cov.self}
                                        onChange={() => {}}
                                        className="h-4 w-4 text-blue-600 focus:ring-blue-500 cursor-pointer"
                                    />
                                    <label htmlFor={`cov-${cov.name}`} className="text-sm font-medium text-gray-700 cursor-pointer">
                                        {cov.name}
                                    </label>
                                </div>
                            ))}
                        </div>

                        {errors.coverageUrl && (
                            <p className="text-sm text-red-500 font-medium animate-in fade-in slide-in-from-top-1">
                                {errors.coverageUrl}
                            </p>
                        )}
                    </div>
                )}
            </section>

            <div className="pt-4 border-t space-y-2">
                {globalError && (
                    <div className="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-md text-sm flex items-center gap-2 animate-in fade-in slide-in-from-top-1">
                        <AlertCircle className="h-4 w-4 shrink-0" />
                        <span>{globalError}</span>
                    </div>
                )}
                <div className="flex items-center space-x-2">
                    <input
                        type="checkbox"
                        id="terms"
                        checked={formData.agreeTerms}
                        onChange={(e) => {
                            setFormData(prev => ({...prev, agreeTerms: e.target.checked}))
                            if(errors.agreeTerms) setErrors(prev => ({...prev, agreeTerms: ""}))
                        }}
                        className={`h-4 w-4 rounded border-gray-300 focus:ring-blue-500 cursor-pointer ${errors.agreeTerms ? '' : ''}`}
                    />
                    <label htmlFor="terms" className={`text-sm cursor-pointer select-none ${errors.agreeTerms ? 'text-red-600' : 'text-gray-600'}`}>
                        {t('register.terms_agree')} <a href="#" className="text-blue-600 hover:underline">{t('register.terms_link')}</a>
                    </label>
                </div>

                {errors.agreeTerms && (
                    <p className="text-sm text-red-500 font-medium">{errors.agreeTerms}</p>
                )}

                <div className="pt-4">
                    <button
                        type="submit"
                        disabled={isSubmitting}
                        className={`w-full font-bold py-3 px-4 rounded-lg transition-all flex items-center justify-center gap-2
                            ${isSubmitting
                            ? 'bg-slate-400 cursor-not-allowed'
                            : 'bg-slate-500 hover:bg-slate-600 text-white'
                        }`}
                    >
                        {isSubmitting ? (
                            <>
                                <Loader2 className="h-5 w-5 animate-spin" />
                                {t('register.btn_registering')}
                            </>
                        ) : (
                            <>
                                <User className="h-5 w-5" />
                                {t('register.btn_register')}
                            </>
                        )}
                    </button>
                </div>

                <p className="text-center mt-4 text-sm text-gray-600">
                    {t('register.have_account')} <a href="/login" className="text-blue-600 font-medium hover:underline">{t('register.login_link')}</a>
                </p>
            </div>
        </form>
    )
}