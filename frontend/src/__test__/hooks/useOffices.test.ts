import { describe, it, expect } from 'vitest';
import { renderHook, waitFor } from "@/__test__/setup/utils.tsx";
import { BASE_URL } from "@/__test__/utils/utils.ts";
import {
    useDoctorOffices,
    useDoctorOffice,
    useDoctorAvailability,
    useDoctorOfficesSpecialties,
    useCreateDoctorOfficeMutation,
    useUpdateOfficeMutation,
    useDeleteOfficeMutation,
    useUpdateDoctorAvailabilityMutation
} from "@/hooks/useOffices.ts";
import type {OfficeDTO} from "@/data/offices.ts";

describe('Doctor Office Hooks Integration Tests', () => {


    describe('useDoctorOffices (Listado)', () => {
        it('debería traer la lista de oficinas del doctor correctamente', async () => {
            const url = `${BASE_URL}/doctors/1/offices`;
            const { result } = renderHook(() => useDoctorOffices(url));

            expect(result.current.isLoading).toBe(true);

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data).toHaveLength(2);
            expect(result.current.data![0].name).toBe('Consultorio Belgrano');
            expect(result.current.data![1].status).toBe('inactive');
        });

        it('debería filtrar oficinas por estado si se solicita', async () => {
            const url = `${BASE_URL}/doctors/1/offices`;
            const { result } = renderHook(() => useDoctorOffices(url, { status: 'active' }));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data).toHaveLength(1);
            expect(result.current.data![0].status).toBe('active');
        });
    });

    describe('useDoctorOffice (Detalle)', () => {
        it('debería traer el detalle de una oficina específica', async () => {
            const url = `${BASE_URL}/doctors/1/offices/1`;
            const { result } = renderHook(() => useDoctorOffice(url));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data?.name).toBe('Office 1');
            expect(result.current.data?.self).toContain('/offices/1');
        });

        it('debería manejar errores si la oficina no existe', async () => {
            const url = `${BASE_URL}/doctors/1/offices/error`;
            const { result } = renderHook(() => useDoctorOffice(url));

            await waitFor(() => expect(result.current.isError).toBe(true));
        });
    });

    describe('useDoctorAvailability (Disponibilidad)', () => {
        it('debería traer toda la disponibilidad si no se filtra', async () => {
            const { result } = renderHook(() => useDoctorAvailability('1'));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data).toHaveLength(2);
        });

        it('debería filtrar disponibilidad por oficina', async () => {
            const { result } = renderHook(() => useDoctorAvailability('1', '2'));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data).toHaveLength(1);
            expect(result.current.data![0].dayOfWeek).toBe(3);
        });
    });


    describe('useDoctorOfficesSpecialties (useQueries)', () => {
        it('debería cargar especialidades de múltiples oficinas en paralelo', async () => {
            const mockOffices: Partial<OfficeDTO>[] = [
                { officeSpecialties: `${BASE_URL}/doctors/1/offices/1/specialties` },
                { officeSpecialties: `${BASE_URL}/doctors/1/offices/2/specialties` }
            ];

            const { result } = renderHook(() => useDoctorOfficesSpecialties(mockOffices as OfficeDTO[]));

            await waitFor(() => expect(result.current.isLoading).toBe(false));

            const allSpecialties = result.current.data;
            expect(allSpecialties).toHaveLength(2);

            expect(allSpecialties[0]).toBeDefined();
            expect(allSpecialties[0].length).toBeGreaterThan(0);
        });
    });


    describe('Mutations', () => {

        it('useCreateDoctorOfficeMutation debería crear una oficina (201)', async () => {
            const { result } = renderHook(() => useCreateDoctorOfficeMutation('1'));

            const newOfficeForm = {
                officeName: 'Nueva Sucursal',
                neighborhoodId: 1,
                address: 'Calle Falsa 123',
                phone: '123456',
                specialtyIds: [1, 2]
            };

            result.current.mutate(newOfficeForm);

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.error).toBeNull();
        });

        it('useUpdateOfficeMutation debería actualizar una oficina (204)', async () => {
            const url = `${BASE_URL}/doctors/1/offices/1`;
            const { result } = renderHook(() => useUpdateOfficeMutation(url));

            const updateForm = {
                active: false
            };

            result.current.mutate(updateForm);

            await waitFor(() => expect(result.current.isSuccess).toBe(true));
        });

        it('useDeleteOfficeMutation debería borrar una oficina (204)', async () => {
            const url = `${BASE_URL}/doctors/1/offices/1`;
            const { result } = renderHook(() => useDeleteOfficeMutation(url));

            result.current.mutate();

            await waitFor(() => expect(result.current.isSuccess).toBe(true));
        });

        it('useUpdateDoctorAvailabilityMutation debería guardar horarios (204)', async () => {
            const { result } = renderHook(() => useUpdateDoctorAvailabilityMutation('1'));

            const availabilityData = {
                doctorOfficeAvailabilities: [
                    { officeId: 1, dayOfWeek: 1, startTime: "08:00:00", endTime: "12:00:00" }
                ]
            };

            result.current.mutate(availabilityData);

            await waitFor(() => expect(result.current.isSuccess).toBe(true));
        });
    });

});