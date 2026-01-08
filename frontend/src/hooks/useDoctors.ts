import {listDoctors, type DoctorsQuery} from "@/data/doctors";
import {keepPreviousData, useQuery} from "@tanstack/react-query";

export function useDoctors(query: DoctorsQuery) {
    return useQuery({
        queryKey: ['auth', 'doctors', query],
        queryFn: () => listDoctors(query),
        placeholderData: keepPreviousData
    })
}


// export function useDoctorMutation




