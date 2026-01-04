import { Outlet } from "react-router-dom";
import Header from "@/components/Header.tsx";
import ScrollToTop from "@/components/ScrollToTop.tsx";

export default function AppLayout() {
    return (
        <div className="flex flex-col min-h-screen">
            <Header />
            <ScrollToTop />
            <main className="flex-1">
                <Outlet />
            </main>
        </div>
    );
}