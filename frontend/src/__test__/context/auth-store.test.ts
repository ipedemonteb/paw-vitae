
import {describe, vi, beforeEach, expect, it} from 'vitest';
import {jwtDecode} from "jwt-decode";
import {clearAuth, getClaims, REFRESH_KEY, setAuth, TOKEN_KEY} from "@/context/auth-store.ts";
import {ROLE_DOCTOR, ROLE_PATIENT} from "@/lib/constants.ts";

// 1. MOCKEAMOS LA LIBRERÍA
vi.mock('jwt-decode', () => ({
    jwtDecode: vi.fn(),
}));

describe('auth store testing', () => {

    beforeEach(() => {
        // Limpieza general
        localStorage.clear();
        sessionStorage.clear();
        vi.clearAllMocks();
        clearAuth();
    });

    it('Guardar el auth token con remember me', () => {

        vi.mocked(jwtDecode).mockReturnValue({
            userId: '1',
            role: ROLE_DOCTOR,
            sub: 'test',
            exp: 999999
        });

        setAuth('token-falso-vitae', 'refresh-falso', true);

        const claims = getClaims();
        expect(claims?.role).toBe(ROLE_DOCTOR);

        expect(localStorage.getItem(TOKEN_KEY)).toBe('token-falso-vitae');
        expect(localStorage.getItem(REFRESH_KEY)).toBe('refresh-falso');

        expect(sessionStorage.getItem(TOKEN_KEY)).toBeNull();
        expect(sessionStorage.getItem(REFRESH_KEY)).toBeNull();

    });
    it('Guardar el auth token sin remember me', () => {

        vi.mocked(jwtDecode).mockReturnValue({
            userId: '1',
            role: ROLE_DOCTOR,
            sub: 'test',
            exp: 999999
        });

        setAuth('token-falso-vitae', 'refresh-falso', false);

        const claims = getClaims();
        expect(claims?.role).toBe(ROLE_DOCTOR);

        expect(sessionStorage.getItem(TOKEN_KEY)).toBe('token-falso-vitae');
        expect(sessionStorage.getItem(REFRESH_KEY)).toBe('refresh-falso');

        expect(localStorage.getItem(TOKEN_KEY)).toBeNull();
        expect(localStorage.getItem(REFRESH_KEY)).toBeNull();

    });

    it('Deberia limpiar bien el auth', () => {
        clearAuth();

        expect(getClaims()).toBeNull();

        expect(localStorage.getItem(TOKEN_KEY)).toBeNull();
        expect(localStorage.getItem(REFRESH_KEY)).toBeNull();
        expect(sessionStorage.getItem(TOKEN_KEY)).toBeNull();
        expect(sessionStorage.getItem(REFRESH_KEY)).toBeNull();
    });

    describe('initAuthFromStorage (Hidratación al inicio)', () => {



        it('Debería restaurar la sesión desde localStorage (Remember Me = TRUE)', async () => {
            const token = 'token-local-123';
            const refresh = 'refresh-local-123';
            localStorage.setItem(TOKEN_KEY, token);
            localStorage.setItem(REFRESH_KEY, refresh);

            vi.mocked(jwtDecode).mockReturnValue({
                userId: '10',
                role: ROLE_PATIENT,
                sub: 'paciente',
                exp: 999999
            });


            const { initAuthFromStorage, getAccessToken, getRefreshToken } = await import('@/context/auth-store.ts');
            initAuthFromStorage();

            expect(getAccessToken()).toBe(token);
            expect(getRefreshToken()).toBe(refresh);

            expect(getClaims()?.role).toBe(ROLE_PATIENT);
        });

        it('Debería restaurar la sesión desde sessionStorage (Remember Me = FALSE)', async () => {
            const token = 'token-session-456';
            const refresh = 'refresh-session-456';
            sessionStorage.setItem(TOKEN_KEY, token);
            sessionStorage.setItem(REFRESH_KEY, refresh);

            vi.mocked(jwtDecode).mockReturnValue({
                userId: '20',
                role: ROLE_DOCTOR,
                sub: 'doctor',
                exp: 999999
            });

            const { initAuthFromStorage, getAccessToken } = await import('@/context/auth-store.ts');
            initAuthFromStorage();

            expect(getAccessToken()).toBe(token);
            expect(getClaims()?.role).toBe(ROLE_DOCTOR);
        });

        it('No debería hacer nada si no hay tokens guardados', async () => {

            const { initAuthFromStorage, getAccessToken } = await import('@/context/auth-store.ts');
            initAuthFromStorage();

            expect(getAccessToken()).toBeUndefined();
            expect(getClaims()).toBeNull();
        });
    });

});


