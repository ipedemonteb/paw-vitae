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
}
export interface RatingForm{
    rating: number;
    comment: string;
    appointmentId: string;
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

export async function getRating(url: string) {
    const res = await api.get<RatingsDTO>(url, {
        headers: {"accept": ContentTypes.RATING}
    });
    return res.data;
}
export async function createRating(ratingData:RatingForm) {
    return await api.post<RatingForm>(`/ratings`, ratingData, {
        headers: {
            "Content-Type": ContentTypes.RATING
        }
    });

}