import {
    type AppointmentForm,
    type AppointmentsQuery, createAppointment,
    getAppointment,
    getAppointmentFiles,
    listAppointments
} from "@/data/appointments.ts";
import {keepPreviousData, useMutation, useQuery} from "@tanstack/react-query";
import {AxiosError} from "axios";



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
export function createAppointmentMutation() {
    return useMutation<any, AxiosError<any>, AppointmentForm>({
        mutationFn: (data: AppointmentForm) => createAppointment(data),
    });
}