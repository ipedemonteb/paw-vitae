import { Input } from "@/components/ui/input";
import { Combobox } from "@/components/ui/combobox.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Search } from "lucide-react";

const heroSection=
    "pt-[100px] bg-[linear-gradient(135deg,var(--background-light)_0%,var(--primary-light)_100%)]";

function Landing() {
    return (
        <div className={heroSection}>
            <HeroSection />
        </div>
    )
}

const heroContainer =
    "relative overflow-visible block pt-[60px] pb-[80px] px-[20px] mx-auto max-w-[1200px] w-full";
const heroContent =
    "flex-[1.5] max-w-[700px] box-border";
const heroTitle =
    "text-[48px] font-[700] leading-[1.2] mb-[20px] text-[var(--text-color)]";
const heroSubtitle =
    "text-[18px] font-medium text-[var(--text-light)] mb-[30px]";
const heroSearch =
    "mb-[40px] w-full";
const searchBar =
    "flex shadow-md";
const heroInput =
    "bg-white rounded-r-none text-[var(--text-light)] py-6 px-4";
const heroCombo =
    "rounded-none py-6 text-gray-500 font-normal hover:text-[var(--text-light)] hover:bg-white cursor-pointer ";
const heroButton =
    "rounded-l-none py-6.25 px-8 bg-[var(--primary-color)] border-[var(--primary-color)] text-[var(--white)] hover:bg-[var(--primary-dark)]";
const heroStats =
    "flex gap-[40px]";
const statsItem =
    "flex flex-col box-border";
const statsNumber =
    "text-[36px] font-[700] leading-[1.2] text-[var(--primary-color)]";
const statsLabel =
    "text-[18px] text-[var(--text-light)]";
const heroSpace =
    "flex-1 flex justify-end relative";

function HeroSection() {
    return (
        <div className={heroContainer}>
            <div className={heroContent}>
                <h1 className={heroTitle}>
                    Find the Right Doctor for Your Health Needs
                </h1>
                <p className={heroSubtitle}>
                    Connect with verified healthcare professionals and book appointments online in minutes.
                </p>
                <div className={heroSearch}>
                    <div className={searchBar}>
                        <Input placeholder="Search by doctor name" className={heroInput} type="search"/>
                        <Combobox className={heroCombo}></Combobox>
                        <Button className={heroButton}>
                            <Search className="h-5 w-5"/>
                            Search
                        </Button>
                    </div>
                </div>
                <div className={heroStats}>
                    <div className={statsItem}>
                        <span className={statsNumber}>268</span>
                        <span className={statsLabel}>Doctors</span>
                    </div>
                    <div className={statsItem}>
                        <span className={statsNumber}>1000</span>
                        <span className={statsLabel}>Patients</span>
                    </div>
                </div>
            </div>
            <div className={heroSpace} />
        </div>
    )
}

export default Landing;