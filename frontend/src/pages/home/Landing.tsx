import { Input } from "@/components/ui/input";
import { Combobox } from "@/components/ui/combobox.tsx";
import { Button } from "@/components/ui/button.tsx";
// import { Card, CardContent } from "@/components/ui/card"
import { Search, PersonStandingIcon, Lightbulb } from "lucide-react";
import { Card } from "@/components/ui/card.tsx";

const landingPage =
    "pt-[100px]";
const heroSection=
    "bg-[linear-gradient(135deg,var(--background-light)_0%,var(--landing-light)_100%)]";

function Landing() {
    return (
        <div className={landingPage}>
            <div className={heroSection}>
                <HeroSection />
            </div>
            <MissionSection />
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

const missionContainer =
    "mx-auto max-w-[1200px] w-full px-[20px] py-[60px] flex flex-col items-center text-center";
const missionHeader =
    "max-w-[800px] text-center mx-auto mt-0 mb-[60px]";
const missionTag =
    "inline-block py-[6px] px-[16px] bg-[var(--landing-light)] text-[var(--primary-color)] rounded-[20px] text-[14px] font-[500] mb-[16px]";
const missionTitle =
    "mb-4 rounded-full px-4 py-[6px] text-[36px] font-[700] text-[var(--text-color)]";
const missionText =
    "text-[18px] text-[var(--text-light)]";
const missionCards =
    "max-w-[1000px] flex gap-[50px] grid-cols-[repeat(auto-fit,minmax(300px,1fr))]";
const missionCard =
    "px-[30px] py-[40px] gap-[0] flex flex-col items-center text-center";
const missionIcons =
    "w-[70px] h-[70px] mb-[24px] flex items-center justify-center bg-[var(--landing-light)] text-[var(--primary-color)] rounded-[50%] text-[28px]";
const missionPersonIcon =
    "text-[var(--landing-light)] bg-[var(--primary-color)] rounded-[50%]";
const missionCardTitle =
    "text-[22px] font-[600] mb-[16px] text-[var(--text-color)]";
const missionCardText =
    "text-[var(--text-light)]";

function MissionSection() {
    return (
        <div className={missionContainer}>
            <div className={missionHeader}>
                <span className={missionTag}>
                    Mission
                </span>
                <h1 className={missionTitle}>
                    Our Mission
                </h1>
                <p className={missionText}>
                    We're committed to making healthcare accessible, transparent, and efficient for everyone.
                </p>
            </div>
            <div className={missionCards}>
                <Card className={missionCard}>
                    <div className={missionIcons}>
                        <PersonStandingIcon className={missionPersonIcon}/>
                    </div>
                    <h3 className={missionCardTitle}>Accessible Healthcare</h3>
                    <p className={missionCardText}>
                        We believe everyone deserves easy access to quality healthcare services without barriers.
                    </p>
                </Card>
                <Card className={missionCard}>
                    <div className={missionIcons}>
                        <Lightbulb />
                    </div>
                    <h3 className={missionCardTitle}>Innovative Solutions</h3>
                    <p className={missionCardText}>
                        We leverage technology to create seamless experiences for patients and healthcare providers.
                    </p>
                </Card>
            </div>
        </div>
    )
}

export default Landing;