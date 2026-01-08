import {listCoverages} from "@/data/coverages.ts";
import {useQuery} from "@tanstack/react-query";

export function useCoverages() {
    return useQuery({
        queryKey: ["coverages"],
        queryFn: listCoverages
    })
}