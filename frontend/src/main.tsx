import { StrictMode  } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import {AuthProvider} from "@/context/authContext.tsx";
import "./i18n"
import {router} from "@/router.tsx";
import {RouterProvider} from "react-router-dom";
import {QueryClientProvider, QueryClient} from"@tanstack/react-query"
// import {Spinner} from "@/components/ui/spinner.tsx";
// import {LoadingFullPageComponent} from "@/components/LoadingFullPageComponent.tsx";

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
                {/*<Suspense fallback={ <LoadingFullPageComponent/>}>*/}
                    <RouterProvider router={router} />
                {/*</Suspense>*/}
            </AuthProvider>
        </QueryClientProvider>
    </StrictMode>,
)