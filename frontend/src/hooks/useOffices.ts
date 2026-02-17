import {keepPreviousData, useMutation, useQueries, useQuery, useQueryClient} from "@tanstack/react-query";
import {
    createDoctorOffice, deleteDoctorOffice, type DoctorAvailabilityFormDTO,
    type DoctorOfficeQuery, getDoctorAvailability,
    getDoctorOffice,
    getDoctorOffices,
    getDoctorOfficeSpecialties,
    type OfficeDTO,
    type OfficeSpecialtyDTO, putDoctorAvailability,
    updateDoctorOffice,
    type UpdateDoctorOfficeForm
} from "@/data/offices.ts";
import {buildDoctorOfficesUrl} from "@/utils/IdUtils.ts";
import type {CreateDoctorOfficeForm} from "@/data/doctors.ts";

export function useDoctorOffices(url?: string, query?: DoctorOfficeQuery) {
    return useQuery({
        queryKey: ['doctors', 'offices', 'list', url, query?.status],
        queryFn: () => getDoctorOffices(url!, query),
        enabled: !!url,
        placeholderData: keepPreviousData
    });
}

export function useDoctorOffice(url?: string) {
    return useQuery({
        queryKey: ['auth', 'doctors', 'offices', url],
        queryFn: () => getDoctorOffice(url!),
        enabled: !!url,
    })
}

export function useDoctorOfficeSpecialties(url?: string) {
    return useQuery({
        queryKey: ['doctors', 'offices', 'specialties', url],
        queryFn: () => getDoctorOfficeSpecialties(url!),
        enabled: !!url
    })
}

export function useDoctorOfficesSpecialties(offices?: OfficeDTO[]) {
    const queries = useQueries({
        queries: (offices ?? []).map((office) => ({
            queryKey: ['doctors', 'offices', 'specialties', office.officeSpecialties ?? null],
            queryFn: () => getDoctorOfficeSpecialties(office.officeSpecialties),
            enabled: !!office.officeSpecialties,
        })),
    });

    const isLoading = queries.some((q) => q.isLoading);
    const isFetching = queries.some((q) => q.isFetching);
    const isError = queries.some((q) => q.isError);

    const data = queries.map((q) => (q.data ?? []) as OfficeSpecialtyDTO[]);

    const refetch = () => Promise.all(queries.map((q) => q.refetch()));

    return { data, isLoading, isFetching, isError, refetch, queries };
}

export function useDoctorAvailability(doctorId?: string, officeId?: string) {
    return useQuery({
        queryKey: ['doctors', 'offices', 'availability', doctorId, officeId ?? 'all'],
        queryFn: () => getDoctorAvailability(doctorId!, officeId),
        enabled: !!doctorId,
    });
}

export function useUpdateDoctorAvailabilityMutation(doctorId?: string) {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (form: DoctorAvailabilityFormDTO) => putDoctorAvailability(doctorId, form),
        onSuccess: async () => {
            await queryClient.invalidateQueries({
                queryKey: ['doctors', 'offices', 'availability'],
            });
        },
    });
}

export function useUpdateOfficeMutation(url: string) {
    const queryClient = useQueryClient()
    return useMutation({
        mutationFn: (form: UpdateDoctorOfficeForm) => updateDoctorOffice(url, form),
        onSuccess: async () => {
           await Promise.all([
               queryClient.invalidateQueries({queryKey: ['auth', 'doctors', 'offices', url]}),
               queryClient.invalidateQueries({queryKey: ['doctors', 'offices', 'list']}),
               queryClient.invalidateQueries({queryKey: ['doctors', 'offices', 'specialties']})
           ])
        }
    })
}

export function useDeleteOfficeMutation(url: string) {
    const queryClient = useQueryClient()
    return useMutation({
        mutationFn: () => deleteDoctorOffice(url),
        onSuccess: async () => {
            await Promise.all([
                queryClient.invalidateQueries({queryKey: ['doctors', 'offices', 'list']}),
                queryClient.invalidateQueries({queryKey: ['doctors', 'offices', 'availability']}),
            ])
        }
    })
}

export function useCreateDoctorOfficeMutation(id?: string) {
    const queryClient = useQueryClient()
    return useMutation({
        mutationFn: (form: CreateDoctorOfficeForm) => createDoctorOffice(buildDoctorOfficesUrl(id!), form),
        onSuccess: async () => {
            await queryClient.invalidateQueries({queryKey: ['doctors', 'offices', 'list']})
        }
    })
}