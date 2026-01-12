import { Check } from "lucide-react";

export function RequirementItem({ isValid, text }: { isValid: boolean; text: string }) {
    return (
        <p className={`text-sm flex items-center gap-2 mt-1.5 font-medium transition-colors duration-200 ${isValid ? "text-green-600" : "text-gray-500"}`}>
            {isValid ? (
                <Check className="h-4 w-4 text-green-600 flex-shrink-0" />
            ) : (
                <div className="h-1.5 w-1.5 rounded-full bg-gray-300 mx-1.5 flex-shrink-0" />
            )}
            {text}
        </p>
    );
}