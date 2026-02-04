import {getCoverage, listCoverages} from "@/data/coverages.ts";
import {useQuery} from "@tanstack/react-query";

export function useCoverages() {
    return useQuery({
        queryKey: ['coverages'],
        queryFn: listCoverages
    })
}

export function useCoverage(url?: string) {
    return useQuery({
        queryKey: ['coverages', url],
        queryFn: () => getCoverage(url!),
        enabled: !!url
    })
}