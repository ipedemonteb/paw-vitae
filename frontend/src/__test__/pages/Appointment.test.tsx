import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import { render, screen, waitFor, within, cleanup } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import Appointment from '@/pages/Appointment';
import AppointmentConfirmation from '@/pages/AppointmentConfirmation';
import { createMemoryRouter, RouterProvider } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { http, HttpResponse } from 'msw';
import { BASE_URL } from '@/__test__/utils/utils';
import { server } from "@/__test__/setup/setup.ts";
import { format, addDays, startOfDay } from 'date-fns';

vi.mock('react-i18next', () => ({
    useTranslation: () => ({
        t: (key: string) => key,
        i18n: { language: 'en', changeLanguage: vi.fn() }
    })
}));

vi.mock('@/hooks/useAuth.ts', () => ({
    useAuth: () => ({ userId: '100', role: 'PATIENT' })
}));

vi.mock('sonner', () => ({
    toast: {
        success: vi.fn(),
        error: (message: string) => {
            const errorDiv = document.createElement('div');
            errorDiv.textContent = message;
            document.body.appendChild(errorDiv);
        }
    }
}));

vi.mock("@/utils/queryUtils.ts", () => ({
    useDelayedBoolean: (val: boolean) => val
}));

window.HTMLElement.prototype.scrollIntoView = vi.fn();
window.HTMLElement.prototype.hasPointerCapture = vi.fn();
window.HTMLElement.prototype.releasePointerCapture = vi.fn();

const today = startOfDay(new Date());
let nextMonday = addDays(today, 1);
while (nextMonday.getDay() !== 1) {
    nextMonday = addDays(nextMonday, 1);
}
const mondayDate = nextMonday.getDate().toString();

const renderApp = (initialPath: string) => {
    const queryClient = new QueryClient({
        defaultOptions: { queries: { retry: false, staleTime: 0 } }
    });

    return render(
        <QueryClientProvider client={queryClient}>
            <RouterProvider router={createMemoryRouter([
                { path: '/appointment/:id', element: <Appointment /> },
                { path: '/appointment/:id/confirmation', element: <AppointmentConfirmation /> },
                { path: '/login', element: <div>Login Page Mock</div> }
            ], { initialEntries: [initialPath] })} />
        </QueryClientProvider>
    );
};

describe('Appointment Booking Flow', () => {

    beforeEach(() => {
        server.use(
            http.get(`${BASE_URL}/doctors/:id/specialties`, () => {
                return HttpResponse.json([{ self: `${BASE_URL}/specialties/1` }]);
            }),
            http.get(`${BASE_URL}/specialties/:id`, ({ request }) => {
                return HttpResponse.json({ self: request.url, name: "Cardiologia" });
            })
        );
    });

    afterEach(() => {
        cleanup();
        vi.clearAllMocks();
        server.resetHandlers();
    });

    it('should let me book an appointment (Happy Path)', async () => {
        server.use(
            http.get(`${BASE_URL}/doctors/:id/slots`, () => HttpResponse.json([]))
        );

        const user = userEvent.setup();
        renderApp(`/appointment/1`);

        await waitFor(() => {
            expect(screen.queryByText(/loading/i)).not.toBeInTheDocument();
            expect(screen.getByText(/Gregory/i)).toBeInTheDocument();
        }, { timeout: 4000 });

        const selects = screen.getAllByRole('combobox');
        await user.click(selects[0]);

        const specialtyOptions = await screen.findAllByRole('option', { name: /Cardiologia/i });
        await user.click(specialtyOptions[0]);

        const officeSelect = screen.getAllByRole('combobox')[1];
        await waitFor(() => expect(officeSelect).toBeEnabled());
        await user.click(officeSelect);

        const officeOptions = await screen.findAllByRole('option', { name: /Belgrano/i });
        await user.click(officeOptions[0]);

        const dateSection = screen.getByText('appointment.booking.date').closest('div');
        const calendarTrigger = within(dateSection!).getByRole('button');
        await user.click(calendarTrigger);

        const dayButtons = await screen.findAllByText(mondayDate);
        const enabledButton = dayButtons.find(b => !b.closest('button')?.disabled);
        if (!enabledButton) throw new Error(`No se encontró día habilitado: ${mondayDate}`);
        await user.click(enabledButton);

        await user.click(await screen.findByText('09:00'));

        const bookButton = screen.getByText('appointment.booking.book');
        await waitFor(() => expect(bookButton).toBeEnabled());
        await user.click(bookButton);

        await waitFor(() => {
            expect(screen.getByText('appointment.confirmation.title')).toBeInTheDocument();
        });
    });

    it('should correctly show the appointment confirmation page', async () => {
        renderApp(`/appointment/999/confirmation`);

        await waitFor(() => {
            expect(screen.queryByText(/loading/i)).not.toBeInTheDocument();
            expect(screen.getByText('appointment.confirmation.title')).toBeInTheDocument();
            expect(screen.getByText(/Gregory/i)).toBeInTheDocument();
            expect(screen.getByText(/Belgrano/i)).toBeInTheDocument();
        }, { timeout: 4000 });
    });

    it('should show error if the appointment creation fails (Error 500)', async () => {
        server.use(
            http.get(`${BASE_URL}/doctors/:id/slots`, () => HttpResponse.json([])),
            http.post(`${BASE_URL}/appointments`, () => {
                return new HttpResponse(null, { status: 500 });
            })
        );

        const user = userEvent.setup();
        renderApp(`/appointment/1`);

        await waitFor(() => expect(screen.getByText(/Gregory/i)).toBeInTheDocument(), { timeout: 4000 });

        const selects = screen.getAllByRole('combobox');
        await user.click(selects[0]);
        const specialtyOptions = await screen.findAllByRole('option', { name: /Cardiologia/i });
        await user.click(specialtyOptions[0]);

        const officeSelect = screen.getAllByRole('combobox')[1];
        await waitFor(() => expect(officeSelect).toBeEnabled());
        await user.click(officeSelect);
        const officeOptions = await screen.findAllByRole('option', { name: /Belgrano/i });
        await user.click(officeOptions[0]);

        const dateSection = screen.getByText('appointment.booking.date').closest('div');
        const calendarTrigger = within(dateSection!).getByRole('button');
        await user.click(calendarTrigger);

        const dayButtons = await screen.findAllByText(mondayDate);
        const enabledButton = dayButtons.find(b => !b.closest('button')?.disabled);
        if(enabledButton) await user.click(enabledButton);

        await user.click(await screen.findByText('09:00'));

        const bookButton = screen.getByText('appointment.booking.book');
        await waitFor(() => expect(bookButton).toBeEnabled());
        await user.click(bookButton);

        await waitFor(() => {
            expect(screen.getByText('appointment.booking.error.failed')).toBeInTheDocument();
        });
    });
});