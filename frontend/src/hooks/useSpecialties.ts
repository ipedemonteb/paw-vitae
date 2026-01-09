import {getSpecialty, listSpecialties} from "@/data/specialties.ts";
import {useQuery} from "@tanstack/react-query";


export function useSpecialties() {
    return useQuery({
        queryKey: ['specialties'],
        queryFn: listSpecialties
    })
}
export function useSpecialty(url?: string) {
    return useQuery({
        queryKey: ['specialties', url],
        queryFn: () => getSpecialty(url!),
        enabled: !!url
    })
}