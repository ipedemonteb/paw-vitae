import {api} from "@/data/Api.ts";
import {ContentTypes} from "@/utils/contentTypes.ts";
import type {AvailabilitySlotForm, CreateDoctorOfficeForm} from "@/data/doctors.ts";

export interface OfficeDTO {
    name: string;
    status: "active" | "inactive"
    doctor: string;
    neighborhood: string;
    officeAvailability: string;
    officeSpecialties: string;
    self: string;
}

export type DoctorOfficeQuery = {
    status: string
}

export interface OfficeSpecialtyDTO {
    specialty: string;
    office: string;
}

export interface AvailabilityDTO {
    dayOfWeek: number;
    startTime: string;
    endTime: string;
    office: string;
}

export interface DoctorAvailabilityFormDTO {
    doctorOfficeAvailabilities: AvailabilitySlotForm[];
}

export type UpdateDoctorOfficeForm = Partial<CreateDoctorOfficeForm> & {
    active?: boolean,
    removed?: boolean
}

export async function getDoctorOffices(officesUrl: string, query?: DoctorOfficeQuery) {
    const res = await api.get<OfficeDTO[]>(officesUrl,{
        headers: {
            Accept: ContentTypes.OFFICE_LIST
        },
        params: {
            status: query?.status
        }
    } );
    return res.data;
}

export async function getDoctorOffice(url: string) {
    const res = await api.get<OfficeDTO>(url, {
        headers: {"accept": ContentTypes.OFFICE,}
    });
    return res.data;
}

export async function getDoctorOfficeSpecialties(url: string) {
    const res = await api.get<OfficeSpecialtyDTO[]>(url, {
        headers: {"accept": ContentTypes.OFFICE_SPECIALTY_LIST,}
    } );
    return res.data;
}

export async function getDoctorOfficeAvailability(url: string) {
    const res = await api.get<AvailabilityDTO[]>(url, {
        headers: {"accept": ContentTypes.AVAILABILITY_LIST,}
    });
    return res.data
}

export async function updateDoctorOffice(url: string, form: UpdateDoctorOfficeForm) {
    const res = await api.patch(url, form, {
        headers: {"Content-Type": ContentTypes.OFFICE}
    })
    return res.data;
}

export async function createDoctorOffice(url: string, form: CreateDoctorOfficeForm) {
    const res = await api.post(url, form, {
        headers: {"Content-Type": ContentTypes.OFFICE}
    })
    return res.data;
}

export async function putDoctorOfficeAvailability(url: string, data: DoctorAvailabilityFormDTO) {
    const res = await api.put(url, data, {
        headers: {
            "content-type": ContentTypes.AVAILABILITY_LIST
        }
    });
    return res.data;
}