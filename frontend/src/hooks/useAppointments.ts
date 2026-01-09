import {type AppointmentsQuery, listAppointments} from "@/data/appointments.ts";
import {useQuery} from "@tanstack/react-query";



export function useAppointments(query: AppointmentsQuery) {
    return useQuery({
        queryKey: ['auth', 'appointments', query.userId],
        queryFn: () => listAppointments(query)
    })
}