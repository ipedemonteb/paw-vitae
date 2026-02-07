import { describe, it, expect, vi, afterEach } from 'vitest';
import { renderHook, waitFor } from '../setup/utils';
import { useLogin, useChangePasswordMutation, useAuth } from '@/hooks/useAuth';
import React, { act } from 'react';
import * as authStore from '@/context/auth-store';
import { AuthContext } from '@/context/authContext';
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";


const mockLogout = vi.fn();

const testQueryClient = new QueryClient({
    defaultOptions: { queries: { retry: false } }
});

const wrapper = ({ children }: { children: React.ReactNode }) => (
    <QueryClientProvider client={testQueryClient}>
        <AuthContext.Provider value={{ logout: mockLogout } as any}>
            {children}
        </AuthContext.Provider>
    </QueryClientProvider>
);

describe('Auth Hooks', () => {

    afterEach(() => {
        vi.clearAllMocks();
    });

    describe('useLogin', () => {
        it('Debería hacer login y guardar los tokens en el store', async () => {
            const setAuthSpy = vi.spyOn(authStore, 'setAuth').mockImplementation(() => {});

            const { result } = renderHook(() => useLogin(), { wrapper });

            act(() => {
                result.current.mutate({ email: 'test@test.com', password: '123', rememberMe: true });
            });

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(setAuthSpy).toHaveBeenCalledWith('fake-jwt-token', 'fake-refresh-token', true);
        });
    });

    describe('useChangePasswordMutation', () => {
        it('Debería detectar rol DOCTOR y llamar al endpoint correcto', async () => {
            const { result } = renderHook(() => useChangePasswordMutation(), { wrapper });

            // Simulamos que al decodificar el token, el usuario es un DOCTOR
            vi.spyOn(authStore, 'getClaims').mockReturnValue({
                userId: '99',
                role: 'DOCTOR',
                sub: 'test',
                exp: 123
            } as any);

            vi.spyOn(authStore, 'setAuth').mockImplementation(() => {});

            act(() => {
                result.current.mutate({
                    email: 'doc@test.com',
                    password: 'newPass',
                    repeatPassword: 'newPass',
                    token: 'recovery-token'
                });
            });

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(mockLogout).toHaveBeenCalled();
        });

        it('Debería detectar rol PATIENT y llamar al endpoint correcto', async () => {
            const { result } = renderHook(() => useChangePasswordMutation(), { wrapper });

            // Simulamos que es un PACIENTE
            vi.spyOn(authStore, 'getClaims').mockReturnValue({
                userId: '50',
                role: 'PATIENT',
                sub: 'test',
                exp: 123
            } as any);
            vi.spyOn(authStore, 'setAuth').mockImplementation(() => {});

            act(() => {
                result.current.mutate({
                    email: 'pat@test.com',
                    password: 'new',
                    repeatPassword: 'new',
                    token: 'tok'
                });
            });

            await waitFor(() => expect(result.current.isSuccess).toBe(true));
            expect(mockLogout).toHaveBeenCalled();
        });

        it('Debería fallar si useAuth no está dentro del Provider', () => {

            try {
                renderHook(() => useAuth());
            } catch (e: any) {
                expect(e.message).toBe("useAuth must be used within an AuthProvider");
            }
        });
    });
});