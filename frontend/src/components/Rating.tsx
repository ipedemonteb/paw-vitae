import * as React from "react";
import { Card } from "@/components/ui/card.tsx";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar.tsx";
import { Quote } from "lucide-react";
import { cn } from "@/lib/utils.ts";
import {RatingStars} from "@/components/RatingStars.tsx";

export type RatingCardProps = React.ComponentPropsWithoutRef<"div"> & {
    comment: string;
    rating: number;
    userName: string;
    userInitials?: string;
    userAvatarSrc?: string;
    timeAgo?: string;
    quoteIcon?: React.ReactNode;
};

function initialsFromName(name: string) {
    const parts = name.trim().split(/\s+/).filter(Boolean);
    const first = parts[0]?.[0] ?? "";
    const second = parts.length > 1 ? parts[parts.length - 1]?.[0] ?? "" : "";
    const res = (first + second).toUpperCase();
    return res || "U";
}

export function RatingCard({
                               className,
                               comment,
                               rating,
                               userName,
                               userInitials,
                               userAvatarSrc,
                               timeAgo = "",
                               quoteIcon,
                               ...props
                           }: RatingCardProps) {
    const initials = userInitials ?? initialsFromName(userName);

    const ratingsContainer = "w-full max-w-[800px] mx-auto";
    const ratingCard =
        "p-[30px] flex flex-col gap-[18px] text-left border-0 rounded-2xl shadow-[var(--shadow-md)]";
    const ratingUpperContent = "flex gap-[16px] items-start";
    const ratingIconBox = "shrink-0 w-[48px] h-[48px] flex items-top justify-center text-[var(--primary-color)]";
    const ratingQuote = "flex-1";
    const ratingText = "text-[16px] text-[var(--text-color)] leading-[1.6]";
    const ratingStars = "mt-[10px]";
    const ratingBottomContent =
        "pt-[14px] border-t border-[var(--gray-200)] flex items-center justify-between";
    const ratingAuthor = "font-[600] text-[var(--text-color)]";
    const ratingMeta = "text-[14px] text-[var(--text-light)]";
    const ratingAuthorRow = "flex items-center gap-[10px]";
    const ratingAvatar = "h-8 w-8";

    return (
        <div className={cn(ratingsContainer, className)} {...props}>
            <Card className={ratingCard}>
                <div className={ratingUpperContent}>
                    <div className={ratingIconBox}>
                        {quoteIcon ?? <Quote className="h-5 w-5" />}
                    </div>

                    <div className={ratingQuote}>
                        <p className={ratingText}>{comment}</p>
                        <RatingStars rating={rating} className={ratingStars}/>
                    </div>
                </div>

                <div className={ratingBottomContent}>
                    <div className={ratingAuthorRow}>
                        <Avatar className={ratingAvatar}>
                            <AvatarImage src={userAvatarSrc ?? ""} alt={userName} />
                            <AvatarFallback className="bg-[var(--landing-light)] text-[var(--primary-color)] font-[600]">
                                {initials}
                            </AvatarFallback>
                        </Avatar>

                        <span className={ratingAuthor}>{userName}</span>
                    </div>

                    {timeAgo ? <span className={ratingMeta}>{timeAgo}</span> : <span />}
                </div>
            </Card>
        </div>
    );
}
