import type {AxiosInstance} from "axios";

export function useUserService(api : AxiosInstance) {
    const login = async (url: string, email: string, password: string): Promise<{ success: boolean; jwt?: string; refreshToken?: string }> => {
        const credentials = btoa(`${email}:${password}`);

        try {
            const response = await api.head(url, {
                headers: {
                    "Accept": "application/json",
                    "Authorization": `Basic ${credentials}`
                }
            });
            const jwt = response.headers["x-vitae-authtoken"];
            const refreshToken = response.headers["x-vitae-refreshtoken"];

            if (jwt && refreshToken) {
                return { success: true, jwt, refreshToken };
            } else {
                return { success: false };
            }
        } catch (error) {
            console.error(error);

            return { success: false };
        }
    };
    return {login};
}