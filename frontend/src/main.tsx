
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.tsx'
import './index.css'
import {AuthProvider} from "@/context/authContext.tsx";
import "./i18n"

createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <AuthProvider>
        <App />
        </AuthProvider>
    </StrictMode>,
)