import { describe, it, expect, vi, beforeEach } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import RegisterPage from "@/pages/Register";
import { createMemoryRouter, RouterProvider } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import {BASE_URL} from "@/__test__/utils/utils.ts";
import {act} from "react";

vi.mock('react-i18next', () => ({
    useTranslation: () => ({ t: (key: string) => key })
}));

const mockRegisterPatient = vi.fn();
const mockRegisterDoctor = vi.fn();

vi.mock("@/hooks/usePatients", () => ({
    useRegisterPatientMutation: () => ({
        mutate: mockRegisterPatient,
        isPending: false
    })
}));

vi.mock("@/hooks/useDoctors", () => ({
    useRegisterDoctorMutation: () => ({
        mutate: mockRegisterDoctor,
        isPending: false
    })
}));

vi.mock("@/hooks/useCoverages", () => ({
    useCoverages: () => ({
        data: [
            { self: `${BASE_URL}/coverages/1`, name: "Osde" },
            { self: `${BASE_URL}/coverages/2`, name: "Swiss Medical" }
        ],
        isLoading: false
    })
}));

vi.mock("@/hooks/useSpecialties", () => ({
    useSpecialties: () => ({
        data: [
            { self: `${BASE_URL}/specialties/1`, name: "Cardiologia" },
            { self: `${BASE_URL}/specialties/2`, name: "Pediatria" }
        ],
        isLoading: false
    })
}));

vi.mock("@/components/ui/neighborhood-form-combobox.tsx", () => ({
    NeighborhoodFormCombobox: ({ onChange, value, error }: any) => (
        <div>
            <label>register.label_neighborhood</label>
            <input
                data-testid="neighborhood-mock"
                value={value || ""}
                onChange={(e) => onChange({ self: e.target.value, name: "Palermo" })}
            />
            {error && <span>{error}</span>}
        </div>
    )
}));




vi.mock("@/components/PasswordInput", () => ({
    PasswordInput: ({ label, id, ...props }: any) => (
        <div>
            <label htmlFor={id}>{label}</label>
            <input id={id} type="password" {...props} />
        </div>
    )
}));
vi.mock("@/components/PasswordStrengthMeter.tsx", () => ({
    PasswordStrengthMeter: () => <div data-testid="strength-meter" />
}));



const renderRegister = (initialEntries = ['/register']) => {
    const queryClient = new QueryClient({
        defaultOptions: { queries: { retry: false } }
    });

    const router = createMemoryRouter([
        { path: '/register', element: <RegisterPage /> },
        { path: '/login', element: <div>Login Page Mock</div> }
    ], { initialEntries });

    return render(
        <QueryClientProvider client={queryClient}>
            <RouterProvider router={router} />
        </QueryClientProvider>
    );
};

describe("Register Page", () => {

    beforeEach(() => {
        vi.clearAllMocks();
    });

    it("should render patient form by default and toggle to doctor form", async () => {
        const user = userEvent.setup();
        renderRegister();

        expect(screen.getByText("register.subtitle_register")).toBeInTheDocument();
        expect(screen.getByRole("button", { name: "register.register_im_doctor" })).toBeInTheDocument();

        const toggleBtn = screen.getByRole("button", { name: "register.register_im_doctor" });
        await user.click(toggleBtn);

        expect(screen.getByText("register.subtitle_doctor")).toBeInTheDocument();
        expect(screen.getByRole("button", { name: "register.register_im_patient" })).toBeInTheDocument();
    });

    it("should show validation errors on empty submission (Patient)", async () => {
        const user = userEvent.setup();
        renderRegister();

        window.HTMLElement.prototype.scrollIntoView = vi.fn();

        const submitBtn = screen.getByRole("button", { name: "register.btn_register" });
        await user.click(submitBtn);

        expect(mockRegisterPatient).not.toHaveBeenCalled();
    });

    it("should submit patient form with valid data", async () => {
        const user = userEvent.setup();
        renderRegister();

        await user.type(screen.getByPlaceholderText("register.placeholder_name"), "Juan");
        await user.type(screen.getByPlaceholderText("register.placeholder_lastname"), "Perez");
        await user.type(screen.getByPlaceholderText("register.placeholder_email"), "juan@test.com");
        await user.type(screen.getByPlaceholderText("register.placeholder_phone"), "11223344");

        const neighborhoodInput = screen.getByTestId("neighborhood-mock");
        await user.type(neighborhoodInput, `${BASE_URL}/neighborhoods/1`);



        await user.type(screen.getByLabelText("register.label_password"), "Password123");
        await user.type(screen.getByLabelText("register.label_repeat_password"), "Password123");

        const osdeRadio = screen.getByLabelText("Osde");
        await user.click(osdeRadio);

        const termsCheckbox = screen.getByRole("checkbox");
        await user.click(termsCheckbox);

        await user.click(screen.getByRole("button", { name: "register.btn_register" }));

        expect(mockRegisterPatient).toHaveBeenCalledWith(expect.objectContaining({
            email: "juan@test.com",
            name: "Juan",
            neighborhoodUrl: `${BASE_URL}/neighborhoods/1`,
            coverageUrl: `${BASE_URL}/coverages/1`
        }), expect.anything());

        const mutationCall = mockRegisterPatient.mock.calls[0];
        const mutationOptions = mutationCall[1];

        act(() => {
            mutationOptions.onSuccess();
        });
        expect(await screen.findByText("register.success_title")).toBeInTheDocument();
        expect(screen.getByText(/juan@test.com/)).toBeInTheDocument();
    });

    it("should navigate stepper and submit doctor form", async () => {
        const user = userEvent.setup();
        renderRegister(['/register?type=doctor']);

        expect(screen.getByText("register.section_personal")).toBeInTheDocument();

        await user.type(screen.getByPlaceholderText("register.placeholder_name"), "Doc");
        await user.type(screen.getByPlaceholderText("register.placeholder_lastname"), "House");
        await user.type(screen.getByPlaceholderText("register.placeholder_email"), "house@test.com");
        await user.type(screen.getByPlaceholderText("register.placeholder_phone"), "55555555");

        await user.type(screen.getByLabelText("register.label_password"), "Password123");
        await user.type(screen.getByLabelText("register.label_repeat_password"), "Password123");

        const nextBtn = screen.getByRole("button", { name: /register.btn_next/i });
        await user.click(nextBtn);

        expect(await screen.findByText("register.section_coverage")).toBeInTheDocument();

        const cardioSpec = screen.getByText("Cardiologia");
        await user.click(cardioSpec);

        const osdeCov = screen.getByText("Osde");
        await user.click(osdeCov);

        const termsCheckbox = screen.getByRole("checkbox");
        await user.click(termsCheckbox);

        const finishBtn = screen.getByRole("button", { name: /register.btn_finish/i });
        await user.click(finishBtn);

        expect(mockRegisterDoctor).toHaveBeenCalledWith(expect.objectContaining({
            email: "house@test.com",
            selectedSpecialties: [`${BASE_URL}/specialties/1`],
            selectedCoverages: [`${BASE_URL}/coverages/1`]
        }), expect.anything());
    });
});