import {
    createBrowserRouter
} from "react-router-dom";

import Landing from './pages/home/Landing.tsx';
import Login from './pages/login/Login.tsx'


export const router = createBrowserRouter(
    [
        {
            path: '/',
            element: <Landing />
        },
        {
            path: '/login',
            element: <Login />
        }


    ]
)