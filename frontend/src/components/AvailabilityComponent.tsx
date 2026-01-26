import React, { useMemo, useState } from "react";
import DashboardNavContainer from "@/components/DashboardNavContainer.tsx";
import DashboardNavHeader from "@/components/DashboardNavHeader.tsx";
import { useTranslation } from "react-i18next";
import { Button } from "../components/ui/button.tsx";
import {
    CalendarClock,
    Plus,
    AlertTriangle,
    Trash2,
    Pencil,
    X,
    Save,
} from "lucide-react";
import DashboardNavEmptyContent from "@/components/DashboardNavEmptyContent.tsx";
import DashboardNavLoader from "@/components/DashboardNavLoader.tsx";
import { Card } from "@/components/ui/card.tsx";
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select.tsx";
import { Spinner } from "@/components/ui/spinner.tsx";

const ghostFilter = "h-0 sm:h-9";
const editButton =
    "mt-2 sm:mt-0 bg-transparent text-(--primary-color) hover:bg-(--primary-bg) cursor-pointer";
const availabilityContainer = "flex flex-col";
const warningContainer =
    "flex items-start gap-3 mb-6 rounded-lg bg-(--primary-bg) p-4 border border-(--primary-color) animate-in fade-in slide-in-from-top-2";
const warningIcon = "h-5 w-5 text-(--primary-dark)";
const warningRightContent = "flex flex-col flex-1 leading-tight";
const warningTitle = "text-sm font-medium text-(--primary-dark)";
const warningText = "mt-1 text-sm text-(--primary-dark)";
const addAvailabilityButton =
    "w-full max-w-3xs text-white bg-(--primary-color) hover:bg-(--primary-dark) cursor-pointer";
const availabilityContentContainer = "flex flex-col gap-6";
const availabilityItems = "flex flex-col items-center gap-4";
const newAvailabilityItem =
    "w-3xs flex flex-row justify-center items-center gap-2 p-4 bg-(--gray-100) text-(--gray-600) border border-(--gray-400) border-dashed rounded-xl hover:bg-(--gray-200) hover:border-(--gray-500) cursor-pointer";
const availabilityButtonsContainer = "flex justify-end gap-2";
const cancelButton =
    "cursor-pointer border border-(--primary-color) text-(--primary-color) hover:text-white hover:bg-(--primary-dark) hover:border-(--primary-dark)";
const saveButton =
    "bg-(--primary-color) text-white hover:bg-(--primary-dark) cursor-pointer";

type AvailabilitySlot = {
    id: number;
    office: string;
    day: string;
    start: string;
    end: string;
};

const initialOffices: AvailabilitySlot[] = [
    { id: 1, office: "Office 1", day: "Tuesday", start: "10:00", end: "11:00" },
    { id: 2, office: "Office 2", day: "Wednesday", start: "12:00", end: "13:00" },
    { id: 3, office: "Office 1", day: "Tuesday", start: "13:00", end: "18:00" },
];

const officeOptions = ["Office 1", "Office 2", "Office 3"];
const dayOptions = [
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday",
    "Sunday",
];
const timeOptions = [
    "10:00",
    "11:00",
    "12:00",
    "13:00",
    "14:00",
    "15:00",
    "16:00",
    "17:00",
    "18:00",
];

export default function AvailabilityComponent() {
    const { t } = useTranslation();

    const isLoading = false;
    const [isEditing, setIsEditing] = useState(false);
    const [isSaving] = useState(false);

    const initialSnapshot = useMemo<AvailabilitySlot[]>(
        () => initialOffices.map((s) => ({ ...s })),
        []
    );

    const [slots, setSlots] = useState<AvailabilitySlot[]>(initialSnapshot);
    const isAvailability = slots.length > 0;

    const handleEdit = () => {
        setIsEditing(true);
    };

    const handleCancel = () => {
        setSlots(initialSnapshot.map((s) => ({ ...s })));
        setIsEditing(false);
    };

    const handleSave = () => {};

    const handleAdd = () => {
        const newId = Date.now();
        setSlots((prev) => [
            ...prev,
            { id: newId, office: "", day: "", start: "", end: "" },
        ]);
    };

    const handleDelete = (id: number) => {
        setSlots((prev) => prev.filter((s) => s.id !== id));
    };

    const handleUpdate = (id: number, patch: Partial<AvailabilitySlot>) => {
        setSlots((prev) =>
            prev.map((s) => (s.id === id ? { ...s, ...patch } : s))
        );
    };

    return (
        <DashboardNavContainer>
            <DashboardNavHeader title={t("availability.headerTitle")}>
                {!isEditing ? (
                    <Button className={editButton} onClick={handleEdit}>
                        <Pencil className="h-5 w-5" />
                        Edit
                    </Button>
                ) : (
                    <div className={ghostFilter} />
                )}
            </DashboardNavHeader>

            {isLoading ? (
                <DashboardNavLoader />
            ) : (
                <div className={availabilityContainer}>
                    <div className={warningContainer}>
                        <AlertTriangle className={warningIcon} aria-hidden="true" />
                        <div className={warningRightContent}>
                            <h3 className={warningTitle}>{t("availability.warning_title")}</h3>
                            <p className={warningText}>{t("availability.warning_text")}</p>
                        </div>
                    </div>

                    {!isAvailability ? (
                        <div className={availabilityContentContainer}>
                            <DashboardNavEmptyContent
                                title={t("availability.emptyContent")}
                                text={t("availability.emptyContentText")}
                                Icon={CalendarClock}
                            />
                            {isEditing ? (
                                <Button className={addAvailabilityButton} onClick={handleAdd}>
                                    <Plus className="h-5 w-5" />
                                    {t("availability.addSchedule")}
                                </Button>
                            ) : null}
                        </div>
                    ) : (
                        <div className={availabilityContentContainer}>
                            <div className={availabilityItems}>
                                {slots.map((slot) => (
                                    <AvailabilityItem
                                        key={slot.id}
                                        slot={slot}
                                        isEditing={isEditing}
                                        onDelete={() => handleDelete(slot.id)}
                                        onChange={(patch) => handleUpdate(slot.id, patch)}
                                    />
                                ))}

                                {isEditing ? (
                                    <Button className={newAvailabilityItem} onClick={handleAdd}>
                                        <Plus className="h-5 w-5" />
                                        <p>Add Availability</p>
                                    </Button>
                                ) : null}
                            </div>

                            {isEditing ? (
                                <div className={availabilityButtonsContainer}>
                                    <Button
                                        variant="outline"
                                        className={cancelButton}
                                        onClick={handleCancel}
                                    >
                                        <X className="w-4 h-4" />
                                        {t("cancel")}
                                    </Button>
                                    <Button
                                        className={saveButton}
                                        disabled={isSaving}
                                        onClick={handleSave}
                                    >
                                        {isSaving ? (
                                            <>
                                                <Spinner className="w-4 h-4 mr-2" />
                                                {t("saving")}
                                            </>
                                        ) : (
                                            <>
                                                <Save className="w-4 h-4" />
                                                {t("save")}
                                            </>
                                        )}
                                    </Button>
                                </div>
                            ) : null}
                        </div>
                    )}
                </div>
            )}
        </DashboardNavContainer>
    );
}

