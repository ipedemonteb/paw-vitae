import React from "react";
import { Card } from "@/components/ui/card.tsx"

interface AppointmentContainerProps {
    children: React.ReactNode;
}

const cardContainer =
    "w-full flex flex-col px-6";

export default function DashboardNavContainer({children}: AppointmentContainerProps) {
    return (
       <Card className={cardContainer}>
            {children}
        </Card>
    );
}