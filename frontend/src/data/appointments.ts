import {api} from "@/data/Api.ts";
import type {PaginationData} from "@/lib/types.ts";
import {parseLinkHeader} from "@/lib/utils.ts";
import {ContentTypes} from "@/utils/contentTypes.ts";
import {APPOINTMENTS_PAGE_SIZE} from "@/lib/constants.ts";

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

export type AppointmentForm={
    appointmentDate: string;
    appointmentHour: string;
    reason: string;
    specialtyId: string;
    doctorId: string;
    officeId: string;
    patientId: string;
    allowFullHistory: boolean;

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

//Hello fellow coder who is reading this. Typescript erases types at runtime so if i pass "lol"
//as a filter it will be sent to the api without a care in the world. Please take precautions if handling
//query params dynamically as i am.
function normalizeAppointmentsQuery(query: AppointmentsQuery) {
    if (Number.isNaN(query.page)) query.page = 1;
    if (Number.isNaN(query.pageSize)) query.pageSize = APPOINTMENTS_PAGE_SIZE;

    const allowedFilters: AppointmentFilter[] = ["all", "today", "week", "month", "cancelled", "completed"];
    if (query.filter && !allowedFilters.includes(query.filter)) {
        query.filter = "all";
    }
}


export async function listAppointments(params: AppointmentsQuery): Promise<PaginationData<AppointmentDTO[]>> {
    normalizeAppointmentsQuery(params)
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
export async function createAppointment(appointment: AppointmentForm) {
    return await api.post<AppointmentDTO>("/appointments", appointment, {
    headers: {"Content-Type": ContentTypes.APPOINTMENT}
});
}

export async function uploadAppointmentFile(appointmentId: string, doc: File, role: 'patient' | 'doctor') {
    const formData = new FormData();
    const paramName = role === 'patient' ? 'file' : 'files';
    formData.append(paramName, doc);
    return await api.post<AppointmentFileDTO>(`/appointments/${appointmentId}/files/${role}`, formData, {
        headers: {"Content-Type": "multipart/form-data"}
    });
}
export async function fetchFileBlob(url: string) {
    const res = await api.get(url, {
        responseType: 'blob'
    });

    return {
        data: res.data as Blob,
        contentType: res.headers['content-type']
    };
}
export async function updateAppointmentReport(id: string, report: string) {
    return await api.patch(`/appointments/${id}`, { report }, {
        headers: { "Content-Type": ContentTypes.APPOINTMENT_REPORT }
    });
}
