import WelcomeCard from "../components/WelcomeCard.tsx";
import LoginCard from "../components/LoginCard.tsx";

const loginPage =
    "bg-(--background-light) px-5 flex justify-center items-start min-h-screen";
const loginContainer =
    "mt-36 flex flex-col md:flex-row my-8 max-w-6xl w-full bg-white rounded-xl items-stretch shadow-[var(--shadow-xl)]";

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