import {http, HttpResponse} from 'msw';

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


    }),

    http.get(`${BASE_URL}/appointments`, ({ request }) => {
        const url = new URL(request.url);
        const page = Number(url.searchParams.get('page') ?? '1');
        const filter = url.searchParams.get('filter');



        let responseBody = [
            {
                id: '1',
                date: '2026-02-01T10:00:00',
                status: 'completo',
                reason: 'Dolor de cabeza',
                allowFullHistory: true,
                report: 'Paciente estable',
                cancellable: false,
                self: `${BASE_URL}/appointments/1`,
                doctor: `${BASE_URL}/doctors/1`,
                patient: `${BASE_URL}/patients/10`,
                specialty: `${BASE_URL}/specialties/cardio`,
                doctorOffice: `${BASE_URL}/offices/5`,
                appointmentFiles: `${BASE_URL}/appointments/1/files`,
                rating: `${BASE_URL}/ratings/1`
            },
            {
                id: '2',
                date: '2026-02-02T14:30:00',
                status: 'confirmado',
                reason: 'Chequeo anual',
                allowFullHistory: false,
                report: '',
                cancellable: true,
                self: `${BASE_URL}/appointments/2`,
                doctor: `${BASE_URL}/doctors/2`,
                patient: `${BASE_URL}/patients/10`,
                specialty: `${BASE_URL}/specialties/derma`,
                doctorOffice: `${BASE_URL}/offices/6`,
                appointmentFiles: `${BASE_URL}/appointments/2/files`,
                rating: null
            }
        ];
        if (filter === 'cancelled') {
            responseBody = [];
        }


        const prevPage = page > 1 ? page - 1 : 1;
        const nextPage = page + 1;
        const lastPage = 5;

        const linkHeader = [
            `<${BASE_URL}/appointments?page=${nextPage}>; rel="next"`,
            `<${BASE_URL}/appointments?page=${prevPage}>; rel="prev"`,
            `<${BASE_URL}/appointments?page=1>; rel="first"`,
            `<${BASE_URL}/appointments?page=${lastPage}>; rel="last"`
        ].join(', ');

        return HttpResponse.json(responseBody, {
            headers: {
                'x-total-count': '50',
                'link': linkHeader,
                'content-type': 'application/json'
            }
        });
    }),

    //Appointment files recibe un app id en el URL
    http.get(`${BASE_URL}/appointments/:id/files`, ({ params }) => {

        const { id } = params;
        if (id === 'error') {
            return new HttpResponse(null, { status: 500 });
        }
        if(id === 'empty') return HttpResponse.json([]);
        return HttpResponse.json([
            {
                id: 'file-100',
                fileName: 'Radiografia_Torax.pdf',
                uploaderRole: 'doctor',

                download: `${BASE_URL}/files/100/download`,
                view: `${BASE_URL}/files/100/view`
            },
            {
                id: 'file-101',
                fileName: 'Estudios_Previos.jpg',
                uploaderRole: 'patient',
                download: `${BASE_URL}/files/101/download`,
                view: `${BASE_URL}/files/101/view`
            }
        ]);

    }),


    http.post(`${BASE_URL}/appointments`, async ({}) => {
        // const body = await request.json();

        return HttpResponse.json(
            { status: 'created' },
            {
                status: 201,
                headers: {
                    'Location': `${BASE_URL}/appointments/123`,
                    'Content-Type': 'application/json'
                }
            }
        );
    }),

    http.post(`${BASE_URL}/appointments/:id/files/patient`, () => {
        return HttpResponse.json({ status: 'uploaded' }, { status: 201 });
    }),


    http.patch(`${BASE_URL}/appointments/:id`, ({ request, params }) => {
        const { id } = params;

        if (id === 'error') {
            return new HttpResponse(null, { status: 500 });
        }

        // (Axios convierte los headers a minúsculas)
        const contentType = request.headers.get('content-type');

        // CASO A: CANCELACIÓN
        if (contentType && contentType.includes('cancel')) {
            // const url = new URL(request.url);
            // if (!url.searchParams.get('userId')) return new HttpResponse(null, { status: 400 });

            return HttpResponse.json({ status: 'cancelled' }, { status: 200 });
        }

        // CASO B: ACTUALIZAR REPORTE
        if (contentType && contentType.includes('report')) {
            return HttpResponse.json({ status: 'report_updated' }, { status: 200 });
        }

        // CASO DEFAULT (Por si acaso)
        return HttpResponse.json({ status: 'ok' }, { status: 200 });
    }),


];