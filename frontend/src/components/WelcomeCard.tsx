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




function WelcomeCard() {
    return (
        <div className={loginWelcome}>


            <div className={welcomeContent}>

                <div className={welcomeIcon}>
                    <i className="fas fa-stethoscope"></i>
                </div>

                <div className={welcomeTitle}>
                    <h1>Bienvenido de nuevo</h1>
                </div>
                <div className={welcomeText}>
                    <p>Tu salud es nuestra prioridad. Agenda tu cita ahora!</p>
                </div>

                <div className={welcomeFeatures}>

                    <div className={featureItem}>
                        <div className={featureIcon}>
                            <i className="fas fa-user-md"></i>
                        </div>
                        <div className={featureTextContainer}>
                            <h3 className={featureTitle}>Médicos Expertos</h3>
                            <p className={featureSubtitle}>Conecta con profesionales verificados</p>
                        </div>
                    </div>

                    <div className={featureItem}>
                        <div className={featureIcon}>
                            <i className="fas fa-calendar-check"></i>
                        </div>
                        <div className={featureTextContainer}>
                            <h3 className={featureTitle}>Citas Fáciles</h3>
                            <p className={featureSubtitle}>Reserva y gestiona tus citas en línea</p>
                        </div>
                    </div>

                    <div className={featureItem}>
                        <div className={featureIcon}>
                            <i className="fas fa-shield-alt"></i>
                        </div>
                        <div className={featureTextContainer}>
                            <h3 className={featureTitle}>Plataforma Segura</h3>
                            <p className={featureSubtitle}>Tus datos están protegidos y privados</p>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    )
}

export default WelcomeCard