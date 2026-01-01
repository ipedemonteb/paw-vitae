import { Check } from "lucide-react";

interface Step {
    id: number;
    label: string;
    icon?: any;
}

interface FormStepperProps {
    currentStep: number;
    steps: Step[];
}

export function FormStepper({ currentStep, steps }: FormStepperProps) {
    return (
        <div className="flex justify-between mb-10 relative px-4">
            <div className="absolute top-1/2 left-0 w-full h-0.5 bg-gray-200 -z-10 -translate-y-1/2 rounded-full"></div>
            {steps.map((s) => (
                <div key={s.id} className="flex flex-col items-center gap-2 bg-white px-2">
                    <div className={`w-10 h-10 rounded-full flex items-center justify-center text-sm font-bold border-2 transition-colors 
                    ${currentStep === s.id
                        ? 'bg-blue-600 border-blue-600 text-white'
                        : currentStep > s.id
                            ? 'bg-green-500 border-green-500 text-white'
                            : 'bg-white border-gray-300 text-gray-400'
                    }`}>
                        {currentStep > s.id ? <Check className="h-5 w-5" /> : s.id}
                    </div>
                    <span className={`text-xs font-medium ${currentStep === s.id ? 'text-blue-600' : 'text-gray-500'}`}>
                        {s.label}
                    </span>
                </div>
            ))}
        </div>
    );
}