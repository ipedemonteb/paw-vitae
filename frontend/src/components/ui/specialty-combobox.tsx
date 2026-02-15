import * as React from "react";
import { Check, ChevronsUpDown } from "lucide-react";
import { cn } from "@/lib/utils.ts";
import { Button } from "@/components/ui/button.tsx";
import {
    Command,
    CommandEmpty,
    CommandGroup,
    CommandInput,
    CommandItem,
    CommandList,
} from "@/components/ui/command.tsx";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover.tsx";
import { useSpecialties } from "@/hooks/useSpecialties.ts";
import { useTranslation } from "react-i18next";
import { Spinner } from "@/components/ui/spinner.tsx";
import { specialtyIdFromSelf } from "@/utils/IdUtils.ts";
import type { SpecialtyDTO } from "@/data/specialties.ts";
import {useDelayedBoolean} from "@/utils/queryUtils.ts";
import {RefetchComponent} from "@/components/ui/refetch.tsx";

type Props = {
    className?: string;
    buttonClassName?: string;
    contentClassName?: string;

    value?: string | null;
    onValueChange?: (self: string | null, derivedId: number | null, dto?: SpecialtyDTO) => void;
};

export function SpecialtyCombobox({
                                      className,
                                      buttonClassName,
                                      contentClassName,
                                      value,
                                      onValueChange,
                                  }: Props) {
    const [open, setOpen] = React.useState(false);
    const [internal, setInternal] = React.useState<string | null>(null);

    const selectedSelf = value !== undefined ? value : internal;

    const { data: specialties, isLoading, isError, refetch, isFetching } = useSpecialties();
    const { t } = useTranslation();

    const triggerClass = cn("w-[200px] justify-between", buttonClassName, className);

    const selectedLabel = React.useMemo(() => {
        if (selectedSelf == null) return t("combobox.specialty.select");
        const found = specialties?.find((s) => s.self === selectedSelf);
        return found ? t(found.name) : t("combobox.specialty.select");
    }, [selectedSelf, specialties, t]);

    const setSelected = (self: string | null, dto?: SpecialtyDTO) => {
        if (value === undefined) setInternal(self);
        const id = self ? specialtyIdFromSelf(self) : null;
        onValueChange?.(self, id, dto);
        setOpen(false);
    };

    const loading = useDelayedBoolean(isLoading || isFetching, 500)

    return (
        <Popover open={open} onOpenChange={setOpen} modal={true}>
            <PopoverTrigger asChild>
                <Button variant="outline" role="combobox" aria-expanded={open} className={triggerClass}>
                    <span className="min-w-0 flex-1 text-left whitespace-nowrap overflow-hidden text-ellipsis">
                        {selectedLabel}
                    </span>
                    <ChevronsUpDown className="opacity-50 shrink-0 ml-2" />
                </Button>
            </PopoverTrigger>

            <PopoverContent className={cn("w-56 p-0", contentClassName)}>
                <Command>
                    <CommandInput placeholder={t("combobox.specialty.search")} className="h-9" />
                    <CommandList>
                        {loading ? (
                            <div className="py-3 h-32 flex items-center justify-center space-x-1">
                                <Spinner className="text-(--gray-400) size-6" />
                            </div>
                        ) : isError ? (
                            <RefetchComponent
                                isFetching={isFetching}
                                onRefetch={() => refetch()}
                                errorText={t("landing.hero.specialties_error")}
                                className="py-3 px-2 h-32 flex flex-col items-center justify-center"
                                textClassName="text-sm"
                            />
                        ) : (
                            <>
                                <CommandEmpty>No Specialties Found</CommandEmpty>
                                <CommandGroup>
                                    <CommandItem
                                        key="specialty.all"
                                        value={t("specialty.all")}
                                        onSelect={() => setSelected(null)}
                                        className="text-(--text-light)"
                                    >
                                        {t("specialty.all")}
                                        <Check className={cn("ml-auto", selectedSelf == null ? "opacity-100" : "opacity-0")} />
                                    </CommandItem>

                                    {specialties?.map((s) => (
                                        <CommandItem
                                            key={s.self}
                                            value={t(s.name)}
                                            onSelect={() => setSelected(s.self, s)}
                                            className="text-(--text-light)"
                                        >
                                            {t(s.name)}
                                            <Check className={cn("ml-auto", selectedSelf === s.self ? "opacity-100" : "opacity-0")} />
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