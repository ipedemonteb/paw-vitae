import { createBrowserRouter, Navigate } from "react-router-dom";
import AppLayout from '@/pages/AppLayout.tsx';
import Landing from './pages/Landing.tsx';
import Login from './pages/Login.tsx'
import Register from "@/pages/Register.tsx";
import About from "@/pages/About.tsx";
import Search from "@/pages/Search.tsx";
import PatientDashboardLayout from "@/pages/patient/PatientDashboardLayout.tsx";
import UserUpcoming from "@/pages/common/UserUpcoming.tsx";
import UserHistory from "@/pages/common/UserHistory.tsx";
import PatientMedicalHistory from "@/pages/patient/PatientMedicalHistory.tsx";
import PatientAccount from "@/pages/patient/PatientAccount.tsx";
import DoctorDashboardLayout from "@/pages/doctor/DoctorDashboardLayout.tsx";
import DoctorAvailability from "@/pages/doctor/DoctorAvailability.tsx";
import DoctorOffices from "@/pages/doctor/DoctorOffices.tsx";
import DoctorAccount from "@/pages/doctor/DoctorAccount.tsx";
import PublicProfile from "@/pages/PublicProfile.tsx";
import AppointmentDetails from "@/pages/AppointmentDetails.tsx";
import RecoverPassword from "@/pages/ResetPassword.tsx";
import ChangePassword from "@/pages/ChangePassword.tsx";
import Appointment from "@/pages/Appointment.tsx";
import VerifyAccount from "@/pages/VerifiyAccount.tsx";

export const router = createBrowserRouter([
    {
        element: <AppLayout />,
        children: [
            {
                index: true,
                element: <Landing />
            },
            {
                path: 'login',
                element: <Login />
            },
            {
                path: 'register',
                element: <Register />
            },
            {
                path: 'about-us',
                element: <About />
            },
            {
                path: 'search',
                element: <Search />
            },
            {
                //TODO: change this path, its for development only
                path: "profile",
                element: <PublicProfile />
            },
            {
                //TODO: change this path, its for development only
                path: "appointment-details",
                element: <AppointmentDetails/>
            },
            {
                path: "appointment",
                element: <Appointment />
            },
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
               path:"recover-password",
                element:<RecoverPassword/>
            },
            {
                path:"change-password",
                element:<ChangePassword/>
            },
            {
              path:"verify",
                element:<VerifyAccount/>
            },
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
        ],
    }
])