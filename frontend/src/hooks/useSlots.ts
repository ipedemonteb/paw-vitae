import { useQuery } from "@tanstack/react-query";
import { getOccupiedSlots } from "@/data/slots";

export function useOccupiedSlots(doctorId?: string, from?: string, to?: string) {
    return useQuery({
        queryKey: ['doctors', doctorId, 'slots', 'occupied', from, to],
        queryFn: () => getOccupiedSlots(doctorId!, from!, to!),
        enabled: !!doctorId && !isNaN(Number(doctorId)) && !!from && !!to,
    });
}