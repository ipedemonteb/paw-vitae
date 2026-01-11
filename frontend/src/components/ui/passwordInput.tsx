import { useState } from "react";
import { Eye, EyeOff, Info } from "lucide-react";
import { cn } from "@/lib/utils";

interface PasswordInputProps extends React.InputHTMLAttributes<HTMLInputElement> {
    label: string;
    tooltip?: string;
}

export function PasswordInput({ label, tooltip, id, required, className, ...props }: PasswordInputProps) {
    const [show, setShow] = useState(false);

    return (
        <div className="space-y-2 w-full"> {/* Aseguramos ancho completo aquí también */}
            <div className="flex items-center gap-2">
                <label htmlFor={id} className="text-sm font-medium text-[var(--text-color)] leading-none">
                    {label} {required && <span className="text-red-500 ml-1">*</span>}
                </label>
                {tooltip && (
                    <div className="group relative">
                        <Info className="h-4 w-4 text-[var(--text-light)] cursor-help" />
                        <span className="absolute bottom-full left-1/2 -translate-x-1/2 mb-2 w-48 p-2 bg-gray-900 text-white text-xs rounded shadow-lg opacity-0 group-hover:opacity-100 transition-opacity pointer-events-none z-20">
                            {tooltip}
                        </span>
                    </div>
                )}
            </div>

            <div className="relative w-full">
                <input
                    id={id}
                    type={show ? "text" : "password"}
                    required={required}
                    className={cn(
                        "flex h-12 w-full rounded-md border border-gray-300 bg-white px-3 py-2 text-base shadow-sm placeholder:text-gray-400 focus:outline-none focus:ring-2 focus:ring-[var(--primary-color)] focus:border-transparent disabled:cursor-not-allowed disabled:opacity-50 pr-10 transition-all",
                        className
                    )}
                    {...props}
                />
                <button
                    type="button"
                    onClick={() => setShow(!show)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 focus:outline-none p-1"
                    tabIndex={-1}
                >
                    {show ? <EyeOff className="h-5 w-5" /> : <Eye className="h-5 w-5" />}
                </button>
            </div>
        </div>
    );
}