import {api} from "@/data/Api.ts";
import {ContentTypes} from "@/utils/contentTypes.js";
import type {AxiosRequestConfig} from "axios";


export type ChangePasswordForm = {
    password: string;
    repeatPassword: string;
}
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

export async function listDoctors(params: DoctorsQuery) {
    const res = await api.get<DoctorDTO[]>("/doctors", {
        params: {
            specialty: params.specialty ?? 0,
            coverage: params.coverage ?? 0,
            weekdays: params.weekdays, // axios will repeat ?weekdays=1&weekdays=2
            keyword: params.keyword ?? "",
            orderBy: params.orderBy ?? "name",
            direction: params.direction ?? "asc",
            page: params.page ?? 1,
        }
    });
    return res.data;
}

export async function changeDoctorPassword(url: string, form: ChangePasswordForm) {
    const res = await api.patch(`${url}`,form,{
        headers: {
            "content-type":  ContentTypes.USER_PASSWORD
        }
    } as AxiosRequestConfig);
    return res.data;
}

export async function getDoctor(id: string) {
    const res = await api.get<DoctorDTO>(`/doctors/${id}`);
    return res.data;
}

export async function getDoctorImage(id: string) {
    const res = await api.get<Blob>(`/doctors/${id}/image`,
        { responseType: "blob" }
    );
    return res.data;
}