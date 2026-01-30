import { useQuery } from "@tanstack/react-query";
import { getDoctorSlots} from "@/data/slots";

export function useDoctorSlots(doctorId?: string) {
    return useQuery({
        queryKey: ['doctors', doctorId, 'slots'],
        queryFn: () => getDoctorSlots(doctorId!),
        enabled: !!doctorId && !isNaN(Number(doctorId)),
    });
}