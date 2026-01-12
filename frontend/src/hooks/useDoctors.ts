import {
    listDoctors,
    type DoctorsQuery,
    getDoctor,
    getDoctorImage,
    type DoctorRegisterData,
    registerDoctor,
    getDoctorCertifications,
    getDoctorBiography,
    getDoctorExperiences,
    getDoctorCoverages,
    getDoctorSpecialties
    registerDoctor, fetchCountDoctors
} from "@/data/doctors";
import {keepPreviousData, useMutation, useQuery} from "@tanstack/react-query";
import {useEffect, useState, useMemo} from "react";
import type {AxiosError} from "axios";

export function useDoctors(query: DoctorsQuery) {
    return useQuery({
        queryKey: ['auth', 'doctors', query],
        queryFn: () => listDoctors(query),
        placeholderData: keepPreviousData
    })
}

export function useDoctor(userId?: string | null, options?: { enabled?: boolean }) {
    return useQuery({
        queryKey: ["doctor", userId],
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

export function useDoctorOffices(url?: string | null) {
    return useQuery({
        queryKey: ['doctor', 'offices', url],
        queryFn: () => getDoctorBiography(url!),
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