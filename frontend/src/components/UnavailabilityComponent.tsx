import DashboardNavHeader from "@/components/DashboardNavHeader.tsx";
import {Button} from "@/components/ui/button.tsx";
import {ArrowRight, CalendarClock, Plus, X} from "lucide-react";
import DashboardNavLoader from "@/components/DashboardNavLoader.tsx";
import DashboardNavContainer from "@/components/DashboardNavContainer.tsx";
import {useTranslation} from "react-i18next";
import { useMemo, useState, useCallback} from "react";
import DashboardNavEmptyContent from "@/components/DashboardNavEmptyContent.tsx";
import {Card} from "@/components/ui/card.tsx";
import {
    Dialog, DialogClose,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle
} from "@/components/ui/dialog.tsx";
import {DatePicker} from "@/components/ui/date-picker.tsx";
import {formatLongDate, localDateToIso} from "@/utils/dateUtils.ts";
import {
    useDoctor,
    useDoctorUnavailability,
    useCreateDoctorUnavailabilityMutation,
    useDeleteDoctorUnavailabilityMutation
} from "@/hooks/useDoctors.ts";
import {useAuth} from "@/hooks/useAuth.ts";
import {Spinner} from "@/components/ui/spinner.tsx";
import {useSearchParams} from "react-router-dom";
import PaginationComponent from "@/components/PaginationComponent.tsx";
import {toast} from "sonner";
import {useDelayedBoolean} from "@/utils/queryUtils.ts";
import {DashboardRefetch} from "@/components/DashboardRefetch.tsx";

type UnavailabilityRange = {
    id?: number;
    startDate: string;
    endDate: string;
};

const getUiLocale = (lang?: string) => (lang?.startsWith("es") ? "es-AR" : "en-US");

const normalizeRange = (startIso: string, endIso: string): UnavailabilityRange => {
    return startIso <= endIso
        ? { startDate: startIso, endDate: endIso }
        : { startDate: endIso, endDate: startIso };
};

const rangeKey = (r: UnavailabilityRange) => `${r.id}_${r.startDate}_${r.endDate}`;

type UnavailabilityError = "range" | "overlap" | null;

const rangesOverlap = (a: UnavailabilityRange, b: UnavailabilityRange) => {
    return a.startDate <= b.endDate && b.startDate <= a.endDate;
};

const hasOverlap = (candidate: UnavailabilityRange, existing: UnavailabilityRange[]) => {
    return existing.some((r) => rangesOverlap(candidate, r));
};

const editButton =
    "mt-2 sm:mt-0 bg-transparent text-(--primary-color) hover:bg-(--primary-bg) cursor-pointer";
const unavailabilityContainer =
    "flex flex-col gap-6";
const unavailabilityEmptyContentContainer =
    "flex flex-col items-center gap-6";
const unavailabilityContentContainer =
    "flex flex-wrap gap-2 justify-start";
const dialogHeader =
    "font-bold text-xl text-[var(--text-color)]";
const dialogText =
    "text-[var(--text-light)] text-base font-normal leading-snug mt-1";
const dialogContent =
    "flex flex-col gap-4";
const dateFieldsGrid =
    "grid grid-cols-1 sm:grid-cols-2 gap-4";
const dateField =
    "flex flex-col gap-2 sm:mb-2 sm:mt-1";
const dateFieldLabel =
    "text-sm font-[500] text-(--text-color)";
const datePickerError =
    "border-[var(--danger)] ring-1 ring-[var(--danger)] bg-(--danger-lighter) hover:bg-(--danger-lighter)";
const dialogErrorText =
    "text-sm text-[var(--danger)] sm:-mt-2";
const dialogFooter =
    "mt-2";
const dialogCancel =
    "bg-white text-(--gray-600) border border-(--gray-400) hover:bg-(--gray-100) hover:border-(--gray-500-) hover:text-(--text-color) cursor-pointer transition-colors";
