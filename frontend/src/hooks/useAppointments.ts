import {
    type AppointmentForm,
    type AppointmentsQuery,
    createAppointment,
    fetchFileBlob,
    getAppointment,
    getAppointmentFiles,
    listAppointments,
    uploadAppointmentFile,
    updateAppointmentReport
} from "@/data/appointments.ts";
import { keepPreviousData, useMutation, useQuery } from "@tanstack/react-query";
import { AxiosError } from "axios";

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
        }
    });
}

export function useUpdateReport() {
    return useMutation({
        mutationFn: async ({ id, report }: { id: string, report: string }) => {
            return await updateAppointmentReport(id, report);
        }
    });
}

export function useUploadDoctorFiles() {
    return useMutation({
        mutationFn: async ({ id, files }: { id: string, files: File[] }) => {
            for (const file of files) {
                await uploadAppointmentFile(id, file, 'doctor');
            }
        }
    });
}

export function useAppointmentFileHandler() {
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