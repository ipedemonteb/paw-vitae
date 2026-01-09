import { useTranslation } from "react-i18next";
import {Stethoscope, BriefcaseMedical, CalendarCheck, ShieldHalf } from "lucide-react";
import type { LucideIcon } from "lucide-react";

const loginWelcome = "bg-[var(--primary-color)] rounded-t-lg md:rounded-l-lg md:rounded-tr-[0] w-full md:w-1/2 md:py-6 lg:py-10 hidden md:block";
const welcomeContent = "flex flex-col h-full justify-center text-white px-12 mb-6";
const welcomeIcon = "w-18 h-18 text-white bg-[var(--bubble-color)] rounded-full p-3 flex items-center justify-center mb-8";
const welcomeTitle = "text-3xl font-bold mb-2 tracking-wide";
const welcomeText = "mb-6";
const welcomeFeatures = "flex flex-col gap-6";

function WelcomeCard() {
    const { t } = useTranslation();
    return (
        <div className={loginWelcome}>
            <div className={welcomeContent}>
                <div className={welcomeIcon}>
                    <Stethoscope className="h-10 w-10" />
                </div>
                <div className={welcomeTitle}>
                    <h1>{t('login.welcome_title')}</h1>
                </div>
                <div className={welcomeText}>
                    <p>{t('login.welcome_subtitle')}</p>
                </div>
                <div className={welcomeFeatures}>
                    <FeatureComponent title={t('login.feature_title_1')} subtitle={t('login.feature_subtitle_1')} Icon={BriefcaseMedical}/>
                    <FeatureComponent title={t('login.feature_title_2')} subtitle={t('login.feature_subtitle_2')} Icon={CalendarCheck}/>
                    <FeatureComponent title={t('login.feature_title_3')} subtitle={t('login.feature_subtitle_3')} Icon={ShieldHalf}/>
                </div>
            </div>
        </div>
    )
}

const featureItem = "flex flex-row items-center bg-[var(--bubble-color)]/25 py-4 px-4 gap-3 rounded-lg";
const featureIcon = "bg-[var(--bubble-color)] rounded-full p-2";
const featureTextContainer = "";
const featureTitle = "text-base font-semibold";
const featureSubtitle = "text-sm text-white/75";

function FeatureComponent({title, subtitle, Icon}:{
    title: string;
    subtitle: string;
    Icon: LucideIcon;
}) {
    return (
        <div className={featureItem}>
            <div className={featureIcon}>
                <Icon className="w-5 h-5"></Icon>
            </div>
            <div className={featureTextContainer}>
                <h3 className={featureTitle}>{title}</h3>
                <p className={featureSubtitle}>{subtitle}</p>
            </div>
        </div>
    )
}

export default WelcomeCard