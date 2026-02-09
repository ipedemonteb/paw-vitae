import { describe, it, expect, vi, beforeAll, afterAll } from 'vitest';
import { render, screen, waitFor, within } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { createMemoryRouter, RouterProvider } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { http, HttpResponse } from 'msw';
import { BASE_URL } from '@/__test__/utils/utils';
import { server } from "@/__test__/setup/setup.ts";
import DoctorAvailability from '@/pages/doctor/DoctorAvailability';
import { Toaster } from 'sonner';
import { startOfDay } from 'date-fns';
import {officeHandlers} from "@/__test__/mocks/OfficesApiMock.ts";

// --- MOCKS ---
vi.mock('react-i18next', () => ({
    useTranslation: () => ({
        t: (key: string) => key,
        i18n: { language: 'en', changeLanguage: vi.fn() }
    })
}));

vi.mock('@/hooks/useAuth.ts', () => ({
    useAuth: () => ({ userId: '1', role: 'ROLE_DOCTOR' })
}));

vi.mock('sonner', () => ({
    toast: {
        success: (msg: string) => {
            const div = document.createElement('div');
            div.textContent = msg;
            document.body.appendChild(div);
        },
        error: (msg: string) => {
            const div = document.createElement('div');
            div.textContent = `Error: ${msg}`;
            document.body.appendChild(div);
        }
    },
    Toaster: () => null
}));

window.HTMLElement.prototype.scrollIntoView = vi.fn();
window.HTMLElement.prototype.hasPointerCapture = vi.fn();
window.HTMLElement.prototype.releasePointerCapture = vi.fn();

// Mock del perfil del doctor (necesario porque la página lo pide al inicio)
const doctorHandler = [
    http.get(`${BASE_URL}/doctors/1`, () => HttpResponse.json({
        id: '1',
        name: 'Gregory',
        lastName: 'House',
        offices: `${BASE_URL}/doctors/1/offices`,
        availability: `${BASE_URL}/doctors/1/availability`,
        unavailability: `${BASE_URL}/doctors/1/unavailability`
    }))
];

const renderComponent = () => {
    const queryClient = new QueryClient({
        defaultOptions: { queries: { retry: false, staleTime: 0 } }
    });

    const router = createMemoryRouter([
        { path: '/', element: <DoctorAvailability /> }
    ], { initialEntries: ['/'] });

    return render(
        <QueryClientProvider client={queryClient}>
            <RouterProvider router={router} />
            <Toaster />
        </QueryClientProvider>
    );
};

describe('Doctor Availability Management', () => {

    beforeAll(() => {
        // Usamos los handlers corregidos
        server.use(...officeHandlers, ...doctorHandler);
    });

    afterAll(() => {
        vi.clearAllMocks();
    });

    it('debería permitir editar la disponibilidad semanal (Happy Path)', async () => {
        const user = userEvent.setup();
        renderComponent();

        await waitFor(() => {
            expect(screen.queryByText(/loading/i)).not.toBeInTheDocument();
            expect(screen.getByText('availability.headerTitle')).toBeInTheDocument();
        });

        await user.click(screen.getByText('edit'));
        await user.click(await screen.findByText('availability.addSchedule'));

        const selects = screen.getAllByRole('combobox');
        const lastIndex = selects.length - 1;

        // Oficina
        await user.click(selects[lastIndex - 3]);
        await user.click(await screen.findByRole('option', { name: /Belgrano/i }));

        // Día
        await user.click(selects[lastIndex - 2]);
        await user.click(await screen.findByRole('option', { name: /tuesday/i }));

        // Inicio
        await user.click(selects[lastIndex - 1]);
        const startOptions = await screen.findAllByRole('option', { name: '10:00' });
        await user.click(startOptions[startOptions.length - 1]);

        // Fin
        await user.click(selects[lastIndex]);
        const endOptions = await screen.findAllByRole('option', { name: '12:00' });
        await user.click(endOptions[endOptions.length - 1]);

        await user.click(screen.getByText('save'));

        await waitFor(() => {
            expect(screen.getByText('availability.toast_saved')).toBeInTheDocument();
        });
    });

    it('debería impedir guardar si hay solapamiento de horarios (Overlap)', async () => {
        const user = userEvent.setup();
        renderComponent();

        await waitFor(() => expect(screen.queryByText(/loading/i)).not.toBeInTheDocument());
        await user.click(screen.getByText('edit'));
        await user.click(await screen.findByText('availability.addSchedule'));

        const selects = screen.getAllByRole('combobox');
        const lastIndex = selects.length - 1;

        // Generamos conflicto con Lunes 09-18 (que viene del mock en officeHandlers)

        // Seleccionar Lunes
        await user.click(selects[lastIndex - 2]);
        const mondayOptions = await screen.findAllByRole('option', { name: /monday/i });
        await user.click(mondayOptions[0]);

        // Hora 10:00
        await user.click(selects[lastIndex - 1]);
        const startOptions = await screen.findAllByRole('option', { name: '10:00' });
        await user.click(startOptions[startOptions.length - 1]);

        // Hora 11:00
        await user.click(selects[lastIndex]);
        const endOptions = await screen.findAllByRole('option', { name: '11:00' });
        await user.click(endOptions[endOptions.length - 1]);

        // FIX IMPORTANTE: Usamos findAllByText porque el error puede renderizarse múltiples veces
        const errors = await screen.findAllByText('availability.overlap');
        expect(errors.length).toBeGreaterThan(0);

        expect(screen.getByText('save')).toBeDisabled();
    });

    it('debería permitir agregar una fecha de no disponibilidad (Vacaciones)', async () => {
        const user = userEvent.setup();
        renderComponent();

        await waitFor(() => {
            expect(screen.queryByText(/loading/i)).not.toBeInTheDocument();
            expect(screen.getByText('unavailability.title')).toBeInTheDocument();
        });

        await user.click(screen.getByText('unavailability.add'));

        const today = startOfDay(new Date());
        const tomorrow = new Date(today);
        tomorrow.setDate(today.getDate() + 1);
        const dayToClick = tomorrow.getDate().toString();

        const dialog = screen.getByRole('dialog');
        const triggers = within(dialog).getAllByRole('button', { name: /date-picker.placeholder/i });

        await user.click(triggers[0]);
        const dayButtonsStart = await screen.findAllByText(dayToClick);
        const dayBtnStart = dayButtonsStart.find(b => b.tagName === 'BUTTON' && !b.hasAttribute('disabled'));
        if(dayBtnStart) await user.click(dayBtnStart);

        await user.click(triggers[1]);
        const dayButtonsEnd = await screen.findAllByText(dayToClick);
        const dayBtnEnd = dayButtonsEnd.find(b => b.tagName === 'BUTTON' && !b.hasAttribute('disabled'));
        if(dayBtnEnd) await user.click(dayBtnEnd);

        const confirmButton = within(dialog).getByText('unavailability.add-unavailability');
        await waitFor(() => expect(confirmButton).toBeEnabled());
        await user.click(confirmButton);

        await waitFor(() => {
            expect(screen.getByText('unavailability.toast_saved')).toBeInTheDocument();
        });
    });
});