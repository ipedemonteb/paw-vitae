import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useAuth } from "@/hooks/useAuth.ts";
import {
    usePatientById,
    useUpdatePatient
} from "@/hooks/usePatients.ts";
import { useCoverage, useCoverages } from "@/hooks/useCoverages.ts";

import {
    User,
    Mail,
    Phone,
    X,
    Save,
    Pencil,
    ShieldPlus,
} from "lucide-react";

import { Card, CardContent } from "@/components/ui/card.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Input } from "@/components/ui/input.tsx";
import { Label } from "@/components/ui/label.tsx";
import { Avatar, AvatarFallback } from "@/components/ui/avatar.tsx";
import GenericError from "@/pages/GenericError.tsx";
import { toast } from "sonner";
import { useQueryClient } from "@tanstack/react-query";
import { cn } from "@/lib/utils";
import DashboardNavHeader from "@/components/DashboardNavHeader.tsx";
import DashboardNavContainer from "@/components/DashboardNavContainer.tsx";
import DashboardNavLoader from "@/components/DashboardNavLoader.tsx";
import {Spinner} from "@/components/ui/spinner.tsx";

const containerStyles = "flex flex-col gap-6 max-w-6xl mx-auto w-full mb-2";
const cardStyles = "p-0 overflow-hidden shadow-md gap-0";
const cardHeaderStyles = "flex items-center justify-between px-6 py-4 bg-white border-b";
const cardTitleStyles = "text-xl font-[500] text-(--text-color) flex items-center gap-2";
const sectionStyles = "pb-6 px-6 pt-2";
const gridStyles = "grid grid-cols-1 md:grid-cols-2 gap-6";
const infoValueStyles = "flex flex-row items-center gap-1 text-(--text-light) font-[400]";
const actionButtonStyles = "bg-[var(--primary-color)] hover:bg-[var(--primary-dark)] cursor-pointer text-white mt-2 md:mt-0 ";

