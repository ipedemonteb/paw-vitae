import {api} from "@/data/Api.ts";

export type AppointmentDTO = {
    date: Date;
    status: string;
    reason: string;
    allowFullHistory: string;
    report: string;
    cancellable: boolean

    self: string;
    doctor: string;
    patient: string;
    specialty: string;
    doctorOffice: string;
    appointmentFiles: string;
    rating: string;
}


export type AppointmentsQuery = {
    userId?: string;
    doctorId?: string;
    collection?: string;
    filter?: string;
    page?: number;
    pageSize?: number;
}


export async function listAppointments(params: AppointmentsQuery) {
    if (!params.userId) return []; //TODO
    const res = await api.get<AppointmentDTO[]>("/appointments", {
        params: {
            userId: params.userId,
            doctorId: params.doctorId,
            collection: params.collection,
            filter: params.filter,
            page: params.page ?? 1,
            pageSize: params.pageSize
        }
    })
    return res.data;
}