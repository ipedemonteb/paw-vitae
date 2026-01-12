import React from "react";

interface FormInputProps extends React.InputHTMLAttributes<HTMLInputElement> {
    label: string;
    error?: string;
}

export function FormInput({ label, id, required, className, error, ...props }: FormInputProps) {
    return (
        <div className="space-y-2">
            <label htmlFor={id} className="text-sm font-medium text-gray-700">
                {label} {required && <span className="text-red-500">*</span>}
            </label>
            <input
                id={id}
                required={required}
                className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 transition-all 
                ${error
                    ? "border-red-500 focus:ring-red-500 text-red-900 placeholder-red-300"
                    : "border-gray-300 focus:ring-blue-500"
                } ${className}`}
                {...props}
            />
            {error && (
                <p className="text-sm text-red-500 font-medium animate-in fade-in slide-in-from-top-1">
                    {error}
                </p>
            )}
        </div>
    );
}