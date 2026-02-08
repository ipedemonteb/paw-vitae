import { StrictMode,Suspense  } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import {AuthProvider} from "@/context/authContext.tsx";
import "./i18n"
import {router} from "@/router.tsx";
import {RouterProvider} from "react-router-dom";
import {QueryClientProvider, QueryClient} from"@tanstack/react-query"
import {Spinner} from "@/components/ui/spinner.tsx";

const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            refetchOnWindowFocus: false
        }
    }
});

createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <QueryClientProvider client={queryClient}>
            <AuthProvider>
                <Suspense fallback={  <Spinner className="text-(--gray-400)"/>}>
                    <RouterProvider router={router} />
                </Suspense>
            </AuthProvider>
        </QueryClientProvider>
    </StrictMode>,
)