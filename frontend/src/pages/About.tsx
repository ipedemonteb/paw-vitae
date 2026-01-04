import { Card } from "@/components/ui/card.tsx";
import {
    Lightbulb,
    PersonStandingIcon,
    Award,
    Heart,
    Shield,
    UsersRound,
    Rocket,
    MapPin,
    Mail,
    Phone,
} from "lucide-react";

const heroSection =
    "bg-[linear-gradient(135deg,var(--background-light)_0%,var(--landing-light)_100%)]";
const grayBackground =
    "bg-[var(--background-light)]";

function About() {
    return (
        <div>
            <div className={heroSection}>
                <HeroSection />
            </div>
            <StorySection />
            <div className={grayBackground}>
                <VisionSection />
            </div>
            <ValuesSection />
            <div className={grayBackground}>
                <ContactSection />
            </div>
        </div>
    );
}

const heroContainer =
    "mt-25 relative overflow-visible block pt-16 pb-20 px-5 mx-auto max-w-6xl w-full";
const heroContent =
    "flex-[1.5] max-w-2xl box-border";
const heroTitle =
    "text-5xl font-bold leading-[1.2] mb-5 text-[var(--text-color)]";
const heroSubtitle = "text-lg font-medium text-[var(--text-light)] mb-8";

function HeroSection() {
    return (
        <div className={heroContainer}>
            <div className={heroContent}>
                <h1 className={heroTitle}>Transforming Healthcare Access</h1>
                <p className={heroSubtitle}>
                    Learn about our mission and the story behind Vitae - the unified medical
                    platform created by ITBA students.
                </p>
            </div>
        </div>
    );
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
const storyDescription =
    "flex flex-col gap-6 text-left";
const storyParagraph =
    "text-[var(--text-light)]";

function StorySection() {
    return (
        <div className={container}>
            <div className={sectionHeader}>
                <span className={sectionTag}>Our Story</span>
                <h1 className={sectionTitle}>The Journey of Vitae</h1>
                <p className={sectionText}>
                    How a group of ITBA students created a platform to revolutionize
                    healthcare access.
                </p>
            </div>

            <div className={storyDescription}>
                <p className={storyParagraph}>
                    Vitae was born in 2025 at the Instituto Tecnológico de Buenos Aires
                    (ITBA) when a group of passionate engineering students identified a
                    critical gap in healthcare accessibility in Latin America.
                </p>
                <p className={storyParagraph}>
                    What started as a university project for the Programming of Web
                    Applications (PAW) course quickly evolved into a comprehensive
                    platform with a mission to connect patients with healthcare providers
                    seamlessly and efficiently.
                </p>
                <p className={storyParagraph}>
                    Our team of dedicated professionals works tirelessly to improve the
                    platform and expand our services to reach more communities, driven by
                    our commitment to innovation, accessibility, and quality healthcare
                    for all.
                </p>
            </div>
        </div>
    );
}

const sectionCards =
    "max-w-5xl w-full flex flex-col md:flex-row gap-8 md:gap-12 items-stretch";
const sectionCard =
    "flex-1 px-8 py-10 flex flex-col items-center text-center";
const sectionIcons =
    "w-16 h-16 mb-6 flex items-center justify-center bg-[var(--landing-light)] text-[var(--primary-color)] rounded-full text-3xl";
const sectionPersonIcon =
    "text-[var(--landing-light)] bg-[var(--primary-color)] rounded-full";
const sectionCardTitle =
    "text-xl font-semibold mb-4 text-[var(--text-color)]";
const sectionCardText = "text-[var(--text-light)]";

function VisionSection() {
    return (
        <div className={container}>
            <div className={sectionHeader}>
                <span className={sectionTag}>Our Vision</span>
                <h1 className={sectionTitle}>What Drives Us Forward</h1>
                <p className={sectionText}>
                    Our core mission and the values that guide our work every day.
                </p>
            </div>

            <div className={sectionCards}>
                <Card className={sectionCard}>
                    <div className={sectionIcons}>
                        <PersonStandingIcon className={sectionPersonIcon} />
                    </div>
                    <h3 className={sectionCardTitle}>Accessibility</h3>
                    <p className={sectionCardText}>
                        We believe healthcare should be accessible to everyone, regardless
                        of location or socioeconomic status. Our platform breaks down
                        barriers to quality medical care.
                    </p>
                </Card>

                <Card className={sectionCard}>
                    <div className={sectionIcons}>
                        <Award />
                    </div>
                    <h3 className={sectionCardTitle}>Quality Care</h3>
                    <p className={sectionCardText}>
                        We are committed to connecting patients with verified, high-quality
                        healthcare providers who deliver exceptional care and service.
                    </p>
                </Card>

                <Card className={sectionCard}>
                    <div className={sectionIcons}>
                        <Lightbulb />
                    </div>
                    <h3 className={sectionCardTitle}>Innovation</h3>
                    <p className={sectionCardText}>
                        We constantly evolve our platform with cutting-edge technology to
                        improve the healthcare experience for both patients and providers.
                    </p>
                </Card>
            </div>
        </div>
    );
}

const valuesCards =
    "max-w-6xl w-full flex flex-col md:flex-row gap-8 md:gap-12 items-stretch";
const valuesCard =
    "flex-1 px-8 py-10 flex flex-col items-center text-center border-t-4 border-t-[var(--primary-color)]";
const valuesHeart =
    "fill-[var(--primary-color)]";

function ValuesSection() {
    return (
        <div className={container}>
            <div className={sectionHeader}>
                <span className={sectionTag}>Our Values</span>
                <h1 className={sectionTitle}>The Principles That Guide Us</h1>
                <p className={sectionText}>
                    Core values that shape our decisions and drive our mission forward
                </p>
            </div>

            <div className={valuesCards}>
                <Card className={valuesCard}>
                    <div className={sectionIcons}>
                        <Heart className={valuesHeart} />
                    </div>
                    <h3 className={sectionCardTitle}>Patient-Centered Care</h3>
                    <p className={sectionCardText}>
                        We put patients first in everything we do, ensuring their needs,
                        preferences, and experiences guide our platform development.
                    </p>
                </Card>

                <Card className={valuesCard}>
                    <div className={sectionIcons}>
                        <Shield />
                    </div>
                    <h3 className={sectionCardTitle}>Trust & Security</h3>
                    <p className={sectionCardText}>
                        We maintain the highest standards of data security and privacy,
                        earning the trust of both patients and healthcare providers.
                    </p>
                </Card>

                <Card className={valuesCard}>
                    <div className={sectionIcons}>
                        <UsersRound />
                    </div>
                    <h3 className={sectionCardTitle}>Community Impact</h3>
                    <p className={sectionCardText}>
                        We strive to make a positive difference in communities by improving
                        healthcare access and outcomes.
                    </p>
                </Card>

                <Card className={valuesCard}>
                    <div className={sectionIcons}>
                        <Rocket />
                    </div>
                    <h3 className={sectionCardTitle}>Forward Thinking</h3>
                    <p className={sectionCardText}>
                        We embrace innovation and continuously seek new ways to improve
                        healthcare delivery through technology.
                    </p>
                </Card>
            </div>
        </div>
    );
}

const contactContainer =
    "w-full flex flex-col lg:flex-row gap-14 items-start";
const contactDetails =
    "w-full lg:w-[520px] flex flex-col gap-10";
const contactData =
    "flex items-start gap-6";
const contactIcon =
    "shrink-0 w-16 h-16 flex items-center justify-center bg-[var(--landing-light)] text-[var(--primary-color)] rounded-full";
const contactTitle =
    "text-xl font-semibold text-[var(--text-color)]";
const contactText =
    "text-[var(--text-light)] leading-[1.6]";
const contactMap =
    "w-full lg:flex-1 overflow-hidden rounded-3xl shadow-[var(--shadow-md)]";

function ContactSection() {
    return (
        <div className={container}>
            <div className={sectionHeader}>
                <span className={sectionTag}>Get in Touch</span>
                <h1 className={sectionTitle}>Contact Us</h1>
                <p className={sectionText}>
                    Have questions or feedback? We'd love to hear from you.
                </p>
            </div>
            <div className={contactContainer}>
                <div className={contactDetails}>
                    <div className={contactData}>
                        <div className={contactIcon}>
                            <MapPin />
                        </div>
                        <div className="text-left">
                            <h3 className={contactTitle}>Visit Us</h3>
                            <p className={contactText}>
                                Iguazú 341, C1437 Cdad. Autónoma de Buenos Aires
                            </p>
                        </div>
                    </div>
                    <div className={contactData}>
                        <div className={contactIcon}>
                            <Mail />
                        </div>
                        <div className="text-left">
                            <h3 className={contactTitle}>Email Us</h3>
                            <p className={contactText}>vitaepaw@gmail.com</p>
                        </div>
                    </div>
                    <div className={contactData}>
                        <div className={contactIcon}>
                            <Phone />
                        </div>
                        <div className="text-left">
                            <h3 className={contactTitle}>Call Us</h3>
                            <p className={contactText}>+54 9 11 6892-1516</p>
                        </div>
                    </div>
                </div>
                <div className={contactMap}>
                    <div className="aspect-[16/10] w-full lg:aspect-auto lg:h-[520px]">
                        <iframe
                            src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3282.5899976326584!2d-58.40244492425507!3d-34.64006077294961!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x95bccb0d94c6f2c3%3A0x4d3c471df45e6b7f!2sIguaz%C3%BA%20341%2C%20C1437ELE%20CABA!5e0!3m2!1sen!2sar!4v1682456788954!5m2!1sen!2sar"
                            className="h-full w-full border-0"
                            allowFullScreen
                            loading="lazy"
                            referrerPolicy="no-referrer-when-downgrade"
                        />
                    </div>
                </div>
            </div>
        </div>
    );
}

export default About;
