import { Card } from "@/components/ui/card"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar.tsx";
import {Calendar, Mail, Phone, Star, Stethoscope, UserRoundSearch} from "lucide-react";
import { clampRating, cn } from "@/lib/utils.ts";
import {Button} from "@/components/ui/button.tsx";

const cardContainer =
    "p-0 gap-0";
const iconContainer =
    "flex items-center justify-center px-8 pt-8 mb-4";
const icon =
    "w-16 h-16";
const dataContainer =
    "flex flex-col justify-center px-10 mb-2";
const dataName =
    "font-[600]";
const dataSpecialty =
    "flex flex-row text-sm items-center gap-1 text-[var(--primary-color)] mb-1";
const dataIcon =
    "h-4 w-4";
const dataContact =
    "flex flex-row items-center gap-1 text-[var(--text-light)] text-sm mb-1";
const ratingStars =
    "mt-[10px] flex items-center gap-[6px]";
const scheduleContainer =
    "flex flex-col justify-center items-stretch gap-2 px-8 py-6";
const scheduleButton =
    "w-full bg-[var(--primary-color)] border border-[var(--primary-color)] text-white py-2 px-4 hover:bg-[var(--primary-dark)] hover:border-[var(--primary-dark)] cursor-pointer";
const viewProfileButton =
    "w-full bg-white text-[var(--primary-color)] border border-[var(--primary-color)] py-2 px-4 hover:text-white hover:bg-[var(--primary-dark)] hover:border-[var(--primary-dark)] cursor-pointer";

function SearchGridCard() {
    const rating = 4.5;

    const r = clampRating(rating);

    return (
        <Card className={cardContainer}>
            <div className={iconContainer}>
                <Avatar className={icon}>
                    <AvatarImage src="https://picsum.photos/200" />
                    <AvatarFallback>A</AvatarFallback>
                </Avatar>
            </div>
            <div className={dataContainer}>
                <h3 className={dataName}>John Doe</h3>
                <div className={dataSpecialty}>
                    <Stethoscope className={dataIcon} />
                    <p>Dermatology</p>
                </div>
                <div className={dataContact}>
                    <Mail className={dataIcon} />
                    <p>johndoe@gmail.com</p>
                </div>
                <div className={dataContact}>
                    <Phone className={dataIcon} />
                    <p>11 1234-5678</p>
                </div>
                <div className={ratingStars} aria-label={`${r} star rating`}>
                    {Array.from({ length: 5 }).map((_, i) => {
                        const filled = i < r;
                        return (
                            <Star
                                key={i}
                                className={cn(
                                    "h-4 w-4",
                                    filled
                                        ? "fill-[var(--primary-color)] text-[var(--primary-color)]"
                                        : "text-[var(--gray-300)]"
                                )}
                            />
                        );
                    })}
                </div>
            </div>
            <div className={scheduleContainer}>
                <Button className={scheduleButton}>
                    <Calendar className={dataIcon} />
                    Schedule
                </Button>
                <Button className={viewProfileButton}>
                    <UserRoundSearch className={dataIcon} />
                    View Profile
                </Button>
            </div>
        </Card>
    )
}

export default SearchGridCard;