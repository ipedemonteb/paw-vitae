import { Card } from "@/components/ui/card.tsx";
import { Lightbulb, PersonStandingIcon, Award, Heart, Shield, UsersRound, Rocket, MapPin } from "lucide-react";

const aboutPage =
    "pt-[100px]";
const heroSection=
    "bg-[linear-gradient(135deg,var(--background-light)_0%,var(--landing-light)_100%)]";
const grayBackground =
    "bg-[var(--background-light)]";

function About() {
    return (
      <div className={aboutPage}>
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

function HeroSection() {
    return (
        <div className={heroContainer}>
            <div className={heroContent}>
                <h1 className={heroTitle}>
                    Transforming Healthcare Access
                </h1>
                <p className={heroSubtitle}>
                    Learn about our mission and the story behind Vitae - the unified medical platform created by ITBA students.
                </p>
            </div>
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
const storyDescription =
    "flex flex-col gap-[24px] text-left";
const storyParagraph =
    "text-[var(--text-light)]";

function StorySection() {
    return (
        <div className={container}>
            <div className={sectionHeader}>
                <span className={sectionTag}>
                    Our Story
                </span>
                <h1 className={sectionTitle}>
                    The Journey of Vitae
                </h1>
                <p className={sectionText}>
                    How a group of ITBA students created a platform to revolutionize healthcare access.
                </p>
            </div>
            <div className={storyDescription}>
                <p className={storyParagraph}>
                    Vitae was born in 2025 at the Instituto Tecnológico de Buenos Aires (ITBA) when a group of passionate engineering students identified a critical gap in healthcare accessibility in Latin America.
                </p>
                <p className={storyParagraph}>
                    What started as a university project for the Programming of Web Applications (PAW) course quickly evolved into a comprehensive platform with a mission to connect patients with healthcare providers seamlessly and efficiently.
                </p>
                <p className={storyParagraph}>
                    Our team of dedicated professionals works tirelessly to improve the platform and expand our services to reach more communities, driven by our commitment to innovation, accessibility, and quality healthcare for all.
                </p>
            </div>
        </div>
    )
}

const sectionCards =
    "max-w-[1000px] w-full flex flex-col md:flex-row gap-[30px] md:gap-[50px] items-stretch";
const sectionCard =
    "flex-1 px-[30px] py-[40px] gap-[0] flex flex-col items-center text-center";
const sectionIcons =
    "w-[70px] h-[70px] mb-[24px] flex items-center justify-center bg-[var(--landing-light)] text-[var(--primary-color)] rounded-[50%] text-[28px]";
const sectionPersonIcon =
    "text-[var(--landing-light)] bg-[var(--primary-color)] rounded-[50%]";
const sectionCardTitle =
    "text-[22px] font-[600] mb-[16px] text-[var(--text-color)]";
const sectionCardText =
    "text-[var(--text-light)]";

function VisionSection() {
    return (
        <div className={container}>
            <div className={sectionHeader}>
                <span className={sectionTag}>
                    Our Vision
                </span>
                <h1 className={sectionTitle}>
                    What Drives Us Forward
                </h1>
                <p className={sectionText}>
                    Our core mission and the values that guide our work every day.
                </p>
            </div>
            <div className={sectionCards}>
                <Card className={sectionCard}>
                    <div className={sectionIcons}>
                        <PersonStandingIcon className={sectionPersonIcon}/>
                    </div>
                    <h3 className={sectionCardTitle}>Accessibility</h3>
                    <p className={sectionCardText}>
                        We believe healthcare should be accessible to everyone, regardless of location or socioeconomic status. Our platform breaks down barriers to quality medical care.
                    </p>
                </Card>
                <Card className={sectionCard}>
                    <div className={sectionIcons}>
                        <Award />
                    </div>
                    <h3 className={sectionCardTitle}>Quality Care</h3>
                    <p className={sectionCardText}>
                        We are committed to connecting patients with verified, high-quality healthcare providers who deliver exceptional care and service.
                    </p>
                </Card>
                <Card className={sectionCard}>
                    <div className={sectionIcons}>
                        <Lightbulb />
                    </div>
                    <h3 className={sectionCardTitle}>Innovation</h3>
                    <p className={sectionCardText}>
                        We constantly evolve our platform with cutting-edge technology to improve the healthcare experience for both patients and providers.
                    </p>
                </Card>
            </div>
        </div>
    )
}

const valuesCards =
    "max-w-[1200px] w-full flex flex-col md:flex-row gap-[30px] md:gap-[50px] items-stretch";
const valuesCard =
    "flex-1 px-[30px] py-[40px] gap-[0] flex flex-col items-center text-center border-t-4 border-t-[var(--primary-color)]";
const valuesHeart =
    "fill-[var(--primary-color)]";

function ValuesSection() {
    return (
        <div className={container}>
            <div className={sectionHeader}>
                <span className={sectionTag}>
                    Our Values
                </span>
                <h1 className={sectionTitle}>
                    The Principles That Guide Us
                </h1>
                <p className={sectionText}>
                    Core values that shape our decisions and drive our mission forward
                </p>
            </div>
            <div className={valuesCards}>
                <Card className={valuesCard}>
                    <div className={sectionIcons}>
                        <Heart className={valuesHeart}/>
                    </div>
                    <h3 className={sectionCardTitle}>Patient-Centered Care</h3>
                    <p className={sectionCardText}>
                        We put patients first in everything we do, ensuring their needs, preferences, and experiences guide our platform development.
                    </p>
                </Card>
                <Card className={valuesCard}>
                    <div className={sectionIcons}>
                        <Shield />
                    </div>
                    <h3 className={sectionCardTitle}>Trust & Security</h3>
                    <p className={sectionCardText}>
                        We maintain the highest standards of data security and privacy, earning the trust of both patients and healthcare providers.
                    </p>
                </Card>
                <Card className={valuesCard}>
                    <div className={sectionIcons}>
                        <UsersRound />
                    </div>
                    <h3 className={sectionCardTitle}>Community Impact</h3>
                    <p className={sectionCardText}>
                        We strive to make a positive difference in communities by improving healthcare access and outcomes.
                    </p>
                </Card>
                <Card className={valuesCard}>
                    <div className={sectionIcons}>
                        <Rocket />
                    </div>
                    <h3 className={sectionCardTitle}>Forward Thinking</h3>
                    <p className={sectionCardText}>
                        We embrace innovation and continuously seek new ways to improve healthcare delivery through technology.
                    </p>
                </Card>
            </div>
        </div>
    )
}

const contactContainer =
    "w-full flex flex-col lg:flex-row gap-[60px] items-start";
const contactDetails =
    "w-full lg:w-[520px] flex flex-col gap-[40px]";
const contactData =
    "flex items-start gap-[24px]";
const contactIcon =
    "shrink-0 w-[60px] h-[60px] flex items-center justify-center bg-[var(--landing-light)] text-[var(--primary-color)] rounded-full"; // SIEMPRE círculo
const contactTitle =
    "text-[20px] font-[600] text-[var(--text-color)]";
const contactText =
    "text-[var(--text-light)] leading-[1.6]";
const contactMap =
    "w-full lg:flex-1 overflow-hidden rounded-[24px] shadow-[var(--shadow-md)]";

function ContactSection() {
    return (
        <div className={container}>
            <div className={sectionHeader}>
                <span className={sectionTag}>
                        Get in Touch
                    </span>
                <h1 className={sectionTitle}>
                    Contact Us
                </h1>
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
                            <p className={contactText}>Iguazú 341, C1437 Cdad. Autónoma de Buenos Aires</p>
                        </div>
                    </div>
                    <div className={contactData}>
                        <div className={contactIcon}>
                            <MapPin />
                        </div>
                        <div className="text-left">
                            <h3 className={contactTitle}>Email Us</h3>
                            <p className={contactText}>vitaepaw@gmail.com</p>
                        </div>
                    </div>
                    <div className={contactData}>
                        <div className={contactIcon}>
                            <MapPin />
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
    )
}

export default About;