const dialogConfirm =
    "bg-(--primary-color) border border-(--primary-color) " +
    "hover:text-white hover:bg-(--primary-dark) hover:border-(--primary-dark) cursor-pointer";

export default function UnavailabilityComponent() {
    const { t, i18n } = useTranslation();
    const locale = getUiLocale(i18n.language);
    const auth = useAuth();
    const [searchParams, setSearchParams] = useSearchParams();

    const handleSetParams = useCallback((updater: (params: URLSearchParams) => void) => {
        setSearchParams((prev) => {
            const next = new URLSearchParams(prev);
            updater(next);
            return next;
        });
    }, [setSearchParams]);

    const page = Number(searchParams.get("page") ?? "1");
    const pageSize = 10;

    const { data: doctor, isLoading: doctorLoading } = useDoctor(auth.userId);

    const unavailabilityUrl = doctor?.unavailability;

    const {
        data: unavailabilityPage,
        isLoading: unavailabilityLoading,
        isError: unavailabilityError,
        refetch: unavailabilityRefetch,
        isFetching: unavailabilityFetching,
    } = useDoctorUnavailability(unavailabilityUrl, {
        page: page,
        pageSize: pageSize
    });

    const unavailabilities = useMemo(() => {
        return (unavailabilityPage?.data || []).map(u => ({
            id: u.id,
            startDate: u.startDate,
            endDate: u.endDate
        }));
    }, [unavailabilityPage]);

    const createUnavailability = useCreateDoctorUnavailabilityMutation(unavailabilityUrl ?? "");
    const deleteUnavailability = useDeleteDoctorUnavailabilityMutation(unavailabilityUrl ?? "");

    const [addOpen, setAddOpen] = useState(false);
    const [startDate, setStartDate] = useState<Date | undefined>(undefined);
    const [endDate, setEndDate] = useState<Date | undefined>(undefined);
    const futureDate = new Date();
    futureDate.setFullYear(futureDate.getFullYear() + 10);

    const isRangeInvalid =
        !!startDate &&
        !!endDate &&
        localDateToIso(endDate) < localDateToIso(startDate);

    const candidateRange =
        startDate && endDate
            ? normalizeRange(localDateToIso(startDate), localDateToIso(endDate))
            : null;

    const isOverlapInvalid =
        !!candidateRange && hasOverlap(candidateRange, unavailabilities);

    const dialogError: UnavailabilityError =
        isRangeInvalid ? "range" : isOverlapInvalid ? "overlap" : null;

    const showError = dialogError !== null;

    const today = useMemo(() => {
        const d = new Date();
        d.setHours(0, 0, 0, 0);
        return d;
    }, []);

    const isLoading = doctorLoading || unavailabilityLoading;
    const pending = createUnavailability.isPending || deleteUnavailability.isPending;
    const [pendingAction, setPendingAction] = useState<"add" | "delete" | null>(null);
    const isUnavailability = unavailabilities.length > 0;

    const onConfirmAdd = () => {
        if (!startDate || !endDate) return;
        if (!unavailabilityUrl) return;

        setPendingAction("add");

        const startIso = localDateToIso(startDate);
        const endIso = localDateToIso(endDate);
        const normalized = normalizeRange(startIso, endIso);

        if (endIso < startIso) return;

        setAddOpen(false);
        setStartDate(undefined);
        setEndDate(undefined);

        createUnavailability.mutate({
            startDate: normalized.startDate,
            endDate: normalized.endDate
        }, {
            onSuccess: () => {
                toast.success(t("unavailability.toast_saved"));
                setPendingAction(null);
            },
            onError: () => {
                toast.error(t("unavailability.toast_error"));
                setPendingAction(null);
            }
        });
    };

    const onDeleteRange = (range: UnavailabilityRange) => {
        if (!unavailabilityUrl) return;
        if (!range.id) return;

        setPendingAction("delete");

        deleteUnavailability.mutate(range.id, {
            onSuccess: () => {
                toast.success(t("unavailability.toast_deleted"));
                setPendingAction(null);
            },
            onError: () => {
                toast.error(t("unavailability.toast_deleted_error"));
                setPendingAction(null);
            }
        });
    };

    const delayedLoading = useDelayedBoolean(isLoading);

    if(unavailabilityError)
        return (
            <DashboardRefetch
                title={t("unavailability.title")}
                text={t("unavailability.error-loading")}
                isFetching={unavailabilityFetching}
                refetch={unavailabilityRefetch}
            />
        );

    return (
        <DashboardNavContainer>
            <DashboardNavHeader title={t("unavailability.title")}>
                <Button className={editButton} onClick={() => setAddOpen(true)}>
                    <Plus className="h-5 w-5" />
                    {t("unavailability.add")}
                </Button>
            </DashboardNavHeader>

            <Dialog
                open={addOpen}
                onOpenChange={(next) => {
                    setAddOpen(next);
                    if (!next) {
                        setStartDate(undefined);
                        setEndDate(undefined);
                    }
                }}
            >
                <DialogContent onOpenAutoFocus={(e) => e.preventDefault()}>
                    <DialogHeader className={dialogHeader}>
                        <DialogTitle>
                            {t("unavailability.dialog.title")}
                        </DialogTitle>
                        <DialogDescription className={dialogText}>
                            {t("unavailability.dialog.description")}
                        </DialogDescription>
                    </DialogHeader>
                    <div className={dialogContent}>
                        <div className={dateFieldsGrid}>
                            <div className={dateField}>
                                <p className={dateFieldLabel}>{t("unavailability.dialog.start")}</p>
                                <DatePicker
                                    value={startDate}
                                    onChange={(d) => setStartDate(d)}
                                    className={showError ? datePickerError : undefined}
                                    fromDate={today}
                                    toDate={futureDate}
                                />
                            </div>

                            <div className={dateField}>
                                <p className={dateFieldLabel}>{t("unavailability.dialog.end")}</p>
                                <DatePicker
                                    value={endDate}
                                    onChange={(d) => setEndDate(d)}
                                    className={showError ? datePickerError : undefined}
                                    fromDate={today}
                                    toDate={futureDate}
                                />
                            </div>
                        </div>

                        {showError && (
                            <p className={dialogErrorText}>
                                {dialogError === "range"
                                    ? t("unavailability.dialog.range-error")
                                    : t("unavailability.dialog.overlap-error")}
                            </p>
                        )}
                    </div>

                    <DialogFooter className={dialogFooter}>
                        <DialogClose asChild>
                            <Button type="button" className={dialogCancel}>
                                {t("cancel")}
                            </Button>
                        </DialogClose>

                        <Button
                            type="button"
                            className={dialogConfirm}
                            onClick={onConfirmAdd}
                            disabled={!startDate || !endDate || showError || !unavailabilityUrl || pending}
                        >
                            {pending && pendingAction === "add" ? (
                                <>
                                    <Spinner className="w-4 h-4 mr-2" />
                                    {t("saving")}
                                </>
                            ) : (
                                t("unavailability.add-unavailability")
                            )}
                        </Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>

            {delayedLoading ? (
                <DashboardNavLoader />
            ) : (
                <div className={unavailabilityContainer}>
                    {!isUnavailability ? (
                        <div className={unavailabilityEmptyContentContainer}>
                            <DashboardNavEmptyContent
                                title={t("unavailability.empty-title")}
                                text={t("unavailability.empty-subtitle")}
                                Icon={CalendarClock}
                            />
                        </div>
                    ) : (
                        <div className={unavailabilityContentContainer}>
                            {unavailabilities.map((r) => (
                                <UnavailabilityItem
                                    key={rangeKey(r)}
                                    range={r}
                                    locale={locale}
                                    pending={pending}
                                    pendingAction={pendingAction}
                                    onDelete={() => onDeleteRange(r)}
                                />
                            ))}
                        </div>
                    )}

                    <PaginationComponent
                        pagination={unavailabilityPage?.pagination}
                        searchParams={{ page, pageSize, setParams: handleSetParams }}
                    />
                </div>
            )}
        </DashboardNavContainer>
    );
}

