import { Skeleton } from "@/components/ui/skeleton.tsx";

const loadingSpecialtiesContainer =
    "flex flex-wrap gap-1 mt-2 sm:px-0 px-5 justify-center sm:justify-start";
const loadingSpecialty =
    "h-6.5 w-20 rounded-full";
const loadingSpecialtyPlus =
    "h-6.6 w-8 rounded-full";

type LoadingSpecialtiesProps = {
    badgesCount: number;
    plusBadge?: boolean;
};

export function LoadingSpecialties({ badgesCount, plusBadge = true }: LoadingSpecialtiesProps) {
    return (
        <div className={loadingSpecialtiesContainer}>
            {new Array(badgesCount).fill(0).map((_, i) => (
                <Skeleton key={i} className={loadingSpecialty} />
            ))}
            {plusBadge && <Skeleton className={loadingSpecialtyPlus} />}
        </div>
    );
}
