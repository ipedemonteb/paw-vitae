export class ApiError extends Error {
    status?: number;
    body?: unknown;

    constructor(message: string, status?: number, body?: unknown) {
        super(message);
        this.name = "ApiError";
        this.status = status;
        this.body = body;
    }
}

const BASE_URL = "http://localhost:8080";

const TOKEN_KEY = "token";
const REFRESH_KEY = "refreshToken";

function setStoredTokens(token?: string, refreshToken?: string) {
    if (token) localStorage.setItem(TOKEN_KEY, token);
    if (refreshToken) localStorage.setItem(REFRESH_KEY, refreshToken);
}

function clearStoredTokens() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(REFRESH_KEY);
}

async function refreshTokens(): Promise<void> {
    const refreshToken = localStorage.getItem(REFRESH_KEY);
    if (!refreshToken) throw new ApiError("No refresh token", 401);

    const res = await fetch(`${BASE_URL}/auth/refresh`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ refreshToken }),
    });

    if (!res.ok) {
        clearStoredTokens();
        throw new ApiError("Refresh failed", res.status);
    }

    // // accept tokens either in JSON or in response headers
    // const ct = res.headers.get("content-type") ?? "";
    // if (ct.includes("application/json")) {
    //     const data = await res.json().catch(() => ({}));
    //     if ((data as any).token) setStoredTokens((data as any).token, (data as any).refreshToken);
    // } else {
        const token = res.headers.get("X-Vitae-AuthToken");
        const rtoken = res.headers.get("X-Vitae-RefreshToken");
        if (token && rtoken) setStoredTokens(token, rtoken);
    // }
}

function extractTokensFromResponse(res: Response) {
    const token = res.headers.get("X-Vitae-AuthToken");
    const refreshToken = res.headers.get("X-Vitae-RefreshToken");
    if (token && refreshToken) setStoredTokens(token, refreshToken);
}

export async function apiRequest<T = any>(
    path: string,
    options: RequestInit = {},
    _retry = true
): Promise<T> {
    const url = `${BASE_URL}${path}`;

    const callerHeaders = (options.headers as Record<string, string>) ?? {};
    const normalizedKeys = Object.keys(callerHeaders).map(k => k.toLowerCase());
    const headers: Record<string, string> = {
        ...(callerHeaders || {}),
    };

    const storedToken = localStorage.getItem(TOKEN_KEY);
    if (storedToken && !normalizedKeys.includes("authorization")) {
        headers["Authorization"] = `Bearer ${storedToken}`;
    }

    let body = options.body;
    if (body !== undefined && typeof body !== "string" && !(body instanceof FormData)) {
        headers["Content-Type"] = headers["Content-Type"] ?? "application/json";
        body = JSON.stringify(body);
    }

    const res = await fetch(url, {
        ...options,
        headers,
        body,
        credentials: options.credentials ?? "include",
    });

    extractTokensFromResponse(res);

    if (res.status === 401 && _retry) {
        try {
            await refreshTokens();
            // retry original request once with new token
            return apiRequest<T>(path, options, false);
        } catch (err) {
            // refresh failed -> propagate
            if (err instanceof ApiError) throw err;
            throw new ApiError("Unauthorized", 401);
        }
    }

    const contentType = res.headers.get("content-type") ?? "";
    const isJson = contentType.includes("application/json");
    const data = isJson ? await res.json().catch(() => undefined) : await res.text().catch(() => undefined);

    if (!res.ok) {
        const msg = (isJson && (data as any)?.message) || res.statusText || "Request failed";
        throw new ApiError(msg, res.status, data);
    }

    return data as T;
}

function base64Encode(str: string): string {
    const bytes = new TextEncoder().encode(str);
    let binary = "";
    for (let i = 0; i < bytes.length; i++) binary += String.fromCharCode(bytes[i]);
    return btoa(binary);
}

export async function login(credentials: { username: string; password: string }) {
    const plain = `${credentials.username}:${credentials.password}`;
    const base64Credentials = base64Encode(plain);
    const headers = { Authorization: `Basic ${base64Credentials}` };

    const data = await apiRequest<{ token?: string; refreshToken?: string }>("/", {
        method: "POST",
        headers,
    }, false);

    if (data?.token && data?.refreshToken) setStoredTokens(data.token, data.refreshToken);
    return data;
}

export async function logout() {
    clearStoredTokens();
    //ANYTHING ELSE?
}

export const http = {
    get: <T = any>(path: string, options?: Omit<RequestInit, 'method'>) => apiRequest<T>(path, {method: 'GET', ...options}),
    post: <T = any>(path: string, options?: Omit<RequestInit, 'method'>) => apiRequest<T>(path, {method: 'POST', ...options}),
    put: <T = any>(path: string, options?: Omit<RequestInit, 'method'>) => apiRequest<T>(path, {method: 'PUT', ...options}),
    patch: <T = any>(path: string, options?: Omit<RequestInit, 'method'>) => apiRequest<T>(path, {method: 'PATCH', ...options}),
    del: <T = any>(path: string, options?: Omit<RequestInit, 'method'>) => apiRequest<T>(path, {method: 'DELETE', ...options}),
};
