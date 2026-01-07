import WelcomeCard from "../components/WelcomeCard.tsx";
import LoginCard from "../components/LoginCard.tsx";

const loginPage =
    "mt-25 bg-[var(--background-light)] px-5 flex items-center justify-center";
const loginContainer =
    "flex flex-col md:flex-row my-8 max-w-6xl w-full bg-white rounded-lg";

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