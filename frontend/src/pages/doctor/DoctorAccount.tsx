import React, { useEffect, useState, useRef } from "react";
import { useTranslation } from "react-i18next";
import { useAuth } from "@/hooks/useAuth.ts";
import {
    useDoctor,
    useDoctorCoverages,
    useDoctorImageUrl,
    useDoctorSpecialties,
    useUpdateDoctorMutation
} from "@/hooks/useDoctors.ts";
import { useSpecialties } from "@/hooks/useSpecialties.ts";
import { useCoverages } from "@/hooks/useCoverages.ts";

import {
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
import DashboardNavHeader from "@/components/DashboardNavHeader.tsx";
import DashboardNavContainer from "@/components/DashboardNavContainer.tsx";
import DashboardNavLoader from "@/components/DashboardNavLoader.tsx";
import {Spinner} from "@/components/ui/spinner.tsx";
import {initialsFallback} from "@/utils/userUtils.ts";
import {Skeleton} from "@/components/ui/skeleton.tsx";

const containerStyles = "flex flex-col gap-6 max-w-6xl mx-auto w-full mb-2";
const cardStyles = "p-0 overflow-hidden shadow-md gap-0";
const cardHeaderStyles = "flex items-center justify-between px-6 py-4 bg-white border-b";
const cardTitleStyles = "text-xl font-[500] text-(--text-color) flex items-center gap-2";
const sectionStyles = "pb-6 px-6 pt-2";
const gridStyles = "grid grid-cols-1 md:grid-cols-2 gap-6";
const infoValueStyles = "flex flex-row items-center gap-1 text-(--text-light) font-[400]";
const actionButtonStyles = "mt-2 sm:mt-0 bg-transparent text-(--primary-color) hover:bg-(--primary-bg) cursor-pointer";
const saveButton = "bg-(--primary-color) text-white hover:bg-(--primary-dark) cursor-pointer w-3xs";

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

    const updateProfileMutation = useUpdateDoctorMutation();

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
    const { url: doctorImageUrl, isLoading: isLoadingImageUrl } = useDoctorImageUrl(auth.userId);
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
            toast.error(t("error.error"), { description: t("dashboard.profile.error.specialties") });
            return;
        }
        if (selectedCovIds.length === 0) {
            toast.error(t("error.error"), { description: t("dashboard.profile.error.coverage") });
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
            onError: () => {
                toast.error(t("error.error"), { description: t("doctor.profile.update_error") });
            }
        });
    };

    const isLoading = isLoadingDoctor || isLoadingCurrentSpecs || isLoadingCurrentCovs || isLoadingAllSpecs || isLoadingAllCovs;

    if (isError || !doctor) return <GenericError code={404} />;
    const isSaving = updateProfileMutation.isPending;

    return (
        <DashboardNavContainer>
            <DashboardNavHeader title={t("patient.dashboard.account")}>
                {!isEditing && (
                    <Button onClick={() => setIsEditing(true)} className={actionButtonStyles}>
                        <Pencil className="w-4 h-4" />
                        {t("dashboard.profile.edit")}
                    </Button>
                )}
                {isEditing && <div className="h-9" />}
            </DashboardNavHeader>
            {isLoading ? <DashboardNavLoader /> :
                <div className={containerStyles}>
                    <Card className={cardStyles}>
                        <div className={cardHeaderStyles}>
                            <h2 className={cardTitleStyles}>
                                <User className="h-5 w-5 text-[var(--primary-color)]" />
                                {t("dashboard.profile.title")}
                            </h2>
                        </div>

                        <CardContent className="p-0 flex flex-col gap-2">
                            <div className="flex flex-col items-center justify-center py-8 bg-(--gray-100) border-b">
                                <div className="relative group">
                                    {isLoadingImageUrl ?
                                        <Skeleton className={"flex justify-center items-center h-32 w-32 border-4 border-white rounded-full"}>
                                            <Spinner className="h-6 w-6 text-(--gray-300)"/>
                                        </Skeleton> :
                                        <Avatar className="h-32 w-32 border-4 border-white shadow-lg">
                                            <AvatarImage src={previewImage || doctorImageUrl || undefined} className="object-cover" />
                                            <AvatarFallback className="text-2xl bg-(--gray-200)">
                                                {initialsFallback(doctor?.name, doctor?.lastName)}
                                            </AvatarFallback>
                                        </Avatar>
                                    }
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
                                    <p className="text-xs text-(--gray-500) mt-2">
                                        {t("dashboard.profile.image")}
                                    </p>
                                )}
                            </div>

                            <div className={sectionStyles}>
                                <h3 className="text-lg font-[500] mb-4 border-b pb-2">
                                    {t("dashboard.profile.personalInfo")}
                                </h3>

                                <div className={gridStyles}>
                                    <div className="space-y-2">
                                        <Label htmlFor="name">{t("dashboard.profile.name")}</Label>
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
                                        <Label htmlFor="lastName">{t("dashboard.profile.lastname")}</Label>
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
                                            {t("dashboard.profile.email")}
                                        </Label>
                                        <div className={infoValueStyles}>
                                            <Mail className="h-4 w-4" />
                                            {doctor.email}
                                        </div>
                                    </div>

                                    <div className="space-y-2">
                                        <Label htmlFor="phone" className="flex items-center gap-2">
                                            {t("dashboard.profile.phone")}
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
                                            <div className={infoValueStyles}>
                                                <Phone className="h-4 w-4" />
                                                {doctor.phone}
                                            </div>
                                        )}
                                    </div>
                                </div>
                            </div>
                        </CardContent>
                    </Card>

                    <div className={gridStyles}>
                        <Card className={cardStyles}>
                            <div className={cardHeaderStyles}>
                                <h3 className={cardTitleStyles}>
                                    <Stethoscope className="h-5 w-5 text-[var(--primary-color)]" />
                                    {t("dashboard.profile.specialties")}
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
                                                        "cursor-pointer border rounded-md p-2 text-sm flex items-center gap-2 transition-all select-none hover:border-(--primary-color)",
                                                        isSelected ? "bg-(--primary-bg) border-(--primary-color) text-(--primary-color) font-medium shadow-sm" : "bg-white text-(--gray-600) border-(--gray-200)"
                                                    )}
                                                >
                                                    <div className={cn("w-4 h-4 rounded border flex items-center justify-center shrink-0", isSelected ? "bg-(--primary-color) border-(--primary-color)" : "border-(--gray-300)")}>
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
                                                <span key={idx} className="bg-(--primary-bg) text-(--primary-color) px-3 py-1 rounded-full text-sm font-medium border border-(--primary-color)">
                                                {t(s.name)}
                                            </span>
                                            ))}
                                        </div>
                                    ) : (
                                        <p className="text-(--gray-400) text-sm">{t("doctor.profile.no-specialties")}</p>
                                    )
                                )}
                            </CardContent>
                        </Card>

                        <Card className={cardStyles}>
                            <div className={cardHeaderStyles}>
                                <h3 className={cardTitleStyles}>
                                    <ShieldPlus className="h-5 w-5 text-[var(--primary-color)]" />
                                    {t("dashboard.profile.coverages")}
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
                                                        "cursor-pointer border rounded-md p-2 text-sm flex items-center gap-2 transition-all hover:border-(--success) select-none",
                                                        isSelected
                                                            ? "bg-(--success-light) border-(--success-dark) text-(--success-dark) font-medium shadow-sm"
                                                            : "bg-white text-(--gray-600) border-(--gray-200)"
                                                    )}
                                                >
                                                    <div className={cn(
                                                        "w-4 h-4 rounded-full border flex items-center justify-center shrink-0",
                                                        isSelected ? "bg-(--success) border-(--success)" : "border-(--gray-300)"
                                                    )}>
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
                                                <span key={idx} className="bg-(--success-light) text-(--success-dark) px-3 py-1 rounded-full text-sm font-medium border border-(--success-dark)">
                                                {c.name}
                                            </span>
                                            ))}
                                        </div>
                                    ) : (
                                        <p className="text-(--gray-400) text-sm">{t("dashboard.profile.no-coverages")}</p>
                                    )
                                )}
                            </CardContent>
                        </Card>
                    </div>
                    {isEditing && (
                        <div className="flex justify-center gap-3 mt-4 flex-col sm:flex-row items-center">
                            <Button
                                variant="outline"
                                className="w-3xs cursor-pointer border border-(--danger) text-(--danger) hover:text-white hover:bg-(--danger)"
                                onClick={handleCancel}
                                disabled={isSaving}
                            >
                                <X className="w-4 h-4 mr-2" />
                                {t("cancel")}
                            </Button>

                            <Button onClick={handleSave} className={saveButton} disabled={isSaving}>
                                {isSaving ? (
                                    <>
                                        <Spinner className="w-4 h-4 mr-2" />
                                        {t("saving")}
                                    </>
                                ) : (
                                    <>
                                        <Save className="w-4 h-4 mr-2" />
                                        {t("save")}
                                    </>
                                )}
                            </Button>
                        </div>
                    )}
                </div>
            }
        </DashboardNavContainer>
    );
}

export default DoctorAccount;