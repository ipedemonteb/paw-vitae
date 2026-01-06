import {listNeighborhoods} from "@/data/neighborhoods";
import {useQuery} from "@tanstack/react-query";


export function useNeighborhoods() {
    return useQuery({
        queryKey: ["neighborhoods"],
        queryFn: listNeighborhoods
    })
}