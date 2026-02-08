import React from "react";

interface FormInputProps extends React.InputHTMLAttributes<HTMLInputElement> {
    label: string;
    error?: string;
}

export function FormInput({ label, id, required, className, error, ...props }: FormInputProps) {
    return (
        <div className="space-y-2">
            <label htmlFor={id} className="text-sm font-medium text-gray-700">
                {label} {required && <span className="text-(--danger)">*</span>}
            </label>
            <input
                id={id}
                required={required}
                className={`w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 transition-all bg-(--gray-50)
                ${error
                    ? "border-(--danger) focus:ring-(--danger) text-(--danger-dark) placeholder-(--danger/2)"
                    : "border-(--gray-200) focus:ring-(--primary-color) focus:border-transparent"
                } ${className}`}
                {...props}
            />
            {error && (
                <p className="text-sm text-(--danger) font-medium animate-in fade-in slide-in-from-top-1">
                    {error}
                </p>
            )}
        </div>
    );
}