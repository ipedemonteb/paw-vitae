import {getNeighborhood, listNeighborhoods} from "@/data/neighborhoods";
import {useQuery} from "@tanstack/react-query";


export function useNeighborhoods() {
    return useQuery({
        queryKey: ["neighborhoods"],
        queryFn: listNeighborhoods
    })
}
export function useNeighborhood(url?: string) {
    return useQuery({
        queryKey: ['neighborhood', url],
        queryFn: () => getNeighborhood(url!),
        enabled: !!url,
        staleTime: Infinity,
    });
}