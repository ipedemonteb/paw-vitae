import { createBrowserRouter, Navigate } from "react-router-dom";
import AppLayout from '@/pages/AppLayout.tsx';
import Landing from './pages/Landing.tsx';
import Login from './pages/Login.tsx'
import Register from "@/pages/Register.tsx";
import About from "@/pages/About.tsx";
import Search from "@/pages/Search.tsx";
import PatientDashboardLayout from "@/pages/patient/PatientDashboardLayout.tsx";
import PatientUpcoming from "@/pages/patient/PatientUpcoming.tsx";
import PatientHistory from "@/pages/patient/PatientHistory.tsx";
import PatientMedicalHistory from "@/pages/patient/PatientMedicalHistory.tsx";
import PatientAccount from "@/pages/patient/PatientAccount.tsx";

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
                path: "patient/dashboard",
                element: <PatientDashboardLayout />,
                children: [
                    {
                        index: true,
                        element: <Navigate to="upcoming" replace />
                    },
                    {
                        path: "upcoming",
                        element: <PatientUpcoming/>
                    },
                    {
                        path: "history",
                        element: <PatientHistory/>
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
            }
        ],
    }
])