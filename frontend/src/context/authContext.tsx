import { createContext, type ReactNode, useCallback, useEffect, useMemo, useState } from "react";
import {
    clearAuth,
    getClaims,
    initAuthFromStorage,
    type JWTClaims,
    subscribeAuth,
} from "@/context/auth-store";
import {type UseMutationResult, useQueryClient} from "@tanstack/react-query";
import type { AxiosError } from "axios";
import {useLogin} from "@/hooks/useAuth.ts";

interface AuthContextType {
    claims: JWTClaims | null;
    isAuthenticated: boolean;
    userId?: string;
    email?: string;
    role?: string;

    login: UseMutationResult<
        {jwt: any, refresh: any},
        AxiosError<unknown, any>,
        {email: string, password: string, rememberMe: boolean},
        unknown
    >;

    logout: () => void;
}

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
    const queryClient = useQueryClient();
    const [claims, setClaims] = useState<JWTClaims | null>(() => {
        initAuthFromStorage();
        return getClaims();
    });

    useEffect(() => {
        return subscribeAuth((s) => setClaims(s.claims));
    }, []);

    const login = useLogin()

    const logout = useCallback(() => {
        window.location.href = "/"
        clearAuth();
        queryClient.removeQueries({ queryKey: ['auth'], exact: false });
    }, [queryClient]);

    const value = useMemo(
        () => ({
            claims,
            isAuthenticated: !!claims,
            userId: claims?.userId,
            role: claims?.role,
            email: claims?.sub,
            login,
            logout
        }),
        [claims, login, logout]
    );

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
}