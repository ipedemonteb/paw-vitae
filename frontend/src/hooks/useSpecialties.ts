import {listSpecialties} from "@/data/specialties.ts";
import {useQuery} from "@tanstack/react-query";


export function useSpecialties() {
    return useQuery({
        queryKey: ["specialties"],
        queryFn: listSpecialties
    })
}