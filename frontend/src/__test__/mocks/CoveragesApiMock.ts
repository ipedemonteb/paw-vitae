import {http, HttpResponse} from 'msw';

const BASE_URL = 'http://localhost:8080/api';


export const coveragesHandlers = [

    http.get(`${BASE_URL}/coverages`, () => {
        return HttpResponse.json([
            {
                name: 'Medifé',
                self: `${BASE_URL}/coverages/1`
            },
            {
                name: 'Galeno',
                self: `${BASE_URL}/coverages/2`
            },
            {
                name: 'OSDE',
                self: `${BASE_URL}/coverages/3`
            }
        ]);
    }),

    http.get(`${BASE_URL}/coverages/:id`, ({ params }) => {
        const { id } = params;

        if (id === 'error') {
            return new HttpResponse(null, { status: 500 });
        }

        let name = 'Cobertura Genérica';
        if (id === '1') name = 'Medifé';
        if (id === '2') name = 'Galeno';
        if (id === '3') name = 'OSDE';

        return HttpResponse.json({
            name,
            self: `${BASE_URL}/coverages/${id}`,

        });
    })


]