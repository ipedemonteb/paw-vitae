import * as React from "react";
import {Star, StarHalf} from "lucide-react";
import { cn } from "@/lib/utils";
import { clampRating } from "@/lib/utils";

export type RatingStarsProps = React.ComponentPropsWithoutRef<"div"> & {
    rating: number;
    max?: number;
    sizeClassName?: string;
};

const starsContainer =
    "flex items-center gap-[6px]";
const starBase =
    "";

export function RatingStars({
                                className,
                                rating,
                                max = 5,
                                sizeClassName = "h-5 w-5",
                                ...props
                            }: RatingStarsProps) {

    const fullStars = clampRating(rating);
    const fraction = rating - fullStars;
    const hasHalf = fraction >= 0.5;

    return (
        <div
            className={cn(starsContainer, className)}
            aria-label={`${rating} star rating`}
            {...props}
        >
            {Array.from({ length: max }).map((_, i) => {
                const filled = i < fullStars;
                return (
                    <div key={i} className={cn("relative", sizeClassName)}>
                        <Star
                            className={cn(
                                starBase,
                                sizeClassName,
                                filled
                                    ? "fill-(--primary-color) text-(--primary-color)"
                                    : "text-(--primary-color)"
                            )}
                        />
                        <StarHalf className={cn(
                            "absolute top-0 left-0 fill-(--primary-color) text-(--primary-color)",
                            sizeClassName,
                            hasHalf && i === fullStars ? "" : "hidden"
                        )}/>
                    </div>
                );
            })}

        </div>
    );
}