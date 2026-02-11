import { describe, it, expect, vi, beforeAll, afterAll } from 'vitest';
import { render, screen, waitFor, within } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { createMemoryRouter, RouterProvider } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { server } from "@/__test__/setup/setup.ts";
import DoctorAvailability from '@/pages/doctor/DoctorAvailability';
import { Toaster } from 'sonner';
import { startOfDay } from 'date-fns';
import {officeHandlers} from "@/__test__/mocks/OfficesApiMock.ts";
import {doctorsHandlers} from "@/__test__/mocks/DoctorsApiMock.ts";

vi.mock('react-i18next', () => ({
    useTranslation: () => ({
        t: (key: string) => key,
        i18n: { language: 'en', changeLanguage: vi.fn() }
    })
}));

vi.mock('@/hooks/useAuth.ts', () => ({
    useAuth: () => ({ userId: '1', role: 'ROLE_DOCTOR' })
}));

vi.mock("@/utils/queryUtils.ts", () => ({
    useDelayedBoolean: (val: boolean) => val
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
        server.use(...officeHandlers, ...doctorsHandlers);
    });

    afterAll(() => {
        vi.clearAllMocks();
    });

    it('should allow the weekly availability to be edited (Happy Path)', async () => {
        const user = userEvent.setup();
        renderComponent();


        await waitFor(() => {
            expect(screen.queryAllByText(/loading/i)).toHaveLength(0);
            expect(screen.getByText('availability.headerTitle')).toBeInTheDocument();
        }, { timeout: 3000 });

        await user.click(screen.getByText('edit'));
        await user.click(await screen.findByText('availability.addSchedule'));

        const selects = screen.getAllByRole('combobox');
        const lastIndex = selects.length - 1;

        await user.click(selects[lastIndex - 3]);
        await user.click(await screen.findByRole('option', { name: /Belgrano/i }));

        await user.click(selects[lastIndex - 2]);
        await user.click(await screen.findByRole('option', { name: /tuesday/i }));

        await user.click(selects[lastIndex - 1]);
        const startOptions = await screen.findAllByRole('option', { name: '10:00' });
        await user.click(startOptions[startOptions.length - 1]);

        await user.click(selects[lastIndex]);
        const endOptions = await screen.findAllByRole('option', { name: '12:00' });
        await user.click(endOptions[endOptions.length - 1]);

        await user.click(screen.getByText('save'));

        await waitFor(() => {
            expect(screen.getByText('availability.toast_saved')).toBeInTheDocument();
        });
    });

    it('should prevent saving the avaialability if there is overlap', async () => {
        const user = userEvent.setup();
        renderComponent();

        await waitFor(() => expect(screen.queryAllByText(/loading/i)).toHaveLength(0));

        await user.click(screen.getByText('edit'));
        await user.click(await screen.findByText('availability.addSchedule'));

        const selects = screen.getAllByRole('combobox');
        const lastIndex = selects.length - 1;

        await user.click(selects[lastIndex - 2]);
        const mondayOptions = await screen.findAllByRole('option', { name: /monday/i });
        await user.click(mondayOptions[0]);

        await user.click(selects[lastIndex - 1]);
        const startOptions = await screen.findAllByRole('option', { name: '10:00' });
        await user.click(startOptions[startOptions.length - 1]);

        await user.click(selects[lastIndex]);
        const endOptions = await screen.findAllByRole('option', { name: '11:00' });
        await user.click(endOptions[endOptions.length - 1]);

        const errors = await screen.findAllByText('availability.overlap');
        expect(errors.length).toBeGreaterThan(0);

        expect(screen.getByText('save')).toBeDisabled();
    });

    it('should be able to add and save unavailability dates', async () => {
        const user = userEvent.setup();
        renderComponent();

        await waitFor(() => {
            expect(screen.queryAllByText(/loading/i)).toHaveLength(0);
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