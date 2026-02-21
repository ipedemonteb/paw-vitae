import { useEffect, useMemo, useState } from "react";
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
import {
    DB_DAY_ORDER, ensureSeconds, labelFromDbDay,
    normalizeDayIndex,
    normalizeTime,
    timeOptions,
    timeToMinutes
} from "@/utils/dateUtils.ts";
import { useAuth } from "@/hooks/useAuth.ts";
import { useDoctor } from "@/hooks/useDoctors.ts";
import {
    useDoctorAvailability,
    useDoctorOffices, useUpdateDoctorAvailabilityMutation,
} from "@/hooks/useOffices.ts";
import {type DoctorAvailabilityFormDTO, type OfficeDTO} from "@/data/offices.ts";
import type { AvailabilityDTO } from "@/data/offices.ts"
import { officeIdFromSelf } from "@/utils/IdUtils.ts";
import {toast} from "sonner";
import {useDelayedBoolean} from "@/utils/queryUtils.ts";
import {DashboardRefetch} from "@/components/DashboardRefetch.tsx";

type AvailabilitySlot = {
    id: string;
    officeId: string;
    day: number | null;
    start: string;
    end: string;
};

function pickSlotFields(slot: any) {
    const day = slot?.day ?? slot?.dayOfWeek ?? slot?.weekday ?? slot?.weekDay;
    const start = slot?.start ?? slot?.startTime ?? slot?.start_time;
    const end = slot?.end ?? slot?.endTime ?? slot?.end_time;
    const officeUrl = slot?.office ?? slot?.officeSelf ?? slot?.office_url;
    return { day, start, end, officeUrl };
}

function computeOverlapIds(slots: AvailabilitySlot[]) {
    const overlap = new Set<string>();


    const groups = new Map<string, AvailabilitySlot[]>();

    for (const s of slots) {

        if (s.day === null || !s.start || !s.end) continue;

        const key = `${s.day}`;

        const arr = groups.get(key) ?? [];
        arr.push(s);
        groups.set(key, arr);
    }

    for (const arr of groups.values()) {
        arr.sort((a, b) => timeToMinutes(a.start) - timeToMinutes(b.start));


        for (let i = 0; i < arr.length; i++) {
            const current = arr[i];
            const currentEnd = timeToMinutes(current.end);

            for (let j = i + 1; j < arr.length; j++) {
                const next = arr[j];
                const nextStart = timeToMinutes(next.start);


                if (nextStart >= currentEnd) {
                    break;
                }

                overlap.add(current.id);
                overlap.add(next.id);

            }
        }
    }

    return overlap;
}

function buildOfficeIndex(offices?: OfficeDTO[] | null) {
    const byId = new Map<string, OfficeDTO>();
    if (!offices) return byId;

    for (const office of offices) {
        const id = officeIdFromSelf(office.self);
        if (!id) continue;
        byId.set(id, office);
    }
    return byId;
}

function buildSlotsFromApi(
    offices?: OfficeDTO[] | null,
    allAvailability?: AvailabilityDTO[] | null
): AvailabilitySlot[] {
    if (!offices || !allAvailability) return [];

    const officeById = buildOfficeIndex(offices);
    const result: AvailabilitySlot[] = [];
    for (const raw of allAvailability) {
        const { day, start, end, officeUrl } = pickSlotFields(raw);

        const officeId = officeIdFromSelf(officeUrl);
        if (!officeId) continue;
        if (!officeById.has(officeId)) continue;

        const normDay = normalizeDayIndex(day);
        if (normDay === null) continue;
        const normStart = normalizeTime(start);
        const normEnd = normalizeTime(end);

        const id = `${officeId}|${normDay}|${normStart}|${normEnd}`;

        result.push({
            id,
            officeId,
            day: normDay,
            start: normStart,
            end: normEnd,
        });
    }

    return result;
}

const ghostFilter =
    "h-0 sm:h-9";
const editButton =
    "mt-2 sm:mt-0 bg-transparent text-(--primary-color) hover:bg-(--primary-bg) cursor-pointer";
const availabilityContainer =
    "flex flex-col gap-0";
const warningContainer =
    "flex items-start gap-3 mb-6 rounded-lg bg-(--primary-bg) p-4 border border-(--primary-color) animate-in fade-in slide-in-from-top-2";
const warningIcon =
    "h-5 w-5 text-(--primary-dark)";
const warningRightContent =
    "flex flex-col flex-1 leading-tight";
