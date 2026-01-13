import {useQuery} from "@tanstack/react-query";
import {getAllRatings, getDoctorRatings} from "@/data/ratings.ts";

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