import { createBrowserRouter } from "react-router-dom";
import AppLayout from '@/pages/AppLayout.tsx';
import Landing from './pages/Landing.tsx';
import Login from './pages/Login.tsx'
import Register from "@/pages/Register.tsx";
import About from "@/pages/About.tsx";


export const router = createBrowserRouter([
    {
        element: <AppLayout />,
        children: [
            {
                path: '/',
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
        ],
    }
])