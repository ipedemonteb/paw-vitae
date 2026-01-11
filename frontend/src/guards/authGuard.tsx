import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "@/hooks/useAuth";

interface AuthGuardProps {
    allowedRoles?: string[]; // Ejemplo: ['DOCTOR', 'PATIENT']
}

export const AuthGuard = ({ allowedRoles }: AuthGuardProps) => {
    const { isAuthenticated, role } = useAuth();
    const location = useLocation();
    if (!isAuthenticated) {
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    if (allowedRoles && allowedRoles.length > 0) {
        const userRoleUpper = role?.toUpperCase() || "";
        const hasPermission = allowedRoles.some(r => r.toUpperCase() === userRoleUpper);
        if (!hasPermission) {
            return <Navigate to="/unauthorized" replace />;
        }
    }

    return <Outlet />;
};