import {useMutation, useQuery} from "@tanstack/react-query";
import {createRating, getAllRatings, getDoctorRatings, getRating,type RatingForm} from "@/data/ratings.ts";
import type {AxiosError} from "axios";

export function useRatings(url?: string) {
    return useQuery({
        queryKey: ['doctor', 'ratings', url],
        queryFn: () => getDoctorRatings(url!),
        enabled: !!url,
    });
}

export function useAllRatings(){
    return useQuery({
        queryKey: [ 'Allratings'],
        queryFn: () => getAllRatings()
    });
}

export function useRating(id?: string) {
    return useQuery({
        queryKey: ['rating', id],
        queryFn: () => getRating(id!),
        enabled: !!id,
    });
}
export function useCreateRating(){
    return useMutation<any,AxiosError<any>,RatingForm>({
        mutationFn: async (ratingData:RatingForm) => createRating(ratingData)
    })
}