import { api } from "@/data/Api";
import {ContentTypes} from "@/utils/contentTypes.ts";

export interface AvailabilitySlotDTO {
    id: number;
    date: string;
    startTime: string;
    status: 'AVAILABLE' | 'UNAVAILABLE'
}

export async function getDoctorSlots(doctorId: string) {
    const res = await api.get<AvailabilitySlotDTO[]>(`/doctors/${doctorId}/slots`, {
        headers: {
            "Accept": ContentTypes.APPLICATION_AVAILABILITY_SLOTS_LIST
        }
    } );

    return res.data;
}
