import React, { useEffect, useState, useRef } from "react";
import { useTranslation } from "react-i18next";
import { useAuth } from "@/hooks/useAuth.ts";
import {
    useDoctor,
    useDoctorCoverages,
    useDoctorImageUrl,
    useDoctorSpecialties,
    usePutDoctorImage,
    useUpdateDoctor, useUpdateDoctorProfile
} from "@/hooks/useDoctors.ts";
import { useSpecialties } from "@/hooks/useSpecialties.ts";
import { useCoverages } from "@/hooks/useCoverages.ts";

import {
    Loader2,
    User,
    Mail,
    Phone,
    Upload,
    X,
    Save,
    Pencil,
    Stethoscope,
    ShieldPlus,
    Check
} from "lucide-react";

import { Card, CardContent } from "@/components/ui/card.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Input } from "@/components/ui/input.tsx";
import { Label } from "@/components/ui/label.tsx";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar.tsx";
import GenericError from "@/pages/GenericError.tsx";
import { toast } from "sonner";
import { cn } from "@/lib/utils";

const containerStyles = "flex flex-col gap-6 max-w-6xl mx-auto w-full pb-10";
const cardStyles = "p-0 overflow-hidden shadow-md";
const cardHeaderStyles = "flex items-center justify-between px-6 py-4 bg-white border-b";
const cardTitleStyles = "text-xl font-semibold text-gray-800 flex items-center gap-2";
const sectionStyles = "p-6";
const gridStyles = "grid grid-cols-1 md:grid-cols-2 gap-6";
const infoValueStyles = "text-base font-medium text-gray-900";
const actionButtonStyles = "bg-[var(--primary-color)] hover:bg-[var(--primary-dark)] text-white";

