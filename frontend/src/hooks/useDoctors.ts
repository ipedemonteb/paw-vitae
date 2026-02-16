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
    putDoctorImage,
    updateDoctorProfileComplete,
    type UnavailabilityQuery, createDoctorUnavailability, type UnavailabilityForm, deleteDoctorUnavailability,
} from "@/data/doctors";
import {keepPreviousData, useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {useEffect, useState, useMemo} from "react";
import type {AxiosError} from "axios";

export function useDoctors(query: DoctorsQuery) {
    const stableQueryKey = useMemo(() => JSON.stringify(query ?? {}), [query]);
    return useQuery({
        queryKey: ['auth', 'doctors', 'list', stableQueryKey],
        queryFn: () => listDoctors(query),
        placeholderData: keepPreviousData
    })
}

export function useDoctor(userId?: string) {
    return useQuery({
        queryKey: ['auth', 'doctors', userId],
        queryFn: () => getDoctor(userId!),
        enabled: !!userId
    });
}

const isNumericId = (id?: string) => !!id && /^\d+$/.test(id);

export function useDoctorImageUrl(id?: string | undefined) {
    const enabled = useMemo(
        () => isNumericId(id),
        [id]
    );

    const query = useQuery({
        queryKey: ['auth', 'doctors', 'image', id],
        queryFn: () => getDoctorImage(id!),
        enabled: enabled,
        staleTime: 5 * 60_000,
        retry: false
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

export function useDoctorSpecialties(url?: string) {
    return useQuery({
        queryKey: ['doctors', 'specialties', url],
        queryFn: () => getDoctorSpecialties(url!),
        enabled: !!url,
    });
}

export function useDoctorCoverages(url?: string) {
    return useQuery({
        queryKey: ['doctors', 'coverages', url],
        queryFn: () => getDoctorCoverages(url!),
        enabled: !!url,
    });
}

export function useDoctorExperience(url?: string) {
    return useQuery({
        queryKey: ['doctors', 'experiences', url],
        queryFn: () => getDoctorExperiences(url!),
        enabled: !!url,
    });
}

export function useDoctorCertifications(url?: string) {
    return useQuery({
        queryKey: ['doctors', 'certifications', url],
        queryFn: () => getDoctorCertifications(url!),
        enabled: !!url,
    });
}

export function useDoctorBiography(url?: string) {
    return useQuery({
        queryKey: ['doctors', 'bio', url],
        queryFn: () => getDoctorBiography(url!),
        enabled: !!url,
    });
}

export function useDoctorUnavailability(url?: string, query?: UnavailabilityQuery) {
    return useQuery({
        queryKey: ['doctors', 'unavailability', url, query?.from, query?.to, query?.page],
        queryFn: () => getDoctorUnavailability(url!, query),
        enabled: !!url,
        placeholderData: keepPreviousData
    });
}

export function useDoctorsCount() {
    return useQuery<number>({
        queryKey: ['doctors', 'counts'],
        queryFn: () => fetchCountDoctors(),
        staleTime: 1000 * 60,
        retry: 1
    });
}

export function  useUpdateDoctorExperienceMutation(url: string ) {
    const queryClient = useQueryClient()
    return useMutation<any, AxiosError<any>, ExperienceForm[]>({
        mutationFn: (data: ExperienceForm[]) => putDoctorExperiences(url,data),
        onSuccess: async () => {
            await queryClient.invalidateQueries({queryKey: ['doctors', 'experiences']})
        }
    });
}

export function  useUpdateDoctorProfileMutation(url: string ) {
    const queryClient = useQueryClient()
    return useMutation<any, AxiosError<any>, {biography: string, description: string}>({
        mutationFn: (data :{biography: string, description: string}) => putDoctorProfile(url,data.biography, data.description),
        onSuccess: async () => {
           await queryClient.invalidateQueries({queryKey: ['doctors', 'bio']})
        }
    });
}

export function useUpdateDoctorCertificatesMutation(url: string ) {
    const queryClient = useQueryClient()
    return useMutation<any, AxiosError<any>, CertificateForm[]>({
        mutationFn: (data: CertificateForm[]) => putDoctorCertificates(url,data),
        onSuccess: async () => {
            await queryClient.invalidateQueries({queryKey: ['doctors', 'certifications']})
        }
    });
}

export function useUpdateDoctorImageMutation(url:string){
    const queryClient = useQueryClient()
    return useMutation<any,AxiosError<any>,File>({
        mutationFn: (data: File) => putDoctorImage(url,data),
        onSuccess: async () => {
            await queryClient.invalidateQueries({queryKey: ['auth', 'doctors', 'image']})
        }
    });
}

export function useUpdateDoctorMutation() {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (params: {
            doctorUrl: string;
            doctorId: string;
            data: DoctorUpdateForm;
            imageFile?: File | null;
        }) => updateDoctorProfileComplete(params),

        onSuccess: async (_, variables) => {
            await Promise.all([
                queryClient.invalidateQueries({ queryKey: ['auth', 'doctors', variables.doctorId], exact: true }),
                queryClient.invalidateQueries({ queryKey: ['auth', 'doctors', 'image'] }),
                queryClient.invalidateQueries({ queryKey: ['auth', 'doctors', 'list'] }),
                queryClient.invalidateQueries({ queryKey: ['doctors', 'specialties'] }),
                queryClient.invalidateQueries({ queryKey: ['doctors', 'coverages'] })
            ])
        }

    });
}

export function useCreateDoctorUnavailabilityMutation(url: string){
    const queryClient = useQueryClient();
    return useMutation<any, AxiosError<any>, UnavailabilityForm>({
        mutationFn: (data: UnavailabilityForm) => createDoctorUnavailability(url, data),
        onSuccess: async () => {
            await queryClient.invalidateQueries({ queryKey: ['doctors', 'unavailability'] });
        }
    });
}

export function useDeleteDoctorUnavailabilityMutation(url: string){
    const queryClient = useQueryClient();
    return useMutation<any, AxiosError<any>, number>({
        mutationFn: (slotId: number) => deleteDoctorUnavailability(url, slotId),
        onSuccess: async () => {
            await queryClient.invalidateQueries({ queryKey: ['doctors', 'unavailability'] });
        }
    });
}