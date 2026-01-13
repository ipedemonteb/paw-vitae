import {api} from "@/data/Api.ts";
import {ContentTypes} from "@/utils/contentTypes.ts";
import type {AxiosRequestConfig} from "axios";

export interface RatingsDTO {
    rating: number;
    comment: string;
    self: string;
    appointment: string;
    doctor: string;
    patient: string;
    patientName: string;
    patientLastName: string;
}
export async function getDoctorRatings(url: string) {
    const res = await api.get<RatingsDTO[]>(url,{
        headers: {
            "accept": ContentTypes.RATING_LIST
        }
    }as AxiosRequestConfig);
    return res.data;
}

export async function getAllRatings(){
    const res = await api.get<RatingsDTO[]>(`/ratings?page=1&pageSize=10`,{
        headers: {
            "accept": ContentTypes.RATING_LIST
        }
    }as AxiosRequestConfig);
    return res.data;
}