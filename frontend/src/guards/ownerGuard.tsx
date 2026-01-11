import { Navigate, Outlet, useParams } from "react-router-dom";
import { useAuth } from "@/hooks/useAuth";

export const OwnerGuard = () => {
    const { userId, role } = useAuth();
    const params = useParams();

    const resourceId = params.id || params.patientId || params.userId || Object.values(params)[0];

    if (!resourceId || !userId) {
        return <Navigate to="/unauthorized" replace />;
    }

    const isOwner = String(userId) === String(resourceId);
    const isDoctor = role?.toUpperCase() === 'ROLE_DOCTOR';

    if (isOwner || isDoctor) {
        return <Outlet />;
    }

    return <Navigate to="/unauthorized" replace />;
};