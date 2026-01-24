import {ToggleGroup, ToggleGroupItem} from "@/components/ui/toggle-group.tsx";
import {useTranslation} from "react-i18next";
import type {SpecialtyDTO} from "@/data/specialties.ts";
import {useEffect, useState} from "react";

type SpecialtyToggleGroupProps = {
    currentSpecialties: string[],
    specialties?: SpecialtyDTO[],
    onValueChange: (s: string[]) => void
}

export default function SpecialtyToggleGroup({specialties, currentSpecialties, onValueChange}: SpecialtyToggleGroupProps) {
    const {t} = useTranslation()
    const [activeSpecialties, setActiveSpecialties] = useState(currentSpecialties)

    useEffect(() => {
        onValueChange(activeSpecialties)
    }, [activeSpecialties])

    return (
        <ToggleGroup value={activeSpecialties} onValueChange={setActiveSpecialties} type="multiple" spacing={1.5} variant="outline" size="sm" className="flex-wrap p-3  border-[0.5px]  border-gray-300 bg-gray-50 rounded-xl overflow-clip  items-center ">
            {specialties?.sort((s1, s2) => s1.name.localeCompare(s2.name)).map(s => (
                <ToggleGroupItem key={s.self} value={s.self} className="border hover:border-gray-300 transition-all text-(--text-light) data-[state=on]:bg-[rgba(var(--primary-light-rgb),0.15)] rounded-full text-xs data-[state=on]:border-(--primary-light) data-[state=on]:text-(--primary-color) cursor-pointer">
                    {t(s.name)}
                </ToggleGroupItem>
            ))}
        </ToggleGroup>
    )
}