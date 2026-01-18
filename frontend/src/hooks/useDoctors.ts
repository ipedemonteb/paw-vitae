import {
    listDoctors,
    type DoctorsQuery,
    getDoctor,
    getDoctorImage,
    type DoctorRegisterData,
    getDoctorCertifications,
    getDoctorBiography,
    getDoctorCoverages,
    getDoctorSpecialties,
    getDoctorOffices,
    registerDoctor,
    fetchCountDoctors,
    getDoctorExperiences,
    getDoctorOfficeSpecialties,
    type ExperienceForm,
    putDoctorExperiences,
    putDoctorCertificates,
    putDoctorProfile, type CertificateForm, type OfficeSpecialtyDTO, getDoctorOfficeAvailability, type AvailabilityDTO,
    getDoctorUnavailability, getDoctorOffice, type DoctorOfficeQuery,
    type DoctorUpdateForm,
    updateDoctor,
    putDoctorImage,
    updateDoctorProfileComplete
} from "@/data/doctors";
import {keepPreviousData, useMutation, useQueries, useQuery, useQueryClient} from "@tanstack/react-query";
import {useEffect, useState, useMemo} from "react";
import type {AxiosError} from "axios";
import type {OfficeDTO} from "@/data/office.ts";

export function useDoctors(query: DoctorsQuery) {
    return useQuery({
        queryKey: ['auth', 'doctors', query],
        queryFn: () => listDoctors(query),
        placeholderData: keepPreviousData
    })
}

export function useDoctor(userId?: string | null, options?: { enabled?: boolean }) {
    return useQuery({
        queryKey: ['auth', 'doctor', userId],
        queryFn: () => getDoctor(userId!),
        enabled: !!userId && (options?.enabled ?? true),
    });
}

const isNumericId = (id?: string) => !!id && /^\d+$/.test(id);

export function useDoctorImageUrl(id?: string | null, options?: { enabled?: boolean }) {
    const enabled = useMemo(
        () => isNumericId(id ?? undefined) && (options?.enabled ?? true),
        [id, options?.enabled]
    );

    const query = useQuery({
        queryKey: ["auth", "doctor", id, "image"],
        queryFn: () => getDoctorImage(id!),
        enabled,
        staleTime: 5 * 60_000,
    });

    const [url, setUrl] = useState<string | null>(null);

    useEffect(() => {
        if (!query.data) {
            setUrl(null);
            return;
        }
        const objectUrl = URL.createObjectURL(query.data);
        setUrl(objectUrl);
        return () => URL.revokeObjectURL(objectUrl);
    }, [query.data]);

    return { ...query, url };
}


export function useRegisterDoctor() {
    return useMutation<any, AxiosError<any>, DoctorRegisterData>({
        mutationFn: (data: DoctorRegisterData) => registerDoctor(data)
    });
}

export function useDoctorSpecialties(url?: string | null) {
    return useQuery({
        queryKey: ['doctor', 'specialties', url],
        queryFn: () => getDoctorSpecialties(url!),
        enabled: !!url,
    });
}

export function useDoctorOfficeSpecialties(url?: string) {
    return useQuery({
        queryKey: ['doctor', 'office', 'specialties', url],
        queryFn: () => getDoctorOfficeSpecialties(url!),
        enabled: !!url
    })
}

export function useDoctorCoverages(url?: string | null) {
    return useQuery({
        queryKey: ['doctor', 'coverages', url],
        queryFn: () => getDoctorCoverages(url!),
        enabled: !!url,
    });
}

export function useDoctorExperience(url?: string | null) {
    return useQuery({
        queryKey: ['doctor', 'experiences', url],
        queryFn: () => getDoctorExperiences(url!),
        enabled: !!url,
    });
}

export function useDoctorCertifications(url?: string | null) {
    return useQuery({
        queryKey: ['doctor', 'certifications', url],
        queryFn: () => getDoctorCertifications(url!),
        enabled: !!url,
    });
}

export function useDoctorBiography(url?: string | null) {
    return useQuery({
        queryKey: ['doctor', 'bio', url],
        queryFn: () => getDoctorBiography(url!),
        enabled: !!url,
    });
}

