import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "@/hooks/useAuth";
import { getClaims } from "@/context/auth-store";

export const GuestGuard = () => {
    const { isAuthenticated } = useAuth();
    const location = useLocation();

    // Verificación robusta (Store + Context)
    const claims = getClaims();
    const isReallyAuthenticated = isAuthenticated || !!claims;

    if (isReallyAuthenticated) {
        const from = (location.state as { from?: Location })?.from?.pathname;
        if (from) {
            return <Navigate to={from} replace />;
        }

        return <Navigate to="/" replace />;
    }

    return <Outlet />;
};