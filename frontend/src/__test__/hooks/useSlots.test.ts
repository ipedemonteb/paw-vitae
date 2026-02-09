import { describe, it, expect } from 'vitest';
import { renderHook, waitFor } from "@/__test__/setup/utils.tsx";
import { useOccupiedSlots } from "@/hooks/useSlots.ts";

describe('useOccupiedSlots Hook', () => {

    it('debería traer los slots ocupados correctamente', async () => {

        const { result } = renderHook(() =>
            useOccupiedSlots('2026-02-01', '2026-02-28', '1')
        );

        expect(result.current.isLoading).toBe(true);

        await waitFor(() => expect(result.current.isSuccess).toBe(true));

        const slots = result.current.data;
        expect(slots).toBeDefined();
        expect(slots?.length).toBeGreaterThan(0);

        expect(slots![0].date).toBeDefined();
        expect(slots![0].date).toBe('2026-02-01')
    });

    it('debería manejar errores del servidor (500)', async () => {
        const { result } = renderHook(() =>
            useOccupiedSlots('2026-02-01', '2026-02-28', 'error')
        );

        await waitFor(() => expect(result.current.isError).toBe(true));
    });

});