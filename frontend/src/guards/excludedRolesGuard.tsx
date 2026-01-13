import {Navigate, Outlet, useLocation} from "react-router-dom";
import { useAuth } from "@/hooks/useAuth";

interface ExcludeRolesGuardProps {
    forbiddenRoles: string[];
}

export const ExcludeRolesGuard = ({
                                      forbiddenRoles
                                  }: ExcludeRolesGuardProps) => {
    const { isAuthenticated, role,userId } = useAuth();
    const location = useLocation();
    if (isAuthenticated && role && forbiddenRoles.includes(role.toUpperCase()) && ( userId && !location.pathname.includes(userId.toString()) && location.pathname.includes("search")) ) {
        return <Navigate to="unauthorized" replace />;
    }

    return <Outlet />;
};