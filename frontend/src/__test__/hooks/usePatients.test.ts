import { describe, it, expect } from 'vitest';
import { renderHook, waitFor, act } from "@/__test__/setup/utils.tsx";
import { BASE_URL } from "@/__test__/utils/utils.ts";
import {
    usePatient,
    usePatientById,
    usePatientsCount,
    useRegisterPatientMutation,
    useUpdatePatientMutation
} from "@/hooks/usePatients.ts";

describe('Patient Hooks Integration Tests', () => {


    describe('usePatient (Get by URL)', () => {
        it('debería traer un paciente usando su URL HATEOAS', async () => {
            const url = `${BASE_URL}/patients/1`;
            const { result } = renderHook(() => usePatient(url));

            expect(result.current.isLoading).toBe(true);

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data).toBeDefined();
            expect(result.current.data?.self).toContain('/patients/1');
        });
    });

    describe('usePatientById (Get by ID)', () => {
        it('debería traer un paciente usando solo su ID', async () => {
            const { result } = renderHook(() => usePatientById('1'));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data?.self).toContain('/patients/1');
        });

        it('debería manejar errores del servidor', async () => {
            const { result } = renderHook(() => usePatientById('error'));

            await waitFor(() => expect(result.current.isError).toBe(true));
        });
    });

    describe('usePatientsCount (HEAD)', () => {
        it('debería retornar la cantidad total de pacientes', async () => {
            const { result } = renderHook(() => usePatientsCount());

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data).toBe(3);
        });
    });

    describe('Mutations', () => {

        it('useRegisterPatientMutation debería registrar un paciente', async () => {
            const { result } = renderHook(() => useRegisterPatientMutation());

            const newPatientData = {
                name: 'Jane',
                lastName: 'Doe',
                email: 'jane@example.com',
                phone: '12345678',
                password: 'password123',
                repeatPassword: 'password123',
                coverageUrl: `${BASE_URL}/coverages/5`,
                neighborhoodUrl: `${BASE_URL}/neighborhoods/10`
            };

            act(() => {
                result.current.mutate(newPatientData);
            });

            await waitFor(() => expect(result.current.isSuccess).toBe(true));
        });

        it('useUpdatePatientMutation debería actualizar datos del paciente', async () => {
            const url = `${BASE_URL}/patients/1`;
            const { result } = renderHook(() => useUpdatePatientMutation(url));

            const updateData = {
                name: 'Jane Updated',
                phone: '99999999'
            };

            act(() => {
                result.current.mutate(updateData);
            });

            await waitFor(() => expect(result.current.isSuccess).toBe(true));
        });
    });

});