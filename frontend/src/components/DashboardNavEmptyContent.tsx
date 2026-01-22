import React from "react";
import type { LucideProps } from "lucide-react";

type DashboardNavEmptyContentProps = {
    title: string,
    text: string,
    Icon: React.ComponentType<LucideProps>
}

export default function DashboardNavEmptyContent({title, text, Icon}: DashboardNavEmptyContentProps) {
    return (
        <div className="w-full flex flex-col gap-2 items-center rounded-xl justify-center h-72 bg-gray-100 border border-dashed border-(--gray-400)">
            <Icon size={60} className="text-(--text-light)"/>
            <span className="font-semibold text-sm">{title}</span>
            <span className="text-sm max-w-1/3 text-center text-(--text-light)">{text}</span>
        </div>
    )
}