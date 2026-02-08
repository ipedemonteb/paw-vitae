import { lazy } from "react";
import { createBrowserRouter, Navigate } from "react-router-dom";


import { GuestGuard } from "@/guards/guestGuard";
import { AuthGuard } from "@/guards/authGuard";
import { ExcludeRolesGuard } from "@/guards/excludedRolesGuard";


const AppLayout = lazy(() => import("@/pages/AppLayout"));
const PatientDashboardLayout = lazy(() =>
    import("@/pages/patient/PatientDashboardLayout")
);
const DoctorDashboardLayout = lazy(() =>
    import("@/pages/doctor/DoctorDashboardLayout")
);


const Landing = lazy(() => import("@/pages/Landing"));
const About = lazy(() => import("@/pages/About"));
const PublicProfile = lazy(() => import("@/pages/PublicProfile"));
const VerifyAccount = lazy(() => import("@/pages/VerifiyAccount"));
const ChangePassword = lazy(() => import("@/pages/ChangePassword"));


const Login = lazy(() => import("@/pages/Login"));
const Register = lazy(() => import("@/pages/Register"));
const RecoverPassword = lazy(() => import("@/pages/ResetPassword"));


const Search = lazy(() => import("@/pages/Search"));
const GenericError = lazy(() => import("@/pages/GenericError"));
const Appointment = lazy(() => import("@/pages/Appointment"));
const AppointmentConfirmation = lazy(() =>
    import("@/pages/AppointmentConfirmation")
);
const AppointmentDetails = lazy(() =>
    import("@/pages/AppointmentDetails")
);


const UserUpcoming = lazy(() => import("@/pages/common/UserUpcoming"));
const UserHistory = lazy(() => import("@/pages/common/UserHistory"));
const PatientMedicalHistory = lazy(() =>
    import("@/pages/patient/PatientMedicalHistory")
);
const PatientAccount = lazy(() =>
    import("@/pages/patient/PatientAccount")
);


const DoctorAvailability = lazy(() =>
    import("@/pages/doctor/DoctorAvailability")
);
const DoctorOffices = lazy(() =>
    import("@/pages/doctor/DoctorOffices")
);
const DoctorAccount = lazy(() =>
    import("@/pages/doctor/DoctorAccount")
);
const MedicalHistory = lazy(() =>
    import("@/pages/MedicalHistory")
);

export const router = createBrowserRouter([
    {
        element: <AppLayout />,
        children: [
            // Rutas públicas
            { index: true, element: <Landing /> },
            { path: "about-us", element: <About /> },
            { path: "profile/:id", element: <PublicProfile /> },
            { path: "change-password", element: <ChangePassword /> },

            // Search (no doctor)
            {
                element: <ExcludeRolesGuard forbiddenRoles={["ROLE_DOCTOR"]} />,
                children: [
                    {
                        path: "search",
                        children: [
                            { index: true, element: <Search /> },
                            { path: ":id", element: <PublicProfile /> },
                        ],
                    },
                ],
            },

            // Guest only
            {
                element: <GuestGuard />,
                children: [
                    { path: "login", element: <Login /> },
                    { path: "verify", element: <VerifyAccount /> },
                    { path: "register", element: <Register /> },
                    { path: "recover-password", element: <RecoverPassword /> },
                ],
            },

            // Patient
            {
                element: <AuthGuard allowedRoles={["ROLE_PATIENT"]} />,
                children: [
                    {
                        path: "patient/dashboard",
                        element: <PatientDashboardLayout />,
                        children: [
                            { index: true, element: <Navigate to="upcoming" replace /> },
                            { path: "upcoming", element: <UserUpcoming /> },
                            { path: "history", element: <UserHistory /> },
                            {
                                path: "medical-history",
                                element: <PatientMedicalHistory />,
                            },
                            { path: "account", element: <PatientAccount /> },
                        ],
                    },
                    { path: "appointment/:id", element: <Appointment /> },
                    {
                        path: "appointment/:id/confirmation",
                        element: <AppointmentConfirmation />,
                    },
                    {
                        path: "patient/dashboard/appointment-details/:id",
                        element: <AppointmentDetails />,
                    },
                ],
            },

            // Doctor
            {
                element: <AuthGuard allowedRoles={["ROLE_DOCTOR"]} />,
                children: [
                    {
                        path: "doctor/dashboard",
                        element: <DoctorDashboardLayout />,
                        children: [
                            { index: true, element: <Navigate to="upcoming" replace /> },
                            { path: "upcoming", element: <UserUpcoming /> },
                            { path: "history", element: <UserHistory /> },
                            {
                                path: "availability",
                                element: <DoctorAvailability />,
                            },
                            { path: "offices", element: <DoctorOffices /> },
                            { path: "account", element: <DoctorAccount /> },
                        ],
                    },
                    {
                        path: "medical-history/:patientId",
                        element: <MedicalHistory />,
                    },
                    {
                        path: "doctor/dashboard/appointment-details/:id",
                        element: <AppointmentDetails />,
                    },
                ],
            },

            // Errors
            { path: "unauthorized", element: <GenericError code={403} /> },
            { path: "server-error", element: <GenericError code={500} /> },
            { path: "*", element: <GenericError code={404} /> },
        ],
    },
]);
