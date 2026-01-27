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
    registerDoctor,
    fetchCountDoctors,
    getDoctorExperiences,
    type ExperienceForm,
    putDoctorExperiences,
    putDoctorCertificates,
    putDoctorProfile, type CertificateForm,
    getDoctorUnavailability,
    type DoctorUpdateForm,
    updateDoctor,
    putDoctorImage,
    updateDoctorProfileComplete,
    type DoctorUnavailabilityFormDTO, putDoctorUnavailability,
} from "@/data/doctors";
import {keepPreviousData, useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {useEffect, useState, useMemo} from "react";
import type {AxiosError} from "axios";

export function useDoctors(query: DoctorsQuery) {
    const stableQueryKey = useMemo(() => JSON.stringify(query ?? {}), [query]);
    return useQuery({
        queryKey: ['auth', 'doctors', stableQueryKey],
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


export function useRegisterDoctorMutation() {
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

export function usePutDoctorUnavailability(url:string){
    const queryClient = useQueryClient();

    return useMutation<any, AxiosError<any>, DoctorUnavailabilityFormDTO>({
        mutationFn: (data: DoctorUnavailabilityFormDTO) => putDoctorUnavailability(url, data),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['doctor', 'unavailability'] });
        }
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