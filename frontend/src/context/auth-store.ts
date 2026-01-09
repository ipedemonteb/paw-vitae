import { jwtDecode } from "jwt-decode";
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

export function setAuth(token?: string, refresh?: string) {
    state.accessToken = token;
    state.refreshToken = refresh;

    if (token) localStorage.setItem(TOKEN_KEY, token);
    if (refresh) localStorage.setItem(REFRESH_KEY, refresh);

    state.claims = token ? jwtDecode<Claims>(token) : null;
    emit();
}

export function clearAuth() {
    state = { accessToken: undefined, refreshToken: undefined, claims: null };
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(REFRESH_KEY);
    emit();
}

export function initAuthFromStorage() {
    const accessToken = localStorage.getItem(TOKEN_KEY) ?? undefined;
    const refreshToken = localStorage.getItem(REFRESH_KEY) ?? undefined;
    setAuth(accessToken, refreshToken);
}
