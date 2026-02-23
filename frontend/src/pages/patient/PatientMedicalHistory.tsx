import {useTranslation} from "react-i18next";
import PastAppointmentComponent from "@/components/PastAppointmentComponent.tsx";
import DashboardNavContainer from "@/components/DashboardNavContainer.tsx";
import DashboardNavHeader from "@/components/DashboardNavHeader.tsx";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.tsx";
import DashboardNavLoader from "@/components/DashboardNavLoader.tsx";
import {useAppointments} from "@/hooks/useAppointments.ts";
import {useAppointmentsQueryParams} from "@/hooks/useQueryParams.ts";
import DashboardNavEmptyContent from "@/components/DashboardNavEmptyContent.tsx";
import {CalendarFoldIcon} from "lucide-react";
import PaginationComponent from "@/components/PaginationComponent.tsx";
import {useDelayedBoolean} from "@/utils/queryUtils.ts";
import {DashboardRefetch} from "@/components/DashboardRefetch.tsx";

const headerContainer = "flex items-center sm:justify-center gap-2 mt-2 sm:mt-0";
const sortTitle = "flex font-normal text-sm items-center justify-center text-(--text-light)";
const selectTrigger =
    "font-light text-normal cursor-pointer select-none " +
    "focus:outline-none focus:ring-0 focus-visible:ring-0 focus-visible:ring-offset-0";

function PatientMedicalHistory() {
    const { t } = useTranslation();
    const searchParams = useAppointmentsQueryParams();
    const sort: "asc" | "desc" = searchParams.sort === "desc" ? "desc" : "asc";

    const { data: appointments, isLoading, isError, refetch, isFetching } = useAppointments({
        collection: "history",
        filter: "completed",
        sort: sort,
        page: searchParams.page,
        pageSize: searchParams.pageSize,
    });

    const completed = (appointments?.data ?? []).filter(a => a.status === "completo");

    const delayedLoading = useDelayedBoolean(isLoading);

    if (isError)
        return (
            <DashboardRefetch
                title={t("medical-history.title")}
                text={t("medical-history.error")}
                isFetching={isFetching}
                refetch={refetch}
            />
        );

    return (
        <DashboardNavContainer>
            <DashboardNavHeader title={t("medical-history.title")}>
                <div className={headerContainer}>
                    <span className={sortTitle}>
                        {t("medical-history.sort.title")}
                    </span>
                    <Select
                        value={sort}
                        onValueChange={(v) => {
                            const next = v as "asc" | "desc";
                            searchParams.setParams((p) => {
                                p.set("sort", next);
                                p.set("page", "1");
                            });
                        }}
                    >
                        <SelectTrigger className={selectTrigger}>
                            <SelectValue />
                        </SelectTrigger>
                        <SelectContent>
                            <SelectItem value="asc">{t("medical-history.sort.old-first")}</SelectItem>
                            <SelectItem value="desc">{t("medical-history.sort.new-first")}</SelectItem>
                        </SelectContent>
                    </Select>
                </div>
            </DashboardNavHeader>
            {delayedLoading ? (
                <DashboardNavLoader />
            ) : completed.length > 0 ? (
                completed.map((a) => (
                    <PastAppointmentComponent key={a.self} appointment={a} />
                ))
            ) : (
                <DashboardNavEmptyContent
                    Icon={CalendarFoldIcon}
                    title={t("appointment.empty.title.past")}
                    text={t("appointment.empty.text.past")}
                />
            )}

            {!delayedLoading && completed.length > 0 && appointments?.pagination && (
                <PaginationComponent pagination={appointments.pagination} searchParams={searchParams} />
            )}
        </DashboardNavContainer>
    );
}

export default PatientMedicalHistory;