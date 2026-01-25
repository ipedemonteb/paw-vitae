import {api} from "@/data/Api.ts";
import {ContentTypes} from "@/utils/contentTypes.js";
import type {PaginationData} from "@/lib/types.ts";
import {parseLinkHeader} from "@/lib/utils.ts";
import type {CoverageDTO} from "@/data/coverages.ts";
import type {SpecialtyDTO} from "@/data/specialties.ts";
import type {OfficeDTO} from "@/data/office.ts";
import {DOCTORS_PAGE_SIZE} from "@/lib/constants.ts";
import qs from 'qs';

export type ChangePasswordForm = {
    password: string;
    repeatPassword: string;
}
export type CertificateForm = {
    certificateName: string;
    issuingEntity: string;
    issueDate: string;
}
export type ExperienceForm = {
    positionTitle: string;
    organizationName: string;
    startDate: string;
    endDate?: string;
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
    image: string;
    unavailability: string;
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
    pageSize?: number;
};

export interface DoctorRegisterData {
    name: string;
    lastName: string;
    email: string;
    phone: string;
    password: string;
    repeatPassword: string;
    selectedSpecialties: string[];
    selectedCoverages: string[];
}
export interface DoctorUpdateForm{
    name?: string;
    lastName?: string;
    phone?: string;
    specialties?: string[];
    coverages?: string[];
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
    bio: string;
    description: string;
    doctor: string;
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

export interface UnavailabilityDTO {
    startDate: string;
    endDate: string;
    doctor: string;
}

export type DoctorOfficeQuery = {
    status: string
}

export type CreateDoctorOfficeForm = {
    officeName: string,
    specialtyIds: number[],
    neighborhoodId: number,
}

export type UpdateDoctorOfficeForm = Partial<CreateDoctorOfficeForm> & {
    active?: boolean,
    removed?: boolean
}


export interface AvailabilitySlotForm {
    dayOfWeek: number;
    startTime: string; // Formato "HH:mm:00"
    endTime: string;   // Formato "HH:mm:00"
}
export interface DoctorAvailabilityFormDTO {
    doctorOfficeAvailabilities: AvailabilitySlotForm[];
}

export interface UnavailabilityForm {
    endDate: string;
    startDate: string;
}

export interface DoctorUnavailabilityFormDTO{
    unavailabilitySlots: UnavailabilityForm[];
}

const extractIdFromUrl = (url: string): string => {
    if (!url) return "";
    const segments = url.replace(/\/$/, "").split("/");
    return segments.pop() || "";
}

function normalizeDoctorsQuery(query: DoctorsQuery) {
    if (Number.isNaN(query.specialty)) query.specialty = 0;
    if (Number.isNaN(query.coverage)) query.coverage = 0;
    if (Number.isNaN(query.page)) query.page = 1;
    if (Number.isNaN(query.pageSize)) query.pageSize = DOCTORS_PAGE_SIZE;
    if (query.weekdays?.length === 0) query.weekdays = undefined;
    query.weekdays = query.weekdays?.filter(w => !Number.isNaN(w))
}

export async function listDoctors(params: DoctorsQuery): Promise<PaginationData<DoctorDTO[]>> {
    normalizeDoctorsQuery(params)
    const res = await api.get<DoctorDTO[]>("/doctors", {
        params: {
            specialty: params.specialty,
            coverage: params.coverage,
            weekdays: params.weekdays,
            keyword: params.keyword,
            orderBy: params.orderBy,
            direction: params.direction,
            page: params.page,
            pageSize: params.pageSize
        },
        paramsSerializer: (p) => qs.stringify(p, { arrayFormat: "repeat" }),
        headers: {
            Accept: ContentTypes.DOCTOR_LIST
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
    const res = await api.patch(url, form,{
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

export async function getDoctorUnavailability(url: string) {
    const res = await api.get<UnavailabilityDTO[]>(url, {
        headers: {"accept": ContentTypes.UNAVAILABILITY_LIST, }
    });
    return res.data
}

export async function putDoctorCertificates(certificationsUrl: string, certificatesList: CertificateForm[]) {
    const payload = {
        certificates: certificatesList
    };
    const res = await api.put(certificationsUrl, payload, {
        headers: {
            "content-type":  ContentTypes.DOCTOR_CERTIFICATION_LIST
        }
    } );
    return res.data;
}

export async function putDoctorProfile(profileUrl: string, biography: string, description: string) {
    const payload = {
        biography,
        description
    };
    const res = await api.put(profileUrl,payload,{
        headers: {
            "content-type":  ContentTypes.DOCTOR_PROFILE
        }
    } );
    return res.data;
}

export async function putDoctorExperiences(experiencesUrl: string, experiencesList: ExperienceForm[]) {
    const payload = {
        experiences: experiencesList
    };
    const res = await api.put(experiencesUrl, payload, {
        headers: {
            "content-type":  ContentTypes.DOCTOR_EXPERIENCE_LIST
        }
    } );
    return res.data;
}

export async function updateDoctor(doctorUrl:string, form: DoctorUpdateForm){
    const res =await api.patch(doctorUrl,form,{
        headers: {
            "content-type": ContentTypes.DOCTOR
        }
    });
    return res.data;
}

export async function putDoctorImage(doctorImageUrl: string, image: File) {
    const formData = new FormData();
    formData.append("file", image);

    const res = await api.put(doctorImageUrl, formData);
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

export async function putDoctorUnavailability(url: string, data:DoctorUnavailabilityFormDTO){
    const res = await api.put(url, data, {
        headers: {
            "content-type" : ContentTypes.UNAVAILABILITY_LIST
        }
    });
    return res.data;
}

export async function updateDoctorProfileComplete(params: {
    doctorUrl: string;
    imageUrl: string;
    data: DoctorUpdateForm;
    imageFile?: File | null;
}) {
    if (params.imageFile) {
        await putDoctorImage(params.imageUrl, params.imageFile);
    }

    return updateDoctor(params.doctorUrl, params.data);
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