function DoctorAccount() {
    const { t } = useTranslation();
    const auth = useAuth();
    const [isEditing, setIsEditing] = useState(false);

    const {
        data: doctor,
        isLoading: isLoadingDoctor,
        isError
    } = useDoctor(auth.userId);

    const {
        data: currentSpecialties,
        isLoading: isLoadingCurrentSpecs
    } = useDoctorSpecialties(doctor?.specialties);

    const {
        data: currentCoverages,
        isLoading: isLoadingCurrentCovs
    } = useDoctorCoverages(doctor?.coverages);

    const {
        data: allSpecialtiesList,
        isLoading: isLoadingAllSpecs
    } = useSpecialties();

    const {
        data: allCoveragesList,
        isLoading: isLoadingAllCovs
    } = useCoverages();

    const updateProfileMutation = useUpdateDoctorProfile();
    const updateDoctorMutation = useUpdateDoctor(doctor?.self || "");
    const updateImageMutation = usePutDoctorImage(doctor?.image || "");

    const [formData, setFormData] = useState({
        name: "",
        lastName: "",
        phone: "",
        email: ""
    });

    const [selectedSpecIds, setSelectedSpecIds] = useState<number[]>([]);
    const [selectedCovIds, setSelectedCovIds] = useState<number[]>([]);

    const [previewImage, setPreviewImage] = useState<string | null>(null);
    const [selectedFile, setSelectedFile] = useState<File | null>(null);
    const fileInputRef = useRef<HTMLInputElement>(null);
    const { url: doctorImageUrl } = useDoctorImageUrl(auth.userId);
    useEffect(() => {
        if (doctor) {
            setFormData({
                name: doctor.name || "",
                lastName: doctor.lastName || "",
                phone: doctor.phone || "",
                email: doctor.email || ""
            });
        }
        if (currentSpecialties) {
            setSelectedSpecIds(currentSpecialties.map(s => Number(s.self.split('/').pop())));
        }
        if (currentCoverages) {
            setSelectedCovIds(currentCoverages.map(c => Number(c.self.split('/').pop())));
        }
    }, [doctor, currentSpecialties, currentCoverages, isEditing]);


    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files[0]) {
            const file = e.target.files[0];
            setSelectedFile(file);
            const objectUrl = URL.createObjectURL(file);
            setPreviewImage(objectUrl);
        }
    };

    const toggleSpecialty = (id: number) => {
        setSelectedSpecIds(prev =>
            prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]
        );
    };

    const toggleCoverage = (id: number) => {
        setSelectedCovIds(prev =>
            prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]
        );
    };

    const handleCancel = () => {
        setIsEditing(false);
        setPreviewImage(null);
        setSelectedFile(null);
    };

    const handleSave = () => {
        if (!doctor || !auth.userId) return;
        if (selectedSpecIds.length === 0) {
            toast.error(t("error"), { description: t("register.errors.select_specialties") });
            return;
        }
        if (selectedCovIds.length === 0) {
            toast.error(t("error"), { description: t("register.errors.select_coverage") });
            return;
        }

        updateProfileMutation.mutate({
            doctorId: auth.userId,
            doctorUrl: doctor.self,
            imageUrl: doctor.image,
            data: {
                name: formData.name,
                lastName: formData.lastName,
                phone: formData.phone,
                specialties: selectedSpecIds.map(String),
                coverages: selectedCovIds.map(String)
            },
            imageFile: selectedFile
        }, {
            onSuccess: () => {
                toast.success(t("success"), { description: t("doctor.profile.update_success") });
                setIsEditing(false);
            },
            onError: (error) => {
                console.error(error);
                toast.error(t("error"), { description: t("doctor.profile.update_error") });
            }
        });
    };


    if (isLoadingDoctor || isLoadingCurrentSpecs || isLoadingCurrentCovs || isLoadingAllSpecs || isLoadingAllCovs) {
        return <div className="flex justify-center mt-36"><Loader2 className="animate-spin h-8 w-8" /></div>;
    }

    if (isError || !doctor) return <GenericError code={404} />;
    const isSaving = updateDoctorMutation.isPending || updateImageMutation.isPending;

    return (
        <div className={containerStyles}>
            <Card className={cardStyles}>
                <div className={cardHeaderStyles}>
                    <h2 className={cardTitleStyles}>
                        <User className="h-5 w-5 text-[var(--primary-color)]" />
                        {t("dashboard.profile.title", "My Profile")}
                    </h2>
                    {!isEditing && (
                        <Button onClick={() => setIsEditing(true)} className={actionButtonStyles}>
                            <Pencil className="w-4 h-4 mr-2" />
                            {t("dashboard.profile.edit", "Edit")}
                        </Button>
                    )}
                </div>

                <CardContent className="p-0">
                    <div className="flex flex-col items-center justify-center py-8 bg-gray-50 border-b">
                        <div className="relative group">
                            <Avatar className="h-32 w-32 border-4 border-white shadow-lg cursor-pointer">
                                <AvatarImage src={previewImage || doctorImageUrl || undefined} className="object-cover" />
                                <AvatarFallback className="text-2xl bg-gray-200">
                                    {doctor.name?.[0]}{doctor.lastName?.[0]}
                                </AvatarFallback>
                            </Avatar>

                            {isEditing && (
                                <div
                                    className="absolute inset-0 bg-black/40 rounded-full flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity cursor-pointer"
                                    onClick={() => fileInputRef.current?.click()}
                                >
                                    <Upload className="text-white h-8 w-8" />
                                </div>
                            )}
                            <input
                                type="file"
                                ref={fileInputRef}
                                className="hidden"
                                accept="image/jpeg,image/png,image/jpg"
                                onChange={handleFileChange}
                                disabled={!isEditing || isSaving}
                            />
                        </div>
                        {isEditing && (
                            <p className="text-xs text-gray-500 mt-2">
                                {t("register.chooseImage", "Click to change image")}
                            </p>
                        )}
                    </div>

                    <div className={sectionStyles}>
                        <h3 className="text-lg font-semibold mb-4 border-b pb-2">
                            {t("dashboard.profile.personalInfo", "Personal Information")}
                        </h3>

                        <div className={gridStyles}>
                            <div className="space-y-2">
                                <Label htmlFor="name">{t("register.firstName", "Name")}</Label>
                                {isEditing ? (
                                    <Input
                                        id="name"
                                        name="name"
                                        value={formData.name}
                                        onChange={handleInputChange}
                                        disabled={isSaving}
                                    />
                                ) : (
                                    <div className={infoValueStyles}>{doctor.name}</div>
                                )}
                            </div>
                            <div className="space-y-2">
                                <Label htmlFor="lastName">{t("register.lastName", "Last Name")}</Label>
                                {isEditing ? (
                                    <Input
                                        id="lastName"
                                        name="lastName"
                                        value={formData.lastName}
                                        onChange={handleInputChange}
                                        disabled={isSaving}
                                    />
                                ) : (
                                    <div className={infoValueStyles}>{doctor.lastName}</div>
                                )}
                            </div>

                            <div className="space-y-2">
                                <Label htmlFor="email" className="flex items-center gap-2">
                                    <Mail className="h-4 w-4" /> {t("dashboard.profile.email", "Email")}
                                </Label>
                                <div className={`${infoValueStyles} text-gray-600`}>{doctor.email}</div>
                            </div>

                            <div className="space-y-2">
                                <Label htmlFor="phone" className="flex items-center gap-2">
                                    <Phone className="h-4 w-4" /> {t("dashboard.profile.phone", "Phone")}
                                </Label>
                                {isEditing ? (
                                    <Input
                                        id="phone"
                                        name="phone"
                                        value={formData.phone}
                                        onChange={handleInputChange}
                                        disabled={isSaving}
                                    />
                                ) : (
                                    <div className={infoValueStyles}>{doctor.phone}</div>
                                )}
                            </div>
                        </div>
                    </div>

                    {isEditing && (
                        <div className="px-6 py-4 bg-gray-50 border-t flex justify-end gap-3">
                            <Button variant="outline" onClick={handleCancel} disabled={isSaving}>
                                <X className="w-4 h-4 mr-2" />
                                {t("logout.cancel", "Cancel")}
                            </Button>
                            <Button onClick={handleSave} className={actionButtonStyles} disabled={isSaving}>
                                {isSaving ? (
                                    <>
                                        <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                                        {t("saving", "Saving...")}
                                    </>
                                ) : (
                                    <>
                                        <Save className="w-4 h-4 mr-2" />
                                        {t("appointment.form.save", "Save")}
                                    </>
                                )}
                            </Button>
                        </div>
                    )}
                </CardContent>
            </Card>

            <div className={gridStyles}>
                <Card className={cardStyles}>
                    <div className={cardHeaderStyles}>
                        <h3 className={cardTitleStyles}>
                            <Stethoscope className="h-5 w-5 text-[var(--primary-color)]" />
                            {t("dashboard.profile.specialties", "Specialties")}
                        </h3>
                    </div>
                    <CardContent className="p-6">
                        {isEditing ? (
                            <div className="grid grid-cols-2 gap-2 max-h-60 overflow-y-auto pr-2">
                                {allSpecialtiesList?.map((s) => {
                                    const sId = Number(s.self.split('/').pop());
                                    const isSelected = selectedSpecIds.includes(sId);
                                    return (
                                        <div
                                            key={sId}
                                            onClick={() => toggleSpecialty(sId)}
                                            className={cn(
                                                "cursor-pointer border rounded-md p-2 text-sm flex items-center gap-2 transition-all hover:border-blue-300 select-none",
                                                isSelected ? "bg-blue-50 border-blue-500 text-blue-700 font-medium shadow-sm" : "bg-white text-gray-600 border-gray-200"
                                            )}
                                        >
                                            <div className={cn("w-4 h-4 rounded border flex items-center justify-center shrink-0", isSelected ? "bg-blue-500 border-blue-500" : "border-gray-300")}>
                                                {isSelected && <Check className="w-3 h-3 text-white" />}
                                            </div>
                                            <span className="truncate">{t(s.name)}</span>
                                        </div>
                                    )
                                })}
                            </div>
                        ) : (
                            currentSpecialties && currentSpecialties.length > 0 ? (
                                <div className="flex flex-wrap gap-2">
                                    {currentSpecialties.map((s, idx) => (
                                        <span key={idx} className="bg-blue-50 text-blue-700 px-3 py-1 rounded-full text-sm font-medium border border-blue-100">
                                            {t(s.name)}
                                        </span>
                                    ))}
                                </div>
                            ) : (
                                <p className="text-gray-400 text-sm">{t("doctor.profile.no_specialties", "No specialties listed")}</p>
                            )
                        )}
                    </CardContent>
                </Card>

                <Card className={cardStyles}>
                    <div className={cardHeaderStyles}>
                        <h3 className={cardTitleStyles}>
                            <ShieldPlus className="h-5 w-5 text-[var(--primary-color)]" />
                            {t("dashboard.profile.coverages", "Coverages")}
                        </h3>
                    </div>
                    <CardContent className="p-6">
                        {isEditing ? (
                            <div className="grid grid-cols-2 gap-2 max-h-60 overflow-y-auto pr-2">
                                {allCoveragesList?.map((c) => {
                                    const cId = Number(c.self.split('/').pop());
                                    const isSelected = selectedCovIds.includes(cId);
                                    return (
                                        <div
                                            key={cId}
                                            onClick={() => toggleCoverage(cId)}
                                            className={cn(
                                                "cursor-pointer border rounded-md p-2 text-sm flex items-center gap-2 transition-all hover:border-green-300 select-none",
                                                isSelected ? "bg-green-50 border-green-500 text-green-700 font-medium shadow-sm" : "bg-white text-gray-600 border-gray-200"
                                            )}
                                        >
                                            <div className={cn("w-4 h-4 rounded border flex items-center justify-center shrink-0", isSelected ? "bg-green-500 border-green-500" : "border-gray-300")}>
                                                {isSelected && <Check className="w-3 h-3 text-white" />}
                                            </div>
                                            <span className="truncate">{c.name}</span>
                                        </div>
                                    )
                                })}
                            </div>
                        ) : (
                            currentCoverages && currentCoverages.length > 0 ? (
                                <div className="flex flex-wrap gap-2">
                                    {currentCoverages.map((c, idx) => (
                                        <span key={idx} className="bg-green-50 text-green-700 px-3 py-1 rounded-full text-sm font-medium border border-green-100">
                                            {c.name}
                                        </span>
                                    ))}
                                </div>
                            ) : (
                                <p className="text-gray-400 text-sm">{t("doctor.profile.no_coverages", "No coverages listed")}</p>
                            )
                        )}
                    </CardContent>
                </Card>
            </div>
        </div>
    );
}

export default DoctorAccount;