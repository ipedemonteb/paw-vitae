"use client"

import * as React from "react"
import { CalendarIcon } from "lucide-react"

import { Button } from "@/components/ui/button"
import { Calendar } from "@/components/ui/calendar"
import { cn } from "@/lib/utils"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"

function getBrowserLocale() {
    if (typeof navigator === "undefined") return "en-US"
    return navigator.language || "en-US"
}

function formatDate(date: Date | undefined, locale: string) {
    if (!date) return ""
    return new Intl.DateTimeFormat(locale, {
        day: "2-digit",
        month: "long",
        year: "numeric",
    }).format(date)
}

export function DatePicker({
                               value,
                               onChange,
                               placeholder = "Select a Date",
                               disabled = false,
                               isDateDisabled,
                           }: {
    value?: Date
    onChange?: (date: Date | undefined) => void
    placeholder?: string
    disabled?: boolean
    isDateDisabled?: (date: Date) => boolean
}) {
    const [open, setOpen] = React.useState(false)
    const [month, setMonth] = React.useState<Date | undefined>(value ?? new Date())

    const locale = React.useMemo(() => getBrowserLocale(), [])
    const label = React.useMemo(() => formatDate(value, locale), [value, locale])

    React.useEffect(() => {
        if (value) setMonth(value)
    }, [value])

    React.useEffect(() => {
        if (disabled) setOpen(false)
    }, [disabled])

    return (
        <Popover
            open={open}
            onOpenChange={(next) => {
                if (disabled) return
                setOpen(next)
            }}
        >
            <PopoverTrigger asChild>
                <Button
                    type="button"
                    variant="outline"
                    disabled={disabled}
                    className={cn(
                        "w-full justify-between bg-background",
                        disabled ? "cursor-not-allowed opacity-60" : "cursor-pointer"
                    )}
                >
                    <span
                        className={cn(
                            "truncate font-[400]",
                            value ? "text-[var(--text-color)]" : "text-[var(--text-light)]"
                        )}
                    >
                        {value ? label : placeholder}
                    </span>
                    <CalendarIcon className="h-4 w-4 text-[var(--text-light)]" />
                </Button>
            </PopoverTrigger>

            <PopoverContent className="w-auto overflow-hidden p-0" align="start" sideOffset={8}>
                <Calendar
                    mode="single"
                    selected={value}
                    captionLayout="dropdown"
                    month={month}
                    onMonthChange={setMonth}
                    disabled={(d) => disabled || !!isDateDisabled?.(d)}
                    onSelect={(d) => {
                        if (disabled) return
                        if (d && isDateDisabled?.(d)) return
                        onChange?.(d)
                        if (d) setMonth(d)
                        setOpen(false)
                    }}
                />
            </PopoverContent>
        </Popover>
    )
}
