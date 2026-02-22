import { describe, it, expect, vi, afterEach, beforeEach } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { createMemoryRouter, RouterProvider } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import RecoverPassword from "@/pages/ResetPassword.tsx";

const mockMutate = vi.fn();
const mockUseUserMutation = vi.fn();

vi.mock('react-i18next', () => ({
    useTranslation: () => ({ t: (key: string, ) => key })
}));

vi.mock('@/hooks/useUser', () => ({
    useUserMutation: () => mockUseUserMutation()
}));

const renderRecoverPassword = () => {
    const queryClient = new QueryClient({
        defaultOptions: { queries: { retry: false, staleTime: 0 } }
    });

    const router = createMemoryRouter([
        { path: '/recover-password', element: <RecoverPassword /> }
    ], { initialEntries: ['/recover-password'] });

    return render(
        <QueryClientProvider client={queryClient}>
            <RouterProvider router={router} />
        </QueryClientProvider>
    );
};

describe("RecoverPassword Page", () => {

    beforeEach(() => {
        mockUseUserMutation.mockReturnValue({
            mutate: mockMutate,
            isPending: false,
            isSuccess: false,
            isError: false
        });
    });

    afterEach(() => {
        vi.clearAllMocks();
    });

    it("should render form elements correctly", () => {
        renderRecoverPassword();

        expect(screen.getByRole("heading", { name: "recover.title" })).toBeInTheDocument();
        expect(screen.getByPlaceholderText("login.placeholder_email")).toBeInTheDocument();
        expect(screen.getByRole("button", { name: "recover.button_send" })).toBeInTheDocument();
    });

    it("should show validation error if email is empty", async () => {
        const user = userEvent.setup();
        renderRecoverPassword();

        const submitBtn = screen.getByRole("button", { name: "recover.button_send" });
        await user.click(submitBtn);

        expect(screen.getByText("recover.error_required")).toBeInTheDocument();
        expect(mockMutate).not.toHaveBeenCalled();
    });

    it("should show validation error if email format is invalid", async () => {
        const user = userEvent.setup();
        renderRecoverPassword();

        const emailInput = screen.getByPlaceholderText("login.placeholder_email");
        await user.type(emailInput, "invalid-email");

        const submitBtn = screen.getByRole("button", { name: "recover.button_send" });
        await user.click(submitBtn);

        expect(screen.getByText("recover.error_invalid")).toBeInTheDocument();
        expect(mockMutate).not.toHaveBeenCalled();
    });

    it("should call mutate with valid email", async () => {
        const user = userEvent.setup();
        renderRecoverPassword();

        const emailInput = screen.getByPlaceholderText("login.placeholder_email");
        await user.type(emailInput, "valid@test.com");

        const submitBtn = screen.getByRole("button", { name: "recover.button_send" });
        await user.click(submitBtn);

        expect(mockMutate).toHaveBeenCalledWith({ email: "valid@test.com" });
    });

    it("should show success view when isSuccess is true", () => {
        mockUseUserMutation.mockReturnValue({
            mutate: vi.fn(),
            isPending: false,
            isSuccess: true,
            isError: false
        });

        renderRecoverPassword();

        expect(screen.getByText("recover.success_title")).toBeInTheDocument();
        expect(screen.queryByPlaceholderText("recover.placeholder_email")).not.toBeInTheDocument();
    });

    it("should show error message when API call fails", () => {
        mockUseUserMutation.mockReturnValue({
            mutate: vi.fn(),
            isPending: false,
            isSuccess: false,
            isError: true
        });

        renderRecoverPassword();
        expect(screen.getByText("recover.error_generic")).toBeInTheDocument();
    });

    it("should show loading state when pending", () => {
        mockUseUserMutation.mockReturnValue({
            mutate: vi.fn(),
            isPending: true,
            isSuccess: false,
            isError: false
        });

        renderRecoverPassword();
        expect(screen.getByRole("button")).toBeDisabled();
        expect(screen.getByText("loading")).toBeInTheDocument();
    });
});