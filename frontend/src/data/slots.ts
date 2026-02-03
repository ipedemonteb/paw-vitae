import { api } from "@/data/Api";
import { ContentTypes } from "@/utils/contentTypes.ts";

export interface OccupiedSlotDTO {
    id: number;
    date: string;
    startTime: string;
}

export async function getOccupiedSlots(doctorId: string, from: string, to: string) {
    const res = await api.get<OccupiedSlotDTO[]>(`/doctors/${doctorId}/slots`, {
        headers: {
            "Accept": ContentTypes.APPLICATION_AVAILABILITY_SLOTS_LIST
        },
        params: {
            from,
            to
        }
    });

    return res.data;
}