const availabilityItemCard = "w-full p-6";
const slotsContainer = "flex flex-col md:items-center gap-4 md:flex-row md:flex-nowrap";
const fieldBlock = "flex flex-col gap-2 min-w-0 md:w-56 md:flex-1";
const fieldLabel = "text-sm font-medium text-[var(--text-color)]";
const selectFixed = "w-full";
const deleteItemButton =
    "text-(--danger) hover:text-white hover:bg-(--danger-dark) rounded-full cursor-pointer";

function AvailabilityItem({
                              slot,
                              isEditing,
                              onDelete,
                              onChange,
                          }: {
    slot: AvailabilitySlot;
    isEditing: boolean;
    onDelete: () => void;
    onChange: (patch: Partial<AvailabilitySlot>) => void;
}) {
    return (
        <Card className={availabilityItemCard}>
            <div className={slotsContainer}>
                <div className={fieldBlock}>
                    <h3 className={fieldLabel}>Office</h3>
                    <Select
                        value={slot.office || undefined}
                        onValueChange={(v) => onChange({ office: v })}
                        disabled={!isEditing}
                    >
                        <SelectTrigger className={selectFixed}>
                            <SelectValue placeholder="Select Office" />
                        </SelectTrigger>
                        <SelectContent position="popper">
                            {officeOptions.map((o) => (
                                <SelectItem key={o} value={o}>
                                    {o}
                                </SelectItem>
                            ))}
                        </SelectContent>
                    </Select>
                </div>

                <div className={fieldBlock}>
                    <h3 className={fieldLabel}>Day of Week</h3>
                    <Select
                        value={slot.day || undefined}
                        onValueChange={(v) => onChange({ day: v })}
                        disabled={!isEditing}
                    >
                        <SelectTrigger className={selectFixed}>
                            <SelectValue placeholder="Select Day" />
                        </SelectTrigger>
                        <SelectContent position="popper">
                            {dayOptions.map((d) => (
                                <SelectItem key={d} value={d}>
                                    {d}
                                </SelectItem>
                            ))}
                        </SelectContent>
                    </Select>
                </div>

                <div className={fieldBlock}>
                    <h3 className={fieldLabel}>Start Time</h3>
                    <Select
                        value={slot.start || undefined}
                        onValueChange={(v) => onChange({ start: v })}
                        disabled={!isEditing}
                    >
                        <SelectTrigger className={selectFixed}>
                            <SelectValue placeholder="Select Time" />
                        </SelectTrigger>
                        <SelectContent position="popper">
                            {timeOptions.map((t) => (
                                <SelectItem key={t} value={t}>
                                    {t}
                                </SelectItem>
                            ))}
                        </SelectContent>
                    </Select>
                </div>

                <div className={fieldBlock}>
                    <h3 className={fieldLabel}>End Time</h3>
                    <Select
                        value={slot.end || undefined}
                        onValueChange={(v) => onChange({ end: v })}
                        disabled={!isEditing}
                    >
                        <SelectTrigger className={selectFixed}>
                            <SelectValue placeholder="Select Time" />
                        </SelectTrigger>
                        <SelectContent position="popper">
                            {timeOptions.map((t) => (
                                <SelectItem key={t} value={t}>
                                    {t}
                                </SelectItem>
                            ))}
                        </SelectContent>
                    </Select>
                </div>

                {isEditing ? (
                    <Button variant="ghost" className={deleteItemButton} onClick={onDelete}>
                        <Trash2 className="h-5 w-5" />
                    </Button>
                ) : null}
            </div>
        </Card>
    );
}
