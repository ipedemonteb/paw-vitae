import DashboardNavContainer from "@/components/DashboardNavContainer.tsx";
import DashboardNavHeader from "@/components/DashboardNavHeader.tsx";
import {CalendarFoldIcon} from "lucide-react";
import AppointmentCard from "@/components/AppointmentCard.tsx";
import {appointmentStatus, appointmentUpcomingFilters} from "@/lib/constants.ts";
import {useTranslation} from "react-i18next";
import {useAppointments} from "@/hooks/useAppointments.ts";
import DashboardNavLoader from "@/components/DashboardNavLoader.tsx";
import DashboardNavEmptyContent from "@/components/DashboardNavEmptyContent.tsx";
import PaginationComponent from "@/components/PaginationComponent.tsx";
import {useAppointmentsQueryParams} from "@/hooks/useQueryParams.ts";
import {useEffect, useState} from "react";
import DashboardNavSelect from "@/components/DashboardNavSelect.tsx";
import {useDelayedBoolean} from "@/utils/queryUtils.ts";
import {DashboardRefetch} from "@/components/DashboardRefetch.tsx";

const typeDictionary = {
    history: {
        title: "appointment.history",
        popoverData: appointmentStatus.filter(s => s !== "confirmed"),
        filterMessage: "appointment.history_filter_message",
        emptyTitle: "appointment.empty.title.past",
        emptyText: "appointment.empty.text.past",
        loadingText: "appointment.loading.past"

    },
    upcoming: {
        title: "appointment.upcoming",
        popoverData: appointmentUpcomingFilters,
        filterMessage: "appointment.upcoming_filter_message",
        emptyTitle: "appointment.empty.title.upcoming",
        emptyText: "appointment.empty.text.upcoming",
        loadingText: "appointment.loading.upcoming"
    }
}

type AppointmentComponentProps = {
    type: "history" | "upcoming"
}

export default function AppointmentComponent({type}: AppointmentComponentProps) {
    const {t} = useTranslation();
    const componentType = typeDictionary[type]
    const [mounted, setMounted] = useState(false);

    const transformFilter = (filterValue: string) => {
        if (!componentType.popoverData.includes(filterValue)) return "appointment.filters.all"
        return "appointment.filters." + filterValue
    }

    const searchParams = useAppointmentsQueryParams();
    const filterValue = searchParams.filter;

    useEffect(() => {
        setMounted(false);
        const id = requestAnimationFrame(() => setMounted(true));
        return () => cancelAnimationFrame(id);
    }, [type]);

    const setFilter = (newFilter: string) => {
        searchParams.setParams((p) => {
            if (newFilter === "all") p.delete("filter");
            else p.set("filter", newFilter);
            p.set("page", "1");
        });
    };

    const { data: appointments, isLoading, isError, refetch, isFetching} = useAppointments({
        collection: type,
        page: searchParams.page,
        pageSize: searchParams.pageSize,
        filter: searchParams.filter,
        sort: type === "history" ? "desc" : "asc",
    });

    const delayedLoading = useDelayedBoolean(isLoading);

    if (isError)
        return (
            <DashboardRefetch
                title={t(componentType.title)}
                text={t("appointment.error")}
                isFetching={isFetching}
                refetch={refetch}
            />
        );

    return (
        <DashboardNavContainer>
            <DashboardNavHeader title={t(componentType.title)}>
                <div className="flex items-center sm:justify-center gap-2 mt-2 sm:mt-0">
                    <span className="flex font-normal text-sm items-center justify-center text-(--text-light)">
                        {t(componentType.filterMessage)}:
                    </span>
                    <DashboardNavSelect all={t("appointment.filters.all")} value={filterValue} onValueChange={(val) => setFilter(val)} content={componentType.popoverData} display={(s:string) => t(transformFilter(s))}/>
                </div>
            </DashboardNavHeader>
            {(delayedLoading) ? (
                <DashboardNavLoader/>
            ) : (appointments?.data !== undefined && appointments.data.length > 0) ? (
                appointments.data.map((a, i) => (
                    <AppointmentCard animationDelay={i} mounted={mounted} key={a.self} appointment={a} isUpcoming={type === "upcoming"}/>
                ))

            ) : (
                <DashboardNavEmptyContent Icon={CalendarFoldIcon} title={t(componentType.emptyTitle)} text={t(componentType.emptyText)}/>
            )}
            {!delayedLoading && appointments?.data !== undefined && appointments?.data?.length > 0 && appointments?.pagination && (
                <PaginationComponent pagination={appointments.pagination} searchParams={searchParams} />
            )}
        </DashboardNavContainer>
    );
}