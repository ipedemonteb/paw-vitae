import WelcomeCard from "../components/WelcomeCard.tsx";
import LoginCard from "../components/LoginCard.tsx";

const loginPage =
    "bg-[var(--background-light)] px-5 flex justify-center items-start min-h-screen";
const loginContainer =
    "mt-32 flex flex-col md:flex-row my-8 max-w-6xl w-full bg-white rounded-lg items-stretch";

function Login() {

    return (
        <div className={loginPage}>
            <div className={loginContainer}>
                <WelcomeCard />
                <LoginCard />
            </div>
        </div>
    )
}

export default Login;