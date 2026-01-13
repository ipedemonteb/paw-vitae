import {api} from "@/data/Api.ts";
import {ContentTypes} from "@/utils/contentTypes.ts";
import type {AxiosRequestConfig} from "axios";

export type NeighborhoodDTO = {
    name: string;
    self: string;
}

export async function listNeighborhoods() {
    const res = await api.get<NeighborhoodDTO[]>("/neighborhoods");
    return res.data;
}

export async function getNeighborhood(url: string) {
    const res = await api.get<NeighborhoodDTO>(url,{
        headers: {
            "accept":ContentTypes.NEIGHBORHOOD
        }
    } as AxiosRequestConfig);
    return res.data;
}