export function useDoctorOffices(url?: string, query?: DoctorOfficeQuery) {
    return useQuery({
        queryKey: ['doctor', 'offices', url, query?.status],
        queryFn: () => getDoctorOffices(url!, query),
        enabled: !!url,
        placeholderData: keepPreviousData
    });
}

export function useDoctorOffice(url?: string | null) {
    return useQuery({
        queryKey: ['doctor', 'office', url],
        queryFn: () => getDoctorOffice(url!),
        enabled: !!url,
    })
}

export function useDoctorOfficesSpecialties(offices?: OfficeDTO[] | null) {
    const queries = useQueries({
        queries: (offices ?? []).map((office) => ({
            queryKey: ['doctor', 'office', 'specialties', office.officeSpecialties ?? []],
            queryFn: () => getDoctorOfficeSpecialties(office.officeSpecialties),
            enabled: !!office.officeSpecialties,
        })),
    });

    const isLoading = queries.some((q) => q.isLoading);
    const isError = queries.some((q) => q.isError);

    const data = queries.map((q) => (q.data ?? []) as OfficeSpecialtyDTO[]);

    return { data, isLoading, isError };
}

export function useDoctorOfficeAvailability(offices?: OfficeDTO[] | null) {
    const queries = useQueries({
        queries: (offices ?? []).map((office) => ({
            queryKey: ['doctor', 'office', 'availability', office.officeAvailability ?? []],
            queryFn: () => getDoctorOfficeAvailability(office.officeAvailability),
            enabled: !!office.officeAvailability,
        })),
    });

    const isLoading = queries.some((q) => q.isLoading);
    const isError = queries.some((q) => q.isError);

    const data = queries.map((q) => (q.data ?? []) as AvailabilityDTO[]);

    return { data, isLoading, isError };
}

export function useDoctorUnavailability(url?: string | null) {
    return useQuery({
        queryKey: ['doctor', 'unavailability', url],
        queryFn: () => getDoctorUnavailability(url!),
        enabled: !!url,
    });
}

export function useDoctorsCount() {
    return useQuery<number>({
        queryKey: ['counts', 'doctors'],
        queryFn: () => fetchCountDoctors(),
        staleTime: 1000 * 60,
        retry: 1
    });
}

export function  usePutDoctorExperience(url: string ) {
    return useMutation<any, AxiosError<any>, ExperienceForm[]>({
        mutationFn: (data: ExperienceForm[]) => putDoctorExperiences(url,data)
    });
}
export function  usePutDoctorProfile(url: string ) {
    return useMutation<any, AxiosError<any>, {biography: string, description: string}>({
        mutationFn: (data :{biography: string, description: string}) => putDoctorProfile(url,data.biography, data.description)
    });
}
export function usePutDoctorCertificates(url: string ) {
    return useMutation<any, AxiosError<any>, CertificateForm[]>({
        mutationFn: (data: CertificateForm[]) => putDoctorCertificates(url,data)
    });
}

export function useUpdateDoctor(url:string){
    return useMutation<any,AxiosError<any>,DoctorUpdateForm>({
        mutationFn: (data: DoctorUpdateForm) => updateDoctor(url,data)
    });
}
export function usePutDoctorImage(url:string){
    return useMutation<any,AxiosError<any>,File>({
        mutationFn: (data: File) => putDoctorImage(url,data)
        });
}
export function useUpdateDoctorProfile() {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (params: {
            doctorUrl: string;
            imageUrl: string;
            data: DoctorUpdateForm;
            imageFile?: File | null;
            doctorId: string;
        }) => updateDoctorProfileComplete(params),

        onSuccess: (_, variables) => {
            queryClient.invalidateQueries({ queryKey: ['auth', 'doctor', variables.doctorId] });
            queryClient.invalidateQueries({ queryKey: ['doctor', 'image', variables.doctorId] });
            queryClient.invalidateQueries({ queryKey: ['doctor', 'specialties'] });
            queryClient.invalidateQueries({ queryKey: ['doctor', 'coverages'] });
        }

    });
}