function PatientAccount() {
    const { t } = useTranslation();
    const auth = useAuth();
    const queryClient = useQueryClient();
    const [isEditing, setIsEditing] = useState(false);

    const { data: patient, isLoading, isError } = usePatientById(auth.userId);
    const { data: allCoveragesList } = useCoverages();

    const { data: coverage } = useCoverage(patient?.coverages);

    const updatePatientMutation = useUpdatePatient(patient?.self || "");

    const [formData, setFormData] = useState({
        name: "",
        lastName: "",
        phone: "",
        email: ""
    });

    const [selectedCoverageId, setSelectedCoverageId] = useState<number | null>(null);

    useEffect(() => {
        if (patient) {
            setFormData({
                name: patient.name || "",
                lastName: patient.lastName || "",
                phone: patient.phone || "",
                email: patient.email || ""
            });
        }
        if (coverage) {
            const id = Number(coverage.self.split('/').pop());
            setSelectedCoverageId(id);
        }
    }, [patient, coverage, isEditing]);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const selectCoverage = (id: number) => {
        setSelectedCoverageId(id);
    };

    const handleCancel = () => {
        setIsEditing(false);
    };

    const handleSave = () => {
        if (!patient) return;

        if (!formData.name.trim()) {
            toast.error(t("error.error"), { description: t("dashboard.profile.error.name") });
            return;
        }

        if (!formData.lastName.trim()) {
            toast.error(t("error.error"), { description: t("dashboard.profile.error.lastname") });
            return;
        }

        if (!formData.phone.trim()) {
            toast.error(t("error.error"), { description: t("dashboard.profile.error.phone") });
            return;
        }

        if (!selectedCoverageId) {
            toast.error(t("error.error"), { description: t("dashboard.profile.error.coverage") });
            return;
        }

        const selectedCoverageObj = allCoveragesList?.find(c => Number(c.self.split('/').pop()) === selectedCoverageId);

        if (!selectedCoverageObj) return;

        updatePatientMutation.mutate({
            name: formData.name,
            lastName: formData.lastName,
            phone: formData.phone,
            coverage: selectedCoverageObj.self.split('/').pop()
        }, {
            onSuccess: () => {
                toast.success(t("success"), {
                    description: t("doctor.profile.update_success")
                });
                setIsEditing(false);
                queryClient.invalidateQueries({ queryKey: ["auth", "patients", "id", auth.userId] });
                queryClient.invalidateQueries({ queryKey: ['coverages'] });
            },
            onError: () => {
                toast.error(t("error.error"), {
                    description: t("doctor.profile.update_error")
                });
            }
        });
    };

    //TODO: Handle correctly
    if (isError || !patient) return <GenericError code={404} />;

    const isSaving = updatePatientMutation.isPending;

    return (
        <DashboardNavContainer>
            <DashboardNavHeader title={t("patient.dashboard.account")}>
                {!isEditing && (
                    <Button onClick={() => setIsEditing(true)} className={actionButtonStyles}>
                        <Pencil className="w-4 h-4 mr-2" />
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
                            <Avatar className="h-32 w-32 border-4 border-white shadow-lg">
                                <AvatarFallback className="text-3xl bg-(--gray-200) text-(--gray-500)">
                                    {patient.name?.[0]}
                                    {patient.lastName?.[0]}
                                </AvatarFallback>
                            </Avatar>
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
                                        <div className={infoValueStyles}>{patient.name}</div>
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
                                        <div className={infoValueStyles}>{patient.lastName}</div>
                                    )}
                                </div>

                                <div className="space-y-2">
                                    <Label htmlFor="email" className="flex items-center gap-2">
                                        {t("dashboard.profile.email")}
                                    </Label>
                                    <div className={infoValueStyles}>
                                        <Mail className="h-4 w-4" />
                                        {patient.email}
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
                                            {patient.phone}
                                        </div>
                                    )}
                                </div>
                            </div>
                        </div>
                    </CardContent>
                </Card>

                <div className="grid grid-cols-1 gap-6">
                    <Card className={cardStyles}>
                        <div className={cardHeaderStyles}>
                            <h3 className={cardTitleStyles}>
                                <ShieldPlus className="h-5 w-5 text-[var(--primary-color)]" />
                                {t("dashboard.profile.coverage")}
                            </h3>
                        </div>
                        <CardContent className="p-6">
                            {isEditing ? (
                                <div className="grid grid-cols-2 md:grid-cols-3 gap-2 max-h-60 overflow-y-auto pr-2">
                                    {allCoveragesList?.map((c) => {
                                        const cId = Number(c.self.split("/").pop());
                                        const isSelected = selectedCoverageId === cId;
                                        return (
                                            <div
                                                key={cId}
                                                onClick={() => selectCoverage(cId)}
                                                className={cn(
                                                    "cursor-pointer border rounded-md p-2 text-sm flex items-center gap-2 transition-all hover:border-(--success) select-none",
                                                    isSelected
                                                        ? "bg-(--success-light) border-(--success-dark) text-(--success-dark) font-medium shadow-sm"
                                                        : "bg-white text-(--gray-600) border-(--gray-200)"
                                                )}
                                            >
                                                <div
                                                    className={cn(
                                                        "w-4 h-4 rounded-full border flex items-center justify-center shrink-0",
                                                        isSelected ? "bg-(--success) border-(--success)" : "border-(--gray-300)"
                                                    )}
                                                >
                                                    {isSelected && <div className="w-2 h-2 bg-white rounded-full" />}
                                                </div>
                                                <span className="truncate">{c.name}</span>
                                            </div>
                                        );
                                    })}
                                </div>
                            ) : (
                                <div className="flex flex-wrap gap-2">
                                    {coverage ? (
                                        <span
                                            key={coverage.name}
                                            className="bg-(--success-light) text-(--success-dark) px-3 py-1 rounded-full text-sm font-medium border border-(--success-dark)"
                                        >
                    {coverage.name}
                  </span>
                                    ) : (
                                        <p className="text-(--gray-400) text-sm">
                                            {t("dashboard.profile.no-coverage")}
                                        </p>
                                    )}
                                </div>
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

                        <Button onClick={handleSave} className={actionButtonStyles + " w-3xs"} disabled={isSaving}>
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

export default PatientAccount;