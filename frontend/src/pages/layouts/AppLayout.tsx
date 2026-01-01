import { Outlet } from "react-router-dom";
import Header from "@/components/Header.tsx";

export default function AppLayout() {
    return (
        <div className="flex flex-col min-h-screen">
            <Header />
            <main className="flex-1 pt-[100px]">
                <Outlet />
            </main>
        </div>
    );
}