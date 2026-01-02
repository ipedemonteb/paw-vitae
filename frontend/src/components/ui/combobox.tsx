"use client"

import * as React from "react"
import { Check, ChevronsUpDown } from "lucide-react"

import { cn } from "@/lib/utils"
import { Button } from "@/components/ui/button"
import {
    Command,
    CommandEmpty,
    CommandGroup,
    CommandInput,
    CommandItem,
    CommandList,
} from "@/components/ui/command"
import {
    Popover,
    PopoverContent,
    PopoverTrigger,
} from "@/components/ui/popover"
import {useSpecialties} from "@/hooks/useSpecialties.ts";
import {useTranslation} from "react-i18next";

type ComboboxProps = {
    className?: string;
    buttonClassName?: string;
    contentClassName?: string;
};

export function Combobox({ className, buttonClassName, contentClassName }: ComboboxProps) {
    const [open, setOpen] = React.useState(false);
    const [value, setValue] = React.useState("");
    const { specialties, loading } = useSpecialties();
    const { t } = useTranslation();


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
                    {value
                        ? t(value)
                        : "Select specialty..."}
                    <ChevronsUpDown className="opacity-50" />
                </Button>
            </PopoverTrigger>

            <PopoverContent className={cn("w-[200px] p-0", contentClassName)}>
                <Command>
                    <CommandInput placeholder="Search specialty..." className="h-9" />
                    <CommandList>
                        {loading ? (
                            <div className="py-3 flex items-center justify-center">
                                <svg
                                    className="animate-spin h-5 w-5 text-gray-600"
                                    xmlns="http://www.w3.org/2000/svg"
                                    fill="none"
                                    viewBox="0 0 24 24"
                                >
                                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
                                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v4a4 4 0 00-4 4H4z" />
                                </svg>
                                <span className="ml-2 text-sm text-gray-700">{t("Loading...")}</span>
                            </div>
                        ) : (
                            <>
                                <CommandEmpty>No Specialties Found</CommandEmpty>
                                <CommandGroup>
                                    <CommandItem
                                        key={"specialty.all"}
                                        value={t("specialty.all")}
                                        onSelect={(currentValue) => {
                                            setValue(currentValue === value ? "" : currentValue);
                                            setOpen(false);
                                        }}
                                    >
                                        {t("specialty.all")}
                                        <Check
                                            className={cn(
                                                "ml-auto",
                                                value === "specialty.all" ? "opacity-100" : "opacity-0"
                                            )}
                                        />
                                    </CommandItem>
                                    {(specialties || []).map((specialty) => (
                                        <CommandItem
                                            key={specialty.name}
                                            value={t(specialty.name)}
                                            onSelect={(currentValue) => {
                                                setValue(currentValue === value ? "" : currentValue);
                                                setOpen(false);
                                            }}
                                        >
                                            {t(specialty.name)}
                                            <Check
                                                className={cn(
                                                    "ml-auto",
                                                    value === specialty.name ? "opacity-100" : "opacity-0"
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

