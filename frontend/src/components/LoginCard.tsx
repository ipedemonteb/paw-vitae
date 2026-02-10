import React, {useState} from "react";
import { Button } from "@/components/ui/button";
import { Mail, Lock, Eye, EyeOff, AlertCircle } from "lucide-react";
import { useAuth } from "@/hooks/useAuth";
import { useTranslation } from "react-i18next";
import {Link, useLocation, useNavigate} from "react-router-dom";
import { getClaims } from "@/context/auth-store";

const cardContainer = "flex flex-col items-center justify-center p-8 md:p-12 bg-white w-full md:w-1/2 rounded-r-xl md:h-160 border-r border-y border-(--gray-200)";
const headerSection = "text-center space-y-2 mb-8";
const headerTitle = "text-3xl font-bold text-(--text-color)";
const headerSubtitle = "text-(--text-color) text-sm";
const formStyles = "flex flex-col gap-4 w-full max-w-sm";
const relativeWrapper = "relative";
const leftIconStyles = "absolute left-3 top-1/2 -translate-y-1/2 text-(--gray-400)";
const baseInputStyles = "w-full pl-10 py-2 bg-(--gray-50) border border-(--gray-200) rounded-lg focus:outline-none focus:ring-2 focus:ring-(--primary-color) focus:border-transparent transition-all";
const emailInputStyles = `${baseInputStyles} pr-4`;
const passwordInputStyles = `${baseInputStyles} pr-12`;
const eyeButtonStyles = "absolute right-3 top-1/2 -translate-y-1/2 text-(--gray-400) hover:text-(--gray-600)";
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

    const { t } = useTranslation();
    const { login } = useAuth();
    const [showPassword, setShowPassword] = useState(false);
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [rememberMe, setRememberMe] = useState(false);
    const location = useLocation();
    const navigate = useNavigate();
    const from = location.state?.from?.pathname;
    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        login.mutate({ email, password, rememberMe: rememberMe }, {
            onSuccess: () => {

                const claims = getClaims();
                const role = claims?.role?.toUpperCase();

                if (from) {
                    navigate(from, { replace: true });
                    return;
                }

                if (role === "ROLE_DOCTOR") {
                    navigate("/doctor/dashboard", { replace: true });
                    return;
                }

                if (role === "ROLE_PATIENT") {
                    navigate("/patient/dashboard", { replace: true });
                    return;
                }

                navigate("/", { replace: true });
            }
        });
    };

    return (
        <div className={cardContainer}>
            <div className={headerSection}>
                <h2 className={headerTitle}>{t('login.login_title')}</h2>
                <p className={headerSubtitle}>{t('login.login_subtitle')}</p>
            </div>

            <form className={formStyles} onSubmit={handleSubmit}>
                <div className={relativeWrapper}>
                    <div className={leftIconStyles}><Mail size={16} /></div>
                    <input
                        type="email"
                        placeholder={t('login.placeholder_email')}
                        className={emailInputStyles}
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        disabled={login.isPending}
                        aria-label={t('login.aria_email')}
                    />
                </div>

                <div className={relativeWrapper}>
                    <div className={leftIconStyles}><Lock size={16} /></div>
                    <input
                        type={showPassword ? "text" : "password"}
                        placeholder={t('login.placeholder_password')}
                        className={passwordInputStyles}
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        disabled={login.isPending}
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
                        <input type="checkbox" className={checkboxStyles}  onClick={() => setRememberMe(!rememberMe)} />
                        <span>{t('login.remember_me')}</span>
                    </label>
                    <Link to="/recover-password" className={forgotLinkStyles}>
                        {t('login.forgot_password')}
                    </Link>
                </div>

                {login.isError && (
                    <div className="bg-red-50 border border-(--danger-light) text-(--danger) text-sm px-4 py-3 rounded-md flex items-center gap-2 animate-in fade-in slide-in-from-top-1">
                        <AlertCircle className="h-4 w-4 shrink-0" />
                        <span>
                            {login.failureReason?.message
                                ? t(login.failureReason.message)
                                : t('login.error_generic')
                            }
                        </span>
                    </div>
                )}

                <Button type="submit" className={submitButtonStyles} disabled={login.isPending}>
                    {login.isPending ? t('login.logging_in') : t('login.button_login')}
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