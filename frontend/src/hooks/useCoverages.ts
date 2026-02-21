import {getCoverage, listCoverages} from "@/data/coverages.ts";
import {useQueries, useQuery} from "@tanstack/react-query";

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
// En src/hooks/useCoverages.ts

export function useCoveragesByUrl(coveragesUrl?: string[]) {
    return useQueries({
        queries: (coveragesUrl ?? []).map((coverage) => ({
            queryKey: ['coverages', coverage],
            queryFn: () => getCoverage(coverage),
        })),
        combine: (results) => {
            return {
                data: results,
                isLoading: results.some(r => r.isLoading),
                isError: results.some(r => r.isError),
                refetch: async () => {
                    return await Promise.allSettled(
                        results.map((r) => r.refetch())
                    );
                },
                isFetching: results.some(r => r.isFetching)
            }
        }
    })
}