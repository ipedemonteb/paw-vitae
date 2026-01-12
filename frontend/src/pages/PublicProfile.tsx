import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar.tsx";
import {
    Mail,
    Phone,
    SquarePen,
    ShieldPlus,
    UserStar,
    Stethoscope,
    FileBadge,
    BadgeCheck,
    Calendar,
    Hospital,
    MapPin,
    BriefcaseBusiness
} from "lucide-react";
import { Card } from "@/components/ui/card.tsx";
import { RatingStars } from "@/components/RatingStars.tsx";
import BadgeComponent from "@/components/BadgeComponent.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Carousel, CarouselContent, CarouselItem, CarouselNext, CarouselPrevious } from "@/components/ui/carousel.tsx";
import { RatingCard } from "@/components/Rating.tsx";
import { ScrollArea } from "@/components/ui/scroll-area.tsx";
import { useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";
import {useDoctorImageUrl} from "@/hooks/useDoctors.ts";
import {
    useDoctor,
    useDoctorBiography, useDoctorCertifications,
    useDoctorCoverages,
    useDoctorExperience,
    useDoctorSpecialties
} from "@/hooks/useDoctors.ts";
import type {DoctorDTO} from "@/data/doctors.ts";
import GenericError from "@/pages/GenericError.tsx";
import {useRatings} from "@/hooks/useRatings.ts";

const profileContainer =
    "flex flex-col mt-36 px-5 mx-auto max-w-6xl w-full gap-6 mb-6";

function PublicProfile() {
    const { id } = useParams<{ id: string }>();
    const { data: doctor, isLoading: isLoadingDoctor, isError } = useDoctor(id);
    const { data: specialties, isLoading: isLoadingSpecialties } = useDoctorSpecialties(doctor?.specialties);
    const {data:coverages, isLoading: isLoadingCoverages} = useDoctorCoverages(doctor?.coverages);
    const {data:experiences, isLoading: isLoadingExperiences} = useDoctorExperience(doctor?.experiences);
    const {data:certifications, isLoading: isLoadingCertifications} = useDoctorCertifications(doctor?.certifications);
    const {data:profile, isLoading: isLoadingProfile} = useDoctorBiography(doctor?.profile);
    const {data:ratings, isLoading: isLoadingRatings} = useRatings(doctor?.ratings);
    if (isLoadingDoctor || isLoadingCertifications|| isLoadingCoverages || isLoadingExperiences  || isLoadingProfile || isLoadingRatings || isLoadingSpecialties) return <div>Loading...</div>;
    if (isError || !doctor) return <GenericError code={404} />;
    const rating = doctor.rating ;
    const ratingCount =doctor.ratingCount;
    const maxBadges = 4;

    return (
      <div className={profileContainer}>
          <ProfileCard doctorId={id} rating={rating} ratingCount={ratingCount} specialties={specialties} maxBadges={maxBadges}/>
          <CoverageCard />
          <OfficesCard />
          <ExperienceCard />
          <CertificatesCard />
          <RatingsCard />
      </div>
    );
}

const titleIcon =
    "w-5 h-5";
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
const ratingContent =
    "flex flex-row items-center gap-2 font-bold";
const ratingText =
    "font-medium text-sm text-[var(--text-light)]";
const editButton =
    "mt-4 w-26 h-10 sm:mt-0 sm:mx-6 bg-[var(--primary-color)] hover:bg-[var(--primary-dark)] sm:ml-auto cursor-pointer";
const card =
    "p-0 gap-0";
const cardTitle =
    "flex items-center px-6 py-2 bg-[var(--primary-color)] text-white gap-1 border border-[var(--primary-color)] rounded-t-xl";
const aboutContent =
    "px-7 pt-4 pb-6";
const cardTitleText =
    "text-lg font-[500]";
const profileContent =
    "flex flex-col gap-0 items-center sm:flex-row pt-6";
const aboutTitle =
    "text-lg font-[500]";
const aboutText =
    "text-[var(--text-light)] text-md";

function ProfileCard({ doctorId, rating, ratingCount, specialties, maxBadges }:{
    doctorId: string | undefined;
    rating: number;
    ratingCount: number;
    specialties: string[];
    maxBadges: number;
}) {
    const { t } = useTranslation();
    const { url: getDoctorImgUrl } = useDoctorImageUrl(doctorId);

    return (
        <Card className={card}>
            <div className={cardTitle}>
                <Stethoscope className={titleIcon}></Stethoscope>
                <h1 className={cardTitleText}>{t("doctor.profile.doctor")}</h1>
            </div>
            <div className={profileContent}>
                <Avatar className={avatarContainer}>
                    <AvatarImage src={getDoctorImgUrl || undefined} />
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
                    <div className={ratingContent}>
                        <RatingStars rating={rating} sizeClassName="h-4 w-4" />
                        <p>{rating}</p>
                        <p className={ratingText}>({ratingCount} {t("doctor.profile.card.rating")})</p>
                    </div>
                    <BadgeComponent specialties={specialties} maxBadges={maxBadges} />
                </div>
                <Button className={editButton}>
                    <SquarePen className="w-4 h-4" />
                    {t("doctor.profile.card.edit")}
                </Button>
            </div>
            <div className={aboutContent}>
                <h1 className={aboutTitle}>{t("doctor.profile.card.about")}</h1>
                <p className={aboutText}>Soy traumatólogo en el Club Estudiantes de La Plata, con más de 40 años en el campo.</p>
            </div>
        </Card>
    );
}

const cardContent =
    "flex flex-col items-center gap-3 px-6 py-6 sm:flex-row sm:flex-wrap sm:justify-center";

function CoverageCard() {
    const { t } = useTranslation();

    return (
        <Card className={card}>
            <div className={cardTitle}>
                <ShieldPlus className={titleIcon}></ShieldPlus>
                <h1 className={cardTitleText}>{t("doctor.profile.coverages")}</h1>
            </div>
            <div className={cardContent}>
                <CoverageComponent coverageImage="https://upload.wikimedia.org/wikipedia/commons/1/18/Logo_OSDE_2020.png" coverageName="OSDE" coveragePlan="310" />
                <CoverageComponent coverageImage="https://images.seeklogo.com/logo-png/33/2/galeno-logo-png_seeklogo-333694.png" coverageName="Galeno" coveragePlan="Premium" />
                <CoverageComponent coverageImage="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQB6p669ljP14WkZ1gAoq3dD6ppJUlIwkp59w&s" coverageName="Swiss Medical" coveragePlan="Plus" />
                <CoverageComponent coverageImage="https://images.seeklogo.com/logo-png/62/1/sancor-salud-medicina-privada-logo-png_seeklogo-621371.png" coverageName="Sancor Salud" coveragePlan="Basic" />
            </div>
        </Card>
    )
}

const componentContainer =
    "flex w-full max-w-3xs flex-col px-10 py-4 items-center border border-[var(--gray-300)] rounded-lg";
const coverageAvatar =
    "w-16 h-16 mb-2";
const coveragePlanText =
    "text-sm text-[var(--text-light)]";

function CoverageComponent({coverageImage, coverageName, coveragePlan}:{
    coverageImage: string;
    coverageName: string;
    coveragePlan: string;
}) {
    return (
        <div className={componentContainer}>
            <Avatar className={coverageAvatar}>
                <AvatarImage src={coverageImage} />
                <AvatarFallback>{coverageName}</AvatarFallback>
            </Avatar>
            <h3>{coverageName}</h3>
            <p className={coveragePlanText}>Plan: {coveragePlan}</p>
        </div>
    );
}

function OfficesCard() {
    const { t } = useTranslation();

    return (
        <Card className={card}>
            <div className={cardTitle}>
                <Hospital className={titleIcon}></Hospital>
                <h1 className={cardTitleText}>{t("doctor.profile.offices")}</h1>
            </div>
            <div className={cardContent}>
                <OfficeComponent officeTitle="Main Office" officeLocation="La Plata" />
                <OfficeComponent officeTitle="Hospital" officeLocation="Munro"/>
            </div>
        </Card>
    );
}

const officeContainer =
    "flex w-full max-w-[200px] flex-col px-10 py-4 items-center border border-[var(--gray-300)] rounded-lg";
const iconContainer =
    "bg-[var(--primary-color)] rounded-full p-2 flex items-center justify-center gap-1 text-white mb-2";
const locationIcon =
    "h-6 w-6 text-white";
const locationContainer =
    "flex items-center gap-1 text-[var(--text-light)] text-sm";
const locationTitle =
    "text-base font-[500]";
const mapPinIcon =
    "h-4 w-4";

function OfficeComponent({officeTitle, officeLocation}:{
    officeLocation: string;
    officeTitle: string;
}) {
    return (
        <div className={officeContainer}>
            <div className={iconContainer}>
                <Hospital className={locationIcon}/>
            </div>
            <h3 className={locationTitle}>{officeTitle}</h3>
            <div className={locationContainer}>
                <MapPin className={mapPinIcon}/>
                <p>{officeLocation}</p>
            </div>
        </div>
    )
}

const experienceContent =
    "pr-6 py-6";
const experienceEmpty =
    "text-sm text-[var(--text-light)]";
const timelineContainer =
    "relative pl-10 " +
    "before:content-[''] before:absolute before:top-3 before:bottom-3 " +
    "before:left-14 before:w-[2px] before:-translate-x-1/2 before:bg-[var(--gray-300)]";

function ExperienceCard() {

    const experiences = [
        {
            position: "Head of Traumatology",
            organization: "Club Estudiantes de La Plata",
            period: "1984 – Present",
            description:
                "Lidero el equipo de traumatología, coordinando cirugías y seguimiento post-operatorio. Enfocado en lesiones deportivas y rehabilitación integral.",
        },
        {
            position: "Traumatologist",
            organization: "Hospital Central",
            period: "1979 – 1984",
            description:
                "Atención clínica y guardias. Participación en protocolos de urgencias y formación de residentes.",
        },
        {
            position: "Resident Physician",
            organization: "Residencia Médica – Ortopedia",
            period: "1975 – 1979",
            description:
                "Formación intensiva en ortopedia y traumatología, rotaciones quirúrgicas y práctica supervisada.",
        },
    ];

    const { t } = useTranslation();

    return (
        <Card className={card}>
            <div className={cardTitle}>
                <BriefcaseBusiness className={titleIcon}></BriefcaseBusiness>
                <h1 className={cardTitleText}>{t("doctor.profile.experiences")}</h1>
            </div>
            <div className={experienceContent}>
                {experiences.length === 0 ? (
                    <p className={experienceEmpty}>
                        No experience added yet.
                    </p>
                ) : (
                    <div className={timelineContainer}>
                        {experiences.map((e, idx) => (
                            <ExperienceItem
                                key={`${e.position}-${idx}`}
                                position={e.position}
                                organization={e.organization}
                                period={e.period}
                                description={e.description}
                                isLast={idx === experiences.length - 1}
                            />
                        ))}
                    </div>
                )}
            </div>
        </Card>
    );
}

const careerItem =
    "relative pb-4";
const careerDot =
    "absolute left-4 top-[6px] -translate-x-1/2 " +
    "w-6 h-6 rounded-full bg-white border-2 border-[var(--gray-300)] " +
    "grid place-items-center " +
    "after:content-[''] after:block after:w-2.5 after:h-2.5 after:rounded-full after:bg-[var(--primary-color)]";
const careerPosition =
    "text-base font-[600] text-[var(--text-color)] leading-tight";
const careerOrganization =
    "text-sm font-[500] text-[var(--primary-color)]";
const careerPeriod =
    "flex items-center gap-1 text-sm text-[var(--text-light)]";
const careerDescription =
    "text-sm text-[var(--text-color)] leading-6";

function ExperienceItem({
                            position,
                            organization,
                            period,
                            description,
                            isLast,
                        }: {
    position: string;
    organization: string;
    period: string;
    description: string;
    isLast: boolean;
}) {
    return (
        <div className={`${careerItem} ${isLast ? "pb-0" : ""}`}>
            <span className={careerDot} />
            <div className="pl-10 flex flex-col gap-1">
                <h3 className={careerPosition}>{position}</h3>
                <p className={careerOrganization}>{organization}</p>
                <div className={careerPeriod}>
                    <Calendar className="w-4 h-4" />
                    <span>{period}</span>
                </div>
                <p className={careerDescription}>{description}</p>
            </div>
        </div>
    );
}


const certificatesContent =
    "flex flex-col gap-2 px-6";
const certificatesScrollWrap =
    "relative";
const certificatesScrollArea =
    "h-68 py-6";
const certificatesFadeBottom =
    "pointer-events-none absolute bottom-0 left-0 right-0 h-10 z-10 bg-linear-to-b from-transparent to-white";

function CertificatesCard() {
    const { t } = useTranslation();

    return (
        <Card className={card}>
            <div className={cardTitle}>
                <FileBadge className={titleIcon}></FileBadge>
                <h1 className={cardTitleText}>{t("doctor.profile.certificates")}</h1>
            </div>
            <div className={certificatesScrollWrap}>
                <ScrollArea className={certificatesScrollArea}>
                    <div className={certificatesContent}>
                        <CertificateComponent certificate="Congreso Nacional de Traumatólogos" issuer="Asociación Nacional de Médicos Traumatólogos" date="12/04/2024"/>
                        <CertificateComponent certificate="Congreso Nacional de Traumatólogos" issuer="Asociación Nacional de Médicos Traumatólogos" date="12/04/2024"/>
                        <CertificateComponent certificate="Congreso Nacional de Traumatólogos" issuer="Asociación Nacional de Médicos Traumatólogos" date="12/04/2024"/>
                    </div>
                </ScrollArea>
                <div className={certificatesFadeBottom} />
            </div>
        </Card>
    );
}

const certificateContainer =
    "flex flex-row items-center border border-[var(--gray-300)] rounded-lg py-3";
const badgeContainer =
    "px-4";
const certificateData =
    "flex flex-col gap-0";
const badgeIcon =
    "h-10 w-10 bg-[var(--primary-color)] text-white rounded-full p-2";
const certificateTitle =
    "text-base font-[500]";
const certificateIssuer =
    "text-sm text-[var(--text-light)]";
const certificateDate =
    "flex flex-row items-center gap-1 text-[var(--text-light)]";
const calendarIcon =
    "w-4 h-4";
const dateText =
    "text-sm mt-1";

function CertificateComponent({certificate, issuer, date}:{
    certificate: string;
    issuer: string;
    date: string;
}) {
    return (
        <div className={certificateContainer}>
            <div className={badgeContainer}>
                <BadgeCheck className={badgeIcon}/>
            </div>
            <div className={certificateData}>
                <h3 className={certificateTitle}>{certificate}</h3>
                <p className={certificateIssuer}>{issuer}</p>
                <div className={certificateDate}>
                    <Calendar className={calendarIcon}/>
                    <p className={dateText}>{date}</p>
                </div>
            </div>
        </div>
    )
}

const ratingsContent =
    "w-full max-w-3xl mx-auto pt-6";
const carousel =
    "relative max-w-3xl w-full mx-auto px-12 -mt-5";
const carouselContent =
    "-ml-4 py-2";
const carouselItem =
    "pl-4 basis-full";

function RatingsCard() {

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

    const { t } = useTranslation();

    return (
        <Card className={card}>
            <div className={cardTitle}>
                <UserStar className={titleIcon}></UserStar>
                <h1 className={cardTitleText}>{t("doctor.profile.ratings")}</h1>
            </div>
            <div className={ratingsContent}>
                <Carousel opts={{ align: "start", loop: true }} className={carousel}>
                    <CarouselContent className={carouselContent}>
                        {ratings.map((r, idx) => (
                            <CarouselItem key={idx} className={carouselItem}>
                                <div className="py-2">
                                    <RatingCard
                                        className="max-w-xl"
                                        comment={r.comment}
                                        rating={r.rating}
                                        userName={r.userName}
                                        timeAgo={r.timeAgo}
                                    />
                                </div>
                            </CarouselItem>
                        ))}
                    </CarouselContent>
                    <CarouselPrevious className="left-2 cursor-pointer" />
                    <CarouselNext className="right-2 cursor-pointer" />
                </Carousel>
            </div>
        </Card>
    );
}

export default PublicProfile;