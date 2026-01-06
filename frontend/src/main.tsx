import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import {AuthProvider} from "@/context/authContext.tsx";
import "./i18n"
import {router} from "@/router.tsx";
import {RouterProvider} from "react-router-dom";
import {QueryClientProvider, QueryClient} from"@tanstack/react-query"

const queryClient = new QueryClient();

createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <QueryClientProvider client={queryClient}>
            <AuthProvider>
                <RouterProvider router={router} />
            </AuthProvider>
        </QueryClientProvider>
    </StrictMode>,
)