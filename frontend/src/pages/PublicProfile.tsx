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
    MapPin
} from "lucide-react";
import { Card } from "@/components/ui/card.tsx";
import { RatingStars } from "@/components/RatingStars.tsx";
import BadgeComponent from "@/components/BadgeComponent.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Carousel, CarouselContent, CarouselItem, CarouselNext, CarouselPrevious } from "@/components/ui/carousel.tsx";
import { RatingCard } from "@/components/Rating.tsx";
import { ScrollArea } from "@/components/ui/scroll-area.tsx";

const profileContainer =
    "flex flex-col mt-36 px-5 mx-auto max-w-6xl w-full gap-6 mb-6";

function PublicProfile() {
    const rating = 3.5;
    const ratingCount = 123;
    const specialties = [
        "General",
        "Cardiology",
        "Dermatology",
        "Pediatrics",
        "Neurology",
        "Oncology",
        "Urology",
        "Ophthalmology",
    ];
    const maxBadges = 4;

    return (
      <div className={profileContainer}>
          <ProfileCard rating={rating} ratingCount={ratingCount} specialties={specialties} maxBadges={maxBadges}/>
          <CoverageCard />
          <OfficesCard />
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
    "mt-4 sm:mt-0 sm:mx-6 bg-[var(--primary-color)] hover:bg-[var(--primary-dark)] sm:ml-auto cursor-pointer";
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

function ProfileCard({ rating, ratingCount, specialties, maxBadges }:{
    rating: number;
    ratingCount: number;
    specialties: string[];
    maxBadges: number;
}) {
    return (
        <Card className={card}>
            <div className={cardTitle}>
                <Stethoscope className={titleIcon}></Stethoscope>
                <h1 className={cardTitleText}>Doctor</h1>
            </div>
            <div className={profileContent}>
                <Avatar className={avatarContainer}>
                    <AvatarImage src="https://picsum.photos/200" />
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
                        <p className={ratingText}>({ratingCount} reviews)</p>
                    </div>
                    <BadgeComponent specialties={specialties} maxBadges={maxBadges} />
                </div>
                <Button className={editButton}>
                    <SquarePen className="w-4 h-4" />
                    Edit
                </Button>
            </div>
            <div className={aboutContent}>
                <h1 className={aboutTitle}>About</h1>
                <p className={aboutText}>Soy traumatólogo en el Club Estudiantes de La Plata, con más de 40 años en el campo.</p>
            </div>
        </Card>
    );
}

const cardContent =
    "flex flex-col items-center gap-3 px-6 py-6 sm:flex-row sm:flex-wrap sm:justify-center";

function CoverageCard() {
    return (
        <Card className={card}>
            <div className={cardTitle}>
                <ShieldPlus className={titleIcon}></ShieldPlus>
                <h1 className={cardTitleText}>Coverage</h1>
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
    return (
        <Card className={card}>
            <div className={cardTitle}>
                <Hospital className={titleIcon}></Hospital>
                <h1 className={cardTitleText}>Offices</h1>
            </div>
            <div className={cardContent}>
                <OfficeComponent />
                <OfficeComponent />
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

function OfficeComponent() {
    return (
        <div className={officeContainer}>
            <div className={iconContainer}>
                <Hospital className={locationIcon}/>
            </div>
            <h3 className={locationTitle}>Main Office</h3>
            <div className={locationContainer}>
                <MapPin className={mapPinIcon}/>
                <p>Almagro</p>
            </div>
        </div>
    )
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
    return (
        <Card className={card}>
            <div className={cardTitle}>
                <FileBadge className={titleIcon}></FileBadge>
                <h1 className={cardTitleText}>Certificates</h1>
            </div>
            <div className={certificatesScrollWrap}>
                <ScrollArea className={certificatesScrollArea}>
                    <div className={certificatesContent}>
                        <CertificateComponent />
                        <CertificateComponent />
                        <CertificateComponent />
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

function CertificateComponent() {
    return (
        <div className={certificateContainer}>
            <div className={badgeContainer}>
                <BadgeCheck className={badgeIcon}/>
            </div>
            <div className={certificateData}>
                <h3 className={certificateTitle}>Congreso Nacional de Traumatólogos</h3>
                <p className={certificateIssuer}>Asociación Nacional de Médicos Traumatólogos</p>
                <div className={certificateDate}>
                    <Calendar className={calendarIcon}/>
                    <p className={dateText}>12/04/2024</p>
                </div>
            </div>
        </div>
    )
}

const ratingsContent =
    "w-full max-w-xl mx-auto pt-6";
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

    return (
        <Card className={card}>
            <div className={cardTitle}>
                <UserStar className={titleIcon}></UserStar>
                <h1 className={cardTitleText}>Ratings</h1>
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