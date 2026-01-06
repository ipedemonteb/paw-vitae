import DashboardNavContainer from "@/components/DashboardNavContainer.tsx";
import DashboardNavHeader from "@/components/DashboardNavHeader.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Popover, PopoverTrigger, PopoverContent} from "@/components/ui/popover.tsx";
import {ChevronDown} from "lucide-react";
import {useState} from "react";
import type {AppointmentDTO} from "@/data/appointments.ts";
import AppointmentCard from "@/components/AppointmentCard.tsx";
import {appointmentStatus, appointmentUpcomingFilters} from "@/lib/constants.ts";
import {useTranslation} from "react-i18next";
import PageNavigation from "@/components/PageNavigation.tsx";

export const mockAppointment: AppointmentDTO = {
    date: new Date('2026-01-04T17:30:00Z'),
    status: 'confirmado',
    reason: 'Routine check-up and follow-up on blood work',
    allowFullHistory: 'true',
    report: 'No abnormal findings. Advise follow-up in 6 months.',
    cancellable: true,
    self: '/api/appointments/1',
    doctor: '/api/doctors/123',
    patient: '/api/patients/456',
    specialty: 'General',
    doctorOffice: 'Clinic A - Room 2',
    appointmentFiles: '[]',
    rating: '5'
};

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
    const data = typeDictionary[type]
    return (
        <DashboardNavContainer>
            <DashboardNavHeader title={t(data.title)}>
                <div className="flex felx-col items-center justify-center gap-2">
                <span className="flex font-normal text-sm items-center justify-center text-(--text-light)">
                    {t(data.filterMessage)}:
                </span>
                    <Popover open={open} onOpenChange={setOpen}>
                        <PopoverTrigger asChild>
                            <Button
                                className="flex bg-white text-black hover:bg-gray-100 flex-row font-light items-center justify-center text-sm gap-2 border rounded-md p-1.5"
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
                            {data.popoverData.map(a => (
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
            <AppointmentCard patientCoverage="Galeno" patientName="Jose" patientLastname="Benegas"
                             appointment={mockAppointment}/>
            <PageNavigation/>
        </DashboardNavContainer>
    );
}