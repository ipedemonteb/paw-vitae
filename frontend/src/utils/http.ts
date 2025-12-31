// // typescript
// // src/lib/http.ts (updated)
//
// export type HttpMethod = "GET" | "POST" | "PUT" | "PATCH" | "DELETE";
//
// export class ApiError extends Error {
//     status: number;
//     details?: unknown;
//
//     constructor(message: string, status: number, details?: unknown) {
//         super(message);
//         this.name = "ApiError";
//         this.status = status;
//         this.details = details;
//     }
// }
//
// type QueryValue = string | number | boolean;
// type Query = Record<string, QueryValue | QueryValue[] | null | undefined>;
//
// type RequestOptions = Omit<RequestInit, "method" | "body"> & {
//     method?: HttpMethod;
//     query?: Query;
//     body?: unknown;
//     timeoutMs?: number;
// };
//
// const BASE_URL = "http://localhost:8080";
// const DEFAULT_TIMEOUT_MS = 20_000;
// const TOKEN_KEY = "accessToken";
//
// export function setToken(token: string | null) {
//     if (token) localStorage.setItem(TOKEN_KEY, token);
//     else localStorage.removeItem(TOKEN_KEY);
// }
//
// export function getToken(): string | null {
//     return localStorage.getItem(TOKEN_KEY);
// }
//
// export function clearToken() {
//     localStorage.removeItem(TOKEN_KEY);
// }
//
// function toQueryString(query?: Query): string {
//     if (!query) return "";
//     const params = new URLSearchParams();
//
//     for (const [key, value] of Object.entries(query)) {
//         if (value === undefined || value === null) continue;
//
//         if (Array.isArray(value)) {
//             for (const v of value) params.append(key, String(v)); // repeat keys
//         } else {
//             params.set(key, String(value));
//         }
//     }
//
//     const s = params.toString();
//     return s ? `?${s}` : "";
// }
//
// async function parseJsonSafe(res: Response): Promise<unknown | null> {
//     const ct = res.headers.get("content-type") ?? "";
//     if (!ct.includes("application/json")) return null;
//     try {
//         return await res.json();
//     } catch {
//         return null;
//     }
// }
//
// async function tryRefreshToken(): Promise<string | null> {
//     try {
//         const res = await fetch(`${BASE_URL}/auth/refresh`, {
//             method: "POST",
//             credentials: "include", // refresh token usually stored as httpOnly cookie
//             headers: { Accept: "application/json" },
//         });
//         if (!res.ok) return null;
//         const data = await res.json().catch(() => null);
//         const newToken = (data as any)?.accessToken ?? null;
//         if (newToken) setToken(newToken);
//         return newToken;
//     } catch {
//         return null;
//     }
// }
//
// export async function apiRequest<T>(path: string, opts: RequestOptions = {}): Promise<T> {
//     const url = `${BASE_URL}${path}${toQueryString(opts.query)}`;
//
//     const controller = new AbortController();
//     const timeout = setTimeout(() => controller.abort(), opts.timeoutMs ?? DEFAULT_TIMEOUT_MS);
//
//     try {
//         const { body, headers: customHeaders, method, query, timeoutMs, ...rest } = opts;
//
//         const attachAuth = (headers: Record<string, string | undefined>) => {
//             const token = getToken();
//             if (token) headers["Authorization"] = `Bearer ${token}`;
//         };
//
//         let init: RequestInit = {
//             method: method ?? "GET",
//             signal: controller.signal,
//             credentials: "include",
//             headers: {
//                 Accept: "application/json",
//                 ...(body !== undefined ? { "Content-Type": "application/json" } : {}),
//                 ...(customHeaders ?? {}),
//             },
//             ...(body !== undefined ? { body: JSON.stringify(body) } : {}),
//             ...rest as RequestInit,
//         };
//
//         // attach token if present
//         attachAuth(init.headers as Record<string, string | undefined>);
//
//         let res = await fetch(url, init);
//
//         // if unauthorized, try refresh once and retry
//         if (res.status === 401) {
//             const newToken = await tryRefreshToken();
//             if (newToken) {
//                 // rebuild headers with new token and retry
//                 init = {
//                     ...init,
//                     headers: {
//                         ...(init.headers as Record<string, string | undefined>),
//                         Authorization: `Bearer ${newToken}`,
//                     },
//                 };
//                 res = await fetch(url, init);
//             }
//         }
//
//         if (res.status === 204) return undefined as T;
//
//         const data = await parseJsonSafe(res);
//
//         if (!res.ok) {
//             const msg =
//                 (data as any)?.message ??
//                 (data as any)?.error ??
//                 res.statusText ??
//                 "Request failed";
//
//             throw new ApiError(msg, res.status, data ?? (await res.text().catch(() => undefined)));
//         }
//
//         if (data === null) {
//             const text = await res.text().catch(() => "");
//             return text as unknown as T;
//         }
//
//         return data as T;
//     } finally {
//         clearTimeout(timeout);
//     }
// }
//
// export const http = {
//     get: <T>(path: string, opts?: Omit<RequestOptions, "method">) =>
//         apiRequest<T>(path, { ...opts, method: "GET" }),
//     post: <T>(path: string, body?: unknown, opts?: Omit<RequestOptions, "method" | "body">) =>
//         apiRequest<T>(path, { ...opts, method: "POST", body }),
//     put: <T>(path: string, body?: unknown, opts?: Omit<RequestOptions, "method" | "body">) =>
//         apiRequest<T>(path, { ...opts, method: "PUT", body }),
//     patch: <T>(path: string, body?: unknown, opts?: Omit<RequestOptions, "method" | "body">) =>
//         apiRequest<T>(path, { ...opts, method: "PATCH", body }),
//     del: <T>(path: string, opts?: Omit<RequestOptions, "method">) =>
//         apiRequest<T>(path, { ...opts, method: "DELETE" }),
// };
//
// export type DoctorDTO = {
//     name: string;
//     lastName: string;
//     email: string;
//     phone: string;
//     rating: number;
//     ratingCount: number;
//     specialties: string;
//     coverages     : string;
//     offices       : string;
//     ratings       : string;
//     profile       : string;
//     experiences   : string;
//     certifications: string;
//     appointments  : string;
//     self          : string;
// };
//
//
// export type DoctorsQuery = {
//     specialty?: number;
//     coverage?: number;
//     weekdays?: number[]; // 0..6
//     keyword?: string;
//     orderBy?: string;
//     direction?: "asc" | "desc";
//     page?: number;
// };
//
// export function listDoctors(query: DoctorsQuery) {
//     return http.get<DoctorDTO>("/doctors/24", { query });
// }

export const USER_CONTENT_TYPE = "application/vnd.vitae.user.v1+json";
export const USER_PASSWORD_CONTENT_TYPE = "application/vnd.vitae.user-password.v1+json";
export const USER_PROFILE_CONTENT_TYPE = "application/vnd.vitae.user-profile.v1+json";
export const USER_ADDRESS_CONTENT_TYPE = "application/vnd.vitae.user-address.v1+json";
export const USER_ROLE_CONTENT_TYPE = "application/vnd.vitae.user-role.v1+json";
export const RESTAURANT_EMPLOYEES_CONTENT_TYPE = "application/vnd.vitae.restaurant-employees.v1+json";

export const NOT_FOUND_STATUS_CODE = 404;
export const ROLES = {
    PATIENT: "PATIENT",
    DOCTOR: "DOCTOR",
    ADMIN: "ADMIN",
    MODERATOR: "MODERATOR"
};