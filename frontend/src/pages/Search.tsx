import { useState } from "react";
import { Input } from "@/components/ui/input";
import { Search as SearchIcon, Stethoscope, ShieldPlus, ChevronsUpDown, Calendar, Funnel } from "lucide-react";
import { Combobox } from "@/components/ui/combobox.tsx";
import {Button} from "@/components/ui/button.tsx";

const container =
    "px-[24px] mx-auto max-w-6xl w-full";

function Search() {
    return (
        <div className={container}>
            <HeroSection />
            <FilterSection />
            <ResultSection />
        </div>
    );
}

const heroContainer =
    "relative mt-12 rounded-xl shadow-md py-10 px-8 mb-8 text-center " +
    "bg-[linear-gradient(135deg,var(--background-light)_0%,var(--landing-light)_100%)]";
const heroTitle =
    "text-3xl text-[var(--text-primary)] font-[700] mb-2";
const heroSubtitle =
    "text-lg text-[var(--text-light)] mb-6 font-[300]";
const searchWrapper =
    "relative w-full max-w-2xl mx-auto";
const searchHero =
    "w-full bg-white rounded-full pr-6 pl-12 py-5 text-slate-900 placeholder:text-slate-400";

function HeroSection() {
    return (
        <div className={heroContainer}>
            <h1 className={heroTitle}>Specialist Doctors</h1>
            <p className={heroSubtitle}>Doctors specializing in: Endocrinology</p>

            <div className={searchWrapper}>
                <SearchIcon className="pointer-events-none absolute left-5 top-1/2 h-5 w-5 -translate-y-1/2 text-slate-400" />
                <Input placeholder="Search for doctors" className={searchHero} />
            </div>
        </div>
    );
}

const filterContainer =
    "flex w-full flex-col gap-4 mb-4 sm:flex-row sm:items-center";
const filterGroup =
    "flex w-full items-center gap-3 text-[var(--text-light)] text-sm flex-1 min-w-0";
const filterLabel =
    "flex items-center gap-1.5 shrink-0";
const filterCombo =
    "w-full flex-1 min-w-0 hover:text-[var(--text-light)] cursor-pointer font-[400]";
const iconSize =
    "h-4 w-4";
const availabilityContainer =
    "flex flex-col mb-6 gap-3";
const availabilityTitle =
    "flex flex-row text-sm items-center gap-1.5 text-[var(--text-light)]";
const availabilityContent =
    "flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between";
const availabilityButtons =
    "flex flex-col gap-4 sm:flex-wrap sm:flex-row";
const availabilityButton =
    "bg-white text-[var(--gray-500)] hover:bg-white border border-[var(--gray-300)] hover:text-[var(--primary-color)] " +
    "hover:border-[var(--primary-color)] data-[selected=true]:bg-[var(--primary-color)] data-[selected=true]:text-white " +
    "data-[selected=true]:border-[var(--primary-color)]";
const availabilityButtonBase =
    "h-10 px-4 rounded-md";
const applyButton =
    "bg-[var(--primary-color)] hover:bg-[var(--primary-dark)] text-white px-8 py-5 rounded-md font-[400] text-sm";

function FilterSection() {

    const [selectedDays, setSelectedDays] = useState<Record<string, boolean>>({
        Monday: false,
        Tuesday: false,
        Wednesday: false,
        Thursday: false,
        Friday: false,
        Saturday: false,
        Sunday: false,
    });

    const toggleDay = (day: keyof typeof selectedDays) => {
        setSelectedDays((prev) => ({ ...prev, [day]: !prev[day] }));
    };

    return (
        <div>
            <div className={filterContainer}>
                <div className={filterGroup}>
                    <div className={filterLabel}>
                        <Stethoscope className={iconSize} />
                        <p>Specialty</p>
                    </div>
                    <Combobox className={filterCombo} />
                </div>
                <div className={filterGroup}>
                    <div className={filterLabel}>
                        <ShieldPlus className={iconSize} />
                        <p>Coverages</p>
                    </div>
                    <Combobox className={filterCombo} />
                </div>
                <div className={filterGroup}>
                    <div className={filterLabel}>
                        <ChevronsUpDown className={iconSize} />
                        <p>Sort by</p>
                    </div>
                    <Combobox className={filterCombo} />
                </div>
            </div>
            <div className={availabilityContainer}>
                <div className={availabilityTitle}>
                    <Calendar className={iconSize}/>
                    <p>Availability</p>
                </div>
                <div className={availabilityContent}>
                    <div className={availabilityButtons}>
                        {Object.keys(selectedDays).map((day) => (
                            <Button
                                key={day}
                                type="button"
                                variant="outline"
                                data-selected={selectedDays[day]}
                                className={`${availabilityButtonBase} ${availabilityButton}`}
                                onClick={() => toggleDay(day as keyof typeof selectedDays)}
                            >
                                {day}
                            </Button>
                        ))}
                    </div>
                    <Button className={applyButton}>
                        <Funnel className=""/>
                        Apply Filters
                    </Button>
                </div>
            </div>
        </div>
    );
}

const resultContainer =
    "";

function ResultSection() {
    return (
        <div className={resultContainer}>
        </div>
    )
}

export default Search;