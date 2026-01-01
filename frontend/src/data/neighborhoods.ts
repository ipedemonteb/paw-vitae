import {api} from "@/data/Api.ts";

export type NeighborhoodDTO = {
    name: string;
    self: string;
}

export async function listNeighborhoods() {
    const res = await api.get<NeighborhoodDTO[]>("/neighborhoods");


    return { data: res.data };
}