import { describe, it, expect, vi, afterAll } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import Search from '@/pages/Search';
import { createMemoryRouter, RouterProvider } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { http, HttpResponse } from 'msw';
import { BASE_URL } from '@/__test__/utils/utils';
import { server } from "@/__test__/setup/setup.ts";

vi.mock('react-i18next', () => ({
    useTranslation: () => ({
        t: (key: string, options?: any) => {
            if (key === 'search.found') return `Encontrados ${options?.doctorsFound}`;
            return key;
        },
        i18n: { language: 'en', changeLanguage: vi.fn() }
    })
}));

globalThis.ResizeObserver = vi.fn().mockImplementation(() => ({
    observe: vi.fn(),
    unobserve: vi.fn(),
    disconnect: vi.fn(),
}));

const mockHouse = {
    name: 'Gregory',
    lastName: 'House',
    email: 'house@princeton.edu',
    phone: '555-0199',
    rating: 5,
    ratingCount: 150,
    specialties: `${BASE_URL}/doctors/1/specialties`,
    coverages: `${BASE_URL}/doctors/1/coverages`,
    offices: `${BASE_URL}/doctors/1/offices`,
    profile: `${BASE_URL}/doctors/1/profile`,
    experiences: `${BASE_URL}/doctors/1/experiences`,
    certifications: `${BASE_URL}/doctors/1/certifications`,
    ratings: `${BASE_URL}/doctors/1/ratings`,
    appointments: `${BASE_URL}/doctors/1/appointments`,
    image: `${BASE_URL}/doctors/1/image`,
    unavailability: `${BASE_URL}/doctors/1/unavailability`,
    self: `${BASE_URL}/doctors/1`
};

const renderSearchPage = () => {
    const queryClient = new QueryClient({
        defaultOptions: { queries: { retry: false, staleTime: 0 } }
    });

    const router = createMemoryRouter([
        { path: '/search', element: <Search /> }
    ], {
        initialEntries: ['/search'],
    });

    return render(
        <QueryClientProvider client={queryClient}>
            <RouterProvider router={router} />
        </QueryClientProvider>
    );
};

describe('Search Page Integration', () => {

    afterAll(() => {
        vi.clearAllMocks();
    });

    it('debería renderizar la página y cargar la lista de doctores', async () => {
        server.use(
            http.get(`${BASE_URL}/doctors`, () => {
                return HttpResponse.json([mockHouse], { headers: { 'x-total-count': '1' } });
            })
        );

        renderSearchPage();

        expect(screen.getByText('search.title')).toBeInTheDocument();
        expect(screen.getByPlaceholderText('search.search')).toBeInTheDocument();

        await waitFor(() => {
            expect(screen.getAllByText(/Gregory/i).length).toBeGreaterThan(0);
            expect(screen.getAllByText(/House/i).length).toBeGreaterThan(0);
        });

        expect(screen.getByText(/Encontrados 1/i)).toBeInTheDocument();
    });

    it('debería permitir escribir en el buscador', async () => {
        server.use(
            http.get(`${BASE_URL}/doctors`, () => {
                return HttpResponse.json([mockHouse], { headers: { 'x-total-count': '1' } });
            })
        );

        const user = userEvent.setup();
        renderSearchPage();

        await waitFor(() => expect(screen.getAllByText(/Gregory/i).length).toBeGreaterThan(0));

        const searchInput = screen.getByPlaceholderText('search.search');
        await user.type(searchInput, 'House');

        await waitFor(() => {
            expect(searchInput).toHaveValue('House');
        });
    });

    it('debería mostrar estado de error si la API falla', async () => {
        server.use(
            http.get(`${BASE_URL}/doctors`, () => {
                return new HttpResponse(null, { status: 500 });
            })
        );

        renderSearchPage();

        await waitFor(() => {
            expect(screen.queryByText(/Gregory/i)).not.toBeInTheDocument();
        });
    });

    it('debería cambiar entre vista Lista y Grilla', async () => {
        server.use(
            http.get(`${BASE_URL}/doctors`, () => {
                return HttpResponse.json([mockHouse], { headers: { 'x-total-count': '1' } });
            })
        );

        const user = userEvent.setup();
        renderSearchPage();

        await waitFor(() => expect(screen.getAllByText(/Gregory/i).length).toBeGreaterThan(0));

        const gridButton = screen.getByLabelText('view-grid');

        await user.click(gridButton);

        expect(gridButton).toHaveClass('bg-[var(--primary-color)]');

        const gridContainer = document.querySelector('.grid.grid-cols-1');
        expect(gridContainer).toBeInTheDocument();
    });
});