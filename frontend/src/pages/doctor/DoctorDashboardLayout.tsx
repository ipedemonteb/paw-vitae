import { Card } from "@/components/ui/card";
import { ButtonGroup } from "@/components/ui/button-group.tsx";
import { Calendar, History, CalendarCheck, HousePlus, User } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";
import { Link, Outlet, useMatch } from "react-router-dom";
import { cn } from "@/lib/utils";
import DoctorProfileCard from "@/components/DoctorProfileCard.tsx";
import {useTranslation} from "react-i18next";

const dashboardCointainer =
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

function DoctorDashboardLayout() {
    const { t } = useTranslation();

    const doctorId = "24";

    return (
        <div className={dashboardCointainer}>
            <DoctorProfileCard doctorId={doctorId}/>
            <Card className={sectionCard}>
                <ButtonGroup orientation="horizontal" className={tabsGroup}>
                    <DashboardTab to="/doctor/dashboard/upcoming" end icon={Calendar}>
                        {t("doctor.dashboard.upcoming")}
                    </DashboardTab>
                    <DashboardTab to="/doctor/dashboard/history" icon={History}>
                        {t("doctor.dashboard.history")}
                    </DashboardTab>
                    <DashboardTab to="/doctor/dashboard/availability" icon={CalendarCheck}>
                        {t("doctor.dashboard.availability")}
                    </DashboardTab>
                    <DashboardTab to="/doctor/dashboard/offices" icon={HousePlus}>
                        {t("doctor.dashboard.offices")}
                    </DashboardTab>
                    <DashboardTab to="/doctor/dashboard/account" icon={User}>
                        {t("doctor.dashboard.account")}
                    </DashboardTab>
                </ButtonGroup>
            </Card>
            <main className="flex-1">
                <Outlet />
            </main>
        </div>
    );
}

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

export default DoctorDashboardLayout;
