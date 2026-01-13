import { createContext, type ReactNode, useCallback, useEffect, useMemo, useState } from "react";
import {
    clearAuth,
    getClaims,
    initAuthFromStorage,
    type JWTClaims,
    subscribeAuth,
} from "@/context/auth-store";
import { useQueryClient } from "@tanstack/react-query";
import type { AxiosError } from "axios";
import {useLogin} from "@/hooks/useAuth.ts";

interface AuthContextType {
    claims: JWTClaims | null;
    isAuthenticated: boolean;
    userId?: string;
    email?: string;
    role?: string;

    login: (email: string, password: string) => Promise<void>;
    isLoggingIn: boolean;
    loginError: AxiosError<any> | null;

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


    const {
        mutateAsync: performLoginMutation,
        isPending: isLoggingIn,
        error: loginError
    } = useLogin();

    const login = useCallback(async (email: string, password: string) => {
        await performLoginMutation({ email, password });
    }, [performLoginMutation]);

    const logout = useCallback(() => {
        clearAuth();
        queryClient.removeQueries({ queryKey: ["auth"], exact: false });
    }, [queryClient]);

    const value = useMemo(
        () => ({
            claims,
            isAuthenticated: !!claims,
            userId: claims?.userId,
            role: claims?.role,
            email: claims?.email,


            login,
            logout,
            isLoggingIn,
            loginError
        }),
        [claims, login, logout, isLoggingIn, loginError]
    );

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
}