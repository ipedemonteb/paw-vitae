import { describe, it, expect } from 'vitest';
import { renderHook, waitFor } from '@/__test__/setup/utils';
import { useUser, useUserMutation } from '@/hooks/useUser'; // Ajusta la ruta
import { ROLE_DOCTOR, ROLE_PATIENT } from '@/lib/constants'; // Tus constantes

describe('User Hooks', () => {

    describe('useUserMutation (Recover Password)', () => {
        it('Deberia enviar el email correctamente', async () => {
            const { result } = renderHook(() => useUserMutation());

            result.current.mutate({ email: 'test@test.com' });

            await waitFor(() => expect(result.current.isSuccess).toBe(true));
        });
    });

    describe('useUser (Fetch Profile)', () => {

        it('deberia traer el perfil del doctor', async () => {
            const { result } = renderHook(() => useUser(ROLE_DOCTOR, '1'));

            expect(result.current.isLoading).toBe(true);
            await waitFor(() => expect(result.current.isSuccess).toBe(true));


            expect(result.current.data?.name).toBe('Gregory');
        });

        it('deberia traer el perfil del paciente', async () => {

            const { result } = renderHook(() => useUser(ROLE_PATIENT, '100'));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data?.name).toBe('Pepe');
        });

        it('No deberia correr si no le paso rol o id (enabled: false)', async () => {
            const { result } = renderHook(() => useUser(undefined, undefined));

            expect(result.current.fetchStatus).toBe('idle');
            expect(result.current.data).toBeUndefined();
        });

        it('deberia mandar error por role invalido', async () => {
            const { result } = renderHook(() => useUser('HACKER_ROLE', '1'));

            await waitFor(() => expect(result.current.isError).toBe(true));

            expect(result.current.error?.message).toBe("Invalid role");
        });
    });
});