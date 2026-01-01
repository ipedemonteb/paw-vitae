import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import {AuthProvider} from "@/context/authContext.tsx";
import "./i18n"
import {router} from "@/router.tsx";
import {RouterProvider} from "react-router-dom";

createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <AuthProvider>
            <RouterProvider router={router} />
        </AuthProvider>
    </StrictMode>,
)