import {useContext} from "react";
import {AuthContext} from "@/context/authContext.tsx";
import {setAuth} from "@/context/auth-store.ts";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import type {AxiosError} from "axios";
import {login} from "@/data/auth.ts";

export function useAuth () {
    const context = useContext(AuthContext);
    if (!context) throw new Error("useAuth must be used within an AuthProvider");
    return context;
}


export function useLogin() {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: ({ email, password }: {email: string, password: string}) =>
            login(email, password),

        onSuccess: (data) => {
            setAuth(data.jwt, data.refresh);
            queryClient.invalidateQueries({ queryKey: ["auth"] });
        },
        onError: (error: AxiosError) => {
            console.error("Login falló", error);
        }
    });
}