import { Card } from "@/components/ui/card";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar.tsx";
import { ButtonGroup } from "@/components/ui/button-group.tsx";
import { Mail, Phone, Calendar, History, ClipboardPlus, User } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";
import {Link, Outlet, useMatch} from "react-router-dom";
import { cn } from "@/lib/utils";

const dashboardCointainer =
    "flex flex-col mt-36 px-5 mx-auto max-w-6xl w-full gap-6";
const profileCard =
    "flex flex-col gap-0 items-center sm:flex-row";
const avatarContainer =
    "flex items-center w-20 h-20 mx-6 mb-2 border border-[var(--primary-light)] border-4 rounded-full sm:mb-0";
const userDataContainer =
    "flex flex-col items-center md:items-start";
const userName =
    "text-[var(--text-color)] text-xl font-[700] mb-1";
const dataContainer =
    "flex flex-row gap-5 text-sm text-[var(--text-light)]";
const contactData =
    "flex flex-row items-center gap-1";
const contactIcon =
    "w-4 h-4";
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
    return (
        <div className={dashboardCointainer}>
            <Card className={profileCard}>
                <Avatar className={avatarContainer}>
                    <AvatarImage src="https://picsum.photos/200" />
                    <AvatarFallback>JD</AvatarFallback>
                </Avatar>
                <div className={userDataContainer}>
                    <h1 className={userName}>John Doe</h1>
                    <div className={dataContainer}>
                        <div className={contactData}>
                            <Mail className={contactIcon} />
                            <p className="max-w-[150px] truncate sm:max-w-[300px] sm:truncate">johndoe@gmail.com</p>
                        </div>
                        <div className={contactData}>
                            <Phone className={contactIcon} />
                            <p>11 1234-5678</p>
                        </div>
                    </div>
                </div>
            </Card>
            <Card className={sectionCard}>
                <ButtonGroup orientation="horizontal" className={tabsGroup}>
                    <DashboardTab to="/patient/dashboard/upcoming" end icon={Calendar}>
                        Upcoming
                    </DashboardTab>
                    <DashboardTab to="/patient/dashboard/history" icon={History}>
                        History
                    </DashboardTab>
                    <DashboardTab to="/patient/dashboard/medical-history" icon={ClipboardPlus}>
                        Medical History
                    </DashboardTab>
                    <DashboardTab to="/patient/dashboard/account" icon={User}>
                        Account
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
