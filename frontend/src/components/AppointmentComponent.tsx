import DashboardNavContainer from "@/components/DashboardNavContainer.tsx";
import DashboardNavHeader from "@/components/DashboardNavHeader.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Popover, PopoverTrigger, PopoverContent} from "@/components/ui/popover.tsx";
import {ChevronDown} from "lucide-react";
import {useState} from "react";
import AppointmentCard from "@/components/AppointmentCard.tsx";
import {appointmentStatus, appointmentUpcomingFilters} from "@/lib/constants.ts";
import {useTranslation} from "react-i18next";
import {
    Pagination,
    PaginationContent, PaginationEllipsis,
    PaginationItem,
    PaginationLink, PaginationNext,
    PaginationPrevious
} from "@/components/ui/pagination.tsx";
import {useAppointments} from "@/hooks/useAppointments.ts";
import {useAuth} from "@/hooks/useAuth.ts";
import DashboardNavLoader from "@/components/DashboardNavLoader.tsx";
import DashboardNavEmptyContent from "@/components/DashboardNavEmptyContent.tsx";

const typeDictionary = {
    history: {
        title: "appointment.history",
        popoverData: appointmentStatus,
        filterMessage: "appointment.history_filter_message"

    },
    upcoming: {
        title: "appointment.upcoming",
        popoverData:appointmentUpcomingFilters,
        filterMessage: "appointment.upcoming_filter_message"
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
    const {data: appointments, isLoading} = useAppointments({userId: auth.userId, collection: type});
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
            {(isLoading && false) ? (
                <DashboardNavLoader item={t("appointment")}/>
            ) : (appointments !== undefined && appointments.length > 0 && false) ? (
                appointments?.map(a => (
                    <AppointmentCard key={a.self} appointment={a}/>
                ))
            ) : (
                <DashboardNavEmptyContent/>
            )}
            <div>
                <Pagination>
                    <PaginationContent className="text-(--text-color) gap-2">
                        <PaginationItem>
                            <PaginationPrevious href="#" />
                        </PaginationItem>
                        <PaginationItem>
                            <PaginationEllipsis />
                        </PaginationItem>
                        <PaginationItem>
                            <PaginationLink href="#">6</PaginationLink>
                        </PaginationItem>
                        <PaginationItem>
                            <PaginationLink href="#">
                                7
                            </PaginationLink>
                        </PaginationItem>
                        <PaginationItem>
                            <PaginationLink href="#" isActive>8</PaginationLink>
                        </PaginationItem>
                        <PaginationItem>
                            <PaginationLink href="#">9</PaginationLink>
                        </PaginationItem>
                        <PaginationItem>
                            <PaginationLink href="#">10</PaginationLink>
                        </PaginationItem>
                        <PaginationItem>
                            <PaginationEllipsis />
                        </PaginationItem>
                        <PaginationItem>
                            <PaginationNext href="#" />
                        </PaginationItem>
                    </PaginationContent>
                </Pagination>
            </div>
        </DashboardNavContainer>
    );
}