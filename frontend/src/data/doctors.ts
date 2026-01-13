import {api} from "@/data/Api.ts";
import {ContentTypes} from "@/utils/contentTypes.js";
import type {PaginationData} from "@/lib/types.ts";
import {parseLinkHeader} from "@/lib/utils.ts";
import type {CoverageDTO} from "@/data/coverages.ts";
import type {SpecialtyDTO} from "@/data/specialties.ts";
import type {OfficeDTO} from "@/data/office.ts";


export type ChangePasswordForm = {
    password: string;
    repeatPassword: string;
}

export type DoctorDTO = {
    name: string;
    lastName: string;
    email: string;
    phone: string;
    rating: number;
    ratingCount: number;
    specialties: string;
    coverages: string;
    offices: string;
    profile: string;
    experiences: string;
    certifications: string;
    ratings: string;
    appointments: string;
    self: string;

};

export type DoctorsQuery = {
    specialty?: number;
    coverage?: number;
    weekdays?: number[];
    keyword?: string;
    orderBy?: string;
    direction?: "asc" | "desc";
    page?: number;
};

export interface DoctorRegisterData {
    name: string;
    lastName: string;
    email: string;
    phone: string;
    password: string;
    repeatPassword: string;
    image: File | null;
    selectedSpecialties: string[];
    selectedCoverages: string[];
}

export interface ExperienceDTO {
    positionTitle: string;
    organizationName: string;
    startDate: string;
    endDate?: string;
    doctor: string;
}
export interface CertificationDTO {
    certificateName: string;
    issuingEntity: string;
    issueDate: string;
    doctor: string;
}
export interface DoctorProfileDTO {
    biography: string;
    description: string;
    doctor: string;
}
const extractIdFromUrl = (url: string): string => {
    if (!url) return "";
    const segments = url.replace(/\/$/, "").split("/");
    return segments.pop() || "";
}

export async function listDoctors(params: DoctorsQuery): Promise<PaginationData<DoctorDTO[]>> {
    const res = await api.get<DoctorDTO[]>("/doctors", {
        params: {
            specialty: params.specialty ?? 0,
            coverage: params.coverage ?? 0,
            weekdays: params.weekdays, // axios will repeat ?weekdays=1&weekdays=2
            keyword: params.keyword ?? "",
            orderBy: params.orderBy ?? "name",
            direction: params.direction ?? "asc",
            page: params.page ?? 1,
        },
        headers: {
            "accept": ContentTypes.DOCTOR_LIST
        }
    });
    const total = Number(res.headers["x-total-count"]);
    const links = parseLinkHeader(res.headers["link"]);
    return {
        data: res.data,
        pagination: {
            next: links.next,
            prev: links.prev,
            first: links.first,
            last: links.last,
            self: links.self,
            total: total
        }
    };
}

export async function changeDoctorPassword(url: string, form: ChangePasswordForm) {
    const res = await api.patch(`${url}`,form,{
        headers: {
            "content-type":  ContentTypes.USER_PASSWORD
        }
    } );
    return res.data;
}

export async function getDoctor(id: string) {
    const res = await api.get<DoctorDTO>(`/doctors/${id}`,{
        headers: {
            "accept": ContentTypes.DOCTOR
        }
    });
    return res.data;
}

export async function getDoctorImage(id: string) {
    const res = await api.get<Blob>(`/doctors/${id}/image`,
        { responseType: "blob" }
    );
    return res.data;
}

export async function  registerDoctor (data: DoctorRegisterData){
    const specialtyIds: number[] = [];
    data.selectedSpecialties.forEach(url => {
        const id = extractIdFromUrl(url);
        if (id) specialtyIds.push(parseInt(id));
    });

    const coverageIds: number[] = [];
    data.selectedCoverages.forEach(url => {
        const id = extractIdFromUrl(url);
        if (id) coverageIds.push(parseInt(id));
    });

    const payload = {
        name: data.name,
        lastName: data.lastName,
        email: data.email,
        phone: data.phone,
        password: data.password,
        repeatPassword: data.repeatPassword,
        specialties: specialtyIds,
        coverages: coverageIds
    };


    return api.post('/doctors', payload, {
        headers: {
            'Content-Type': ContentTypes.DOCTOR,
        }
    });
}


export async function fetchCountDoctors(): Promise<number> {
    const res = await api.head('/doctors')
    const header = res.headers['x-total-count'] ?? res.headers['X-Total-Count'];
    const parsed = parseInt(String(header ?? ''), 10);
    return Number.isNaN(parsed) ? 0 : parsed;
}

export async function getDoctorSpecialties(specialtyUrl: string) {
    const res = await api.get<SpecialtyDTO[]>(specialtyUrl,{
        headers: {"accept": ContentTypes.SPECIALTY_LIST,}
    } );
    return res.data;
}
export async function getDoctorCoverages(coverageUrl: string) {
    const res = await api.get<CoverageDTO[]>(coverageUrl,{
        headers: {"accept": ContentTypes.COVERAGE_LIST,}
    } );
    return res.data;
}
export async function getDoctorExperiences(experiencesUrl: string) {
    const res = await api.get<ExperienceDTO[]>(experiencesUrl,{
        headers: {"accept": ContentTypes.DOCTOR_EXPERIENCE_LIST,}
    } );
    return res.data;
}
export async function getDoctorCertifications(certificationsUrl: string) {
    const res = await api.get<CertificationDTO[]>(certificationsUrl,{
        headers: {"accept": ContentTypes.DOCTOR_CERTIFICATION_LIST,}
    } );
    return res.data;
}
export async function getDoctorBiography(profileUrl: string) {
    const res = await api.get<DoctorProfileDTO>(profileUrl,{
        headers: {"accept": ContentTypes.DOCTOR_PROFILE,}
    } );
    return res.data;
}
export async function getDoctorOffices(officesUrl: string) {
    const res = await api.get<OfficeDTO[]>(officesUrl,{
        headers: {"accept": ContentTypes.OFFICE_LIST,}
    } );
    return res.data;
}