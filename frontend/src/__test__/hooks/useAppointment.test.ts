import { describe, it, expect } from 'vitest';
import { renderHook, waitFor } from '../setup/utils';
import { useAppointment } from "@/hooks/useAppointments.ts";
//TODO: La idea es que hagamos un describe grande por archivo de hooks. Uno describe por hook y despues un it por caso

describe('useAppointments File', () => {

    describe('useAppointment Hook', () => {

        it('debería retornar el DTO con las URLs HATEOAS correctas', async () => {
            const { result } = renderHook(() => useAppointment('1'));

            expect(result.current.isLoading).toBe(true);

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            const data = result.current.data;

            expect(data?.status).toBe('completo');
            expect(data?.reason).toBe('Consulta general');

            expect(data?.doctor).toBeTypeOf('string');
            expect(data?.doctor).toContain('/doctors/1');
        });

        it('debería manejar errores del servidor (500)', async () => {
            const { result } = renderHook(() => useAppointment('error'));
            await waitFor(() => expect(result.current.isError).toBe(true));
        });

        it('debería incluir la URL de rating si el turno ya fue calificado', async () => {
            const { result } = renderHook(() => useAppointment('rated-1'));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data?.rating).toBeDefined();
            expect(result.current.data?.rating).toContain('/ratings/99');
        });

        it('no debería tener rating si el turno está completo pero sin calificar', async () => {
            const { result } = renderHook(() => useAppointment('unrated-1'));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data?.rating).toBeFalsy();
        });

        it('no debería tener rating si el turno no está completo', async () => {
            const { result } = renderHook(() => useAppointment('uncompleted-1'));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data?.rating).toBeFalsy();
        });
    });
});