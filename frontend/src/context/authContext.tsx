import { createContext, useContext, useState, useEffect, type ReactNode } from "react";
import { useUserService } from "../hooks/userService.ts";
import { api } from "../data/Api.ts";

interface   AuthContextType {
    isAuthenticated: boolean;
    login: (email: string, pass: string) => Promise<boolean>;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType>({} as AuthContextType);

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);


    const userService = useUserService(api);

    useEffect(() => {
        const token = localStorage.getItem("jwt");
        if (token) setIsAuthenticated(true);
    }, []);

    const login = async (email: string, pass: string) => {
        try {

            const result = await userService.login("/", email, pass);

            if (result.success) {
                if (result.jwt != null) {
                    localStorage.setItem("jwt", result.jwt);
                }
                if (result.refreshToken != null) {
                    localStorage.setItem("refreshToken", result.refreshToken);
                }

                api.defaults.headers.common['Authorization'] = `Bearer ${result.jwt}`;

                setIsAuthenticated(true);
                return true;
            }
            return false;

        } catch (error) {
            console.error("Error en AuthContext:", error);
            return false;
        }
    };

    const logout = () => {
        localStorage.removeItem("jwt");
        localStorage.removeItem("refreshToken");
        delete api.defaults.headers.common['Authorization'];
        setIsAuthenticated(false);
    };

    return (
        <AuthContext.Provider value={{ isAuthenticated, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};