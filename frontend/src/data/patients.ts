import {api} from "@/data/Api.ts";

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

export async function getPatient(url: string) {
    const res = await api.get<PatientDTO>(url)
    return res.data;
}