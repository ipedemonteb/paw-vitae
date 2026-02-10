import { describe, it, expect, vi, beforeAll, afterAll } from 'vitest';
import { render, screen, waitFor, within } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import Appointment from '@/pages/Appointment';
import AppointmentConfirmation from '@/pages/AppointmentConfirmation';
import { createMemoryRouter, RouterProvider } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { http, HttpResponse } from 'msw';
import { BASE_URL } from '@/__test__/utils/utils';
import { server } from "@/__test__/setup/setup.ts";
import { format, addDays, startOfDay } from 'date-fns';
import {doctorsHandlers} from "@/__test__/mocks/DoctorsApiMock.ts";
import {appointmentHandlers} from "@/__test__/mocks/AppointmentsApiMock.ts";
import {officeHandlers} from "@/__test__/mocks/OfficesApiMock.ts";
import {slotsHandler} from "@/__test__/mocks/SlotsApiMock.ts";

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

window.HTMLElement.prototype.scrollIntoView = vi.fn();
window.HTMLElement.prototype.hasPointerCapture = vi.fn();
window.HTMLElement.prototype.releasePointerCapture = vi.fn();

const today = startOfDay(new Date());
let nextMonday = addDays(today, 1);
while (nextMonday.getDay() !== 1) {
    nextMonday = addDays(nextMonday, 1);
}
const mondayDate = nextMonday.getDate().toString();
const mondayDateStr = format(nextMonday, 'yyyy-MM-dd');

const mockAppointmentCreated = {
    id: '999',
    date: `${mondayDateStr}T10:00:00`,
    doctor: `${BASE_URL}/doctors/1`,
    specialty: `${BASE_URL}/specialties/1`,
    doctorOffice: `${BASE_URL}/doctors/1/offices/1`
};

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

    beforeAll(() => {
        server.use(
            ...doctorsHandlers,
            ...appointmentHandlers,
            ...officeHandlers,
            ...slotsHandler,

            http.get(`${BASE_URL}/appointments/999`, () => {
                return HttpResponse.json(mockAppointmentCreated);
            }),

            http.get(`${BASE_URL}/neighborhoods/:id`, () => {
                return HttpResponse.json({ name: 'Palermo', self: `${BASE_URL}/neighborhoods/1` });
            }),

            http.get(`${BASE_URL}/doctors/:did/offices/:oid/specialties`, () => {
                return HttpResponse.json([
                    { specialty: `${BASE_URL}/specialties/1`, office: `${BASE_URL}/doctors/1/offices/1` }
                ]);
            })
        );
    });

    afterAll(() => {
        vi.clearAllMocks();
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
        });

        const selects = screen.getAllByRole('combobox');
        await user.click(selects[0]);
        await user.click(await screen.findByRole('option', { name: /Cardiologia/i }));

        const officeSelect = screen.getAllByRole('combobox')[1];
        await waitFor(() => expect(officeSelect).toBeEnabled());
        await user.click(officeSelect);
        await user.click(await screen.findByRole('option', { name: /Belgrano/i }));

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
        });
    });

    it('shoudl show error if the appointment creation fails (Error 500)', async () => {
        server.use(
            http.get(`${BASE_URL}/doctors/:id/slots`, () => HttpResponse.json([])),
            http.post(`${BASE_URL}/appointments`, () => {
                return new HttpResponse(null, { status: 500 });
            })
        );

        const user = userEvent.setup();
        renderApp(`/appointment/1`);

        await waitFor(() => expect(screen.getByText(/Gregory/i)).toBeInTheDocument());

        const selects = screen.getAllByRole('combobox');
        await user.click(selects[0]);
        await user.click(await screen.findByRole('option', { name: /Cardiologia/i }));

        const officeSelect = screen.getAllByRole('combobox')[1];
        await waitFor(() => expect(officeSelect).toBeEnabled());
        await user.click(officeSelect);
        await user.click(await screen.findByRole('option', { name: /Belgrano/i }));

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