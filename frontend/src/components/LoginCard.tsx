import React, { useState } from "react";
import { Button } from "@/components/ui/button";
import { Mail, Lock, Eye, EyeOff } from "lucide-react";
import { useAuth } from "@/context/authContext.tsx";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";

const cardContainer = "flex flex-col items-center justify-center p-8 md:p-12 bg-white w-full rounded-xl md:flex-1";
const headerSection = "text-center space-y-2 mb-8";
const headerTitle = "text-3xl font-bold text-[var(--text-color)]";
const headerSubtitle = "text-[var(--text-color)] text-sm";
const formStyles = "flex flex-col gap-4 w-full max-w-sm";
const relativeWrapper = "relative";
const leftIconStyles = "absolute left-3 top-1/2 -translate-y-1/2 text-gray-400";
const baseInputStyles = "w-full pl-10 py-2 bg-gray-50 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all";
const emailInputStyles = `${baseInputStyles} pr-4`;
const passwordInputStyles = `${baseInputStyles} pr-12`;
const eyeButtonStyles = "absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600";
const optionsRow = "flex items-center justify-between text-sm";
const checkboxLabel = "flex items-center gap-2 cursor-pointer text-gray-600";
const checkboxStyles = "w-4 h-4 rounded border-gray-300 text-[var(--primary-color)] focus:ring-blue-500";
const forgotLinkStyles = "text-[var(--primary-color)] font-medium hover:underline";
const submitButtonStyles = "w-full text-base py-4 font-semibold mt-3 bg-[var(--primary-color)] hover:bg-[var(--primary-dark)] cursor-pointer";
const footerSection = "flex flex-col gap-2 mt-6 text-center";
const footerText = "text-sm text-gray-500";
const linksContainer = "flex flex-col gap-1 text-sm font-medium";
const registerLinkStyles = "text-[var(--primary-color)] hover:text-[var(--primary-dark)] hover:underline";


function LoginCard() {
    const [showPassword, setShowPassword] = useState(false);
    const { login } = useAuth();
    const navigate = useNavigate();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [isLoading, setIsLoading] = useState(false);

    const { t } = useTranslation();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (isLoading) return;
        setIsLoading(true);
        setError("");

        try {
            const success = await login(email, password);
            if (success) {
                navigate("/");
            } else {
                setError(t('login.error_credentials'));
            }
        } catch (err) {
            setError(t('login.error_generic'));
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className={cardContainer}>
            <div className={headerSection}>
                <h2 className={headerTitle}>{t('login.login_title')}</h2>
                <p className={headerSubtitle}>
                    {t('login.login_subtitle')}
                </p>
            </div>
            <form className={formStyles} onSubmit={handleSubmit}>
                <div className={relativeWrapper}>
                    <div className={leftIconStyles}>
                        <Mail size={16} />
                    </div>
                    <input
                        type="email"
                        placeholder={t('login.placeholder_email')}
                        className={emailInputStyles}
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        aria-label={t('login.aria_email')}
                    />
                </div>
                <div className={relativeWrapper}>
                    <div className={leftIconStyles}>
                        <Lock size={16} />
                    </div>
                    <input
                        type={showPassword ? "text" : "password"}
                        placeholder={t('login.placeholder_password')}
                        className={passwordInputStyles}
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        aria-label={t('login.aria_password')}
                    />
                    <button
                        type="button"
                        onClick={() => setShowPassword(!showPassword)}
                        className={eyeButtonStyles}
                        aria-label={showPassword ? t('login.hide_password') : t('login.show_password')}
                    >
                        {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
                    </button>
                </div>
                <div className={optionsRow}>
                    <label className={checkboxLabel}>
                        <input type="checkbox" className={checkboxStyles} />
                        <span>{t('login.remember_me')}</span>
                    </label>
                    <a href="#" className={forgotLinkStyles}>
                        {t('login.forgot_password')}
                    </a>
                </div>
                {error && <div className="text-red-500 text-sm">{error}</div>}
                <Button type="submit" className={submitButtonStyles} disabled={isLoading}>
                    {isLoading ? t('login.logging_in') : t('login.button_login')}
                </Button>
            </form>
            <div className={footerSection}>
                <p className={footerText}>{t('login.no_account')}</p>
                <div className={linksContainer}>
                    <Link to="/register?type=doctor" className={registerLinkStyles}>
                        {t('login.register_doctor')}
                    </Link>
                    <Link to="/register?type=patient" className={registerLinkStyles}>
                        {t('login.register_patient')}
                    </Link>
                </div>
            </div>
        </div>
    );
}

export default LoginCard;