import WelcomeCard from "../../components/WelcomeCard";
import LoginContainer from "../../components/LoginContainer.tsx";
import LoginCard from "../../components/LoginCard.tsx";

const loginPage =
    "flex-1 pt-[130px] pb-[30px] flex items-center justify-center bg-gray-100 min-h-[calc(100vh-100px)]";

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