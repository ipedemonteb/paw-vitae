import WelcomeCard from "../../components/WelcomeCard";
import LoginContainer from "../../components/LoginContainer.tsx";
import LoginCard from "../../components/LoginCard.tsx";

const loginPage ="flex-1 flex items-center justify-center bg-gray-100 p-4 min-h-[calc(100vh-100px)]";

function Login() {

    return (
        <div className="flex flex-col min-h-screen">
            <div className={loginPage}>
                <LoginContainer>
                    <WelcomeCard />
                    <LoginCard />
                </LoginContainer>
            </div>
        </div>
    )
}

export default Login;