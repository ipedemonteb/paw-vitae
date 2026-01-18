import {
    type AppointmentForm,
    type AppointmentsQuery, createAppointment,
    getAppointment,
    getAppointmentFiles,
    listAppointments, uploadAppointmentFile
} from "@/data/appointments.ts";
import {keepPreviousData, useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
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
export function useBookAppointment() {
    const queryClient = useQueryClient();

    return useMutation<string, AxiosError<any>, { form: AppointmentForm, files: File[] }>({
        mutationFn: async ({ form, files }) => {
            const res = await createAppointment(form);
            const location = res.headers['location'] || res.headers['Location'];
            const newId = location?.split('/').pop();
            if (!newId) {
                throw new Error("No se pudo obtener el ID del turno creado");
            }
            console.log("aca antes")
            for (const file of files) {
                console.log("entree")
                await uploadAppointmentFile(newId, file, 'patient');
            }

            return newId;
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['auth', 'appointments'] });
        }
    });
}