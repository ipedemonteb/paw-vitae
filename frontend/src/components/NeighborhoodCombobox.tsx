import {useState} from "react";
import {Popover, PopoverContent, PopoverTrigger} from "@/components/ui/popover.tsx";
import {Check, ChevronsUpDown} from "lucide-react";
import {Button} from "@/components/ui/button.tsx";
import {useNeighborhood, useNeighborhoods} from "@/hooks/useNeighborhoods.ts";
import {useTranslation} from "react-i18next";
import {Command, CommandEmpty, CommandGroup, CommandInput, CommandItem, CommandList} from "@/components/ui/command.tsx";
import {Spinner} from "@/components/ui/spinner.tsx";
import {cn} from "@/lib/utils.ts";

type NeighborhoodComboboxProps = {
    currentNeighborhood?: string
}

export default function NeighborhoodCombobox({currentNeighborhood}: NeighborhoodComboboxProps) {
    const [open, setOpen] = useState(false)
    const {t} = useTranslation()
    const {data: neighborhoods, isLoading: isLoadingNeighborhoods} = useNeighborhoods();
    const {data: neighborhood} = useNeighborhood(currentNeighborhood)
    const [value, setValue] = useState(neighborhood?.name ?? t("offices.neighborhoods.all"))
    return (
        <Popover open={open} onOpenChange={setOpen}>
            <PopoverTrigger asChild>
                <Button
                    variant="outline"
                    role="combobox"
                    aria-expanded={open}
                    className="flex-1 min-w-0 hover:text-(--text-light) cursor-pointer font-normal w-70 justify-between"
                >
                    {value}
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
                                <CommandEmpty>No Neighborhoods Found</CommandEmpty>
                                <CommandGroup>
                                    <CommandItem
                                        key={t("offices.neighborhoods.all")}
                                        value={t("offices.neighborhoods.all")}
                                        onSelect={(currentValue) => {
                                            setValue(currentValue)
                                            setOpen(false)
                                        }}
                                        className="text-(--text-light)"
                                    >
                                        {t("offices.neighborhoods.all")}
                                        <Check
                                            className={cn(
                                                "ml-auto",
                                                value === t("offices.neighborhoods.all") ? "opacity-100" : "opacity-0"
                                            )}
                                        />
                                    </CommandItem>
                                    {neighborhoods?.map((c) => (
                                        <CommandItem
                                            key={c.name}
                                            value={c.name}
                                            onSelect={(currentValue) => {
                                                setValue(currentValue)
                                                setOpen(false)
                                            }}
                                            className="text-(--text-light)"
                                        >
                                            {c.name}
                                            <Check
                                                className={cn(
                                                    "ml-auto",
                                                    value === c.name ? "opacity-100" : "opacity-0"
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