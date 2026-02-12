import React from "react";
import { cn } from "@/lib/utils";

interface FormInputProps extends React.InputHTMLAttributes<HTMLInputElement> {
    label?: string;
    error?: string;
    hideLabel?: boolean;
    icon?: React.ReactNode;
}

export function FormInput({ label, id, required, className, error, hideLabel = false, icon, ...props }: FormInputProps) {
    return (
        <div className="space-y-2">
            {!hideLabel && (
                <label htmlFor={id} className="text-sm font-medium text-(--text-color)">
                    {label} {required && <span className="text-(--danger)">*</span>}
                </label>
            )}
            {hideLabel && label && <label htmlFor={id} className="sr-only">{label}</label>}

            <div className={cn("relative w-full", !hideLabel && "mt-0")}>
                {icon && (
                    <div className={cn("absolute left-3 top-1/2 -translate-y-1/2 pointer-events-none",
                            error ? "text-(--danger-placeholder)" : "text-(--gray-400)"
                        )}
                    >
                        {icon}
                    </div>
                )}
                <input
                    id={id}
                    required={required}
                    className={cn(
                        "w-full h-11 px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 transition-colors bg-(--gray-50)",
                        icon && "pl-10",
                        error
                            ? "border-(--danger) focus:ring-(--danger) text-(--danger-dark) placeholder:text-(--danger-placeholder)"
                            : "border-(--gray-200) focus:ring-(--primary-color) focus:border-transparent",
                        className
                    )}
                    {...props}
                />
            </div>

            {error && <p className="text-sm text-(--danger) font-medium animate-in fade-in slide-in-from-top-1">{error}</p>}
        </div>
    );
}