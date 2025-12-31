import axios, {
    AxiosError,
    AxiosHeaders,
    type AxiosInstance,
    type InternalAxiosRequestConfig,
} from "axios";

const BASE_URL = "http://localhost:8080/";

const TOKEN_KEY = "jwt";
const REFRESH_KEY = "refreshToken";

const AUTHZ = "Authorization";
const NEW_JWT_HEADER = "x-vitae-authtoken";
const NEW_REFRESH_HEADER = "x-vitae-refreshtoken";

function getJwt() {
    return localStorage.getItem(TOKEN_KEY);
}
function getRefresh() {
    return localStorage.getItem(REFRESH_KEY);
}
function setStoredTokens(jwt?: string, refresh?: string) {
    if (jwt) localStorage.setItem(TOKEN_KEY, jwt);
    if (refresh) localStorage.setItem(REFRESH_KEY, refresh);
}
function clearStoredTokens() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(REFRESH_KEY);
}

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
    const jwt = getJwt();
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
            clearStoredTokens();
            return Promise.reject(err);
        }

        if (isRefreshing) {
            const ok = await waitForRefresh();
            if (!ok) {
                clearStoredTokens();
                return Promise.reject(err);
            }

            const jwt = getJwt();
            original.headers = AxiosHeaders.from(original.headers);
            if (jwt) original.headers.set(AUTHZ, `Bearer ${jwt}`);

            return api.request(original);
        }

        isRefreshing = true;

        try {
            const refresh = getRefresh();
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

            if (newJwt || newRefresh) {
                setStoredTokens(newJwt, newRefresh);
                if (newJwt) api.defaults.headers.common[AUTHZ] = `Bearer ${newJwt}`;
            }

            notifyWaiters(true);

            return replayRes;
        } catch (e) {
            notifyWaiters(false);
            clearStoredTokens();
            return Promise.reject(e);
        } finally {
            isRefreshing = false;
        }
    }
);
