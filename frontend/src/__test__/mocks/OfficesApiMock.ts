import { http, HttpResponse } from "msw";
import { BASE_URL } from "@/__test__/utils/utils.ts";

export const officeHandlers = [

    http.get(`${BASE_URL}/doctors/:doctorId/offices`, ({ params, request }) => {
        const { doctorId } = params;
        const url = new URL(request.url);
        const status = url.searchParams.get('status');

        if (doctorId === "error") return new HttpResponse(null, { status: 500 });

        let offices = [
            {
                name: 'Consultorio Belgrano',
                status: "active",
                doctor: `${BASE_URL}/doctors/${doctorId}`,
                neighborhood: `${BASE_URL}/neighborhoods/1`,
                officeAvailability: `${BASE_URL}/doctors/${doctorId}/offices/1/availability`,
                officeSpecialties: `${BASE_URL}/doctors/${doctorId}/offices/1/specialties`,
                self: `${BASE_URL}/doctors/${doctorId}/offices/1`
            },
            {
                name: 'Consultorio Palermo',
                status: "inactive",
                doctor: `${BASE_URL}/doctors/${doctorId}`,
                neighborhood: `${BASE_URL}/neighborhoods/2`,
                officeAvailability: `${BASE_URL}/doctors/${doctorId}/offices/2/availability`,
                officeSpecialties: `${BASE_URL}/doctors/${doctorId}/offices/2/specialties`,
                self: `${BASE_URL}/doctors/${doctorId}/offices/2`
            }
        ];

        if (status) {
            offices = offices.filter(o => o.status === status);
        }

        return HttpResponse.json(offices);
    }),

    http.get(`${BASE_URL}/doctors/:doctorId/offices/:officeId`, ({ params }) => {
        const { doctorId, officeId } = params;

        if (officeId === "error") return new HttpResponse(null, { status: 500 });

        return HttpResponse.json({
            name: `Office ${officeId}`,
            status: "active",
            doctor: `${BASE_URL}/doctors/${doctorId}`,
            neighborhood: `${BASE_URL}/neighborhoods/1`,
            officeAvailability: `${BASE_URL}/doctors/${doctorId}/offices/${officeId}/availability`,
            officeSpecialties: `${BASE_URL}/doctors/${doctorId}/offices/${officeId}/specialties`,
            self: `${BASE_URL}/doctors/${doctorId}/offices/${officeId}`
        });
    }),

    http.get(`${BASE_URL}/doctors/:doctorId/offices/:officeId/specialties`, ({ params }) => {
        const { doctorId, officeId } = params;

        return HttpResponse.json([
            {
                specialty: `${BASE_URL}/specialties/1`,
                office: `${BASE_URL}/doctors/${doctorId}/offices/${officeId}`
            },
            {
                specialty: `${BASE_URL}/specialties/2`,
                office: `${BASE_URL}/doctors/${doctorId}/offices/${officeId}`
            }
        ]);
    }),

    http.get(`${BASE_URL}/doctors/:doctorId/availability`, ({ params, request }) => {
        const { doctorId } = params;
        const url = new URL(request.url);
        const officeId = url.searchParams.get('officeId');

        if (doctorId === "error") return new HttpResponse(null, { status: 500 });

        const availabilities = [
            {
                dayOfWeek: 1,
                startTime: "09:00:00",
                endTime: "18:00:00",
                office: `${BASE_URL}/doctors/${doctorId}/offices/1`
            },
            {
                dayOfWeek: 3,
                startTime: "10:00:00",
                endTime: "14:00:00",
                office: `${BASE_URL}/doctors/${doctorId}/offices/2`
            }
        ];

        if (officeId) {
            if (officeId === '1') {
                return HttpResponse.json([availabilities[0]]);
            }
            if (officeId === '2') {
                return HttpResponse.json([availabilities[1]]);
            }
        }

        return HttpResponse.json(availabilities);
    }),

    http.post(`${BASE_URL}/doctors/:doctorId/offices`, ({ params }) => {
        const { doctorId } = params;
        const newId = 999;

        return new HttpResponse(null, {
            status: 201,
            headers: {
                'Location': `${BASE_URL}/doctors/${doctorId}/offices/${newId}`
            }
        });
    }),

    http.patch(`${BASE_URL}/doctors/:doctorId/offices/:officeId`, () => {
        return new HttpResponse(null, { status: 204 });
    }),

    http.delete(`${BASE_URL}/doctors/:doctorId/offices/:officeId`, () => {
        return new HttpResponse(null, { status: 204 });
    }),

    http.put(`${BASE_URL}/doctors/:doctorId/availability`, () => {
        return new HttpResponse(null, { status: 204 });
    })
];