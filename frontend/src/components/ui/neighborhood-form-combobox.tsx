import * as React from "react"
import { Check, ChevronsUpDown, MapPin } from "lucide-react"
import { useTranslation } from "react-i18next"

import { cn } from "@/lib/utils"
import { Spinner } from "@/components/ui/spinner"
import { useNeighborhoods } from "@/hooks/useNeighborhoods"
import {
    Popover,
    PopoverContent,
    PopoverTrigger,
} from "@/components/ui/popover"
import {
    Command,
    CommandEmpty,
    CommandGroup,
    CommandInput,
    CommandItem,
    CommandList,
} from "@/components/ui/command"

type Neighborhood = { self: string; name: string }

type NeighborhoodFormComboboxProps = {
    id: string
    label: string
    required?: boolean

    value?: string
    onChange: (n?: Neighborhood) => void

    disabled?: boolean
    error?: string

    placeholder?: string
    className?: string
}

export function NeighborhoodFormCombobox({
                                             id,
                                             label,
                                             required,
                                             value,
                                             onChange,
                                             disabled,
                                             error,
                                             placeholder,
                                             className,
                                         }: NeighborhoodFormComboboxProps) {
    const { t } = useTranslation()
    const [open, setOpen] = React.useState(false)

    const { data: neighborhoods, isLoading } = useNeighborhoods()

    const selected = React.useMemo(
        () => neighborhoods?.find((n) => n.self === value),
        [neighborhoods, value]
    )

    const displayText =
        selected?.name ??
        placeholder ??
        t("register.placeholder_neighborhood") ??
        "Neighborhood"

    const triggerClasses = cn(
        "w-full h-11 px-3 py-2 border rounded-lg transition-all bg-(--gray-50) text-left",
        "focus:outline-none focus:ring-2 focus:border-transparent",
        error
            ? "border-(--danger) focus:ring-(--danger) text-(--danger-dark) placeholder-(--danger/2)"
            : "border-(--gray-200) focus:ring-(--primary-color)",
        disabled ? "opacity-70 cursor-not-allowed" : "cursor-pointer",
        "flex items-center justify-between gap-2",
        className
    )

    return (
        <div className="space-y-2" id={id}>
            <label htmlFor={id} className="text-sm font-medium (--text-color)">
                {label} {required && <span className="text-(--danger)">*</span>}
            </label>

            <Popover open={open} onOpenChange={(v) => !disabled && setOpen(v)}>
                <PopoverTrigger asChild>
                    <button
                        type="button"
                        id={id}
                        disabled={disabled}
                        aria-expanded={open}
                        aria-haspopup="listbox"
                        className={triggerClasses}
                    >
            <span className="flex items-center gap-2 min-w-0">
              <MapPin
                  className={cn(
                      "h-4 w-4 shrink-0",
                      error ? "text-(--danger)" : "text-(--gray-400)",
                      selected?.name ? "text-(--text-color)" : "text-(--gray-400)"
                  )}
              />
              <span
                  className={cn(
                      "truncate",
                      selected?.name ? "text-(--text-color)" : "text-(--gray-400)"
                  )}
              >
                {displayText}
              </span>
            </span>

                        {isLoading ? (
                            <Spinner className="size-4 text-(--text-light)" />
                        ) : (
                            <ChevronsUpDown className="h-4 w-4 opacity-50 shrink-0" />
                        )}
                    </button>
                </PopoverTrigger>

                <PopoverContent className="w-[--radix-popover-trigger-width] p-0">
                    <Command>
                        <CommandInput
                            placeholder={
                                t("offices.neighborhoods.placeholder") ??
                                t("register.placeholder_neighborhood") ??
                                "Search neighborhood..."
                            }
                            className="h-9"
                        />
                        <CommandList onWheel={(e) => e.stopPropagation()}>
                            {isLoading ? (
                                <div className="py-3 flex items-center justify-center">
                                    <Spinner className="text-(--text-light) size-5" />
                                </div>
                            ) : (
                                <>
                                    <CommandEmpty>
                                        {t("neighborhoodCombobox.empty") ??
                                            t("register.no_neighborhoods") ??
                                            "No neighborhoods found"}
                                    </CommandEmpty>
                                    <CommandGroup>
                                        {neighborhoods?.map((n) => (
                                            <CommandItem
                                                key={n.self}
                                                value={n.name}
                                                onSelect={() => {
                                                    onChange(n)
                                                    setOpen(false)
                                                }}
                                                className="text-(--text-light)"
                                            >
                                                {n.name}
                                                <Check
                                                    className={cn(
                                                        "ml-auto",
                                                        value === n.self ? "opacity-100" : "opacity-0"
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

            {error && (
                <p className="text-sm text-(--danger) font-medium animate-in fade-in slide-in-from-top-1">
                    {error}
                </p>
            )}
        </div>
    )
}
