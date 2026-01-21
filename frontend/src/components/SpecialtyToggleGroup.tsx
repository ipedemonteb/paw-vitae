import {ToggleGroup, ToggleGroupItem} from "@/components/ui/toggle-group.tsx";
import {useTranslation} from "react-i18next";
import {useState} from "react";
import type {SpecialtyDTO} from "@/data/specialties.ts";
import type {UseQueryResult} from "@tanstack/react-query";

type SpecialtyToggleGroupProps = {
    // currentSpecialtiesUrl?: string[],
    currentSpecialties:  UseQueryResult<SpecialtyDTO, Error>[],
    specialties?: SpecialtyDTO[]
}

export default function SpecialtyToggleGroup({currentSpecialties, specialties}: SpecialtyToggleGroupProps) {
    const {t} = useTranslation()
    const[activeValue, setActiveValue] = useState(currentSpecialties.filter(s => s.data !== undefined).map(s => s.data.name))
    return (
        <ToggleGroup value={activeValue} onValueChange={setActiveValue} type="multiple" spacing={1.5} variant="outline" size="sm" className="flex-wrap p-3 border border-gray-300 bg-gray-50 ">
            {specialties?.sort((s1, s2) => s1.name.localeCompare(s2.name)).map(s => (
                <ToggleGroupItem key={s.name} value={s.name} className="border hover:border-gray-300 transition-all text-(--text-light) data-[state=on]:bg-[rgba(var(--primary-light-rgb),0.15)] rounded-full text-xs data-[state=on]:border-(--primary-light) data-[state=on]:text-(--primary-color) cursor-pointer">
                    {t(s.name)}
                </ToggleGroupItem>
            ))}
        </ToggleGroup>
    )
}