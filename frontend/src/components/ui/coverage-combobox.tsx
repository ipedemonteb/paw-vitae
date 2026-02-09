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
import type {DoctorQueryParams, PaginationParams} from "@/hooks/useQueryParams.ts";
import {useEffect} from "react";

type ComboboxProps = {
    className?: string;
    buttonClassName?: string;
    contentClassName?: string;
    searchParams: PaginationParams & {setParams: (updater: (p: URLSearchParams) => void) => void} & DoctorQueryParams
};

export function CoverageCombobox({ className, buttonClassName, contentClassName, searchParams }: ComboboxProps) {
    const [open, setOpen] = React.useState(false);

    let coverage = searchParams.coverage

    const setSelected = (self?: string)=>  {
        searchParams.setParams((p) => {
            if (!self) p.delete("coverage")
            else p.set("coverage", String(coverageIdFromSelf(self)));
            p.set("page", "1")
        })
        setOpen(false);
    }

    const transformCoverage = (id?: string) => {
        const numberId = Number(id);
        if (Number.isNaN(numberId)) return undefined;
        return coverages?.find((value) => (coverageIdFromSelf(value.self) === numberId))
    }

    const { data: coverages, isLoading } = useCoverages();
    const { t } = useTranslation();

    useEffect(() => {
        if (isLoading) return;
        const c = transformCoverage(coverage);
        setSelected(c?.self)
    }, [coverage])


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
                    {transformCoverage(coverage)?.name ?? t("search.coverage.select")}
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
                                    <CommandItem
                                        key={t("search.coverage.all")}
                                        value={t("search.coverage.all")}
                                        onSelect={(currentValue) => {
                                            setSelected(currentValue)
                                        }}
                                        className="text-(--text-light)"
                                    >
                                        {t("search.coverage.all")}
                                        <Check
                                            className={cn(
                                                "ml-auto",
                                                !transformCoverage(coverage) ? "opacity-100" : "opacity-0"
                                            )}
                                        />
                                    </CommandItem>
                                    {coverages?.map((c) => (
                                        <CommandItem
                                            key={c.name}
                                            value={c.self}
                                            onSelect={(currentValue) => {
                                                setSelected(currentValue)
                                            }}
                                            className="text-(--text-light)"
                                        >
                                            {c.name}
                                            <Check
                                                className={cn(
                                                    "ml-auto",
                                                    coverage === String(coverageIdFromSelf(c.self)) ? "opacity-100" : "opacity-0"
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

