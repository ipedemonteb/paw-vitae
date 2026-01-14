import {useQuery} from "@tanstack/react-query";
import {getAllRatings, getDoctorRatings, getRating} from "@/data/ratings.ts";

export function useRatings(url?: string | null) {
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

export function useRating(id?: string | null) {
    return useQuery({
        queryKey: ['rating', id],
        queryFn: () => getRating(id!),
        enabled: !!id,
    });
}