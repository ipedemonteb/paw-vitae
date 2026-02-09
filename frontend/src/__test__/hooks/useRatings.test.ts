import { describe, it, expect } from 'vitest';
import { renderHook, waitFor, act } from "@/__test__/setup/utils.tsx";
import { useRatings, useAllRatings, useRating, useCreateRating } from "@/hooks/useRatings.ts";
import { BASE_URL } from "@/__test__/utils/utils.ts";

describe('Ratings Hooks', () => {

    describe('useRatings (Doctor List)', () => {
        it('debería retornar una lista de ratings filtrada por doctor', async () => {
            const mockUrl = `${BASE_URL}/ratings?doctorId=1`;

            const { result } = renderHook(() => useRatings(mockUrl));

            expect(result.current.isLoading).toBe(true);

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data).toHaveLength(2);
            expect(result.current.data![0].comment).toContain('Mock Doctor');
            expect(result.current.data![0].rating).toBe(5);
        });

        it('debería manejar errores del servidor', async () => {
            const errorUrl = `${BASE_URL}/ratings?doctorId=error`;

            const { result } = renderHook(() => useRatings(errorUrl));

            await waitFor(() => expect(result.current.isError).toBe(true));
        });
    });

    describe('useAllRatings (Global List)', () => {
        it('debería retornar la lista global paginada', async () => {
            const { result } = renderHook(() => useAllRatings());

            expect(result.current.isLoading).toBe(true);

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data).toBeDefined();
            expect(result.current.data![0].comment).toContain('Mock All');
        });
    });

    describe('useRating (Detail)', () => {
        it('debería retornar el detalle de un rating específico', async () => {
            const detailUrl = `${BASE_URL}/ratings/99`;

            const { result } = renderHook(() => useRating(detailUrl));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data?.comment).toBe('Experiencia detallada mockeada');
            expect(result.current.data?.rating).toBe(1);
        });

        it('debería manejar error si el rating no existe o falla', async () => {
            const errorUrl = `${BASE_URL}/ratings/error`;
            const { result } = renderHook(() => useRating(errorUrl));

            await waitFor(() => expect(result.current.isError).toBe(true));
        });
    });

    describe('useCreateRating (Mutation)', () => {
        it('debería crear un rating exitosamente', async () => {
            const { result } = renderHook(() => useCreateRating());

            const newRatingData = {
                rating: 5,
                comment: 'Excelente servicio',
                appointmentId: '123'
            };

            act(() => result.current.mutate(newRatingData));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));
        });
    });

});