"use client"

import * as React from "react"
import { Check, ChevronsUpDown } from "lucide-react"

import { cn } from "@/lib/utils.ts"
import { Button } from "@/components/ui/button.tsx"
import {
    Command,
    CommandEmpty,
    CommandGroup,
    CommandInput,
    CommandItem,
    CommandList,
} from "@/components/ui/command.tsx"
import {
    Popover,
    PopoverContent,
    PopoverTrigger,
} from "@/components/ui/popover.tsx"
import {useTranslation} from "react-i18next";
import {Spinner} from "@/components/ui/spinner.tsx";
import {specialtyIdFromSelf} from "@/utils/IdUtils.ts";
import type {DoctorQueryParams, PaginationParams} from "@/hooks/useQueryParams.ts";
import {useEffect} from "react";
import {useSpecialties} from "@/hooks/useSpecialties.ts";

type ComboboxProps = {
    className?: string;
    buttonClassName?: string;
    contentClassName?: string;
    searchParams: PaginationParams & {setParams: (updater: (p: URLSearchParams) => void) => void} & DoctorQueryParams
};

export function SearchSpecialtyCombobox({ className, buttonClassName, contentClassName, searchParams }: ComboboxProps) {
    const [open, setOpen] = React.useState(false);

    let specialty = searchParams.specialty

    const setSelected = (self?: string)=>  {
        searchParams.setParams((p) => {
            if (!self) p.delete("specialty")
            else p.set("specialty", String(specialtyIdFromSelf(self)));
            p.set("page", "1")
        })
        setOpen(false);
    }

    const transformSpecialty = (id?: string) => {
        const numberId = Number(id);
        if (Number.isNaN(numberId)) return undefined;
        return specialties?.find((value) => (specialtyIdFromSelf(value.self) === numberId))
    }

    const { data: specialties, isLoading } = useSpecialties();
    const { t } = useTranslation();

    useEffect(() => {
        if (isLoading) return;
        const s = transformSpecialty(specialty);
        setSelected(s?.self)
    }, [specialty])


    const triggerClass = cn("w-[200px] justify-between", buttonClassName, className);

    return (
        <Popover open={open} onOpenChange={setOpen}>
            <PopoverTrigger asChild>
                <Button
                    variant="outline"
                    role="combobox"
                    aria-expanded={open}
                    className={triggerClass}
                >
                    {t(transformSpecialty(specialty)?.name ?? "search.specialty.select")}
                    <ChevronsUpDown className="opacity-50" />
                </Button>
            </PopoverTrigger>

            <PopoverContent className={cn("w-50 p-0", contentClassName)}>
                <Command>
                    <CommandInput placeholder={t("search.specialty.search")} className="h-9" />
                    <CommandList>
                        {isLoading ? (
                            <div className="py-3 flex items-center justify-center space-x-1">
                                <Spinner className="text-(--text-light) size-5"/>
                            </div>
                        ) : (
                            <>
                                <CommandEmpty>{t("search.specialty.empty")}</CommandEmpty>
                                <CommandGroup>
                                    <CommandItem
                                        key={t("search.specialty.all")}
                                        value={t("search.specialty.all")}
                                        onSelect={(currentValue) => {
                                            setSelected(currentValue)
                                        }}
                                        className="text-(--text-light)"
                                    >
                                        {t("search.specialty.all")}
                                        <Check
                                            className={cn(
                                                "ml-auto",
                                                !transformSpecialty(specialty) ? "opacity-100" : "opacity-0"
                                            )}
                                        />
                                    </CommandItem>
                                    {specialties?.map((s) => (
                                        <CommandItem
                                            key={s.name}
                                            value={s.self}
                                            onSelect={(currentValue) => {
                                                setSelected(currentValue)
                                            }}
                                            className="text-(--text-light)"
                                        >
                                            {t(s.name)}
                                            <Check
                                                className={cn(
                                                    "ml-auto",
                                                    specialty === String(specialtyIdFromSelf(s.self)) ? "opacity-100" : "opacity-0"
                                                )}
                                            />
                                        </CommandItem>
                                    ))}
                                </CommandGroup>
                            </>
                        )}
                    </CommandList>
                </Command>
            </PopoverContent>
        </Popover>
    );
}