const warningTitle =
    "text-sm font-medium text-(--primary-dark)";
const warningText =
    "mt-1 text-sm text-(--primary-dark)";
const addAvailabilityButton =
    "w-full max-w-3xs text-white bg-(--primary-color) hover:bg-(--primary-dark) cursor-pointer";
const availabilityContentContainer =
    "flex flex-col gap-6";
const availabilityEmptyContentContainer =
    "flex flex-col items-center gap-6";
const availabilityItems =
    "flex flex-col items-center gap-4";
const newAvailabilityItem =
    "mt-2 w-3xs flex flex-row justify-center items-center gap-2 p-4 bg-transparent text-(--primary-color) border border-(--primary-color) border-dashed rounded-xl hover:bg-(--primary-bg) cursor-pointer";
const availabilityButtonsContainer =
    "flex justify-center md:justify-end gap-2";
const cancelButton =
    "cursor-pointer border border-(--primary-color) text-(--primary-color) hover:text-white hover:bg-(--primary-dark) hover:border-(--primary-dark)";
const saveButton =
    "bg-(--primary-color) text-white hover:bg-(--primary-dark) cursor-pointer";

export default function AvailabilityComponent() {
    const { t } = useTranslation();
    const auth = useAuth();

    const doctorQuery = useDoctor(auth.userId);
    const officesQuery = useDoctorOffices(doctorQuery.data?.offices);
    const availabilityQuery = useDoctorAvailability(auth.userId);

    const isLoading = doctorQuery?.isLoading || officesQuery?.isLoading || availabilityQuery?.isLoading;
    const isError = doctorQuery?.isError || officesQuery?.isError || availabilityQuery?.isError;
    const isFetching =
        doctorQuery.isFetching || officesQuery.isFetching || availabilityQuery.isFetching;
    const handleRefetchAll = async () => {
        await Promise.all([
            doctorQuery.refetch(),
            officesQuery.refetch(),
            availabilityQuery.refetch(),
        ]);
    };

    const officeOptions = useMemo(() => {
        const offices = officesQuery.data ?? [];
        return offices
            .map((o) => {
                const id = officeIdFromSelf(o.self);
                if (!id) return null;
                return { id, name: o.name };
            })
            .filter(Boolean) as Array<{ id: string; name: string }>;
    }, [officesQuery.data]);

    const slotsFromApi = useMemo(
        () => buildSlotsFromApi(officesQuery.data, availabilityQuery.data),
        [officesQuery.data, availabilityQuery.data]
    );

    const [isEditing, setIsEditing] = useState(false);
    const [isSaving, setIsSaving] = useState(false);

    const [slots, setSlots] = useState<AvailabilitySlot[]>([]);
    const [snapshot, setSnapshot] = useState<AvailabilitySlot[]>([]);

    const syncKey = useMemo(
        () => slotsFromApi.map((s) => s.id).join("||"),
        [slotsFromApi]
    );

    useEffect(() => {
        if (isEditing) return;

        const next = slotsFromApi.map((s) => ({ ...s }));

        setSlots(next);
        setSnapshot(next);
    }, [syncKey, isEditing]);

    const isAvailability = slots.length > 0;
    const overlapIds = useMemo(() => computeOverlapIds(slots), [slots]);

    const hasTimeOrderError = useMemo(() => {
        for (const s of slots) {
            if (!s.start || !s.end) continue;
            if (timeToMinutes(s.end) <= timeToMinutes(s.start)) return true;
        }
        return false;
    }, [slots]);

    const hasIncompleteSlot = useMemo(() => {
        for (const s of slots) {
            if (!s.officeId) return true;
            if (s.day === null) return true;
            if (!s.start) return true;
            if (!s.end) return true;
        }
        return false;
    }, [slots]);

    const canSave = isEditing && !isSaving && !hasIncompleteSlot && overlapIds.size === 0 && !hasTimeOrderError;

    const handleEdit = () => setIsEditing(true);

    const handleCancel = () => {
        setSlots(snapshot.map((s) => ({ ...s })));
        setIsEditing(false);
    };

    const putAvailability = useUpdateDoctorAvailabilityMutation(auth.userId);

    const handleSave = async () => {
        if (isSaving) return;
        if (!auth.userId) return;
        if (overlapIds.size > 0) return;
        for (const s of slots) {
            if (!s.officeId || s.day === null || !s.start || !s.end) return;

            const startMin = timeToMinutes(s.start);
            const endMin = timeToMinutes(s.end);

            if (startMin >= endMin) return;
        }

        const form: DoctorAvailabilityFormDTO = {
            doctorOfficeAvailabilities: slots.map((s) => {
                const officeIdNum = Number(s.officeId);

                if (!Number.isFinite(officeIdNum)) {
                    throw new Error();
                }
                if (s.day === null) {
                    throw new Error();
                }

                return {
                    officeId: officeIdNum,
                    dayOfWeek: s.day,
                    startTime: ensureSeconds(s.start),
                    endTime: ensureSeconds(s.end),
                };
            }),
        };

        try {
            setIsSaving(true);
            await putAvailability.mutateAsync(form);
            toast.success(t("availability.toast_saved"));
            setIsEditing(false);
            setSnapshot(slots.map((x) => ({ ...x })));
        } catch {
            toast.error(t("availability.toast_error"));
        }finally {
            setIsSaving(false);
        }
    };

    const handleAdd = () => {
        const newId = `new-${Date.now()}`;
        setSlots((prev) => [
            ...prev,
            { id: newId, officeId: "", day: null, start: "", end: "" },
        ]);
    };

    const handleDelete = (id: string) => {
        setSlots((prev) => prev.filter((s) => s.id !== id));
    };

    const handleUpdate = (id: string, patch: Partial<AvailabilitySlot>) => {
        setSlots((prev) => prev.map((s) => (s.id === id ? { ...s, ...patch } : s)));
    };

    const delayedLoading = useDelayedBoolean(isLoading);

    if(isError)
        return (
            <DashboardRefetch
                title={t("availability.headerTitle")}
                text={t("availability.error-loading")}
                isFetching={isFetching}
                refetch={handleRefetchAll}
            />
        );

    return (
        <DashboardNavContainer>
            <DashboardNavHeader title={t("availability.headerTitle")}>
                {!isEditing ? (
                    <Button className={editButton} onClick={handleEdit}>
                        <Pencil className="h-5 w-5" />
                        {t("edit")}
                    </Button>
                ) : (
                    <div className={ghostFilter} />
                )}
            </DashboardNavHeader>

            {delayedLoading ? (
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
                    <div className={availabilityContentContainer}>
                        {!isAvailability ? (
                            <div className={availabilityEmptyContentContainer}>
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
                            <div className={availabilityItems}>
                                {slots.map((slot) => (
                                    <AvailabilityItem
                                        key={slot.id}
                                        slot={slot}
                                        isEditing={isEditing}
                                        hasOverlap={overlapIds.has(slot.id)}
                                        onDelete={() => handleDelete(slot.id)}
                                        onChange={(patch) => handleUpdate(slot.id, patch)}
                                        officeOptions={officeOptions}
                                    />
                                ))}

                                {isEditing ? (
                                    <Button className={newAvailabilityItem} onClick={handleAdd}>
                                        <Plus className="h-5 w-5" />
                                        <p>{t("availability.addSchedule")}</p>
                                    </Button>
                                ) : null}
                            </div>
                        )}

                        {isEditing ? (
                            <div className={availabilityButtonsContainer}>
                                <Button variant="outline" className={cancelButton} onClick={handleCancel}>
                                    <X className="w-4 h-4" />
                                    {t("cancel")}
                                </Button>
                                <Button className={saveButton} disabled={!canSave} onClick={handleSave}>
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
                        ) : (
                            <div />
                        )}
                    </div>
                </div>
            )}
        </DashboardNavContainer>
    );
}

