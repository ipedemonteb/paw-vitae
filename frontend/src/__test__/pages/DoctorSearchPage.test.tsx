import { describe, it, expect, vi, beforeAll, afterAll } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import Search from '@/pages/Search';
import { createMemoryRouter, RouterProvider } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { http, HttpResponse } from 'msw';
import { BASE_URL } from '@/__test__/utils/utils';
import { server } from "@/__test__/setup/setup.ts";
import { createMockDoctor } from '@/__test__/utils/factories';

vi.mock('react-i18next', () => ({
    useTranslation: () => ({
        t: (key: string, options?: any) => {
            if (key === "search.found" && options?.doctorsFound !== undefined) {
                return `Encontrados ${options.doctorsFound}`;
            }
            return key;
        }
    })
}));

Object.defineProperty(window, 'matchMedia', {
    writable: true,
    value: vi.fn().mockImplementation(query => ({
        matches: true,
        media: query,
        onchange: null,
        addListener: vi.fn(),
        removeListener: vi.fn(),
        addEventListener: vi.fn(),
        removeEventListener: vi.fn(),
        dispatchEvent: vi.fn(),
    })),
});

const renderSearch = () => {
    const queryClient = new QueryClient({
        defaultOptions: { queries: { retry: false, staleTime: 0 } }
    });
    queryClient.clear();

    const router = createMemoryRouter([
        { path: '/doctors', element: <Search /> }
    ], { initialEntries: ['/doctors'] });

    return render(
        <QueryClientProvider client={queryClient}>
            <RouterProvider router={router} />
        </QueryClientProvider>
    );
};

describe('Search Page Integration', () => {

    const mockHouse = createMockDoctor({ name: "Gregory", lastName: "House" });

    beforeAll(() => {
        server.resetHandlers();

        server.use(
            http.get(`${BASE_URL}/doctors`, ({ request }) => {
                const url = new URL(request.url);

                if (url.searchParams.get('pageSize') === '5') {
                    return HttpResponse.json([], { headers: { 'x-total-count': '0' } });
                }

                return HttpResponse.json([mockHouse], {
                    headers: { 'x-total-count': '1', 'Link': '' }
                });
            }),
            http.get(`${BASE_URL}/neighborhoods/:id`, () => HttpResponse.json({ name: 'Palermo' }))
        );
    });

    afterAll(() => {
        vi.clearAllMocks();
    });

    it('should render the page and load the list of doctors', async () => {
        renderSearch();

        expect(screen.getByText("search.title")).toBeInTheDocument();

        await waitFor(() => {
            expect(screen.getByText(/Encontrados \d+/)).toBeInTheDocument();
        }, { timeout: 3000 });

        const cards = screen.getAllByText("Gregory House");
        expect(cards.length).toBeGreaterThan(0);
        expect(cards[0]).toBeInTheDocument();
    });

    it('should allow writing in the search bar', async () => {
        const user = userEvent.setup();
        renderSearch();

        const searchInput = screen.getByPlaceholderText("search.search");
        await user.type(searchInput, "House");
        expect(searchInput).toHaveValue("House");
    });

    it('should switch between List and Grid view, verifying the change of structure', async () => {
        const user = userEvent.setup();
        renderSearch();

        await waitFor(() => expect(screen.getByText(/Encontrados \d+/)).toBeInTheDocument());

        expect(screen.getByTestId("view-list")).toBeInTheDocument();
        expect(screen.queryByTestId("view-grid")).not.toBeInTheDocument();

        const gridButton = screen.getByLabelText("view-grid");
        await user.click(gridButton);

        await waitFor(() => {
            expect(screen.getByTestId("view-grid")).toBeInTheDocument();
        });

        expect(screen.queryByTestId("view-list")).not.toBeInTheDocument();
    });
});