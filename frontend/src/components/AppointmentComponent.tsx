import DashboardNavContainer from "@/components/DashboardNavContainer.tsx";
import DashboardNavHeader from "@/components/DashboardNavHeader.tsx";
import {CalendarFoldIcon} from "lucide-react";
import AppointmentCard from "@/components/AppointmentCard.tsx";
import {appointmentStatus, appointmentUpcomingFilters} from "@/lib/constants.ts";
import {useTranslation} from "react-i18next";
import {useAppointments} from "@/hooks/useAppointments.ts";
import {useAuth} from "@/hooks/useAuth.ts";
import DashboardNavLoader from "@/components/DashboardNavLoader.tsx";
import DashboardNavEmptyContent from "@/components/DashboardNavEmptyContent.tsx";
import PaginationComponent from "@/components/PaginationComponent.tsx";
import {useAppointmentsQueryParams} from "@/hooks/useQueryParams.ts";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.tsx";
import {useEffect, useState} from "react";

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
    const auth = useAuth()
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

    const {data: appointments, isLoading} = useAppointments({
        userId: auth.userId,
        collection: type,
        page: searchParams.page,
        pageSize: searchParams.pageSize,
        filter: searchParams.filter,
    });

    return (
        <DashboardNavContainer>
            <DashboardNavHeader title={t(componentType.title)}>
                <div className="flex items-center sm:justify-center gap-2 mt-2 sm:mt-0">
                    <span className="flex font-normal text-sm items-center justify-center text-(--text-light)">
                        {t(componentType.filterMessage)}:
                    </span>
                    <Select
                        value={filterValue}
                        onValueChange={(val) => setFilter(val)}
                    >
                        <SelectTrigger className="bg-white text-black hover:bg-gray-100 font-light text-sm border rounded-md cursor-pointer">
                            <SelectValue />
                        </SelectTrigger>

                        <SelectContent position="popper">
                            <SelectItem value="all">{t("appointment.filters.all")}</SelectItem>
                            {componentType.popoverData.map((a) => (
                                <SelectItem key={a} value={a}>
                                    {t(transformFilter(a))}
                                </SelectItem>
                            ))}
                        </SelectContent>
                    </Select>
                </div>
            </DashboardNavHeader>
            {(isLoading) ? (
                <DashboardNavLoader/>
            ) : (appointments?.data !== undefined && appointments.data.length > 0) ? (
                appointments.data.map((a, i) => (
                    <AppointmentCard animationDelay={i} mounted={mounted} key={a.self} appointment={a} isUpcoming={type === "upcoming"}/>
                ))
            ) : (
                <DashboardNavEmptyContent Icon={CalendarFoldIcon} title={t(componentType.emptyTitle)} text={t(componentType.emptyText)}/>
            )}
            {!isLoading && appointments?.data !== undefined && appointments?.data?.length > 0 && appointments?.pagination && (
                <PaginationComponent pagination={appointments.pagination} searchParams={searchParams} />
            )}
        </DashboardNavContainer>
    );
}