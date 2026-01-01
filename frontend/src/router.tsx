import { createBrowserRouter } from "react-router-dom";
import AppLayout from '@/pages/layouts/AppLayout.tsx';
import Landing from './pages/home/Landing.tsx';
import Login from './pages/login/Login.tsx'


export const router = createBrowserRouter([
    {
        element: <AppLayout />,
        children: [
            {
                path: '/',
                element: <Landing />
            },
            {
                path: '/login',
                element: <Login />
            }
        ],
    }
])