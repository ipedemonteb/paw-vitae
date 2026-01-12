import {useQuery} from "@tanstack/react-query";
import {getDoctorRatings} from "@/data/ratings.ts";

export function useRatings(url?: string | null) {
    return useQuery({
        queryKey: ['doctor', 'ratings', url],
        queryFn: () => getDoctorRatings(url!),
        enabled: !!url,
    });
}