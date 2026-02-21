import { jwtDecode } from "jwt-decode";
import {ROLE_DOCTOR, ROLE_PATIENT} from "@/lib/constants.ts";
type Claims = {
    sub: string;
    email?: string;
    role?: string;
    exp: number;
    iat: number;

    userId: string;

    [k: string]: unknown;
};

export type JWTClaims = Claims & Record<string, unknown>


type AuthState = {
    accessToken?: string;
    refreshToken?: string;
    claims: Claims | null;
};

export const TOKEN_KEY = "jwt";
export const REFRESH_KEY = "refreshToken";

let state: AuthState = { accessToken: undefined, refreshToken: undefined, claims: null };
const listeners = new Set<(s: AuthState) => void>();

function emit() {
    for (const l of listeners) l(state);
}

export function subscribeAuth(listener: (s: AuthState) => void) {
    listeners.add(listener);
    listener(state); // initial push
    return () => {
        listeners.delete(listener); // ignore the boolean
    };
}

export const getAccessToken = () => state.accessToken;
export const getRefreshToken = () => state.refreshToken;
export const getClaims = () => state.claims;

function validateJWTClaims(claims: JWTClaims) {
    if (!claims.userId || !claims.role || !claims.sub) throw new Error("Missing Claims");
    if (claims.role.toUpperCase() !== ROLE_DOCTOR && claims.role.toUpperCase() !== ROLE_PATIENT) throw new Error("Invalid Role")
}


export function setAuth(token?: string, refresh?: string, rememberMe?: boolean) {
    let claims: JWTClaims | null = null;
    if (token) {
        try {
            claims = jwtDecode(token);
        } catch (e) {
            throw new Error("Invalid JWT Format");
        }
        validateJWTClaims(claims!);
    }

    state.claims = claims;
    state.accessToken = token;
    state.refreshToken = refresh;

    if (token && refresh) {
        if (rememberMe) {
            localStorage.setItem(TOKEN_KEY, token);
            localStorage.setItem(REFRESH_KEY, refresh);
            sessionStorage.removeItem(TOKEN_KEY);
            sessionStorage.removeItem(REFRESH_KEY);
        } else {
            sessionStorage.setItem(TOKEN_KEY, token);
            sessionStorage.setItem(REFRESH_KEY, refresh);
            localStorage.removeItem(TOKEN_KEY);
            localStorage.removeItem(REFRESH_KEY);
        }
    }

    emit();
}


//The clear auth can be silent, so the logout function does not race against the authGuard, and there is no blink of the login page before showing the home page after logout.
export function clearAuth(silent = false) {
    state = { accessToken: undefined, refreshToken: undefined, claims: null };
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(REFRESH_KEY);
    sessionStorage.removeItem(TOKEN_KEY);
    sessionStorage.removeItem(REFRESH_KEY);
    if (!silent) {
        emit();
    }
}

export function initAuthFromStorage() {
    const localToken = localStorage.getItem(TOKEN_KEY);
    const sessionToken = sessionStorage.getItem(TOKEN_KEY);

    const accessToken = localToken ?? sessionToken ?? undefined;

    const localRefresh = localStorage.getItem(REFRESH_KEY);
    const sessionRefresh = sessionStorage.getItem(REFRESH_KEY);

    const refreshToken = localRefresh ?? sessionRefresh ?? undefined;

    const isRemembered = !!localToken;

    if (accessToken) {
        setAuth(accessToken, refreshToken, isRemembered);
    }

}
