import { Input } from "@/components/ui/input";
import { Combobox } from "@/components/ui/combobox.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Search, PersonStandingIcon, Lightbulb, CalendarDays, ShieldPlus, ShieldCheck, Pointer } from "lucide-react";
import { Card } from "@/components/ui/card.tsx";
import { RatingCard } from "@/components/ui/rating.tsx";
import { Carousel, CarouselContent, CarouselItem, CarouselNext, CarouselPrevious } from "@/components/ui/carousel.tsx";

// TODO: internacionalizacion

const landingPage =
    "pt-[100px]";
const heroSection=
    "bg-[linear-gradient(135deg,var(--background-light)_0%,var(--landing-light)_100%)]";
const grayBackground =
    "bg-[var(--background-light)]";

function Landing() {
    return (
        <div className={landingPage}>
            <div className={heroSection}>
                <HeroSection />
            </div>
            <MissionSection />
            <div className={grayBackground}>
                <ProcessSection />
            </div>
            <FeaturesSection />
            <div className={grayBackground}>
                <RatingsSection />
            </div>
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

const container =
    "mx-auto max-w-[1200px] w-full px-[20px] py-[60px] flex flex-col items-center text-center";
const sectionHeader =
    "max-w-[800px] text-center mx-auto mt-0 mb-[60px]";
const sectionTag =
    "inline-block py-[6px] px-[16px] bg-[var(--landing-light)] text-[var(--primary-color)] rounded-[20px] text-[14px] font-[500] mb-[16px]";
const sectionTitle =
    "mb-4 rounded-full px-4 py-[6px] text-[36px] font-[700] text-[var(--text-color)]";
const sectionText =
    "text-[18px] text-[var(--text-light)]";
const sectionCards =
    "max-w-[1000px] flex gap-[50px] grid-cols-[repeat(auto-fit,minmax(300px,1fr))]";
const sectionCard =
    "px-[30px] py-[40px] gap-[0] flex flex-col items-center text-center";
const sectionIcons =
    "w-[70px] h-[70px] mb-[24px] flex items-center justify-center bg-[var(--landing-light)] text-[var(--primary-color)] rounded-[50%] text-[28px]";
const sectionPersonIcon =
    "text-[var(--landing-light)] bg-[var(--primary-color)] rounded-[50%]";
const sectionCardTitle =
    "text-[22px] font-[600] mb-[16px] text-[var(--text-color)]";
const sectionCardText =
    "text-[var(--text-light)]";

function MissionSection() {
    return (
        <div className={container}>
            <div className={sectionHeader}>
                <span className={sectionTag}>
                    Mission
                </span>
                <h1 className={sectionTitle}>
                    Our Mission
                </h1>
                <p className={sectionText}>
                    We're committed to making healthcare accessible, transparent, and efficient for everyone.
                </p>
            </div>
            <div className={sectionCards}>
                <Card className={sectionCard}>
                    <div className={sectionIcons}>
                        <PersonStandingIcon className={sectionPersonIcon}/>
                    </div>
                    <h3 className={sectionCardTitle}>Accessible Healthcare</h3>
                    <p className={sectionCardText}>
                        We believe everyone deserves easy access to quality healthcare services without barriers.
                    </p>
                </Card>
                <Card className={sectionCard}>
                    <div className={sectionIcons}>
                        <Lightbulb />
                    </div>
                    <h3 className={sectionCardTitle}>Innovative Solutions</h3>
                    <p className={sectionCardText}>
                        We leverage technology to create seamless experiences for patients and healthcare providers.
                    </p>
                </Card>
            </div>
        </div>
    )
}

const stepsSection =
    "flex flex-col gap-[40px] max-w-[800px] mx-auto";
const step =
    "flex items-center gap-[30px] relative";
const stepNumber =
    "shrink-0 w-[60px] h-[60px] flex items-center justify-center bg-[var(--primary-color)] rounded-[50%] text-[var(--white)] font-[700] text-[24px]";
const stepContent =
    "p-[30px] gap-[20px] flex flex-row items-center";
const stepDescription =
    "flex flex-col items-start text-left";
const stepIcon =
    "w-[60px] h-[60px] flex items-center justify-center rounded-[12px] text-[var(--primary-color)] bg-[var(--landing-light)]";
const stepTitle =
    "text-[22px] font-[600] text-[var(--text-color)]";
const stepText =
    "text-[var(--text-light)]";
const startButton =
    "mt-[60px] p-[26px] text-[16px] font-[500] bg-[var(--primary-color)] hover:bg-[var(--primary-dark)] cursor-pointer";

function ProcessSection() {
    return (
        <div className={container}>
            <div className={sectionHeader}>
                <span className={sectionTag}>
                    Process
                </span>
                <h1 className={sectionTitle}>
                    How Vitae Works
                </h1>
                <p className={sectionText}>
                    Finding and booking appointments with healthcare professionals has never been easier.
                </p>
            </div>
            <div className={stepsSection}>
                <div className={step}>
                    <div className={stepNumber}>1</div>
                    <Card className={stepContent}>
                        <div className={stepIcon}>
                            <Search />
                        </div>
                        <div className={stepDescription}>
                            <h3 className={stepTitle}>Search for Specialists</h3>
                            <p className={stepText}>Find the right healthcare professional by specialty, location, or availability.</p>
                        </div>
                    </Card>
                </div>
                <div className={step}>
                    <div className={stepNumber}>2</div>
                    <Card className={stepContent}>
                        <div className={stepIcon}>
                            <CalendarDays />
                        </div>
                        <div className={stepDescription}>
                            <h3 className={stepTitle}>Book an Appointment</h3>
                            <p className={stepText}>Select a convenient time slot and book your appointment in just a few clicks.</p>
                        </div>
                    </Card>
                </div>
                <div className={step}>
                    <div className={stepNumber}>3</div>
                    <Card className={stepContent}>
                        <div className={stepIcon}>
                            <ShieldPlus />
                        </div>
                        <div className={stepDescription}>
                            <h3 className={stepTitle}>Visit Your Doctor</h3>
                            <p className={stepText}>Attend your appointment and receive the care you need from qualified professionals.</p>
                        </div>
                    </Card>
                </div>
            </div>
            {/*TODO: Make button go to login*/}
            <Button className={startButton}>Get Started Now</Button>
        </div>
    )
}

function FeaturesSection() {
    return (
        <div className={container}>
            <div className={sectionHeader}>
                <span className={sectionTag}>
                    Benefits
                </span>
                <h1 className={sectionTitle}>
                    Why Choose Vitae
                </h1>
                <p className={sectionText}>
                    Our platform offers unique benefits for patients seeking quality healthcare.
                </p>
            </div>
            <div className={sectionCards}>
                <Card className={sectionCard}>
                    <div className={sectionIcons}>
                        <ShieldCheck />
                    </div>
                    <h3 className={sectionCardTitle}>Secure & Private</h3>
                    <p className={sectionCardText}>
                        Your personal and medical information is protected with advanced security.
                    </p>
                </Card>
                <Card className={sectionCard}>
                    <div className={sectionIcons}>
                        <Pointer />
                    </div>
                    <h3 className={sectionCardTitle}>Easy to Use</h3>
                    <p className={sectionCardText}>
                        Our intuitive interface makes finding and booking appointments simple.
                    </p>
                </Card>
            </div>
        </div>
    )
}

const ratings = [
    {
        comment:
            "Muy buena atención. Me atendieron rápido y el doctor explicó todo con claridad.",
        rating: 5,
        userName: "John Doe",
        timeAgo: "2 days ago",
    },
    {
        comment:
            "Excelente plataforma. Encontré un especialista en minutos y conseguí turno para el mismo día.",
        rating: 5,
        userName: "María González",
        timeAgo: "1 week ago",
    },
    {
        comment:
            "La experiencia fue muy simple y clara. Me gustó poder filtrar por especialidad y disponibilidad.",
        rating: 4,
        userName: "Lucas Pérez",
        timeAgo: "3 weeks ago",
    },
];

const ratingsContainer =
    "w-full max-w-[900px] mx-auto";
const carousel =
    "relative max-w-[800px] w-full mx-auto px-12 mt-[-20px]";
const carouselContent =
    "-ml-4 py-2";
const carouselItem =
    "pl-4 basis-full";

function RatingsSection() {
    return (
        <div className={container}>
            <div className={sectionHeader}>
                <span className={sectionTag}>
                    Ratings
                </span>
                <h1 className={sectionTitle}>
                    Doctor Ratings
                </h1>
                <p className={sectionText}>
                    See what our patients say about our doctors.
                </p>
            </div>
            <div className={ratingsContainer}>
                <Carousel opts={{ align: "start", loop: true }} className={carousel}>
                    <CarouselContent className={carouselContent}>
                        {ratings.map((r, idx) => (
                            <CarouselItem key={idx} className={carouselItem}>
                                <div className="py-2">
                                    <RatingCard
                                        className="max-w-none"
                                        comment={r.comment}
                                        rating={r.rating}
                                        userName={r.userName}
                                        timeAgo={r.timeAgo}
                                    />
                                </div>
                            </CarouselItem>
                        ))}
                    </CarouselContent>
                    <CarouselPrevious className="left-2" />
                    <CarouselNext className="right-2" />
                </Carousel>
            </div>
        </div>
    )
}

export default Landing;