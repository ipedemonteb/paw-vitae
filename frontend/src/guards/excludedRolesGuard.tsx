import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "@/hooks/useAuth";

interface ExcludeRolesGuardProps {
    forbiddenRoles: string[];
}

export const ExcludeRolesGuard = ({
                                      forbiddenRoles
                                  }: ExcludeRolesGuardProps) => {
    const { isAuthenticated, role } = useAuth();
    if (isAuthenticated && role && forbiddenRoles.includes(role.toUpperCase())) {
        return <Navigate to="unauthorized" replace />;
    }

    return <Outlet />;
};