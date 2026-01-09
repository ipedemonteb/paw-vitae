import {createContext, type ReactNode, useCallback, useEffect, useMemo, useState} from "react";
import {
    clearAuth,
    initAuthFromStorage,
    type JWTClaims,
    subscribeAuth,
} from "@/context/auth-store.ts";
import {login as loginApi} from "@/data/auth.ts";
import {useQueryClient} from "@tanstack/react-query";

interface AuthContextType {
    claims: JWTClaims | null;
    isAuthenticated: boolean;

    userId?: string;
    email?: string;
    role?: string;

    login: (email: string, password: string) => Promise<{success: boolean, errorMessage?: string}>;
    logout: () => void;
}

export const AuthContext = createContext<AuthContextType | undefined>(undefined);


export function AuthProvider({ children }: { children: ReactNode })  {
    const [claims, setClaims] = useState<JWTClaims | null>(null)
    const queryClient = useQueryClient();

    useEffect(() => {
        initAuthFromStorage();
        return subscribeAuth((s) => setClaims(s.claims));
    }, []);

    const login = useCallback(async (email: string, password: string) => {
        return loginApi(email, password);
    }, []);

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
        }),
        [claims, login, logout]
    );

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
}