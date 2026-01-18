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
import Confirmation from "@/pages/Confirmation.tsx";
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

import { GuestGuard } from "@/guards/guestGuard";
import { AuthGuard } from "@/guards/authGuard";
import { ExcludeRolesGuard } from "@/guards/excludedRolesGuard.tsx";
import MedicalHistory from "@/pages/MedicalHistory.tsx";

export const router = createBrowserRouter([
    {
        element: <AppLayout />,
        children: [

            // Rutas Públicas
            {
                index: true,
                element: <Landing />
            },
            {
                path: 'about-us',
                element: <About />
            },
            {
                path: "profile/:id",
                element: <PublicProfile />
            },
            // TODO: CHANGE
            {
                path: "medical-history",
                element: <MedicalHistory />
            },

            // Rutas Restringidas
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

            // Rutas sin Logueo Requerido
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

            // Rutas de Usuario Logueado (Paciente o Doctor)
            // {
            //     element: <AuthGuard />,
            //     children: [
            //         {
            //             path: "appointment/:id",
            //             element: <Appointment />
            //         },
            //     ]
            // },

            // Rutas de Paciente
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
                        ]
                    },
                    {
                        path: "appointment/:id",
                        element: <Appointment />,
                    },
                    {
                        path: "appointment/:id/confirmation",
                        element: <Confirmation />,
                    },
                    {
                        path: "patient/dashboard/appointment-details/:id",
                        element: <AppointmentDetails />,
                    },
                ]
            },

            // Rutas de Doctor
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
                    },
                    {
                        path: "doctor/dashboard/appointment-details/:id",
                        element: <AppointmentDetails />,
                    }
                ]
            },

            // Manejo de Errores
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