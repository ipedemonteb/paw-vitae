import React, {useState} from "react";
import {Button} from "@/components/ui/button";
import {Mail, Lock} from "lucide-react";
import {useAuth} from "@/hooks/useAuth";
import {useTranslation} from "react-i18next";
import {Link, useLocation, useNavigate} from "react-router-dom";
import {getClaims} from "@/context/auth-store";
import {FormInput} from "@/components/FormInput.tsx";
import {PasswordInput} from "@/components/PasswordInput.tsx";

const cardContainer = "flex flex-col items-center justify-center p-8 md:p-12 bg-white w-full md:w-1/2 rounded-r-xl md:h-160 border-r border-y border-(--gray-200)";
const headerSection = "text-center space-y-2 mb-8";
const headerTitle = "text-3xl font-bold text-(--text-color)";
const headerSubtitle = "text-(--text-color) text-sm";
const formStyles = "flex flex-col gap-4 w-full max-w-sm";
const fieldWrapper = "relative w-full";
const optionsRow = "flex items-center justify-between text-sm mb-1";
const checkboxLabel = "flex items-center gap-2 cursor-pointer text-(--gray-600)";
const checkboxStyles = "w-4 h-4 rounded border-(--gray-300) text-[var(--primary-color)] focus:ring-(--primary-color)";
const forgotLinkStyles = "text-(--primary-color) font-[400] hover:underline hover:text-(--primary-dark)";
const submitButtonStyles = "w-full text-base py-4 font-semibold mt-3 bg-[var(--primary-color)] hover:bg-[var(--primary-dark)] cursor-pointer";
const footerSection = "flex flex-col gap-2 mt-6 text-center";
const footerText = "text-sm text-(--gray-500)";
const linksContainer = "flex flex-col gap-1 text-sm font-medium";
const registerLinkStyles = "text-[var(--primary-color)] hover:text-[var(--primary-dark)] hover:underline";

function LoginCard() {
    const {t} = useTranslation();
    const {login} = useAuth();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [rememberMe, setRememberMe] = useState(false);
    const [errors, setErrors] = useState<{email?: string; password?: string}>({});
    const location = useLocation();
    const navigate = useNavigate();
    const from = location.state?.from?.pathname;

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        const newErrors: {email?: string; password?: string} = {};
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
        if (!emailRegex.test(email)) newErrors.email = t("recover.error_invalid");
        if (!email) newErrors.email = t("register.errors.required");
        if (!password) newErrors.password = t("register.errors.required");
        if (Object.keys(newErrors).length > 0) return setErrors(newErrors);

        setErrors({});
        login.mutate({email, password, rememberMe}, {
            onSuccess: () => {
                const claims = getClaims();
                const role = claims?.role?.toUpperCase();
                if (from) return navigate(from, {replace: true});
                if (role === "ROLE_DOCTOR") return navigate("/doctor/dashboard", {replace: true});
                if (role === "ROLE_PATIENT") return navigate("/patient/dashboard", {replace: true});
                navigate("/", {replace: true});
            },
            onError: () => {
                setErrors({
                    email: " ",
                    password: t("login.error_generic")
                });
            }
        });
    };

    return (
        <div className={cardContainer}>
            <div className={headerSection}>
                <h2 className={headerTitle}>{t("login.login_title")}</h2>
                <p className={headerSubtitle}>{t("login.login_subtitle")}</p>
            </div>

            <form className={formStyles} onSubmit={handleSubmit} noValidate>
                <div className={fieldWrapper}>
                    <FormInput
                        id="email"
                        name="email"
                        type="email"
                        label={t("login.aria_email")}
                        hideLabel
                        icon={<Mail size={16} />}
                        placeholder={t("login.placeholder_email")}
                        value={email}
                        onChange={(e) => { setEmail(e.target.value); if (errors.email) setErrors(p => ({...p, email: undefined})); }}
                        required
                        disabled={login.isPending}
                        error={errors.email}
                    />
                </div>

                <div className={fieldWrapper}>
                    <PasswordInput
                        id="password"
                        name="password"
                        label={t("login.aria_password")}
                        hideLabel
                        icon={<Lock size={16} />}
                        placeholder={t("login.placeholder_password")}
                        value={password}
                        onChange={(e) => { setPassword(e.target.value); if (errors.password) setErrors(p => ({...p, password: undefined})); }}
                        required
                        disabled={login.isPending}
                        error={errors.password}
                    />
                </div>

                <div className={optionsRow}>
                    <label className={checkboxLabel}>
                        <input type="checkbox" className={checkboxStyles} checked={rememberMe} onChange={(e) => setRememberMe(e.target.checked)} />
                        <span>{t("login.remember_me")}</span>
                    </label>
                    <Link to="/recover-password" className={forgotLinkStyles}>{t("login.forgot_password")}</Link>
                </div>

                <Button type="submit" className={submitButtonStyles} disabled={login.isPending}>
                    {login.isPending ? t("login.logging_in") : t("login.button_login")}
                </Button>
            </form>

            <div className={footerSection}>
                <p className={footerText}>{t("login.no_account")}</p>
                <div className={linksContainer}>
                    <Link to="/register?type=doctor" className={registerLinkStyles}>{t("login.register_doctor")}</Link>
                    <Link to="/register?type=patient" className={registerLinkStyles}>{t("login.register_patient")}</Link>
                </div>
            </div>
        </div>
    );
}

export default LoginCard;