import { describe, it, expect, vi, afterEach } from "vitest";
import { render, screen, cleanup } from "@testing-library/react";
import GenericError from "@/pages/GenericError.tsx";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { createMemoryRouter, RouterProvider } from "react-router-dom";

vi.mock('react-i18next', () => ({
    useTranslation: () => ({
        t: (key: string) => key,
        i18n: { language: 'en', changeLanguage: vi.fn() }
    })
}));

afterEach(() => {
    cleanup();
});

const renderComponent = (code: number) => {
    const queryClient = new QueryClient({
        defaultOptions: { queries: { retry: false, staleTime: 0 } }
    });

    const router = createMemoryRouter([
        { path: '/', element: <GenericError code={code}/> }
    ], { initialEntries: ['/'] });

    return render(
        <QueryClientProvider client={queryClient}>
            <RouterProvider router={router} />
        </QueryClientProvider>
    );
};

describe("GenericError Page", () => {

    it("should show error 403 on screen", () => {
        renderComponent(403);
        expect(screen.getAllByText(/403/i).length).toBeGreaterThan(0);
    });

    it("should show error 404 on screen", () => {
        renderComponent(404);
        expect(screen.getAllByText(/404/i).length).toBeGreaterThan(0);
    });

});