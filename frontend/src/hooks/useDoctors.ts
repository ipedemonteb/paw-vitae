import {listDoctors, type DoctorsQuery} from "@/data/doctors";
import {useQuery} from "@tanstack/react-query";

export function useDoctors(query: DoctorsQuery) {
    return useQuery({
        queryKey: ["doctors"],
        queryFn: () => listDoctors(query),
    })
}


// export function useDoctorMutation




