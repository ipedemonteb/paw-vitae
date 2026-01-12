import {api, NEW_JWT_HEADER, NEW_REFRESH_HEADER} from "@/data/Api.ts"
// import {setAuth} from "@/context/auth-store.ts";
// export async function login(email: string, password: string)  {
//     const credentials = btoa(email + ":" + password);
//     try {
//         const res = await api.head("/", {headers: {Authorization: `Basic ${credentials}`}});
//
//         const jwt = res.headers[NEW_JWT_HEADER];
//         const refresh = res.headers[NEW_REFRESH_HEADER];
//
//         if (jwt && refresh) setAuth(jwt, refresh)
//         else return {success: false, errorMessage: 'login.error_credentials'}
//
//         return {success: true};
//     } catch (e) {
//         return {success: false, errorMessage: 'login.error_generic'};
//     }
// }

export async function login(email: string, password: string) {
    const credentials = btoa(email + ":" + password);

    const res = await api.head("/", {
        headers: { Authorization: `Basic ${credentials}` }
    });

    const jwt = res.headers[NEW_JWT_HEADER];
    const refresh = res.headers[NEW_REFRESH_HEADER];

    if (!jwt || !refresh) {
        throw new Error("login.error_credentials");
    }

    return { jwt, refresh };
}