import {
    type AppointmentForm,
    type AppointmentsQuery,
    createAppointment,
    fetchFileBlob,
    getAppointment,
    getAppointmentFiles,
    listAppointments,
    uploadAppointmentFile,
    updateAppointmentReport, cancelAppointment
} from "@/data/appointments.ts";
import {keepPreviousData, useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import { AxiosError } from "axios";
import {useMemo} from "react";

export function useAppointments(query: AppointmentsQuery) {
    const stableQueryKey = useMemo(() => JSON.stringify(query ?? {}), [query]);
    return useQuery({
        queryKey: ['auth', 'appointments', 'list', stableQueryKey],
        queryFn: () => listAppointments(query),
        enabled: !!query.userId,
        placeholderData: keepPreviousData
    })
}

export function useAppointment(id?: string) {
    return useQuery({
        queryKey: ['auth', 'appointments', id],
        queryFn: () => getAppointment(id!),
        enabled: !!id,
    });
}

export function useAppointmentFiles(url?: string) {
    return useQuery({
        queryKey: ['auth', 'appointments', 'files', url],
        queryFn: () => getAppointmentFiles(url!),
        enabled: !!url,
    })
}

export function useBookAppointmentMutation() {
    const queryClient = useQueryClient()
    return useMutation<string, AxiosError<any>, { form: AppointmentForm, files: File[] }>({
        mutationFn: async ({ form, files }) => {
            const res = await createAppointment(form);
            const location = res.headers['location'] || res.headers['Location'];
            const newId = location?.split('/').pop();

            if (!newId) {
                throw new Error("No se pudo obtener el ID del turno creado");
            }

            for (const file of files) {
                await uploadAppointmentFile(newId, file, 'patient');
            }

            return newId;
        },
        onSuccess: async (_, { form }) => {
            await queryClient.invalidateQueries({ queryKey: ['auth', 'appointments', 'list'] });
            await queryClient.invalidateQueries({ queryKey: ['doctors', form.doctorId, 'slots'] });
        }
    });
}

export function useUpdateReportMutation() {
    const queryClient = useQueryClient()
    return useMutation({
        mutationFn: ({ id, report }: { id: string, report: string }) => updateAppointmentReport(id, report),
        onSuccess: async (_, variables) => {
            await queryClient.invalidateQueries({
                queryKey: ['auth', 'appointments', variables.id],
                exact: true
            })
        }
    });
}

export function useUploadDoctorFilesMutation() {
    const queryClient = useQueryClient()
    return useMutation({
        mutationFn: async ({ id, files }: { id: string, files: File[] }) => {
            for (const file of files) {
                await uploadAppointmentFile(id, file, 'doctor');
            }
        },
        onSuccess: async () => {
            await queryClient.invalidateQueries({queryKey: ['auth', 'appointments','files']})
        }
    });
}

export function useAppointmentFileHandlerMutation() {
    return useMutation({
        mutationFn: async ({ url, action, fileName }: { url: string, action: 'view' | 'download', fileName: string }) => {
            const { data, contentType } = await fetchFileBlob(url);

            const type = action === 'view' ? 'application/pdf' : contentType;
            const blob = new Blob([data], { type });
            const tempUrl = window.URL.createObjectURL(blob);
            return { tempUrl, action, fileName };
        },
        onSuccess: ({ tempUrl, action, fileName }) => {
            if (action === 'view') {
                window.open(tempUrl, '_blank');
            } else {
                const link = document.createElement('a');
                link.href = tempUrl;
                link.setAttribute('download', fileName);
                document.body.appendChild(link);
                link.click();
                link.remove();
            }
            setTimeout(() => window.URL.revokeObjectURL(tempUrl), 100);
        },
    });
}

export function useCancelAppointmentMutation() {
    const queryClient = useQueryClient();

    return useMutation<void, AxiosError<any>, { id: string; userId: string }>({
        mutationFn: ({ id, userId }) => cancelAppointment(id, userId),
        onSuccess: async (_, variables) => {
            await Promise.all([
                queryClient.invalidateQueries({ queryKey: ['auth', 'appointments', 'list'] }),
                queryClient.invalidateQueries({ queryKey: ['auth', 'appointments', variables.id], exact: true })
            ])
        },
    });
}