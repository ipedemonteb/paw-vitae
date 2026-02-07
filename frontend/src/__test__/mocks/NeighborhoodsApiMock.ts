import {http, HttpResponse} from 'msw';

const BASE_URL = 'http://localhost:8080/api';


export const neighborhoodHandlers = [

    http.get(`${BASE_URL}/neighborhoods`, () => {
        return HttpResponse.json([
            {
                name: 'Belgrano',
                self: `${BASE_URL}/neighborhoods/1`
            },
            {
                name: 'Recoleta',
                self: `${BASE_URL}/neighborhoods/2`
            },
            {
                name: 'Palermo',
                self: `${BASE_URL}/neighborhoods/3`
            }
        ]);
    }),

    http.get(`${BASE_URL}/neighborhoods/:id`, ({ params }) => {
        const { id } = params;

        if (id === 'error') {
            return new HttpResponse(null, { status: 500 });
        }

        let name = 'Cobertura Genérica';
        if (id === '1') name = 'Belgrano';
        if (id === '2') name = 'Recoleta';
        if (id === '3') name = 'Palermo';

        return HttpResponse.json({
            name,
            self: `${BASE_URL}/neighborhoods/${id}`,

        });
    })


]