import React from "react"
import { Check } from "lucide-react"
import { cn } from "@/lib/utils"

type Step = {
    id: number
    label: string
}

type FormStepperProps = {
    currentStep: number
    steps: Step[]
    className?: string
}

export function FormStepper({ currentStep, steps, className }: FormStepperProps) {

    const isDone = (id: number) => currentStep > id
    const isActive = (id: number) => currentStep === id

    return (
        <div className={cn("w-full mb-10", className)}>
            <div
                className="grid items-center gap-3"
                style={{ gridTemplateColumns: `repeat(${steps.length * 2 - 1}, minmax(0, 1fr))` }}
            >
                {steps.map((s, idx) => {
                    const done = isDone(s.id)
                    const active = isActive(s.id)
                    const upcoming = !done && !active

                    const circleClass = cn(
                        "h-10 w-10 rounded-full border-2 flex items-center justify-center text-sm font-bold transition-colors",
                        done && "bg-(--success) border-(--success) text-white",
                        active && "bg-(--primary-color) border-(--primary-color) text-white",
                        upcoming && "bg-white border-(--gray-300) text-(--gray-400)"
                    )

                    const labelClass = cn(
                        "mt-2 text-xs font-medium text-center transition-colors",
                        active ? "text-(--primary-color)" : done ? "text-(--success)" : "text-(--gray-500)"
                    )

                    return (
                        <React.Fragment key={s.id}>

                            <div className="flex flex-col items-center col-span-1">
                                <div className={circleClass}>
                                    {done ? <Check className="h-5 w-5" /> : s.id}
                                </div>
                                <span className={labelClass}>{s.label}</span>
                            </div>

                            {idx < steps.length - 1 && (
                                <div className="col-span-1 flex items-center">
                                    <div
                                        className={cn(
                                            "h-1 w-full rounded-full transition-colors",
                                            currentStep > s.id ? "bg-(--success)" : "bg-(--gray-200)"
                                        )}
                                    />
                                </div>
                            )}
                        </React.Fragment>
                    )
                })}
            </div>

        </div>
    )
}
