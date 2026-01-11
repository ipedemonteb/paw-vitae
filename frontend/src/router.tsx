import { createBrowserRouter, Navigate } from "react-router-dom";
import AppLayout from '@/pages/AppLayout.tsx';

import Landing from './pages/Landing.tsx';
import About from "@/pages/About.tsx";
import PublicProfile from "@/pages/PublicProfile.tsx";
import VerifyAccount from "@/pages/VerifiyAccount.tsx";

import Login from './pages/Login.tsx';
import Register from "@/pages/Register.tsx";
import RecoverPassword from "@/pages/ResetPassword.tsx";
import ChangePassword from "@/pages/ChangePassword.tsx";

import Search from "@/pages/Search.tsx";

import Appointment from "@/pages/Appointment.tsx";
import AppointmentDetails from "@/pages/AppointmentDetails.tsx";
import PatientDashboardLayout from "@/pages/patient/PatientDashboardLayout.tsx";
import UserUpcoming from "@/pages/common/UserUpcoming.tsx";
import UserHistory from "@/pages/common/UserHistory.tsx";
import PatientMedicalHistory from "@/pages/patient/PatientMedicalHistory.tsx";
import PatientAccount from "@/pages/patient/PatientAccount.tsx";

import DoctorDashboardLayout from "@/pages/doctor/DoctorDashboardLayout.tsx";
import DoctorAvailability from "@/pages/doctor/DoctorAvailability.tsx";
import DoctorOffices from "@/pages/doctor/DoctorOffices.tsx";
import DoctorAccount from "@/pages/doctor/DoctorAccount.tsx";
import GenericError from "@/pages/GenericError.tsx";

import { GuestGuard } from "@/guards/GuestGuard";
import { AuthGuard } from "@/guards/AuthGuard";
import { ExcludeRolesGuard } from "@/guards/excludedRolesGuard.tsx";

export const router = createBrowserRouter([
    {
        element: <AppLayout />,
        children: [
            // ============================================================
            // 1. RUTAS TOTALMENTE PÚBLICAS (Accesibles para TODOS)
            // ============================================================
            {
                index: true,
                element: <Landing />
            },
            {
                path: 'about-us',
                element: <About />
            },
            {
                path: "profile",
                element: <PublicProfile />
            },

            // ============================================================
            // 2. RUTAS PÚBLICAS RESTRINGIDAS (Search)
            // Pacientes e Invitados PUEDEN entrar. Doctores NO.
            // ============================================================
            {
                element: <ExcludeRolesGuard forbiddenRoles={['ROLE_DOCTOR']}  />,
                children: [
                    {
                        path: 'search',
                        children: [
                            {
                                index: true,
                                element: <Search />
                            },
                            {
                                path: ":id",
                                element: <PublicProfile/>
                            }
                        ]
                    }
                ]
            },

            // ============================================================
            // 3. RUTAS DE INVITADO (Solo si NO estás logueado)
            // Si ya tienes sesión, te rebota al Home
            // ============================================================
            {
                element: <GuestGuard />,
                children: [
                    {
                        path: 'login',
                        element: <Login />
                    },
                    {
                        path: "verify",
                        element: <VerifyAccount/>
                    },
                    {
                        path: 'register',
                        element: <Register />
                    },
                    {
                        path: "recover-password",
                        element: <RecoverPassword/>
                    },
                    {
                        path: "change-password",
                        element: <ChangePassword/>
                    },
                ]
            },

            // ============================================================
            // 4. RUTAS PROTEGIDAS GENERALES (Cualquier usuario logueado)
            // ============================================================
            {
                element: <AuthGuard />,
                children: [
                    {
                        path: "appointment-details",
                        element: <AppointmentDetails/>
                    },
                ]
            },

            // ============================================================
            // 5. RUTAS DE PACIENTE (Solo Rol PATIENT)
            // ============================================================
            {
                element: <AuthGuard allowedRoles={['ROLE_PATIENT']} />,
                children: [
                    {
                        path: "patient/dashboard",
                        element: <PatientDashboardLayout />,
                        children: [
                            {
                                index: true,
                                element: <Navigate to="upcoming" replace />
                            },
                            {
                                path: "upcoming",
                                element: <UserUpcoming/>
                            },
                            {
                                path: "history",
                                element: <UserHistory/>
                            },
                            {
                                path: "medical-history",
                                element: <PatientMedicalHistory/>
                            },
                            {
                                path: "account",
                                element: <PatientAccount/>
                            },
                            {
                                path: "appointment",
                                element: <Appointment />
                            },
                        ]
                    }
                ]
            },

            // ============================================================
            // 6. RUTAS DE DOCTOR (Solo Rol DOCTOR)
            // ============================================================
            {
                element: <AuthGuard allowedRoles={['ROLE_DOCTOR']} />,
                children: [
                    {
                        path: "doctor/dashboard",
                        element: <DoctorDashboardLayout />,
                        children: [
                            {
                                index: true,
                                element: <Navigate to="upcoming" replace />
                            },
                            {
                                path: "upcoming",
                                element: <UserUpcoming/>
                            },
                            {
                                path: "history",
                                element: <UserHistory/>
                            },
                            {
                                path: "availability",
                                element: <DoctorAvailability/>
                            },
                            {
                                path: "offices",
                                element: <DoctorOffices/>
                            },
                            {
                                path: "account",
                                element: <DoctorAccount/>
                            }
                        ]
                    }
                ]
            },

            // ============================================================
            // 7. MANEJO DE ERRORES
            // ============================================================
            {
                path: "/unauthorized",
                element: <GenericError code={403} />
            },
            {
                path: "/server-error",
                element: <GenericError code={500} />
            },
            {
                path: "*",
                element: <GenericError code={404} />
            }
        ],
    }
])