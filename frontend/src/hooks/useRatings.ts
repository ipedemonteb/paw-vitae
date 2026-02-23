import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {createRating, getAllRatings, getDoctorRatings, getRating,type RatingForm} from "@/data/ratings.ts";
import type {AxiosError} from "axios";

export function useRatings(url?: string) {
    return useQuery({
        queryKey: ['doctors', 'ratings', url],
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

export function useRating(url?: string) {
    return useQuery({
        queryKey: ['ratings', url],
        queryFn: () => getRating(url!),
        enabled: !!url,
    });
}
export function useCreateRating(){
    const queryClient = useQueryClient()
    return useMutation<any,AxiosError<any>,RatingForm>({
        mutationFn: async (ratingData:RatingForm) => createRating(ratingData),
        onSuccess: async (_, variables) => {
            await Promise.all([
                queryClient.invalidateQueries({queryKey: ['doctors', 'ratings']}),
                queryClient.invalidateQueries({queryKey: ['ratings']}),
                queryClient.invalidateQueries({
                    queryKey: ['auth', 'appointments', variables.appointmentId],
                    exact: true
                }),
                queryClient.invalidateQueries({queryKey: ['auth', 'appointments', 'list']})
            ])
        }
    })
}