// TypeScript
// frontend/src/components/LoginCard.tsx
import React, { useState } from "react";
import { Button } from "@/components/ui/button";
import { Mail, Lock, Eye, EyeOff } from "lucide-react";
import { useAuth } from "@/context/authContext.tsx";
import { useNavigate } from "react-router-dom";
import {useTranslation} from "react-i18next";

const cardContainer = "flex-1 flex flex-col items-center justify-center p-8 md:p-12 bg-white w-full h-full";
const contentWrapper = "w-full max-w-sm space-y-8";
const headerSection = "text-center space-y-2";
const headerTitle = "text-3xl font-bold text-gray-900";
const headerSubtitle = "text-gray-500 text-sm";

const formStyles = "space-y-6";
const inputGroup = "space-y-2";
const relativeWrapper = "relative";
const leftIconStyles = "absolute left-3 top-1/2 -translate-y-1/2 text-gray-400";
const baseInputStyles = "w-full pl-10 py-3 bg-gray-50 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all";
const emailInputStyles = `${baseInputStyles} pr-4`;
const passwordInputStyles = `${baseInputStyles} pr-12`;
const eyeButtonStyles = "absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600";

const optionsRow = "flex items-center justify-between text-sm";
const checkboxLabel = "flex items-center gap-2 cursor-pointer text-gray-600";
const checkboxStyles = "w-4 h-4 rounded border-gray-300 text-blue-600 focus:ring-blue-500";
const forgotLinkStyles = "text-blue-600 font-medium hover:underline";

const submitButtonStyles = "w-full h-12 text-base font-semibold bg-blue-600 hover:bg-blue-700 shadow-lg shadow-blue-600/20";

const footerSection = "space-y-4 pt-4 border-t border-gray-100 text-center";
const footerText = "text-sm text-gray-500";
const linksContainer = "flex flex-col gap-2 text-sm font-medium";
const registerLinkStyles = "text-blue-600 hover:text-blue-700 hover:underline";

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
                setError("Credenciales inválidas");
            }
        } catch (err) {
            setError("Error en el inicio de sesión");
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className={cardContainer}>
            <div className={contentWrapper}>

                <div className={headerSection}>
                    <h2 className={headerTitle}>{t('login.login_title')}</h2>
                    <p className={headerSubtitle}>
                        {t('login.login_subtitle')}
                    </p>
                </div>

                <form className={formStyles} onSubmit={handleSubmit}>

                    <div className={inputGroup}>
                        <div className={relativeWrapper}>
                            <div className={leftIconStyles}>
                                <Mail size={20} />
                            </div>
                            <input
                                type="email"
                                placeholder={t('login.placeholder_email')}
                                className={emailInputStyles}
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                                aria-label="Correo electrónico"
                            />
                        </div>
                    </div>

                    <div className={inputGroup}>
                        <div className={relativeWrapper}>
                            <div className={leftIconStyles}>
                                <Lock size={20} />
                            </div>
                            <input
                                type={showPassword ? "text" : "password"}
                                placeholder={t('login.placeholder_password')}
                                className={passwordInputStyles}
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                                aria-label="Contraseña"
                            />
                            <button
                                type="button"
                                onClick={() => setShowPassword(!showPassword)}
                                className={eyeButtonStyles}
                                aria-label={showPassword ? "Ocultar contraseña" : "Mostrar contraseña"}
                            >
                                {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
                            </button>
                        </div>
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
                        {isLoading ? "Ingresando..." : "Login"}
                    {/*    TODO: HOW TO TRANSLATE THIS?*/}
                    </Button>

                </form>

                <div className={footerSection}>
                    <p className={footerText}>{t('login.no_account')}</p>

                    <div className={linksContainer}>
                        <a href="/register-doctor" className={registerLinkStyles}>
                            {t('login.register_doctor')}
                        </a>
                        <a href="/register-patient" className={registerLinkStyles}>
                            {t('login.register_patient')}
                        </a>
                    </div>
                </div>

            </div>
        </div>
    );
}

export default LoginCard;