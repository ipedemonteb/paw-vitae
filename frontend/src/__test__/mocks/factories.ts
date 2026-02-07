import { http, HttpResponse } from 'msw';

const BASE_URL = 'http://localhost:8080/api';

type Item = { name: string; id: string };

export function createCatalogHandlers(endpoint: string, items: Item[]) {
    return [

        http.get(`${BASE_URL}/${endpoint}`, () => {
            const response = items.map(item => ({
                name: item.name,
                self: `${BASE_URL}/${endpoint}/${item.id}`
            }));
            return HttpResponse.json(response);
        }),

        http.get(`${BASE_URL}/${endpoint}/:id`, ({ params }) => {
            const { id } = params;

            if (id === 'error') return new HttpResponse(null, { status: 500 });

            const item = items.find(i => i.id === id);

            if (!item) return new HttpResponse(null, { status: 404 });

            return HttpResponse.json({
                name: item.name,
                self: `${BASE_URL}/${endpoint}/${id}`
            });
        })
    ];
}