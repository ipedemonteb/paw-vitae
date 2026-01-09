import axios, {
    AxiosError,
    AxiosHeaders,
    type AxiosInstance,
    type InternalAxiosRequestConfig,
} from "axios";

import {getAccessToken, clearAuth, getRefreshToken, setAuth} from "@/context/auth-store.ts";

const BASE_URL = "http://localhost:8080/";

const AUTHZ = "Authorization";
export const NEW_JWT_HEADER = "x-vitae-authtoken";
export const NEW_REFRESH_HEADER = "x-vitae-refreshtoken";

export const api: AxiosInstance = axios.create({
    baseURL: BASE_URL,
    timeout: 5000,
});

type Cfg = InternalAxiosRequestConfig & {
    _retriedWithRefresh?: boolean;
};

let isRefreshing = false;
let waiters: Array<(ok: boolean) => void> = [];

function notifyWaiters(ok: boolean) {
    waiters.forEach((fn) => fn(ok));
    waiters = [];
}

function waitForRefresh(): Promise<boolean> {
    return new Promise((resolve) => waiters.push(resolve));
}


api.interceptors.request.use((config) => {
    const jwt = getAccessToken();
    if (jwt) {
        const headers = AxiosHeaders.from(config.headers);
        if (!headers.get(AUTHZ)) headers.set(AUTHZ, `Bearer ${jwt}`);
        config.headers = headers;
    }
    return config;
});

function isExpiredJwt401(err: AxiosError): boolean {
    if (err.response?.status !== 401) return false;

    const header = err.response.headers?.["www-authenticate"];
    if (!header || typeof header !== "string") return false;

    const h = header.toLowerCase();

    return (
        h.includes("bearer") &&
        h.includes("invalid_token") &&
        h.includes("expired")
    );
}


api.interceptors.response.use(
    (res) => res,
    async (err: AxiosError) => {
        const original = err.config as Cfg | undefined;

        if (!original || !isExpiredJwt401(err)) return Promise.reject(err);

        //para no loop infinito
        if (original._retriedWithRefresh) {
            clearAuth();
            return Promise.reject(err);
        }

        if (isRefreshing) {
            const ok = await waitForRefresh();
            if (!ok) {
                clearAuth();
                return Promise.reject(err);
            }

            const jwt = getAccessToken();
            original.headers = AxiosHeaders.from(original.headers);
            if (jwt) original.headers.set(AUTHZ, `Bearer ${jwt}`);

            return api.request(original);
        }

        isRefreshing = true;

        try {
            const refresh = getRefreshToken();
            if (!refresh) throw new Error("No refresh token available");

            const replayHeaders = AxiosHeaders.from(original.headers);
            replayHeaders.set(AUTHZ, `Bearer ${refresh}`);

            const replayCfg: Cfg = {
                ...original,
                headers: replayHeaders,
                _retriedWithRefresh: true,
            };

            const replayRes = await api.request(replayCfg);

            const newJwt = replayRes.headers?.[NEW_JWT_HEADER] as string | undefined;
            const newRefresh = replayRes.headers?.[NEW_REFRESH_HEADER] as string | undefined;


            if (newJwt && newRefresh) {
                setAuth(newJwt, newRefresh);
                api.defaults.headers.common[AUTHZ] = `Bearer ${newJwt}`;
            }

            notifyWaiters(true);

            return replayRes;
        } catch (e) {
            notifyWaiters(false);
            clearAuth();
            return Promise.reject(e);
        } finally {
            isRefreshing = false;
        }
    }
);
