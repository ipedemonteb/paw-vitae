"use client"

import { useState } from "react"
import { MapPin, Loader2, User } from "lucide-react"
import { useTranslation } from "react-i18next"

import { useCoverages } from "@/hooks/useCoverages"
import { useNeighborhoods } from "@/hooks/useNeighborhoods"
import { useUserService } from "@/hooks/userService"
import { api } from "@/data/Api"

import { FormInput } from "@/components/ui/FormInput"
import { PasswordInput } from "@/components/ui/passwordInput"

interface PatientFormProps {
    onSuccess: (email: string) => void;
}

export function PatientForm({ onSuccess }: PatientFormProps) {
    const { t } = useTranslation()
    const userService = useUserService(api)

    const [isSubmitting, setIsSubmitting] = useState(false)

    const coverages = useCoverages()
    const neighborhoods = useNeighborhoods()

    const [formData, setFormData] = useState({
        name: "",
        lastName: "",
        email: "",
        phone: "",
        password: "",
        repeatPassword: "",
        coverageUrl: "",
        neighborhoodUrl: "",
        neighborhoodDisplayName: ""
    })

    const [neighborhoodQuery, setNeighborhoodQuery] = useState("")
    const [showNeighborhoods, setShowNeighborhoods] = useState(false)

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { id, value } = e.target
        setFormData(prev => ({ ...prev, [id]: value }))
    }

    const handleNeighborhoodSelect = (neighborhood: { name: string, self: string }) => {
        setFormData(prev => ({
            ...prev,
            neighborhoodUrl: neighborhood.self,
            neighborhoodDisplayName: neighborhood.name
        }))
        setShowNeighborhoods(false)
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()

        if (formData.password !== formData.repeatPassword) {
            alert(t('register.errors.passwords_mismatch'))
            return
        }

        const passwordRegex = /^(?=.*[A-Z])(?=.*\d).{8,}$/;
        if (!passwordRegex.test(formData.password)) {
            alert(t('register.errors.password_requirements'))
            return
        }

        if (!formData.coverageUrl || !formData.neighborhoodUrl) {
            alert(t('register.errors.select_valid'))
            return
        }

        setIsSubmitting(true)

        try {
            await userService.registerPatient(formData);
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

    const filteredNeighborhoods = neighborhoods.neighborhoods.filter(n =>
        n.name.toLowerCase().includes(neighborhoodQuery.toLowerCase())
    )


    return (
        <form onSubmit={handleSubmit} className="space-y-8 animate-in fade-in slide-in-from-bottom-4 duration-500">

            <section>
                <h2 className="text-xl font-bold text-blue-600 mb-6 border-b-2 border-blue-600 w-fit pb-1">
                    {t('register.section_personal')}
                </h2>
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

                    <div className="space-y-2 relative">
                        <label htmlFor="neighborhood" className="text-sm font-medium text-gray-700">
                            {t('register.label_neighborhood')} <span className="text-red-500">*</span>
                        </label>
                        <div className="relative">
                            <MapPin className="absolute left-3 top-2.5 h-4 w-4 text-gray-400" />
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
                                className="w-full pl-9 pr-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 transition-all"
                                autoComplete="off"
                            />
                        </div>

                        {showNeighborhoods && (
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
                        )}
                    </div>

                    <div className="md:col-span-2">
                        <FormInput
                            id="phone"
                            label={t('register.label_phone')}
                            value={formData.phone}
                            onChange={handleInputChange}
                            placeholder={t('register.placeholder_phone')}
                            required
                        />
                    </div>
                </div>
            </section>

            <section>
                <h2 className="text-xl font-bold text-blue-600 mb-6 border-b-2 border-blue-600 w-fit pb-1">
                    {t('register.section_security')}
                </h2>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
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
            </section>

            <section>
                <h2 className="text-xl font-bold text-blue-600 mb-6 border-b-2 border-blue-600 w-fit pb-1">
                    {t('register.section_coverage')} <span className="text-red-500 text-sm">*</span>
                </h2>

                {!coverages ? (
                    <div className="flex items-center gap-2 text-gray-500 text-sm"><Loader2 className="h-4 w-4 animate-spin"/> {t('register.loader_coverages')}</div>
                ) : (
                    <div className="flex flex-wrap gap-4">
                        {coverages.coverages.map((cov: any) => (
                            <div
                                key={cov.self}
                                onClick={() => setFormData(prev => ({...prev, coverageUrl: cov.self}))}
                                className={`flex items-center space-x-2 border p-4 rounded-lg cursor-pointer w-full md:w-auto transition-colors select-none
                                    ${formData.coverageUrl === cov.self ? 'bg-blue-50 border-blue-500 shadow-sm' : 'hover:bg-gray-50 border-gray-200'}`}
                            >
                                <input
                                    type="radio"
                                    id={`cov-${cov.name}`}
                                    name="coverage"
                                    value={cov.self}
                                    checked={formData.coverageUrl === cov.self}
                                    onChange={(e) => setFormData(prev => ({...prev, coverageUrl: e.target.value}))}
                                    className="h-4 w-4 text-blue-600 focus:ring-blue-500 cursor-pointer"
                                />
                                <label htmlFor={`cov-${cov.name}`} className="text-sm font-medium text-gray-700 cursor-pointer">
                                    {cov.name}
                                </label>
                            </div>
                        ))}
                    </div>
                )}
            </section>

            <div className="pt-4 border-t">
                <div className="flex items-center space-x-2 mb-6">
                    <input type="checkbox" id="terms" required className="h-4 w-4 text-blue-600 rounded border-gray-300 focus:ring-blue-500 cursor-pointer"/>
                    <label htmlFor="terms" className="text-sm text-gray-600 cursor-pointer select-none">
                        {t('register.terms_agree')} <a href="#" className="text-blue-600 hover:underline">{t('register.terms_link')}</a>
                    </label>
                </div>

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

                <p className="text-center mt-4 text-sm text-gray-600">
                    {t('register.have_account')} <a href="/login" className="text-blue-600 font-medium hover:underline">{t('register.login_link')}</a>
                </p>
            </div>
        </form>
    )
}