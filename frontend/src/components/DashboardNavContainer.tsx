import React from "react";

interface AppointmentContainerProps {
    children: React.ReactNode;
}

export default function DashboardNavContainer({children}: AppointmentContainerProps) {
    return (
        <div
            className="mt-25 w-full max-w-300 rounded-xl flex flex-col items-center justify-center p-6 gap-4 bg-white shadow h-full"
        >
            {children}
        </div>
    );
}