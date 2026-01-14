import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "@/hooks/useAuth";
import { getClaims } from "@/context/auth-store";

export const GuestGuard = () => {
    const { isAuthenticated } = useAuth();
    const location = useLocation();

    const claims = getClaims();
    const isReallyAuthenticated = isAuthenticated || !!claims;

    if (isReallyAuthenticated) {
        const from = (location.state as { from?: Location })?.from?.pathname;

        if (from) {
            return <Navigate to={from} replace />;
        }


        const role = claims?.role?.toUpperCase();

        if (role === 'ROLE_DOCTOR') {
            return <Navigate to="/doctor/dashboard/upcoming" replace />;
        }

        if (role === 'ROLE_PATIENT') {
            return <Navigate to="/patient/dashboard/upcoming" replace />;
        }

        return <Navigate to="/" replace />;
    }

    return <Outlet />;
};