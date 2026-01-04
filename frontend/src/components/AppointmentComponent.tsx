import DashboardNavContainer from "@/components/DashboardNavContainer.tsx";
import DashboardNavHeader from "@/components/DashboardNavHeader.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Popover, PopoverTrigger} from "@/components/ui/popover.tsx";
import {ChevronDown} from "lucide-react";
import {PopoverContent} from "@radix-ui/react-popover";
import {useState} from "react";
import type {AppointmentDTO} from "@/data/appointments.ts";
import AppointmentCard from "@/components/AppointmentCard.tsx";
import {appointmentStatus} from "@/lib/constants.ts";
import {useTranslation} from "react-i18next";

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
    specialty: 'General Medicine',
    doctorOffice: 'Clinic A - Room 2',
    appointmentFiles: '[]',
    rating: '5'
};

export default function AppointmentComponent() {
    const [open, setOpen] = useState(false);
    const [value, setValue] = useState("All History")
    const {t} = useTranslation();
    return (
        <DashboardNavContainer>
            <DashboardNavHeader title={"Appointment History"}>
                <div className="flex felx-col items-center justify-center gap-2">
                <span className="flex font-normal text-sm items-center justify-center text-(--text-light)">
                    Status:
                </span>
                    <Popover open={open} onOpenChange={setOpen}>
                        <PopoverTrigger
                            className="flex flex-row font-light items-center justify-center text-sm gap-2 border rounded-md p-1.5">{value}
                            <ChevronDown size={20}/></PopoverTrigger>
                        <PopoverContent className="min-w-fit max-w-28 p-0">
                            <Button value="All History" onClick={(e) => {
                                setValue(e.currentTarget.value);
                                setOpen(false)
                            }}
                                    className="w-full text-xs bg-white font-light hover:bg-gray-100 hover:text-black flex text-(--text-light) justify-baseline">All
                                History</Button>
                            {appointmentStatus.map(a => (
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
        </DashboardNavContainer>
    );
}