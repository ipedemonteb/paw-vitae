import { describe, it, expect, vi, beforeAll, afterAll } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { createMemoryRouter, RouterProvider, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { http, HttpResponse } from 'msw';
import { BASE_URL } from '@/__test__/utils/utils';
import { server } from "@/__test__/setup/setup.ts";
import AppointmentDetails from '@/pages/AppointmentDetails';
import { Toaster } from 'sonner';
import PatientDashboardLayout from "@/pages/patient/PatientDashboardLayout.tsx";
import UserUpcoming from "@/pages/common/UserUpcoming.tsx";
import {ratingHandler} from "@/__test__/mocks/RatingApiMock.ts";




vi.mock('react-i18next', () => ({
    useTranslation: () => ({
        t: (key: string) => key,
        i18n: { language: 'en', changeLanguage: vi.fn() }
    })
}));

vi.mock('@/hooks/useAuth.ts', () => ({
    useAuth: () => ({ userId: '10', role: 'ROLE_PATIENT' })
}));

vi.mock('sonner', () => ({
    toast: {
        success: (msg: string) => {
            const div = document.createElement('div');
            div.textContent = msg;
            document.body.appendChild(div);
        },
        error: vi.fn()
    },
    Toaster: () => null
}));

window.HTMLElement.prototype.scrollIntoView = vi.fn();
window.HTMLElement.prototype.hasPointerCapture = vi.fn();
window.HTMLElement.prototype.releasePointerCapture = vi.fn();

const mockAppointmentList = [
    {
        id: '100',
        date: '2026-12-25T10:00:00',
        status: 'confirmado',
        doctor: `${BASE_URL}/doctors/1`,
        specialty: `${BASE_URL}/specialties/1`,
        self: `${BASE_URL}/appointments/100`,
        reason: "Consulta general"
    }
];

const mockAppointmentDetail = {
    ...mockAppointmentList[0],
    reason: 'Dolor de espalda',
    patient: `${BASE_URL}/patients/10`,
    doctorOffice: `${BASE_URL}/doctors/1/offices/1`,
    allowFullHistory: true,
    cancellable: true,
    appointmentFiles: `${BASE_URL}/appointments/100/files`,
    rating: null,
    report: ""
};

const mockDoctor = {
    name: 'Gregory',
    lastName: 'House',
    email: 'house@princeton.edu',
    phone: '555-0199',
    self: `${BASE_URL}/doctors/1`
};

const mockPatient = {
    name: 'Paciente',
    lastName: 'Prueba',
    email: 'patient@test.com',
    self: `${BASE_URL}/patients/10`
};

const dashboardHandlers = [
    http.get(`${BASE_URL}/patients/10`, () => HttpResponse.json(mockPatient)),
    http.get(`${BASE_URL}/patients/10/image`, () => new HttpResponse(null, { status: 404 })),

    http.get(`${BASE_URL}/appointments`, ({ request }) => {
        const url = new URL(request.url);
        const collection = url.searchParams.get('collection');
        if (collection === 'upcoming') {
            return HttpResponse.json(mockAppointmentList, { headers: { 'x-total-count': '1' } });
        }
        return HttpResponse.json([], { headers: { 'x-total-count': '0' } });
    }),

    http.get(`${BASE_URL}/appointments/100`, () => HttpResponse.json(mockAppointmentDetail)),

    http.get(`${BASE_URL}/doctors/1`, () => HttpResponse.json(mockDoctor)),
    http.get(`${BASE_URL}/doctors/1/image`, () => new HttpResponse(null, { status: 404 })),
    http.get(`${BASE_URL}/specialties/1`, () => HttpResponse.json({ name: 'Traumatologia' })),

    http.get(`${BASE_URL}/doctors/1/offices/1`, () => HttpResponse.json({
        name: 'Consultorio Central',
        neighborhood: `${BASE_URL}/neighborhoods/1`
    })),
    http.get(`${BASE_URL}/neighborhoods/1`, () => HttpResponse.json({ name: 'Palermo' })),

    http.get(`${BASE_URL}/appointments/100/files`, () => HttpResponse.json([])),

    http.patch(`${BASE_URL}/appointments/100`, () => {
        return HttpResponse.json({ status: 'cancelled' }, { status: 200 });
    })
];

const renderWithRouter = (initialEntries = ['/patient/dashboard/upcoming']) => {
    const queryClient = new QueryClient({
        defaultOptions: { queries: { retry: false, staleTime: 0 } }
    });

    const router = createMemoryRouter([
        {
            path: '/patient/dashboard',
            element: <PatientDashboardLayout />,
            children: [
                { index: true, element: <Navigate to="upcoming" replace /> },
                { path: 'upcoming', element: <UserUpcoming /> },
                { path: 'appointment-details/:id', element: <AppointmentDetails /> }
            ]
        }
    ], { initialEntries });

    return render(
        <QueryClientProvider client={queryClient}>
            <RouterProvider router={router} />
            <Toaster />
        </QueryClientProvider>
    );
};

describe('Patient Appointment Dashboard Flow', () => {

    beforeAll(() => {
        server.use(...dashboardHandlers, ...ratingHandler);
    });

    afterAll(() => {
        vi.clearAllMocks();
    });

    it('debería ver turnos próximos, navegar al detalle y cancelar el turno', async () => {
        const user = userEvent.setup();
        renderWithRouter();

        await waitFor(() => {
            expect(screen.queryByText(/loading/i)).not.toBeInTheDocument();
            expect(screen.getByText('Paciente Prueba')).toBeInTheDocument();
            expect(screen.getByText('appointment.upcoming')).toBeInTheDocument();
        });

        const detailsButtons = await screen.findAllByText('appointment.card.details');
        const detailsButton = detailsButtons[0];

        await user.click(detailsButton);

        await waitFor(() => {
            expect(screen.getByText('appointment.details.title')).toBeInTheDocument();
            expect(screen.getByText('Dolor de espalda')).toBeInTheDocument();
        });

        const cancelButton = screen.getByText('appointment.cancel.cancel');
        await user.click(cancelButton);

        const dialogTitle = await screen.findByText('appointment.cancel.title');
        expect(dialogTitle).toBeInTheDocument();

        const confirmButton = screen.getByText('appointment.cancel.confirmation');
        await user.click(confirmButton);

        await waitFor(() => {
            expect(screen.getByText('appointment.upcoming')).toBeInTheDocument();
        });
    });
});