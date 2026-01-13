import {useMutation, useQuery} from "@tanstack/react-query";
import {
    fetchCountsPatient,
    getPatient,
    getPatientById,
    type PatientRegisterData,
    registerPatient
} from "@/data/patients.ts";
import type {AxiosError} from "axios";

export function usePatient(url?: string) {
    return useQuery({
        queryKey: ['auth', 'patients', url],
        queryFn: () => getPatient(url!),
        enabled: !!url
    })
}

export function usePatientById(
    id?: string,
    opts?: { enabled?: boolean }
) {
    return useQuery({
        queryKey: ["auth", "patients", "id", id],
        queryFn: () => getPatientById(id!),
        enabled: (opts?.enabled ?? true) && !!id,
    });
}

export function useRegisterPatient(){
    return useMutation<any, AxiosError<any>, PatientRegisterData>({
        mutationFn: (data: PatientRegisterData) => registerPatient(data)
    });
}


export function usePatientsCount() {
    return useQuery<number>({
        queryKey: ['counts', 'patient'],
        queryFn: () => fetchCountsPatient(),
        staleTime: 1000 * 60,
        retry: 1
    });
}