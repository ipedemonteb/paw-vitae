import { Card } from "@/components/ui/card";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar.tsx";
import { Mail, Phone } from "lucide-react";

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
    "p-0";

function PatientDashboardLayout() {
    return (
        <div className={dashboardCointainer}>
            <Card className={profileCard}>
                <Avatar className={avatarContainer}>
                    <AvatarImage src="https://picsum.photos/200"/>
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

            </Card>
        </div>
    )
}

export default PatientDashboardLayout;