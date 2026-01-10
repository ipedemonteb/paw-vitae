import { Card } from "@/components/ui/card.tsx"
import DoctorProfileCard from "@/components/DoctorProfileCard.tsx";
import { Stethoscope, Hospital } from "lucide-react";
import {Select, SelectContent, SelectGroup, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.tsx";

const appointmentBackground =
    "bg-[var(--background-light)] flex justify-center items-start min-h-screen";
const cardContainer =
    "mt-36 px-5 mx-auto max-w-6xl w-full mb-8";
const appointmentContainer =
    "p-0 pb-8";
const appointmentHeader =
    "flex flex-col items-center py-8 rounded-t-lg gap-3 " +
    "bg-[linear-gradient(135deg,var(--background-light)_0%,var(--landing-light)_100%)]"
const appointmentTitle =
    "font-bold text-4xl text-center text-[var(--text-color)]";
const appointmentContent =
    "flex flex-col px-8 gap-4";
const selectorsContainer =
    "flex flex-col md:flex-row gap-6";
const selectorCard =
    "flex flex-row w-full gap-0";
const iconContainer =
    "bg-[var(--primary-bg)] rounded-full p-5 text-[var(--primary-color)] mx-5";
const icon =
    "w-8 h-8"
const selectorContent =
    "flex flex-col gap-1"
const selectorButton =
    "cursor-pointer"

function Appointment() {
    const selectedSpecialty = "General";

    return (
        <div className={appointmentBackground}>
            <div className={cardContainer}>
                <Card className={appointmentContainer}>
                    <div className={appointmentHeader}>
                        <h1 className={appointmentTitle}>Book an Appointment</h1>
                        <p>Fill out the form below to schedule your appointment</p>
                    </div>
                    <div className={appointmentContent}>
                        <DoctorProfileCard />
                        <div className={selectorsContainer}>
                            <Card className={selectorCard}>
                                <div className={iconContainer}>
                                    <Stethoscope className={icon}/>
                                </div>
                                <div className={selectorContent}>
                                    <p>Selected Specialty:</p>
                                    <Select>
                                        <SelectTrigger className={selectorButton}>
                                            <SelectValue placeholder={selectedSpecialty}/>
                                        </SelectTrigger>
                                        <SelectContent>
                                            <SelectGroup>
                                                <SelectItem value="general">General</SelectItem>
                                                <SelectItem value="cardiology">Cardiology</SelectItem>
                                                <SelectItem value="endocrinology">Endocrinology</SelectItem>
                                                <SelectItem value="dermatolofy">Dermatology</SelectItem>
                                            </SelectGroup>
                                        </SelectContent>
                                    </Select>
                                </div>
                            </Card>
                            <Card className={selectorCard}>
                                <div className={iconContainer}>
                                    <Hospital className={icon}/>
                                </div>
                                <div className={selectorContent}>
                                    <p>Selected Office:</p>
                                    <Select>
                                        <SelectTrigger className={selectorButton}>
                                            <SelectValue placeholder="Select Office"/>
                                        </SelectTrigger>
                                        <SelectContent>
                                            <SelectGroup>
                                                <SelectItem value="main">Main Office</SelectItem>
                                                <SelectItem value="hospital">Hospital</SelectItem>
                                                <SelectItem value="oficina2">Oficina Mataderos</SelectItem>
                                                <SelectItem value="oficina3">Oficina Parque Chas</SelectItem>
                                            </SelectGroup>
                                        </SelectContent>
                                    </Select>
                                </div>
                            </Card>
                        </div>
                    </div>
                </Card>
            </div>
        </div>
    )
}

export default Appointment;