const availabilityItemCard =
    "w-full p-6";
const availabilityItemOverlap =
    "bg-(--danger-lighter) border border-(--danger)";
const slotsContainer =
    "flex flex-col md:items-center gap-4 md:flex-row md:flex-nowrap";
const fieldBlock =
    "flex flex-col gap-2 min-w-0 md:w-56 md:flex-1";
const fieldLabel =
    "text-sm font-medium text-(--text-color)";
const selectFixed =
    "w-full";
const deleteItemButton =
    "text-(--danger) hover:text-white hover:bg-(--danger-dark) rounded-full cursor-pointer";
const overlapText =
    "text-sm text-(--danger) font-medium";
const invalidSelectTrigger =
    "border-(--danger) focus:ring-(--danger) focus:ring-2 focus:ring-offset-0";
const invalidText =
    "text-sm text-(--danger) font-medium mt-2";

function AvailabilityItem({
                              slot,
                              isEditing,
                              hasOverlap,
                              onDelete,
                              onChange,
                              officeOptions,
                          }: {
    slot: AvailabilitySlot;
    isEditing: boolean;
    hasOverlap: boolean;
    onDelete: () => void;
    onChange: (patch: Partial<AvailabilitySlot>) => void;
    officeOptions: Array<{ id: string; name: string }>;
}) {
    const { t } = useTranslation();

    const hasStart = !!slot.start;
    const hasEnd = !!slot.end;

    const isTimeOrderInvalid = useMemo(() => {
        if (!hasStart || !hasEnd) return false;
        return timeToMinutes(slot.end) <= timeToMinutes(slot.start);
    }, [hasStart, hasEnd, slot.start, slot.end]);

    const showTimeError = isEditing && isTimeOrderInvalid;

    return (
        <Card className={`${availabilityItemCard} ${hasOverlap ? availabilityItemOverlap : ""} ${showTimeError ? availabilityItemOverlap : ""}`}>
            <div className={slotsContainer}>
                <div className={fieldBlock}>
                    <h3 className={fieldLabel}>{t("availability.office")}</h3>
                    <Select
                        value={slot.officeId || ""}
                        onValueChange={(v) => onChange({ officeId: v })}
                        disabled={!isEditing}
                    >
                        <SelectTrigger className={selectFixed}>
                            <SelectValue placeholder={t("availability.select.office")} />
                        </SelectTrigger>
                        <SelectContent position="popper">
                            {officeOptions.map((o) => (
                                <SelectItem key={o.id} value={o.id}>
                                    {o.name}
                                </SelectItem>
                            ))}
                        </SelectContent>
                    </Select>
                </div>

                <div className={fieldBlock}>
                    <h3 className={fieldLabel}>{t("availability.dayOfWeek")}</h3>
                    <Select
                        value={slot.day === null ? "" : String(slot.day)}
                        onValueChange={(v) => onChange({ day: Number(v) })}
                        disabled={!isEditing}
                    >
                        <SelectTrigger className={selectFixed}>
                            <SelectValue placeholder={t("availability.select.day")} />
                        </SelectTrigger>
                        <SelectContent position="popper">
                            {DB_DAY_ORDER.map((dbDay) => (
                                <SelectItem key={dbDay} value={String(dbDay)}>
                                    {t(`days.${labelFromDbDay(dbDay)}`)}
                                </SelectItem>
                            ))}
                        </SelectContent>
                    </Select>
                </div>

                <div className={fieldBlock}>
                    <h3 className={fieldLabel}>{t("availability.startTime")}</h3>
                    <Select
                        value={slot.start || ""}
                        onValueChange={(v) => onChange({ start: v })}
                        disabled={!isEditing}
                    >
                        <SelectTrigger className={`${selectFixed} ${showTimeError ? invalidSelectTrigger : ""}`}>
                            <SelectValue placeholder={t("availability.select.time")} />
                        </SelectTrigger>
                        <SelectContent position="popper">
                            {timeOptions.map((tt) => (
                                <SelectItem key={tt} value={tt}>
                                    {tt}
                                </SelectItem>
                            ))}
                        </SelectContent>
                    </Select>
                </div>

                <div className={fieldBlock}>
                    <h3 className={fieldLabel}>{t("availability.endTime")}</h3>
                    <Select
                        value={slot.end || ""}
                        onValueChange={(v) => onChange({ end: v })}
                        disabled={!isEditing}
                    >
                        <SelectTrigger className={`${selectFixed} ${showTimeError ? invalidSelectTrigger : ""}`}>
                            <SelectValue placeholder={t("availability.select.time")} />
                        </SelectTrigger>
                        <SelectContent position="popper">
                            {timeOptions.map((tt) => (
                                <SelectItem key={tt} value={tt}>
                                    {tt}
                                </SelectItem>
                            ))}
                        </SelectContent>
                    </Select>
                </div>

                {isEditing ? (
                    <Button type="button" variant="ghost" className={deleteItemButton} onClick={onDelete}>
                        <Trash2 className="h-5 w-5" />
                    </Button>
                ) : null}
            </div>

            {hasOverlap ? <p className={overlapText}>{t("availability.overlap")}</p> : null}

            {showTimeError ? (
                <p className={invalidText}>{t("availability.errorEndBeforeStart")}</p>
            ) : null}
        </Card>
    );
}