const unavailabilityItemCard =
    "w-full sm:w-[240px] p-0 flex flex-row gap-0 items-start relative";
const unavailabilityItemContent =
    "flex flex-col py-2 px-3 pr-10";
const unavailabilityItemTitle =
    "text-sm font-medium";
const unavailabilityItemDate =
    "text-sm text-(--text-light)";
const unavailabilityItemDateRange =
    "flex flex-row items-center text-(--text-light) gap-1";
const unavailabilityItemDeleteButton =
    "cursor-pointer hover:bg-transparent hover:text-(--danger) transition-none h-6 w-6 p-0 absolute top-2 right-2";
const dialogDelete =
    "text-white bg-[var(--danger)] border border-[var(--danger)] hover:text-white hover:bg-[var(--danger-dark)] hover:border hover:border-[var(--danger-dark)] cursor-pointer";

function UnavailabilityItem({
                                range,
                                locale,
                                onDelete,
                                pending,
                                pendingAction,
                            }: {
    range: { id?: number; startDate: string; endDate: string };
    locale: string;
    onDelete: () => void;
    pending: boolean;
    pendingAction: "add" | "delete" | null;
}) {
    const { t } = useTranslation();
    const isSingleDate = range.startDate === range.endDate;

    const [deleteOpen, setDeleteOpen] = useState(false);

    const startLabel = formatLongDate(range.startDate, locale);
    const endLabel = formatLongDate(range.endDate, locale);

    const onConfirmDelete = () => {
        setDeleteOpen(false);
        onDelete();
    };

    return (
        <Card className={unavailabilityItemCard}>
            <div className={unavailabilityItemContent}>
                <h3 className={unavailabilityItemTitle}>
                    {isSingleDate ? t("unavailability.single") : t("unavailability.range")}
                </h3>

                {isSingleDate ? (
                    <p className={unavailabilityItemDate}>{startLabel}</p>
                ) : (
                    <div className={unavailabilityItemDateRange}>
                        <p className={unavailabilityItemDate}>{startLabel}</p>
                        <ArrowRight className="w-4 h-4" />
                        <p className={unavailabilityItemDate}>{endLabel}</p>
                    </div>
                )}
            </div>

            <Button
                type="button"
                variant="ghost"
                className={unavailabilityItemDeleteButton}
                onClick={() => setDeleteOpen(true)}
            >
                <X className="w-4 h-4" />
            </Button>

            <Dialog open={deleteOpen} onOpenChange={setDeleteOpen}>
                <DialogContent onOpenAutoFocus={(e) => e.preventDefault()}>
                    <DialogHeader className={dialogHeader}>
                        <DialogTitle>{t("unavailability.item-dialog.title")}</DialogTitle>
                        <DialogDescription className={dialogText}>
                            {t("unavailability.item-dialog.subtitle")}
                        </DialogDescription>
                    </DialogHeader>
                    <DialogFooter className={dialogFooter}>
                        <DialogClose asChild>
                            <Button type="button" className={dialogCancel}>
                                {t("cancel")}
                            </Button>
                        </DialogClose>
                        <Button
                            type="button"
                            className={dialogDelete}
                            onClick={onConfirmDelete}
                            disabled={pending}
                        >
                            {pending && pendingAction === "delete" ? (
                                <>
                                    <Spinner className="w-4 h-4" />
                                    {t("common.deleting")}
                                </>
                            ) : (
                                t("delete")
                            )}
                        </Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </Card>
    );
}