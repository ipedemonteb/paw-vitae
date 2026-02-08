import { http, HttpResponse } from 'msw';
import {createMockDoctor, createMockPatient} from "@/__test__/utils/factories.ts";

const BASE_URL = 'http://localhost:8080/api';


export const userHandlers = [

    http.post(`${BASE_URL}/users`, () => {
        return HttpResponse.json({ status: 'ok' }, { status: 201 });
    }),


    http.get(`${BASE_URL}/doctors/:id`, ({ params }) => {
        const { id } = params;
        if (id === 'error') return new HttpResponse(null, { status: 500 });

        return HttpResponse.json(createMockDoctor());
    }),

    http.get(`${BASE_URL}/patients/:id`, ({ params }) => {
        const { id } = params;
        if (id === 'error') return new HttpResponse(null, { status: 500 });

        return HttpResponse.json(createMockPatient());
    })


]