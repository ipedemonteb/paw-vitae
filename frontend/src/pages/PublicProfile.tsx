import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar.tsx";
import { Mail, Phone, SquarePen, User } from "lucide-react";
import { Card } from "@/components/ui/card.tsx";
import { RatingStars } from "@/components/RatingStars.tsx";
import BadgeComponent from "@/components/BadgeComponent.tsx";
import { Button } from "@/components/ui/button.tsx";

const profileContainer =
    "flex flex-col mt-36 px-5 mx-auto max-w-6xl w-full gap-6";

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
          <ProfileCard rating={rating} ratingCount={ratingCount} specialties={specialties} maxBadges={maxBadges} />
          <AboutCard />
      </div>
    );
}

const profileCard =
    "flex flex-col gap-0 items-center sm:flex-row";
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

function ProfileCard({ rating, ratingCount, specialties, maxBadges }:{
    rating: number;
    ratingCount: number;
    specialties: string[];
    maxBadges: number;
}) {
    return (
        <Card className={profileCard}>
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
        </Card>
    )
}

const aboutCard =
    "p-0 gap-0";
const cardTitle =
    "flex items-center px-6 py-2 bg-[var(--primary-color)] text-white gap-1 border border-[var(--primary-color)] rounded-t-xl";
const cardContent =
    "px-6 py-4";
const cardTitleText =
    "text-lg font-[500]";

function AboutCard() {
    return (
        <Card className={aboutCard}>
            <div className={cardTitle}>
                <User className="w-6 h-6"></User>
                <h1 className={cardTitleText}>About</h1>
            </div>
            <div className={cardContent}>
                <p>Soy traumatólogo en el Club Estudiantes de La Plata, con más de 40 años en el campo.</p>
            </div>
        </Card>
    )
}

export default PublicProfile;