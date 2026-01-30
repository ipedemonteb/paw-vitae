import {getSpecialty, listSpecialties} from "@/data/specialties.ts";
import {useQueries, useQuery} from "@tanstack/react-query";

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

export function useSpecialtiesByUrl(specialtiesUrl?: string[]) {
    return useQueries({
        queries: (specialtiesUrl ?? []).map((specialty) => ({
            queryKey: ['specialties', specialty],
            queryFn: () => getSpecialty(specialty),
        })),
        combine: (results) => {
            return {
                data: results,
                isLoading: results.some(r => r.isLoading)
            }
        }
    })
}