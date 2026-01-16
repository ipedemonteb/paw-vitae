import {api} from "@/data/Api.ts";
import type { AxiosRequestConfig } from "axios";
import { ContentTypes } from "@/utils/contentTypes.js";
export type PatientDTO = {
    name: string;
    lastName: string;
    email: string;
    phone: string;
    coverages: string;
    neighborhood: string;
    appointments: string;
    self: string
}
export type ChangePasswordForm = {
    password: string;
    repeatPassword: string;

}

export interface PatientRegisterData {
    name: string;
    lastName: string;
    email: string;
    phone: string;
    password: string;
    repeatPassword: string;
    coverage: string;
    neighborhoodId: string;
}
export interface PatientUpdateData {
    name?: string;
    lastName?: string;
    phone?: string;
    coverage?: string;
}

const extractIdFromUrl = (url: string): string => {
    if (!url) return "";
    const segments = url.replace(/\/$/, "").split("/");
    return segments.pop() || "";
}

export async function getPatient(url: string) {
    const res = await api.get<PatientDTO>(url)
    return res.data;
}

export const patientUrl = (id: string) => `/patients/${id}`;

export async function getPatientById(id: string) {
    const res = await api.get<PatientDTO>(patientUrl(id));
    return res.data;
}

export async function changePatientPassword(url: string, form: ChangePasswordForm) {
    const res = await api.patch(`${url}`,form,{
        headers: {
           "content-type":  ContentTypes.USER_PASSWORD
        }
    } as AxiosRequestConfig);
    return res.data;
}

export async function registerPatient(data: PatientRegisterData) {
    const coverageId = extractIdFromUrl(data.coverageUrl);
    const neighborhoodId = extractIdFromUrl(data.neighborhoodUrl);

    if (!coverageId || !neighborhoodId) {
        throw new Error("Invalid IDs for coverage or neighborhood");
    }

    const payload = {
        name: data.name,
        lastName: data.lastName,
        email: data.email,
        password: data.password,
        repeatPassword: data.repeatPassword,
        phone: data.phone,
        coverage: parseInt(coverageId),       // Backend espera Long
        neighborhoodId: parseInt(neighborhoodId) // Backend espera Long
    };

    return api.post('/patients', payload,
        { headers: { 'Content-Type': 'application/vnd.vitae.patient.v1+json' } });
}


export async function fetchCountsPatient(): Promise<number> {
    const res = await api.head('/patients')
    const header = res.headers['x-total-count'] ?? res.headers['X-Total-Count'];
    const parsed = parseInt(String(header ?? ''), 10);
    return Number.isNaN(parsed) ? 0 : parsed;
}
export async function updatePatient(url: string, data: PatientUpdateData) {
    const res = await api.patch(url, data, {
        headers: {
            'Content-Type': ContentTypes.PATIENT
        }
    });
    return res.data;
}