import {api} from "@/data/Api.ts";

export type CoverageDTO = {
    name: string;
    self: string;
}

export async function listCoverages() {
    const res = await api.get<CoverageDTO[]>("/coverages");


    return { data: res.data };
}