import DashboardNavContainer from "@/components/DashboardNavContainer.tsx";
import DashboardNavHeader from "@/components/DashboardNavHeader.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Popover, PopoverTrigger, PopoverContent} from "@/components/ui/popover.tsx";
import {CalendarFoldIcon, ChevronDown} from "lucide-react";
import {useState} from "react";
import AppointmentCard from "@/components/AppointmentCard.tsx";
import {appointmentStatus, appointmentUpcomingFilters} from "@/lib/constants.ts";
import {useTranslation} from "react-i18next";
import {useAppointments} from "@/hooks/useAppointments.ts";
import {useAuth} from "@/hooks/useAuth.ts";
import DashboardNavLoader from "@/components/DashboardNavLoader.tsx";
import DashboardNavEmptyContent from "@/components/DashboardNavEmptyContent.tsx";
import PaginationComponent from "@/components/PaginationComponent.tsx";
import {useAppointmentsQueryParams} from "@/hooks/useAppointmentsQueryParams.ts";

const typeDictionary = {
    history: {
        title: "appointment.history",
        popoverData: appointmentStatus,
        filterMessage: "appointment.history_filter_message",
        emptyTitle: "appointment.empty.title.past",
        emptyText: "appointment.empty.text.past",
        emptyIcon: CalendarFoldIcon

    },
    upcoming: {
        title: "appointment.upcoming",
        popoverData:appointmentUpcomingFilters,
        filterMessage: "appointment.upcoming_filter_message",
        emptyTitle: "appointment.empty.title.upcoming",
        emptyText: "appointment.empty.text.upcoming",
        emptyIcon: CalendarFoldIcon
    }
}

type AppointmentComponentProps = {
    type: "history" | "upcoming"
}

export default function AppointmentComponent({type}: AppointmentComponentProps) {
    const {t} = useTranslation();
    const [open, setOpen] = useState(false);
    const [value, setValue] = useState(t("appointment.all"))
    const componentType = typeDictionary[type]
    const auth = useAuth()

    const searchParams = useAppointmentsQueryParams()

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
                <div className="flex felx-col items-center sm:justify-center gap-2 mt-2 sm:mt-0">
                    <span className="flex font-normal text-sm items-center justify-center text-(--text-light)">
                        {t(componentType.filterMessage)}:
                    </span>
                    <Popover open={open} onOpenChange={setOpen}>
                        <PopoverTrigger asChild>
                            <Button
                                className="flex bg-white text-black hover:bg-gray-100 flex-row font-light items-center justify-center text-sm gap-1 border rounded-md cursor-pointer"
                                variant="outline"
                                role="combobox"
                                aria-expanded={open}
                            >
                                {value}
                                <ChevronDown size={20}/>
                            </Button>
                        </PopoverTrigger>
                        <PopoverContent className="min-w-fit max-w-28 p-0">
                            <Button value={t("appointment.all")} onClick={(e) => {
                                setValue(e.currentTarget.value);
                                setOpen(false)
                            }}
                                    className="w-full text-xs bg-white font-light hover:bg-gray-100 hover:text-black flex text-(--text-light) justify-baseline">{t("appointment.all")}</Button>
                            {componentType.popoverData.map(a => (
                                <Button value={t(a)} onClick={(e) => {
                                    setValue(e.currentTarget.value);
                                    setOpen(false)
                                }}
                                        className="w-full text-xs bg-white font-light hover:bg-gray-100 hover:text-black flex text-(--text-light) justify-baseline">{t(a)}</Button>
                            ))}
                        </PopoverContent>
                    </Popover>
                </div>
            </DashboardNavHeader>
            {(isLoading) ? (
                <DashboardNavLoader item={t("appointment")}/>
            ) : (appointments?.data !== undefined && appointments.data.length > 0) ? (
                appointments.data.map(a => (
                    <AppointmentCard key={a.self} appointment={a}/>
                ))
            ) : (
                <DashboardNavEmptyContent Icon={componentType.emptyIcon} title={t(componentType.emptyTitle)} text={t(componentType.emptyText)}/>
            )}
            <PaginationComponent pagination={appointments?.pagination} searchParams={searchParams}/>
        </DashboardNavContainer>
    );
}