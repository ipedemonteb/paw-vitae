import React from "react";

type AppointmentHeaderProps = {
    title: string;
    children: React.ReactNode
}

export default function DashboardNavHeader({title, children}: AppointmentHeaderProps) {
    return (
        <div className="mb-2 relative font-semibold text-xl w-full flex border-b items-center py-3 justify-between">
            <span>
                {title}
            </span>
            {children}
            <div className="w-10 h-0 bg-violet-950 absolute bottom-0 left-0 border-b-4 rounded-2xl border-(--primary-color)">
            </div>
        </div>
    );
}