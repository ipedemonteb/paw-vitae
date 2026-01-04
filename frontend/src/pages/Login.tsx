import WelcomeCard from "../components/WelcomeCard.tsx";
import LoginContainer from "../components/LoginContainer.tsx";
import LoginCard from "../components/LoginCard.tsx";

const loginPage =
    "mt-25 flex-1 py-[30px] flex items-center justify-center bg-gray-100 min-h-[calc(100vh-100px)]";

function Login() {

    return (
        <div className={loginPage}>
            <LoginContainer>
                <WelcomeCard />
                <LoginCard />
            </LoginContainer>
        </div>
    )
}

export default Login;