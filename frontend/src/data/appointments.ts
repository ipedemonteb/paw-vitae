import {api} from "@/data/Api.ts";
import type {PaginationData} from "@/lib/types.ts";
import {parseLinkHeader} from "@/lib/utils.ts";
import {ContentTypes} from "@/utils/contentTypes.ts";

export type AppointmentDTO = {
    date: string;
    status: 'completo' | 'cancelado'  | 'confirmado';
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

export type AppointmentFileDTO = {
    id: string;
    fileName: string;
    uploaderRole: string;
    download: string;
    view: string;
}

export type AppointmentCollection = "upcoming" | "history";
export type AppointmentFilter = "all" | "today" | "week" | "month" | "cancelled" | "completed";


export type AppointmentsQuery = {
    userId?: string;
    doctorId?: string;
    collection?: AppointmentCollection;
    filter?: AppointmentFilter;
    page?: number;
    pageSize?: number;
}


export async function listAppointments(params: AppointmentsQuery): Promise<PaginationData<AppointmentDTO[]>> {
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
    const total = Number(res.headers["x-total-count"]);
    const links = parseLinkHeader(res.headers["link"]);
    return {
        data: res.data,
        pagination: {
            next: links.next,
            prev: links.prev,
            first: links.first,
            last: links.last,
            self: links.self,
            total: total
        }
    };
}

export async function getAppointment(id: string) {
    const res = await api.get<AppointmentDTO>(`/appointments/${id}`, {
        headers: {"accept": ContentTypes.APPOINTMENT}
    });
    return res.data;
}

export async function getAppointmentFiles(id: string) {
    const res = await api.get<AppointmentFileDTO[]>(`/appointments/${id}/files`, {
        headers: {"accept": ContentTypes.APPOINTMENT_FILE_LIST}
    })
    return res.data;
}