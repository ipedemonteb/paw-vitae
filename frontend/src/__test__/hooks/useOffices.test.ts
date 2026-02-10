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


    describe('useDoctorOffices (List)', () => {
        it('should bring the list of the offices of the doctor correctly', async () => {
            const url = `${BASE_URL}/doctors/1/offices`;
            const { result } = renderHook(() => useDoctorOffices(url));

            expect(result.current.isLoading).toBe(true);

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data).toHaveLength(2);
            expect(result.current.data![0].name).toBe('Consultorio Belgrano');
            expect(result.current.data![1].status).toBe('inactive');
        });

        it('should filter the offices by their status if asked for', async () => {
            const url = `${BASE_URL}/doctors/1/offices`;
            const { result } = renderHook(() => useDoctorOffices(url, { status: 'active' }));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data).toHaveLength(1);
            expect(result.current.data![0].status).toBe('active');
        });
    });

    describe('useDoctorOffice (Detail)', () => {
        it('should bring the details of a specific office', async () => {
            const url = `${BASE_URL}/doctors/1/offices/1`;
            const { result } = renderHook(() => useDoctorOffice(url));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data?.name).toBe('Office 1');
            expect(result.current.data?.self).toContain('/offices/1');
        });

        it('should handle errors if the office does not exist', async () => {
            const url = `${BASE_URL}/doctors/1/offices/error`;
            const { result } = renderHook(() => useDoctorOffice(url));

            await waitFor(() => expect(result.current.isError).toBe(true));
        });
    });

    describe('useDoctorAvailability (Availability)', () => {
        it('should bring all availability if not filtered', async () => {
            const { result } = renderHook(() => useDoctorAvailability('1'));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data).toHaveLength(2);
        });

        it('should filter availability by office', async () => {
            const { result } = renderHook(() => useDoctorAvailability('1', '2'));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data).toHaveLength(1);
            expect(result.current.data![0].dayOfWeek).toBe(3);
        });
    });


    describe('useDoctorOfficesSpecialties (useQueries)', () => {
        it('should parallel load specialties of multiple offices', async () => {
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

        it('should create an office (201)', async () => {
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

        it('should update an office (204)', async () => {
            const url = `${BASE_URL}/doctors/1/offices/1`;
            const { result } = renderHook(() => useUpdateOfficeMutation(url));

            const updateForm = {
                active: false
            };

            result.current.mutate(updateForm);

            await waitFor(() => expect(result.current.isSuccess).toBe(true));
        });

        it('should delete an office (204)', async () => {
            const url = `${BASE_URL}/doctors/1/offices/1`;
            const { result } = renderHook(() => useDeleteOfficeMutation(url));

            result.current.mutate();

            await waitFor(() => expect(result.current.isSuccess).toBe(true));
        });

        it('should update the schedule (204)', async () => {
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