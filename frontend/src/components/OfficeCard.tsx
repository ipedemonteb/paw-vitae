import type {OfficeDTO} from "@/data/offices.ts";
import {Building2, MapPinIcon, Circle} from "lucide-react";
import {useNeighborhood} from "@/hooks/useNeighborhoods.ts";
import {Skeleton} from "@/components/ui/skeleton.tsx";
import {useTranslation} from "react-i18next";

const baseStatusClassName = "rounded-xl transition-all font-normal border text-xs py-1 px-2 flex items-center justify-center gap-1";
const activeClassName = "bg-(--success-light) border-(--success) text-(--success) " + baseStatusClassName;
const inactiveClassName = "bg-gray-100 border-gray-300 text-(--text-light) " + baseStatusClassName;

const fillClassName = "size-3 transition-all";
const activeFillClassName = "fill-(--success) text-(--success) opacity-80 animate-pulse " + fillClassName;
const inactiveFillClassName = "fill-gray-300 text-gray-300 " + fillClassName;

const statusDictionary = { active: activeClassName, inactive: inactiveClassName } as const;
const fillStatusDictionary = { active: activeFillClassName, inactive: inactiveFillClassName } as const;

type OfficeCardProps = {
    office: OfficeDTO;
    animateInDelay: number;
    mounted: boolean;
};

export default function OfficeCard({office, animateInDelay, mounted}: OfficeCardProps) {
    const {data: neighborhood, isLoading: isLoadingNeighborhood} = useNeighborhood(office.neighborhood);
    const {t} = useTranslation();

    return (
        <div style={{transitionDelay: `${animateInDelay * 100}ms`}} className={`flex flex-col justify-center items-stretch transition-all w-full ${mounted ? "opacity-100 translate-y-0 scale-100" : "opacity-0 translate-y-4 scale-95"}`}>
            <div className="relative w-full h-56 peer flex transition-all overflow-hidden rounded-2xl items-center border-2 sm:max-w-56 sm:mx-auto">
                <div className="text-ellipsis transition-opacity text-[1.3rem] flex justify-center-safe items-center overflow-hidden flex-col font-semibold rounded-2xl w-full px-8 py-7">
                    <Building2 className="size-14 stroke-[1.5]"/>
                    <p className="w-full text-center min-w-0 text-ellipsis text-nowrap overflow-clip">{office.name}</p>
                    <div className="flex text-sm items-center gap-1 pb-3 font-normal text-(--text-light)">
                        <MapPinIcon className="size-4 text-(--primary-color)" />
                        {(neighborhood?.name && !isLoadingNeighborhood) ? neighborhood.name : <Skeleton className="w-24 h-3.5" />}
                    </div>
                    <div className={statusDictionary[office.status]}>
                        <Circle className={fillStatusDictionary[office.status]} />
                        {t("offices.status." + office.status)}
                    </div>
                </div>
            </div>
        </div>
    );
}
