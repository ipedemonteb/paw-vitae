import {http, HttpResponse} from 'msw';

const BASE_URL = 'http://localhost:8080/api';


export const coveragesHandlers = [

    http.get(`${BASE_URL}/coverages`, () => {
        return HttpResponse.json([
            {
                id: '1', // Agregué ID (útil para keys de React)
                name: 'Medifé',
                self: `${BASE_URL}/coverages/1`
            },
            {
                id: '2',
                name: 'Galeno',
                self: `${BASE_URL}/coverages/2`
            },
            {
                id: '3',
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
            id,
            name,
            self: `${BASE_URL}/coverages/${id}`,

        });
    })


]