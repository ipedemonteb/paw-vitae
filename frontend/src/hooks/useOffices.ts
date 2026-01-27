import {keepPreviousData, useMutation, useQueries, useQuery, useQueryClient} from "@tanstack/react-query";
import {
    type AvailabilityDTO, createDoctorOffice,
    type DoctorAvailabilityFormDTO,
    type DoctorOfficeQuery,
    getDoctorOffice,
    getDoctorOfficeAvailability,
    getDoctorOffices,
    getDoctorOfficeSpecialties,
    type OfficeDTO,
    type OfficeSpecialtyDTO,
    putDoctorOfficeAvailability,
    updateDoctorOffice,
    type UpdateDoctorOfficeForm
} from "@/data/offices.ts";
import type {AxiosError} from "axios";
import {buildDoctorOfficesUrl} from "@/utils/IdUtils.ts";
import type {CreateDoctorOfficeForm} from "@/data/doctors.ts";

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
        queryKey: ['doctor', 'offices', url],
        queryFn: () => getDoctorOffice(url!),
        enabled: !!url,
    })
}

export function useDoctorOfficeSpecialties(url?: string) {
    return useQuery({
        queryKey: ['doctor', 'offices', 'specialties', url],
        queryFn: () => getDoctorOfficeSpecialties(url!),
        enabled: !!url
    })
}

export function useDoctorOfficesSpecialties(offices?: OfficeDTO[] | null) {
    const queries = useQueries({
        queries: (offices ?? []).map((office) => ({
            queryKey: ['doctor', 'offices', 'specialties', office.officeSpecialties ?? []],
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
            queryKey: ['doctor', 'offices', 'availability', office.officeAvailability ?? []],
            queryFn: () => getDoctorOfficeAvailability(office.officeAvailability),
            enabled: !!office.officeAvailability,
        })),
    });

    const isLoading = queries.some((q) => q.isLoading);
    const isError = queries.some((q) => q.isError);

    const data = queries.map((q) => (q.data ?? []) as AvailabilityDTO[]);

    return { data, isLoading, isError };
}

export function usePutDoctorOfficeAvailability(url: string) {
    const queryClient = useQueryClient();

    return useMutation<any, AxiosError<any>, DoctorAvailabilityFormDTO>({
        mutationFn: (data: DoctorAvailabilityFormDTO) => putDoctorOfficeAvailability(url, data),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['doctor', 'offices', 'availability'] });
        }
    });
}

export function useUpdateOfficeMutation(url: string) {
    const queryClient = useQueryClient()
    return useMutation({
        mutationFn: (form: UpdateDoctorOfficeForm) => updateDoctorOffice(url, form),
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: ['offices']})
        }
    })
}

export function useCreateDoctorOfficeMutation(id: string) {
    const queryClient = useQueryClient()
    const url = buildDoctorOfficesUrl(id);
    return useMutation({
        mutationFn: (form: CreateDoctorOfficeForm) => createDoctorOffice(url, form),
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: ['doctor', 'offices', url]})
        }
    })
}