import {api} from "@/data/Api.ts";

export type CoverageDTO = {
    name: string;
    self: string;
}

export async function listCoverages() {
    const res = await api.get<CoverageDTO[]>("/coverages");
    return res.data;
}

export async function getCoverage(url: string) {
    const res = await api.get<CoverageDTO>(url);
    return res.data;
}