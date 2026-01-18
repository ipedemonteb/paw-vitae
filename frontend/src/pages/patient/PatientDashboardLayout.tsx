import { Card } from "@/components/ui/card";
import { ButtonGroup } from "@/components/ui/button-group.tsx";
import { Calendar, History, ClipboardPlus, User } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";
import {Link, Outlet, useMatch} from "react-router-dom";
import { cn } from "@/lib/utils";
import PatientProfileCard from "@/components/PatientProfileCard.tsx";
import {useTranslation} from "react-i18next";
import {useAuth} from "@/hooks/useAuth.ts";

const dashboardContainer =
    "flex flex-col mt-36 px-5 mx-auto max-w-6xl w-full gap-6";
const sectionCard =
    "p-0 overflow-hidden shadow-[var(--shadow-md)]";
const tabsGroup =
    "w-full flex flex-col sm:flex-row";
const tabBtnBase =
    "relative flex-1 w-full h-16 py-4 sm:py-0 rounded-none bg-white text-[var(--text-light)] " +
    "hover:bg-white hover:text-[var(--primary-color)] font-[600] gap-2 " +
    "after:content-[''] after:absolute after:left-0 after:bottom-0 after:h-1 after:w-0 after:bg-[var(--primary-color)] " +
    "after:transition-[width] after:duration-300 hover:after:w-full";
const tabBtnActive =
    "text-[var(--primary-color)] after:w-full shadow-[inset_0_-1px_0_0_var(--primary-color)]";
const tabIcon =
    "h-5 w-5";
const tabIconActive =
    "text-[var(--primary-color)]";
const tabIconInactive =
    "text-[var(--text-light)] group-hover:text-[var(--primary-color)]";

function DashboardTab({ to, end, icon: Icon, children }: {
    to: string;
    end?: boolean;
    icon: React.ElementType;
    children: React.ReactNode;
}) {
    const match = useMatch({ path: to, end: end });
    return (
        <Button
            asChild
            type="button"
            variant="ghost"
            className={cn("group", tabBtnBase, match && tabBtnActive)}
        >
            <Link to={to}>
                <Icon className={cn(tabIcon, match ? tabIconActive : tabIconInactive)} />
                {children}
            </Link>
        </Button>
    );
}

function PatientDashboardLayout() {
    const auth=useAuth();
    const { t } = useTranslation();
    const patientId=auth.userId
    return (
        <div className={dashboardContainer}>
            <PatientProfileCard patientId={patientId || ""}/>
            <Card className={sectionCard}>
                <ButtonGroup orientation="horizontal" className={tabsGroup}>
                    <DashboardTab to="/patient/dashboard/upcoming" end icon={Calendar}>
                        {t("patient.dashboard.upcoming")}
                    </DashboardTab>
                    <DashboardTab to="/patient/dashboard/history" icon={History}>
                        {t("patient.dashboard.history")}
                    </DashboardTab>
                    <DashboardTab to="/patient/dashboard/medical-history" icon={ClipboardPlus}>
                        {t("patient.dashboard.medical-history")}
                    </DashboardTab>
                    <DashboardTab to="/patient/dashboard/account" icon={User}>
                        {t("patient.dashboard.account")}
                    </DashboardTab>
                </ButtonGroup>
            </Card>
            <main className="flex-1">
                <Outlet />
            </main>
        </div>
    );
}

export default PatientDashboardLayout;