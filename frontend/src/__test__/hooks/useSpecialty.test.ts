import { describe, it, expect } from 'vitest';
import { renderHook, waitFor } from '@/__test__/setup/utils';
import { useSpecialties, useSpecialty, useSpecialtiesByUrl } from '@/hooks/useSpecialties';

const BASE_URL = 'http://localhost:8080/api';

describe('Specialties Hooks', () => {

    describe('useSpecialties (List)', () => {
        it('deberia retornar una lista de especialdiades', async () => {
            const { result } = renderHook(() => useSpecialties());

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data).toHaveLength(4);
            expect(result.current.data![0].name).toBe('Cardiología');
        });
    });


    describe('useSpecialty (Single)', () => {
        it('deberia retornar una unica especialidad', async () => {
            const url = `${BASE_URL}/specialties/1`;
            const { result } = renderHook(() => useSpecialty(url));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));
            expect(result.current.data?.name).toBe('Cardiología');
        });
        it('deberia retornar error si no existe la especialidad', async () => {
            const {result} = renderHook(() => useSpecialty('/neighborhoods/error'));
            await waitFor(() => expect(result.current.isError).toBe(true));
        })
    });


    describe('useSpecialtiesByUrl (useQueries)', () => {

        it('deberia fetchear varias especialidades en paralelo', async () => {
            const urlsToFetch = [
                `${BASE_URL}/specialties/1`,
                `${BASE_URL}/specialties/2`
            ];

            const { result } = renderHook(() => useSpecialtiesByUrl(urlsToFetch));


            expect(result.current.isLoading).toBe(true);

            await waitFor(() => expect(result.current.isLoading).toBe(false));


            const results = result.current.data;

            expect(results).toHaveLength(2);



            expect(results[0].isSuccess).toBe(true);
            expect(results[0].data?.name).toBe('Cardiología');

            expect(results[1].isSuccess).toBe(true);
            expect(results[1].data?.name).toBe('Dermatología');
        });

        it('deberia retornar un array vacio si no se da un URL', async () => {
            const { result } = renderHook(() => useSpecialtiesByUrl([]));

            await waitFor(() => expect(result.current.isLoading).toBe(false));
            expect(result.current.data).toHaveLength(0);
        });
    });
});