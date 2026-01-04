import { Input } from "@/components/ui/input.tsx";
import { Combobox } from "@/components/ui/combobox.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Search, PersonStandingIcon, Lightbulb, CalendarDays, ShieldPlus, ShieldCheck, Pointer } from "lucide-react";
import { Card } from "@/components/ui/card.tsx";
import { RatingCard } from "@/components/ui/rating.tsx";
import { Carousel, CarouselContent, CarouselItem, CarouselNext, CarouselPrevious } from "@/components/ui/carousel.tsx";
import { Link } from "react-router-dom";

// TODO: internacionalizacion

const heroSection=
    "bg-[linear-gradient(135deg,var(--background-light)_0%,var(--landing-light)_100%)]";
const grayBackground =
    "bg-[var(--background-light)]";

function Landing() {
    return (
        <div>
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
    "relative overflow-visible block pt-16 pb-20 px-5 mx-auto max-w-6xl w-full";
const heroContent =
    "flex-[1.5] max-w-2xl box-border";
const heroTitle =
    "text-5xl font-bold leading-[1.2] mb-5 text-[var(--text-color)]";
const heroSubtitle =
    "text-lg font-medium text-[var(--text-light)] mb-8";
const heroSearch =
    "mb-10 w-full bg-transparent";
const searchBar =
    "flex items-stretch overflow-hidden rounded-md border border-gray-200 bg-white shadow-md";
const heroInput =
    "flex-1 h-12 border-0 rounded-none px-4 text-[var(--text-light)] shadow-none focus-visible:ring-0 hover:bg-[var(--gray-100)]";
const heroCombo =
    "h-12 border-0 rounded-none px-4 text-gray-500 font-normal hover:text-[var(--text-light)] hover:bg-[var(--gray-100)] cursor-pointer shadow-none border-l border-gray-200";
const heroButton =
    "h-12 rounded-none px-8 bg-[var(--primary-color)] text-[var(--white)] hover:bg-[var(--primary-dark)] cursor-pointer";
const heroStats =
    "flex gap-10";
const statsItem =
    "flex flex-col box-border";
const statsNumber =
    "text-4xl font-bold leading-[1.2] text-[var(--primary-color)]";
const statsLabel =
    "text-lg text-[var(--text-light)]";
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
    "mx-auto max-w-6xl w-full px-5 py-16 flex flex-col items-center text-center";
const sectionHeader =
    "max-w-3xl text-center mx-auto mt-0 mb-16";
const sectionTag =
    "inline-block py-1.5 px-4 bg-[var(--landing-light)] text-[var(--primary-color)] rounded-2xl text-sm font-medium mb-4";
const sectionTitle =
    "mb-4 rounded-full px-4 py-1.5 text-4xl font-bold text-[var(--text-color)]";
const sectionText =
    "text-lg text-[var(--text-light)]";
const sectionCards =
    "max-w-5xl w-full flex flex-col md:flex-row gap-8 md:gap-12 items-stretch";
const sectionCard =
    "flex-1 px-8 py-10 gap-0 flex flex-col items-center text-center";
const sectionIcons =
    "w-16 h-16 mb-6 flex items-center justify-center bg-[var(--landing-light)] text-[var(--primary-color)] rounded-full text-3xl";
const sectionPersonIcon =
    "text-[var(--landing-light)] bg-[var(--primary-color)] rounded-full";
const sectionCardTitle =
    "text-xl font-semibold mb-4 text-[var(--text-color)]";
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
    "flex flex-col gap-10 max-w-3xl mx-auto";
const step =
    "flex items-center gap-8 relative";
const stepNumber =
    "shrink-0 flex-none size-16 aspect-square grid place-items-center " +
    "bg-[var(--primary-color)] rounded-full text-[var(--white)] font-bold text-2xl";
const stepContent =
    "p-8 gap-5 flex flex-row items-center";
const stepDescription =
    "flex flex-col items-start text-left";
const stepIcon =
    "shrink-0 flex-none size-16 aspect-square grid place-items-center " +
    "rounded-xl text-[var(--primary-color)] bg-[var(--landing-light)]";
const stepTitle =
    "text-xl font-semibold text-[var(--text-color)]";
const stepText =
    "text-[var(--text-light)]";
const startButton =
    "mt-16 p-6.5 text-base font-medium bg-[var(--primary-color)] hover:bg-[var(--primary-dark)] cursor-pointer";

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
            <Button className={startButton}>
                <Link to="/login">
                    Get Started Now
                </Link>
            </Button>
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
    "relative max-w-3xl w-full mx-auto px-12 -mt-5";
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
