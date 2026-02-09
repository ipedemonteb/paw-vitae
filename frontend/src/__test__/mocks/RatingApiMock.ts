import { http, HttpResponse } from "msw";
import {BASE_URL} from "@/__test__/utils/utils.ts";


export const ratingHandler = [


    http.get(`${BASE_URL}/ratings`, ({ request }) => {
        const url = new URL(request.url);
        const doctorId = url.searchParams.get('doctorId');
        const page = url.searchParams.get('page');

        if (doctorId) {
            if(doctorId === 'error') return new HttpResponse(null, {status : 500});

            return HttpResponse.json([
                {
                    rating: 5,
                    comment: 'Fue rapido (Mock Doctor)',
                    self: `${BASE_URL}/ratings/1`,
                    appointment: `${BASE_URL}/appointments/1`,
                    doctor: `${BASE_URL}/doctors/${doctorId}`,
                    patient: `${BASE_URL}/patients/1`
                },
                {
                    rating: 4,
                    comment: 'Me atendieron tarde',
                    self: `${BASE_URL}/ratings/2`,
                    appointment: `${BASE_URL}/appointments/12`,
                    doctor: `${BASE_URL}/doctors/${doctorId}`,
                    patient: `${BASE_URL}/patients/1`

                }
            ]);
        }

        if (page) {
            return HttpResponse.json([
                {
                    rating: 5,
                    comment: 'Fue rapido (Mock All)',
                    self: `${BASE_URL}/ratings/10`,
                    appointment: `${BASE_URL}/appointments/10`,
                    doctor: `${BASE_URL}/doctors/1`,
                    patient: `${BASE_URL}/patients/1`
                },
                {
                    rating: 3,
                    comment: 'Me atendieron tarde pero mas alla de eso todo bien',
                    self: `${BASE_URL}/ratings/2`,
                    appointment: `${BASE_URL}/appointments/12`,
                    doctor: `${BASE_URL}/doctors/2`,
                    patient: `${BASE_URL}/patients/5`

                }
            ]);
        }

        return new HttpResponse(null, { status: 400, statusText: 'Missing params' });
    }),

    http.get(`${BASE_URL}/ratings/:ratingId`, ({ params }) => {
        const { ratingId } = params;

        if(ratingId === "error") return new HttpResponse(null, {status : 500});

        return HttpResponse.json({
            rating: 1,
            comment: 'Experiencia detallada mockeada',
            self: `${BASE_URL}/ratings/${ratingId}`,
            appointment: `${BASE_URL}/appointments/99`,
            doctor: `${BASE_URL}/doctors/3`,
            patient: `${BASE_URL}/patients/4`
        });
    }),

    http.post(`${BASE_URL}/ratings`, () => {
        return HttpResponse.json(
            { status: 'created' },
            {
                status: 201,
                headers: {
                    'Location': `${BASE_URL}/ratings/999`,
                }
            }
        );
    })
];