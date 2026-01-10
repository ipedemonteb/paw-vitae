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
                           }: {
    value?: Date
    onChange?: (date: Date | undefined) => void
    placeholder?: string
}) {
    const [open, setOpen] = React.useState(false)
    const [month, setMonth] = React.useState<Date | undefined>(value ?? new Date())

    const locale = React.useMemo(() => getBrowserLocale(), [])
    const label = React.useMemo(() => formatDate(value, locale), [value, locale])

    React.useEffect(() => {
        if (value) setMonth(value)
    }, [value])

    return (
        <Popover open={open} onOpenChange={setOpen}>
            <PopoverTrigger asChild>
                <Button
                    type="button"
                    variant="outline"
                    className="w-full justify-between bg-background cursor-pointer"
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
                    onSelect={(d) => {
                        onChange?.(d)
                        if (d) setMonth(d)
                        setOpen(false)
                    }}
                />
            </PopoverContent>
        </Popover>
    )
}
