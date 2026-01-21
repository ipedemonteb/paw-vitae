import { Badge } from "@/components/ui/badge.tsx";
import { HoverCard, HoverCardContent, HoverCardTrigger } from "@/components/ui/hover-card.tsx";
import {useTranslation} from "react-i18next";

const badgeContainer =
    "flex flex-wrap gap-1 mt-2 sm:px-0 px-5 justify-center sm:justify-start";
const badge =
    "bg-white text-[var(--primary-color)] border border-[var(--primary-color)] px-2 py-1 rounded-full";
const moreBadge =
    "bg-[var(--primary-color)] text-white border border-[var(--primary-color)] hover:border hover:border-[var(--primary-dark)] hover:bg-[var(--primary-dark)]";
const hoverContent =
    "w-auto max-w-xs p-3";
const hoverBadges =
    "flex flex-wrap gap-1";

function BadgeComponent({specialties, maxBadges}:{
    specialties: string[];
    maxBadges: number;
}) {

    const { t } = useTranslation();
    const shownBadges = specialties.slice(0, maxBadges);
    const hiddenBadges = specialties.slice(maxBadges);
    const hiddenCount = hiddenBadges.length;

    return (
        <div className={badgeContainer}>
            {shownBadges.map((s) => (
                <Badge className={badge} key={t(s)}>
                    {t(s)}
                </Badge>
            ))}
            {hiddenCount > 0 && (
                <HoverCard openDelay={100}>
                    <HoverCardTrigger className="cursor-default" asChild>
                        <Badge className={moreBadge}>+{hiddenCount}</Badge>
                    </HoverCardTrigger>

                    <HoverCardContent className={hoverContent}>
                        <div className={hoverBadges}>
                            {hiddenBadges.map((s) => (
                                <Badge key={t(s)} className={badge}>
                                    {t(s)}
                                </Badge>
                            ))}
                        </div>
                    </HoverCardContent>
                </HoverCard>
            )}
        </div>
    )
}

export default BadgeComponent;