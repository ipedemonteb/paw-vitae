import {api} from "@/data/Api.ts";
import type {PaginationData} from "@/lib/types.ts";

export type AppointmentDTO = {
    date: Date;
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

type Links = Partial<Record<"first" | "last" | "self" | "prev" | "next", string>>;

function parseLinkHeader(header?: string): Links {
    if (!header) return {};
    const links: Links = {};

    for (const part of header.split(",")) {
        const section = part.split(";").map(s => s.trim());
        const url = section[0]?.replace(/^<|>$/g, "");
        const rel = section.find(s => s.startsWith("rel="))?.split("=")[1]?.replace(/"/g, "");

        if (url && rel) (links as any)[rel] = url;
    }
    return links;
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