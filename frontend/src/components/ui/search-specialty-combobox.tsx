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
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover.tsx"
import { useTranslation } from "react-i18next"
import { Spinner } from "@/components/ui/spinner.tsx"
import { specialtyIdFromSelf } from "@/utils/IdUtils.ts"
import type { DoctorQueryParams, PaginationParams } from "@/hooks/useQueryParams.ts"
import { useSpecialties } from "@/hooks/useSpecialties.ts"
import { useDelayedBoolean } from "@/utils/queryUtils.ts"
import { RefetchComponent } from "@/components/ui/refetch.tsx"

type ComboboxProps = {
    className?: string
    buttonClassName?: string
    contentClassName?: string
    searchParams: PaginationParams & {
        setParams: (updater: (p: URLSearchParams) => void) => void
    } & DoctorQueryParams
}

export function SearchSpecialtyCombobox({
                                            className,
                                            buttonClassName,
                                            contentClassName,
                                            searchParams,
                                        }: ComboboxProps) {
    const [open, setOpen] = React.useState(false)
    const { t } = useTranslation()

    const specialty = searchParams.specialty

    const { data: specialties, isLoading, isError, refetch, isFetching } = useSpecialties()
    const loading = useDelayedBoolean(isLoading || isFetching, 500)

    const selectedDto = React.useMemo(() => {
        const numberId = Number(specialty)
        if (!specialty || Number.isNaN(numberId)) return undefined
        return specialties?.find((s) => specialtyIdFromSelf(s.self) === numberId)
    }, [specialty, specialties])

    const selectedLabel = selectedDto ? t(selectedDto.name) : t("search.specialty.select")

    const setSelected = (self: string | null) => {
        searchParams.setParams((p) => {
            if (!self) p.delete("specialty")
            else p.set("specialty", String(specialtyIdFromSelf(self)))
            p.set("page", "1")
        })
        setOpen(false)
    }

    const triggerClass = cn("w-[200px] justify-between", buttonClassName, className)

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
                    <CommandInput placeholder={t("search.specialty.search")} className="h-9" />
                    <CommandList>
                        {loading ? (
                            <div className="py-3 h-32 flex items-center justify-center space-x-1">
                                <Spinner className="text-(--gray-400) size-6" />
                            </div>
                        ) : isError ? (
                            <RefetchComponent
                                isFetching={isFetching}
                                onRefetch={() => refetch()}
                                errorText={t("search.specialty.error")}
                                className="py-3 px-2 h-32 flex flex-col items-center justify-center"
                                textClassName="text-sm"
                            />
                        ) : (
                            <>
                                <CommandEmpty>{t("search.specialty.empty")}</CommandEmpty>

                                <CommandGroup>
                                    <CommandItem
                                        key="specialty.all"
                                        value={t("search.specialty.all")}
                                        onSelect={() => setSelected(null)}
                                        className="text-(--text-light)"
                                    >
                                        {t("search.specialty.all")}
                                        <Check className={cn("ml-auto", !selectedDto ? "opacity-100" : "opacity-0")} />
                                    </CommandItem>

                                    {specialties?.map((s) => (
                                        <CommandItem
                                            key={s.self}
                                            value={t(s.name)}
                                            onSelect={() => setSelected(s.self)}
                                            className="text-(--text-light)"
                                        >
                                            {t(s.name)}
                                            <Check
                                                className={cn(
                                                    "ml-auto",
                                                    selectedDto?.self === s.self ? "opacity-100" : "opacity-0"
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