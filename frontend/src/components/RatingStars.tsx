import * as React from "react";
import { Star } from "lucide-react";
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
    const r = clampRating(rating);

    return (
        <div
            className={cn(starsContainer, className)}
            aria-label={`${r} star rating`}
            {...props}
        >
            {Array.from({ length: max }).map((_, i) => {
                const filled = i < r;
                return (
                    <Star
                        key={i}
                        className={cn(
                            starBase,
                            sizeClassName,
                            filled
                                ? "fill-[var(--primary-color)] text-[var(--primary-color)]"
                                : "text-[var(--gray-300)]"
                        )}
                    />
                );
            })}
        </div>
    );
}