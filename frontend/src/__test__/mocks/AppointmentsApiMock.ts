import { http, HttpResponse } from 'msw';

const BASE_URL = 'http://localhost:8080/api';

export const appointmentHandlers = [
    http.get(`${BASE_URL}/appointments/:id`, ({ params }) => {
        const { id } = params;

        if (id === 'error') {
            return new HttpResponse(null, { status: 500 });
        }

        // Respuesta base común
        const baseResponse = {
            id: id,
            date: '2026-02-06T10:00:00',
            reason: 'Consulta general',
            allowFullHistory: true,
            report: 'Reporte médico...',
            cancellable: false,
            self: `${BASE_URL}/appointments/${id}`,
            doctor: `${BASE_URL}/doctors/1`,
            patient: `${BASE_URL}/patients/1`,
            specialty: `${BASE_URL}/specialties/1`,
            doctorOffice: `${BASE_URL}/offices/1`,
            appointmentFiles: `${BASE_URL}/appointments/${id}/files`
        };

        // CASO 1: Turno con Rating (Simulamos que el ID 'rated-1' tiene calificación)
        if (id === 'rated-1') {
            return HttpResponse.json({
                ...baseResponse,
                status: 'completo',
                rating: `${BASE_URL}/ratings/99`
            });
        }

        // CASO 2: Turno sin Rating (Simulamos que el ID 'unrated-1' NO tiene calificación)
        if (id === 'unrated-1') {
            return HttpResponse.json({
                ...baseResponse,
                status: 'completo',
                rating: undefined
            });
        }

        //CASO 3: Turno no completado (deberia tener status 'incompleto' NO tiene califaicacion)
        if ( id === 'uncompleted-1') {
            return HttpResponse.json({
                ...baseResponse,
                status: 'incompleto',
                rating: undefined
            });
        }


        // Default: Devolvemos uno completo estándar
        return HttpResponse.json({
            ...baseResponse,
            status: 'completo',
            rating: `${BASE_URL}/ratings/1`
        });


    })
];