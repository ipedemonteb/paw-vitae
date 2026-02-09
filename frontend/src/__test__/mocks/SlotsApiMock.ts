import { http, HttpResponse } from 'msw';
import {BASE_URL} from "@/__test__/utils/utils.ts";

export const slotsHandler = [

    http.get(`${BASE_URL}/doctors/:id/slots`, ({ request, params }) => {
        const { id } = params;

        if( id == 'error') return new HttpResponse(null, {status : 500})

        const url = new URL(request.url);
        const from = url.searchParams.get('from');

        return HttpResponse.json([
            {
                date: from,
                time: '10:00',
                occupied: true
            },
            {
                date: from,
                time: '11:00',
                occupied: false
            }
        ]);
    })

]