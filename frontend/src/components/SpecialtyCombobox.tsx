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
import {useSpecialties} from "@/hooks/useSpecialties.ts";
import {useTranslation} from "react-i18next";
import {Spinner} from "@/components/ui/spinner.tsx";

type ComboboxProps = {
    className?: string;
    buttonClassName?: string;
    contentClassName?: string;
};

export function SpecialtyCombobox({ className, buttonClassName, contentClassName }: ComboboxProps) {
    const [open, setOpen] = React.useState(false);
    const [value, setValue] = React.useState("");
    const { data: specialties, isLoading } = useSpecialties();
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
                        : t("combobox.specialty.select")}
                    <ChevronsUpDown className="opacity-50" />
                </Button>
            </PopoverTrigger>

            <PopoverContent className={cn("w-[200px] p-0", contentClassName)}>
                <Command>
                    <CommandInput placeholder={t("combobox.specialty.search")} className="h-9" />
                    <CommandList>
                        {isLoading ? (
                            <div className="py-3 flex items-center justify-center space-x-1">
                                <Spinner className="text-(--text-light) size-5"/>
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
                                        className="text-(--text-light)"
                                    >
                                        {t("specialty.all")}
                                        <Check
                                            className={cn(
                                                "ml-auto",
                                                value === "specialty.all" ? "opacity-100" : "opacity-0"
                                            )}
                                        />
                                    </CommandItem>
                                    {specialties?.map((specialty) => (
                                        <CommandItem
                                            key={specialty.name}
                                            value={t(specialty.name)}
                                            onSelect={(currentValue) => {
                                                setValue(currentValue === value ? "" : currentValue);
                                                setOpen(false);
                                            }}
                                            className="text-(--text-light)"
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

