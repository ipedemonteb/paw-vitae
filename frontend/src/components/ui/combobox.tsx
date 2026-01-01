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

const specialties = [
    { value: "all", label: "All Specialties" },
    { value: "general", label: "General" },
    { value: "cardiology", label: "Cardiology" },
    { value: "dermatology", label: "Dermatology" },
    { value: "endocrinology", label: "Endocrinology" },
    { value: "gastroenterology", label: "Gastroenterology" },
    { value: "hematology", label: "Hematology" },
    { value: "infectious-diseases", label: "Infectious Diseases" },
    { value: "nephrology", label: "Nephrology" },
    { value: "neurology", label: "Neurology" },
    { value: "oncology", label: "Oncology" },
    { value: "pulmonology", label: "Pulmonology" },
    { value: "rheumatology", label: "Rheumatology" },
    { value: "urology", label: "Urology" },
    { value: "pediatrics", label: "Pediatrics" },
    { value: "gynecology", label: "Gynecology" },
    { value: "traumatology", label: "Traumatology" },
];

type ComboboxProps = {
    className?: string;
    buttonClassName?: string;
    contentClassName?: string;
};

export function Combobox({ className, buttonClassName, contentClassName }: ComboboxProps) {
    const [open, setOpen] = React.useState(false);
    const [value, setValue] = React.useState("");

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
                        ? specialties.find((f) => f.value === value)?.label
                        : "Select specialty..."}
                    <ChevronsUpDown className="opacity-50" />
                </Button>
            </PopoverTrigger>

            <PopoverContent className={cn("w-[200px] p-0", contentClassName)}>
                <Command>
                    <CommandInput placeholder="Search specialty..." className="h-9" />
                    <CommandList>
                        <CommandEmpty>No framework found.</CommandEmpty>
                        <CommandGroup>
                            {specialties.map((specialty) => (
                                <CommandItem
                                    key={specialty.value}
                                    value={specialty.value}
                                    onSelect={(currentValue) => {
                                        setValue(currentValue === value ? "" : currentValue);
                                        setOpen(false);
                                    }}
                                >
                                    {specialty.label}
                                    <Check
                                        className={cn(
                                            "ml-auto",
                                            value === specialty.value ? "opacity-100" : "opacity-0"
                                        )}
                                    />
                                </CommandItem>
                            ))}
                        </CommandGroup>
                    </CommandList>
                </Command>
            </PopoverContent>
        </Popover>
    );
}

