const loginWelcome = "flex-1 relative flex flex-col justify-center p-8 md:p-12 text-white bg-blue-600 overflow-hidden min-h-[600px]";

const welcomeContent = "relative z-10 flex flex-col gap-6";

const welcomeIcon = "w-16 h-16 flex items-center justify-center bg-white/20 rounded-full text-3xl mb-2";

const welcomeTitle = "text-4xl font-bold leading-tight select-none";
const welcomeText = "text-lg font-medium opacity-90 select-none";

const welcomeFeatures = "flex flex-col gap-4 mt-4";

const featureItem = "flex items-center gap-4 p-4 rounded-xl bg-white/10 hover:bg-white/20 transition-colors backdrop-blur-sm border border-white/5";

const featureIcon = "flex-shrink-0 flex items-center justify-center w-12 h-12 bg-white/20 rounded-full text-xl";

const featureTextContainer = "flex flex-col";
const featureTitle = "font-bold text-base select-none";
const featureSubtitle = "text-sm opacity-80 select-none";

import { useTranslation } from "react-i18next";



function WelcomeCard() {
    const { t } = useTranslation();
    return (
        <div className={loginWelcome}>


            <div className={welcomeContent}>

                <div className={welcomeIcon}>
                    <i className="fas fa-stethoscope"></i>
                </div>

                <div className={welcomeTitle}>
                    <h1>{t('login.welcome_title')}</h1>
                </div>
                <div className={welcomeText}>
                    <p>{t('login.welcome_subtitle')}</p>
                </div>

                <div className={welcomeFeatures}>

                    <div className={featureItem}>
                        <div className={featureIcon}>
                            <i className="fas fa-user-md"></i>
                        </div>
                        <div className={featureTextContainer}>
                            <h3 className={featureTitle}>{t('login.feature_title_1')}</h3>
                            <p className={featureSubtitle}>{t('login.feature_subtitle_1')}</p>
                        </div>
                    </div>

                    <div className={featureItem}>
                        <div className={featureIcon}>
                            <i className="fas fa-calendar-check"></i>
                        </div>
                        <div className={featureTextContainer}>
                            <h3 className={featureTitle}>{t('login.feature_title_2')}</h3>
                            <p className={featureSubtitle}>{t('login.feature_subtitle_2')}</p>
                        </div>
                    </div>

                    <div className={featureItem}>
                        <div className={featureIcon}>
                            <i className="fas fa-shield-alt"></i>
                        </div>
                        <div className={featureTextContainer}>
                            <h3 className={featureTitle}>{t('login.feature_title_3')}</h3>
                            <p className={featureSubtitle}>{t('login.feature_subtitle_3')}</p>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    )
}

export default WelcomeCard