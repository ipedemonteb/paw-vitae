import React from "react";

type AppointmentHeaderProps = {
    title: string | React.ReactNode;
    children?: React.ReactNode
}

export default function DashboardNavHeader({title, children}: AppointmentHeaderProps) {
    return (
        <div className="flex flex-col sm:flex-row relative font-semibold text-xl w-full border-b items-center py-3 sm:justify-between min-h-15">
            <span>
                {title}
            </span>
            {children}
            <div className="hidden sm:block w-10 h-0 bg-violet-950 absolute bottom-0 left-0 border-b-4 rounded-2xl border-(--primary-color)">
            </div>
        </div>
    );
}