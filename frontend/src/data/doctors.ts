import {api} from "@/data/Api.ts";
import type {PaginationLinks} from "@/lib/types.ts";

export type DoctorDTO = {
    name: string;
    lastName: string;
    email: string;
    phone: string;
    rating: number;
    ratingCount: string;
    specialties: string;
    coverages: string;
    offices: string;
    profile: string;
    experiences: string;
    certifications: string;
    ratings: string;
    appointments: string;
    self: string;

};

export type DoctorsQuery = {
    specialty?: number;
    coverage?: number;
    weekdays?: number[];
    keyword?: string;
    orderBy?: string;
    direction?: "asc" | "desc";
    page?: number;
};

export async function listDoctors(params: DoctorsQuery, signal?: AbortSignal) {
    const res = await api.get<DoctorDTO[]>("/doctors", {
        params: {
            specialty: params.specialty ?? 0,
            coverage: params.coverage ?? 0,
            weekdays: params.weekdays, // axios will repeat ?weekdays=1&weekdays=2
            keyword: params.keyword ?? "",
            orderBy: params.orderBy ?? "name",
            direction: params.direction ?? "asc",
            page: params.page ?? 1,
        },
        signal, // axios supports AbortController in modern versions
    });

    const total = Number(res.headers["x-total-count"] ?? 0);
    const links: PaginationLinks = res.headers["link"]; // if you used Link headers

    return { data: res.data, total, links };
}