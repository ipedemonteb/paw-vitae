import {api} from "@/data/Api.ts";

export type SpecialtyDTO = {
    name: string;
    self: string;
}

export async function listSpecialties() {
    const res = await api.get<SpecialtyDTO[]>("/specialties")
    return res.data;
}

export async function getSpecialty(url: string) {
    const res = await api.get<SpecialtyDTO>(url);
    return res.data;
}