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
import {useCoverages} from "@/hooks/useCoverages.ts";
import {coverageIdFromSelf} from "@/utils/IdUtils.ts";

type ComboboxProps = {
    className?: string;
    buttonClassName?: string;
    contentClassName?: string;
    onValueChange?: (coverageId: string | null) => void;
};

export function CoverageCombobox({ className, buttonClassName, contentClassName, onValueChange }: ComboboxProps) {
    const [open, setOpen] = React.useState(false);
    const [value, setValue] = React.useState<string | null>(null);

    const setSelected = (v: string, self: string)=>  {
        onValueChange?.(String(coverageIdFromSelf(self)))
        setValue(v === value ? null : v)
        setOpen(false);
    }

    const { data: coverages, isLoading } = useCoverages();
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
                        : "Select coverage..."}
                    <ChevronsUpDown className="opacity-50" />
                </Button>
            </PopoverTrigger>

            <PopoverContent className={cn("w-50 p-0", contentClassName)}>
                <Command>
                    <CommandInput placeholder="Search coverage..." className="h-9" />
                    <CommandList>
                        {isLoading ? (
                            <div className="py-3 flex items-center justify-center space-x-1">
                                <Spinner className="text-(--text-light) size-5"/>
                            </div>
                        ) : (
                            <>
                                <CommandEmpty>No Coverages Found</CommandEmpty>
                                <CommandGroup>
                                    {coverages?.map((coverage) => (
                                        <CommandItem
                                            key={coverage.name}
                                            value={t(coverage.name)}
                                            onSelect={(currentValue) => {
                                                setSelected(currentValue, coverage.self)
                                            }}
                                            className="text-(--text-light)"
                                        >
                                            {t(coverage.name)}
                                            <Check
                                                className={cn(
                                                    "ml-auto",
                                                    value === coverage.name ? "opacity-100" : "opacity-0"
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

