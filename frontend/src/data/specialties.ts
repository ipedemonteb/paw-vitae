import {api} from "@/data/Api.ts";

export type SpecialtyDTO = {
    name: string;
    self: string;
}

export async function listSpecialties() {
    const res = await api.get<SpecialtyDTO[]>("/specialties");


    return { data: res.data };
}