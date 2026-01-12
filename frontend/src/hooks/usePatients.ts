import {useMutation, useQuery} from "@tanstack/react-query";
import {getPatient, type PatientRegisterData, registerPatient} from "@/data/patients.ts";
import type {AxiosError} from "axios";

export function usePatient(url?: string) {
    return useQuery({
        queryKey: ['auth', 'patients', url],
        queryFn: () => getPatient(url!),
        enabled: !!url
    })
}

export function useRegisterPatient(){
    return useMutation<any, AxiosError<any>, PatientRegisterData>({
        mutationFn: (data: PatientRegisterData) => registerPatient(data)
    });
}