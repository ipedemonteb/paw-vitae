import { useState } from "react";
import { Eye, EyeOff, Info } from "lucide-react";

interface PasswordInputProps extends React.InputHTMLAttributes<HTMLInputElement> {
    label: string;
    tooltip?: string;
}

export function PasswordInput({ label, tooltip, id, required, ...props }: PasswordInputProps) {
    const [show, setShow] = useState(false);

    return (
        <div className="space-y-2">
            <div className="flex items-center gap-2">
                <label htmlFor={id} className="text-sm font-medium text-gray-700">
                    {label} {required && <span className="text-red-500">*</span>}
                </label>
                {tooltip && (
                    <div className="group relative">
                        <Info className="h-4 w-4 text-gray-400 cursor-help" />
                        <span className="absolute bottom-full left-1/2 -translate-x-1/2 mb-2 w-48 p-2 bg-gray-800 text-white text-xs rounded opacity-0 group-hover:opacity-100 transition-opacity pointer-events-none z-10">
                            {tooltip}
                        </span>
                    </div>
                )}
            </div>
            <div className="relative">
                <input
                    id={id}
                    type={show ? "text" : "password"}
                    required={required}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    {...props}
                />
                <button
                    type="button"
                    onClick={() => setShow(!show)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-500 hover:text-gray-700"
                    tabIndex={-1}
                >
                    {show ? <EyeOff className="h-4 w-4" /> : <Eye className="h-4 w-4" />}
                </button>
            </div>
        </div>
    );
}