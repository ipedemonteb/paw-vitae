import { describe, it, expect, vi, afterEach, beforeEach } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import Login from "@/pages/Login";
import { createMemoryRouter, RouterProvider } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

const mockLoginMutate = vi.fn();
const mockUseAuth = vi.fn();

vi.mock('react-i18next', () => ({
    useTranslation: () => ({ t: (key: string) => key })
}));

const mockNavigate = vi.fn();
vi.mock('react-router-dom', async () => {
    const actual = await vi.importActual('react-router-dom');
    return {
        ...actual,
        useNavigate: () => mockNavigate,
        useLocation: () => ({ state: {} }),
    };
});

vi.mock('@/hooks/useAuth', () => ({
    useAuth: () => mockUseAuth()
}));

vi.mock('@/context/auth-store', () => ({
    getClaims: () => ({ role: 'ROLE_PATIENT' })
}));

const renderLogin = () => {
    const queryClient = new QueryClient({
        defaultOptions: { queries: { retry: false, staleTime: 0 } }
    });

    const router = createMemoryRouter([
        { path: '/login', element: <Login /> }
    ], { initialEntries: ['/login'] });

    return render(
        <QueryClientProvider client={queryClient}>
            <RouterProvider router={router} />
        </QueryClientProvider>
    );
};

describe("Login Page", () => {

    beforeEach(() => {
        mockUseAuth.mockReturnValue({
            login: {
                mutate: mockLoginMutate,
                isPending: false,
                isError: false,
                failureReason: null
            }
        });
    });

    afterEach(() => {
        vi.clearAllMocks();
    });

    it("should render login form elements correctly", () => {
        renderLogin();

        expect(screen.getByText("login.welcome_title")).toBeInTheDocument();
        expect(screen.getByText("login.login_title")).toBeInTheDocument();
        expect(screen.getByPlaceholderText("login.placeholder_email")).toBeInTheDocument();
        expect(screen.getByPlaceholderText("login.placeholder_password")).toBeInTheDocument();
        expect(screen.getByRole("button", { name: "login.button_login" })).toBeInTheDocument();
    });

    it("should allow typing in email and password fields", async () => {
        const user = userEvent.setup();
        renderLogin();

        const emailInput = screen.getByPlaceholderText("login.placeholder_email");
        const passwordInput = screen.getByPlaceholderText("login.placeholder_password");

        await user.type(emailInput, "test@example.com");
        await user.type(passwordInput, "password123");

        expect(emailInput).toHaveValue("test@example.com");
        expect(passwordInput).toHaveValue("password123");
    });

    it("should toggle password visibility", async () => {
        const user = userEvent.setup();
        renderLogin();

        const passwordInput = screen.getByPlaceholderText("login.placeholder_password");
        const toggleButton = screen.getByRole("button", { name: /show password/i });
        expect(passwordInput).toHaveAttribute("type", "password");

        await user.click(toggleButton);
        expect(passwordInput).toHaveAttribute("type", "text");

        await user.click(toggleButton);
        expect(passwordInput).toHaveAttribute("type", "password");
    });

    it("should call login mutation with correct data on submit", async () => {
        const user = userEvent.setup();
        renderLogin();

        await user.type(screen.getByPlaceholderText("login.placeholder_email"), "doctor@test.com");
        await user.type(screen.getByPlaceholderText("login.placeholder_password"), "123456");

        const rememberCheckbox = screen.getByLabelText("login.remember_me");
        await user.click(rememberCheckbox);

        await user.click(screen.getByRole("button", { name: "login.button_login" }));

        expect(mockLoginMutate).toHaveBeenCalledWith({
            email: "doctor@test.com",
            password: "123456",
            rememberMe: true
        }, expect.anything());
    });

    it("should display generic error message when login fails", () => {
        mockUseAuth.mockReturnValue({
            login: {
                mutate: vi.fn(),
                isPending: false,
                isError: true,
                failureReason: null
            }
        });

        renderLogin();
        const errorMessages = screen.getAllByText("login.error_generic");
        expect(errorMessages).toHaveLength(2);

        errorMessages.forEach(msg => {
            expect(msg).toBeVisible();
        });

    });

    it("should disable submit button while loading", () => {
        mockUseAuth.mockReturnValue({
            login: {
                mutate: vi.fn(),
                isPending: true,
                isError: false
            }
        });

        renderLogin();
        const submitButton = screen.getByRole("button", { name: "login.logging_in" });
        expect(submitButton).toBeDisabled();
    });

    it("should contain correct links for recovery and registration", () => {
        renderLogin();

        const recoverLink = screen.getByText("login.forgot_password");
        expect(recoverLink).toHaveAttribute("href", "/recover-password");

        const doctorRegister = screen.getByText("login.register_doctor");
        expect(doctorRegister).toHaveAttribute("href", "/register?type=doctor");

        const patientRegister = screen.getByText("login.register_patient");
        expect(patientRegister).toHaveAttribute("href", "/register?type=patient");
    });
});