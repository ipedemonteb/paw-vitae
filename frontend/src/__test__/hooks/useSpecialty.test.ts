import { describe, it, expect } from 'vitest';
import { renderHook, waitFor } from '@/__test__/setup/utils';
import { useSpecialties, useSpecialty, useSpecialtiesByUrl } from '@/hooks/useSpecialties';
import {BASE_URL} from "@/__test__/utils/utils.ts";

describe('Specialties Hooks', () => {

    describe('useSpecialties (List)', () => {
        it('should return a list of specialties', async () => {
            const { result } = renderHook(() => useSpecialties());

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data).toHaveLength(4);
            expect(result.current.data![0].name).toBe('Cardiología');
        });
    });


    describe('useSpecialty (Single)', () => {
        it('should return a single specialty', async () => {
            const url = `${BASE_URL}/specialties/1`;
            const { result } = renderHook(() => useSpecialty(url));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));
            expect(result.current.data?.name).toBe('Cardiología');
        });
        it('should return error if the specialty does not exist', async () => {
            const {result} = renderHook(() => useSpecialty('/neighborhoods/error'));
            await waitFor(() => expect(result.current.isError).toBe(true));
        })
    });


    describe('useSpecialtiesByUrl (useQueries)', () => {

        it('should parallel fetch specialties', async () => {
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

        it('should return an empty array if the URL is not provided', async () => {
            const { result } = renderHook(() => useSpecialtiesByUrl([]));

            await waitFor(() => expect(result.current.isLoading).toBe(false));
            expect(result.current.data).toHaveLength(0);
        });
    });
});