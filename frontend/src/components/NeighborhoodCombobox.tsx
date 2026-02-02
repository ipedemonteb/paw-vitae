import {useState} from "react";
import {Popover, PopoverContent, PopoverTrigger} from "@/components/ui/popover.tsx";
import {Check, ChevronsUpDown} from "lucide-react";
import {Button} from "@/components/ui/button.tsx";
import {useNeighborhood, useNeighborhoods} from "@/hooks/useNeighborhoods.ts";
import {useTranslation} from "react-i18next";
import {Command, CommandEmpty, CommandGroup, CommandInput, CommandItem, CommandList} from "@/components/ui/command.tsx";
import {Spinner} from "@/components/ui/spinner.tsx";
import {cn} from "@/lib/utils.ts";
import {Skeleton} from "@/components/ui/skeleton.tsx";

type NeighborhoodComboboxProps = {
    value?: string,
    onChange: (val: string) => void,
    mutationPending: boolean
}

export default function NeighborhoodCombobox({value, onChange, mutationPending}: NeighborhoodComboboxProps) {
    const [open, setOpen] = useState(false)
    const {t} = useTranslation()
    const {data: neighborhoods, isLoading: isLoadingNeighborhoods} = useNeighborhoods();
    const {data: neighborhood, isLoading: isLoadingNeighborhood} = useNeighborhood(value)

    if (isLoadingNeighborhood) {
        return (
            <Skeleton className="min-w-0 w-70 h-9" />
        )
    }

    return (
        <Popover open={open} onOpenChange={setOpen}>
            <PopoverTrigger asChild>
                <Button
                    variant="outline"
                    disabled={mutationPending}
                    role="combobox"
                    aria-expanded={open}
                    className={"flex-1 min-w-0 hover:text-(--text-light) cursor-pointer font-normal w-70 justify-between"}
                >
                    <p className={neighborhood?.name ? "" : "opacity-70"}>{neighborhood?.name ?? t("neighborhoodCombobox.placeholder")}</p>
                    <ChevronsUpDown className="opacity-50" />
                </Button>
            </PopoverTrigger>
            <PopoverContent className="w-60 p-0">
                <Command>
                    <CommandInput placeholder={t("offices.neighborhoods.placeholder")} className="h-9"/>
                    <CommandList onWheel={(e) => e.stopPropagation()}>
                        {isLoadingNeighborhoods ? (
                            <div className="py-3 flex items-center justify-center space-x-1">
                                <Spinner className="text-(--text-light) size-5"/>
                            </div>
                        ) : (
                            <>
                                <CommandEmpty>{t("neighborhoodCombobox.empty")}</CommandEmpty>
                                <CommandGroup>
                                    {neighborhoods?.map((c) => (
                                        <CommandItem
                                            key={c.self}
                                            value={c.self}
                                            onSelect={(currentValue) => {
                                                onChange(currentValue)
                                                setOpen(false)
                                            }}
                                            className="text-(--text-light)"
                                        >
                                            {c.name}
                                            <Check
                                                className={cn(
                                                    "ml-auto",
                                                    value === c.self ? "opacity-100" : "opacity-0"
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
    )
}