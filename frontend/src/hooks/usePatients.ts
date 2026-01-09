import {useQuery} from "@tanstack/react-query";
import {getPatient} from "@/data/patients.ts";

export function usePatient(url?: string) {
    return useQuery({
        queryKey: ['auth', 'patients', url],
        queryFn: () => getPatient(url!),
        enabled: !!url
    })
}