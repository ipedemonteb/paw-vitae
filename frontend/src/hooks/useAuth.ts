import {useContext} from "react";
import {AuthContext} from "@/context/authContext.tsx";
import {getClaims, setAuth} from "@/context/auth-store.ts";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {changePassword, login} from "@/data/auth.ts";
import {ROLE_DOCTOR} from "@/lib/constants.ts";
import type {AxiosError} from "axios";

export function useAuth () {
    const context = useContext(AuthContext);
    if (!context) throw new Error("useAuth must be used within an AuthProvider");
    return context;
}


export function useLogin() {
    const queryClient = useQueryClient();

    return useMutation<
        { jwt: any; refresh: any },
        AxiosError<unknown, any>,
        { email: string; password: string; rememberMe: boolean },
        unknown
    >({
        mutationFn: ({ email, password, rememberMe }) =>
            login(email, password, rememberMe),

        onSuccess: (data, variables) => {
            setAuth(data.jwt, data.refresh, variables.rememberMe);
            queryClient.invalidateQueries({ queryKey: ["auth"] });
        }
    });
}

export function useChangePasswordMutation() {
    const {logout} = useAuth()
    return useMutation({
        mutationFn: async ({email, password, repeatPassword}: {email: string, password: string, repeatPassword: string}) => {
            const {jwt, refresh} = await login(email, password)

            setAuth(jwt, refresh)

            const claims = getClaims()!;
            const userId = claims.userId;
            const userRole = claims.role;

            const formPayload = {
                password: password,
                repeatPassword: repeatPassword
            };

            let url;

            if (userRole === ROLE_DOCTOR) url = `/doctors/${userId}` //I CHECK IF ROLE VALID WHEN LOGIN SO NO OTHER POSSIBLE ROLE
            else url = `/patients/${userId}`

            await changePassword(url, formPayload)
        },
        onSettled: () => logout()
    })
}