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
    userId: string;
    collection?: string;
    filter?: string;
    page?: number;
    pageSize?: number;
}

export async function listAppointments(params: AppointmentsQuery) {
    const res = await api.get("/appointments", {
        params: {
            userId: params.userId,
            collection: params.collection,
            filter: params.filter,
            page: params.page ?? 1,
            pageSize: params.pageSize
        }
    })
    return res.data;
}