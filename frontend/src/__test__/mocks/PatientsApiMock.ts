import {BASE_URL} from "@/__test__/utils/utils.ts";
import {http, HttpResponse} from "msw";
import {createMockPatient} from "@/__test__/utils/factories.ts";


export const patientHandler = [

    http.get(`${BASE_URL}/patients/:id`, ({ params }) => {

        const {id} = params;

        if(id === 'error') return new HttpResponse(null, {status : 500});

        return HttpResponse.json(createMockPatient);

    }),


    http.head(`${BASE_URL}/patients`, ()  => {
        return new HttpResponse(null, {
            headers: {
                'x-total-count': '3'
            }
        });
    }),

    http.patch(`${BASE_URL}/patients/:id`, () => {
        return new HttpResponse(null, { status: 204 });
    }),

    http.post(`${BASE_URL}/patients`, () => {
        return HttpResponse.json(
            { status: 'created' },
            {
                status: 201,
                headers: {
                    'Location': `${BASE_URL}/patients/101`
                }
            }
        );
    }),






]