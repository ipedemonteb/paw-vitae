import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {
    fetchCountsPatient,
    getPatient,
    getPatientById,
    type PatientRegisterData, type PatientUpdateData,
    registerPatient, updatePatient
} from "@/data/patients.ts";
import type {AxiosError} from "axios";

export function usePatient(url?: string) {
    return useQuery({
        queryKey: ['auth', 'patients', url],
        queryFn: () => getPatient(url!),
        enabled: !!url
    })
}

export function usePatientById(id?: string) {
    return useQuery({
        queryKey: ['auth', 'patients', 'id', id],
        queryFn: () => getPatientById(id!),
        enabled: !!id,
    });
}

export function useRegisterPatientMutation(){
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
export function useUpdatePatientMutation(url:string){
    const queryClient = useQueryClient()
    return useMutation<any, AxiosError<any>, PatientUpdateData>({
        mutationFn:(data :PatientUpdateData) =>  updatePatient(url,data),
        onSuccess: async () => {
            await queryClient.invalidateQueries({queryKey: ['auth', 'patients']})
        }
    });
}