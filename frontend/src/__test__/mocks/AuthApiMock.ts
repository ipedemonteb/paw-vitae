import {http, HttpResponse} from 'msw';


const BASE_URL = 'http://localhost:8080/api';


export const authHandlers = [


    http.head(`${BASE_URL}/`, () => {
        return new HttpResponse(null, {
            status: 200,
            headers: {
                'x-vitae-authtoken': 'fake-jwt-token',
                'x-vitae-refreshtoken': 'fake-refresh-token'
            }
        });
    }),

    http.patch(`${BASE_URL}/doctors/:id`, () => {
        return HttpResponse.json({ status: 'ok' }, { status: 200 });
    }),

    http.patch(`${BASE_URL}/patients/:id`, () => {
        return HttpResponse.json({ status: 'ok' }, { status: 200 });
    }),
];