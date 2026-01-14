import {type AppointmentsQuery, getAppointment, getAppointmentFiles, listAppointments} from "@/data/appointments.ts";
import {keepPreviousData, useQuery} from "@tanstack/react-query";

export function useAppointments(query: AppointmentsQuery) {
    const { userId, doctorId, collection, filter, page, pageSize } = query ?? {};

    return useQuery({
        queryKey: [
            "auth",
            "appointments",
            userId ?? null,
            doctorId ?? null,
            collection ?? null,
            filter ?? null,
            page ?? null,
            pageSize ?? null,
        ],
        queryFn: () => listAppointments(query),
        enabled: !!query.userId,
        placeholderData: keepPreviousData
    })
}

export function useAppointment(id?: string | null) {
    return useQuery({
        queryKey: ['appointment', id],
        queryFn: () => getAppointment(id!),
        enabled: !!id,
    });
}

export function useAppointmentFiles(id?: string | null) {
    return useQuery({
        queryKey: ['appointment', id, 'files'],
        queryFn: () => getAppointmentFiles(id!),
        enabled: !!id,
    })
}