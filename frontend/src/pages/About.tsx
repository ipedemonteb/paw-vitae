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
import {useTranslation} from "react-i18next";

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
    const { t } = useTranslation();

    return (
        <div className={heroContainer}>
            <div className={heroContent}>
                <h1 className={heroTitle}>{t("about.hero.title")}</h1>
                <p className={heroSubtitle}>
                    {t("about.hero.subtitle")}
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
    const { t } = useTranslation();

    return (
        <div className={container}>
            <div className={sectionHeader}>
                <span className={sectionTag}>{t("about.story.tag")}</span>
                <h1 className={sectionTitle}>{t("about.story.title")}</h1>
                <p className={sectionText}>{t("about.story.subtitle")}</p>
            </div>

            <div className={storyDescription}>
                <p className={storyParagraph}>{t("about.story.text.paragraph_1")}</p>
                <p className={storyParagraph}>{t("about.story.text.paragraph_2")}</p>
                <p className={storyParagraph}>{t("about.story.text.paragraph_3")}</p>
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
    const { t } = useTranslation();

    return (
        <div className={container}>
            <div className={sectionHeader}>
                <span className={sectionTag}>{t("about.vision.tag")}</span>
                <h1 className={sectionTitle}>{t("about.vision.title")}</h1>
                <p className={sectionText}>{t("about.vision.subtitle")}</p>
            </div>

            <div className={sectionCards}>
                <Card className={sectionCard}>
                    <div className={sectionIcons}>
                        <PersonStandingIcon className={sectionPersonIcon} />
                    </div>
                    <h3 className={sectionCardTitle}>{t("about.vision.accessibility_card.title")}</h3>
                    <p className={sectionCardText}>{t("about.vision.accessibility_card.text")}</p>
                </Card>

                <Card className={sectionCard}>
                    <div className={sectionIcons}>
                        <Award />
                    </div>
                    <h3 className={sectionCardTitle}>{t("about.vision.quality_card.title")}</h3>
                    <p className={sectionCardText}>{t("about.vision.quality_card.text")}</p>
                </Card>

                <Card className={sectionCard}>
                    <div className={sectionIcons}>
                        <Lightbulb />
                    </div>
                    <h3 className={sectionCardTitle}>{t("about.vision.innovation_card.title")}</h3>
                    <p className={sectionCardText}>{t("about.vision.innovation_card.text")}</p>
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
    const { t } = useTranslation();

    return (
        <div className={container}>
            <div className={sectionHeader}>
                <span className={sectionTag}>{t("about.values.tag")}</span>
                <h1 className={sectionTitle}>{t("about.values.title")}</h1>
                <p className={sectionText}>{t("about.values.subtitle")}</p>
            </div>
            <div className={valuesCards}>
                <Card className={valuesCard}>
                    <div className={sectionIcons}>
                        <Heart className={valuesHeart} />
                    </div>
                    <h3 className={sectionCardTitle}>{t("about.values.patient_card.title")}</h3>
                    <p className={sectionCardText}>{t("about.values.patient_card.text")}</p>
                </Card>
                <Card className={valuesCard}>
                    <div className={sectionIcons}>
                        <Shield />
                    </div>
                    <h3 className={sectionCardTitle}>{t("about.values.trust_card.title")}</h3>
                    <p className={sectionCardText}>{t("about.values.trust_card.text")}</p>
                </Card>
                <Card className={valuesCard}>
                    <div className={sectionIcons}>
                        <UsersRound />
                    </div>
                    <h3 className={sectionCardTitle}>{t("about.values.community_card.title")}</h3>
                    <p className={sectionCardText}>{t("about.values.community_card.text")}</p>
                </Card>
                <Card className={valuesCard}>
                    <div className={sectionIcons}>
                        <Rocket />
                    </div>
                    <h3 className={sectionCardTitle}>{t("about.values.forward_card.title")}</h3>
                    <p className={sectionCardText}>{t("about.values.forward_card.text")}</p>
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
    const { t } = useTranslation();

    return (
        <div className={container}>
            <div className={sectionHeader}>
                <span className={sectionTag}>{t("about.contact.tag")}</span>
                <h1 className={sectionTitle}>{t("about.contact.title")}</h1>
                <p className={sectionText}>{t("about.contact.subtitle")}</p>
            </div>
            <div className={contactContainer}>
                <div className={contactDetails}>
                    <div className={contactData}>
                        <div className={contactIcon}>
                            <MapPin />
                        </div>
                        <div className="text-left">
                            <h3 className={contactTitle}>{t("about.contact.visit_component.title")}</h3>
                            <p className={contactText}>{t("about.contact.visit_component.info")}</p>
                        </div>
                    </div>
                    <div className={contactData}>
                        <div className={contactIcon}>
                            <Mail />
                        </div>
                        <div className="text-left">
                            <h3 className={contactTitle}>{t("about.contact.email_component.title")}</h3>
                            <p className={contactText}>{t("about.contact.email_component.info")}</p>
                        </div>
                    </div>
                    <div className={contactData}>
                        <div className={contactIcon}>
                            <Phone />
                        </div>
                        <div className="text-left">
                            <h3 className={contactTitle}>{t("about.contact.call_component.title")}</h3>
                            <p className={contactText}>{t("about.contact.call_component.info")}</p>
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
