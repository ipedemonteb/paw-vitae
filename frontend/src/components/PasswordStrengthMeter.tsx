import { Check } from "lucide-react"
import { useTranslation } from "react-i18next"
import { useEffect, useState } from "react"

function RequirementItem({ isValid, text }: { isValid: boolean; text: string }) {
    return (
        <div className={`text-xs flex items-center gap-2 mt-1 font-medium transition-colors duration-200 ${isValid ? "text-(--success)" : "text-(--gray-500)"}`}>
            {isValid ? (
                <Check className="h-3.5 w-3.5 text-(--success) shrink-0" />
            ) : (
                <div className="h-1.5 w-1.5 rounded-full bg-(--gray-300) mx-1 shrink-0" />
            )}
            {text}
        </div>
    );
}

interface PasswordStrengthMeterProps {
    password: string;
}

export function PasswordStrengthMeter({ password }: PasswordStrengthMeterProps) {
    const { t } = useTranslation();

    const [strength, setStrength] = useState(0);
    const [checks, setChecks] = useState({
        minLength: false,
        uppercase: false,
        number: false
    });

    useEffect(() => {
        let score = 0;
        if (password.length >= 8) score++;
        if (password.length >= 12) score++;
        if (/[A-Z]/.test(password)) score++;
        if (/[0-9]/.test(password)) score++;
        if (/[^A-Za-z0-9]/.test(password)) score++;

        setStrength(score);
        setChecks({
            minLength: password.length >= 8,
            uppercase: /[A-Z]/.test(password),
            number: /[0-9]/.test(password)
        });
    }, [password]);

    const getStrengthStyles = () => {
        if (!password) return { width: "0%", color: "bg-(--gray-200)", label: "" };
        if (strength < 2) return { width: "20%", color: "bg-(--danger)", label: t("change_password.strength.weak") };
        if (strength < 4) return { width: "60%", color: "bg-(--warning)", label: t("change_password.strength.medium") };
        return { width: "100%", color: "bg-(--success)", label: t("change_password.strength.strong") };
    };

    const style = getStrengthStyles();

    return (
        <div className="mt-2">
            <div className={`transition-all duration-300 ${password ? 'opacity-100 max-h-24' : 'opacity-0 max-h-0 overflow-hidden'}`}>
                <div className="flex justify-between text-xs mb-1">
                    <span className="text-(--gray-500) font-medium">{t("register.strength")}</span>
                    <span className="text-gray-900 font-bold">{style.label}</span>
                </div>
                <div className="w-full h-1.5 bg-(--gray-100) rounded-full overflow-hidden">
                    <div
                        className={`h-full transition-all duration-500 ease-out ${style.color}`}
                        style={{ width: style.width }}
                    />
                </div>
            </div>

            <div className="mt-3 space-y-1 pl-1">
                <RequirementItem
                    isValid={checks.minLength}
                    text={t("change_password.req_length") || "Mínimo 8 caracteres"}
                />
                <RequirementItem
                    isValid={checks.uppercase}
                    text={t("change_password.req_uppercase") || "Una mayúscula"}
                />
                <RequirementItem
                    isValid={checks.number}
                    text={t("change_password.req_number") || "Un número"}
                />
            </div>
        </div>
    );
}