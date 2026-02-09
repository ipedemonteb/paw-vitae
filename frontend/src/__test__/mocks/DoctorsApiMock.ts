import { http, HttpResponse } from "msw";
import { BASE_URL } from "@/__test__/utils/utils.ts";
import { createMockDoctor } from "@/__test__/utils/factories.ts";


export const doctorsHandlers = [

    http.get(`${BASE_URL}/doctors`, () => {
        return HttpResponse.json([
            {
                name: 'Gregory',
                lastName: 'House',
                email: 'house@princeton.edu',
                phone: '555-0199',
                rating: 4,
                ratingCount: 150,
                specialties: `${BASE_URL}/doctors/1/specialties`,
                coverages: `${BASE_URL}/doctors/1/coverages`,
                offices: `${BASE_URL}/doctors/1/offices`,
                profile: `${BASE_URL}/doctors/1/profile`,
                experiences: `${BASE_URL}/doctors/1/experiences`,
                certifications: `${BASE_URL}/doctors/1/certifications`,
                ratings: `${BASE_URL}/doctors/1/ratings`,
                appointments: `${BASE_URL}/doctors/1/appointments`,
                image: `${BASE_URL}/doctors/1/image`,
                unavailability: `${BASE_URL}/doctors/1/unavailability`,
                self: `${BASE_URL}/doctors/1`
            },
            {
                name: 'John',
                lastName: 'Smith',
                email: 'jsmith@princeton.edu',
                phone: '555-01199',
                rating: 5,
                ratingCount: 150,
                specialties: `${BASE_URL}/doctors/2/specialties`,
                coverages: `${BASE_URL}/doctors/2/coverages`,
                offices: `${BASE_URL}/doctors/2/offices`,
                profile: `${BASE_URL}/doctors/2/profile`,
                experiences: `${BASE_URL}/doctors/2/experiences`,
                certifications: `${BASE_URL}/doctors/2/certifications`,
                ratings: `${BASE_URL}/doctors/2/ratings`,
                appointments: `${BASE_URL}/doctors/2/appointments`,
                image: `${BASE_URL}/doctors/2/image`,
                unavailability: `${BASE_URL}/doctors/2/unavailability`,
                self: `${BASE_URL}/doctors/2`
            },
            {
                name: 'Martha',
                lastName: 'Smith',
                email: 'msmith@princeton.edu',
                phone: '5535-0199',
                rating: 3,
                ratingCount: 10,
                specialties: `${BASE_URL}/doctors/3/specialties`,
                coverages: `${BASE_URL}/doctors/3/coverages`,
                offices: `${BASE_URL}/doctors/3/offices`,
                profile: `${BASE_URL}/doctors/3/profile`,
                experiences: `${BASE_URL}/doctors/3/experiences`,
                certifications: `${BASE_URL}/doctors/3/certifications`,
                ratings: `${BASE_URL}/doctors/3/ratings`,
                appointments: `${BASE_URL}/doctors/3/appointments`,
                image: `${BASE_URL}/doctors/3/image`,
                unavailability: `${BASE_URL}/doctors/3/unavailability`,
                self: `${BASE_URL}/doctors/3`
            }
        ], {
            headers: {
                'x-total-count': '3'
            }
        });
    }),

    http.head(`${BASE_URL}/doctors`, () => {
        return new HttpResponse(null, {
            headers: {
                'x-total-count': '3'
            }
        });
    }),

    http.get(`${BASE_URL}/doctors/:id`, ({ params }) => {
        const { id } = params;
        if (id === "error") return new HttpResponse(null, { status: 500 });
        return HttpResponse.json(createMockDoctor({
            image: `${BASE_URL}/doctors/${id}/image`
        }));
    }),

    http.get(`${BASE_URL}/doctors/:id/image`, () => {
        const imageBlob = new Blob(['fake-image-content'], { type: 'image/jpeg' });
        return new HttpResponse(imageBlob, {
            headers: {
                'Content-Type': 'image/jpeg',
            }
        });
    }),

    http.get(`${BASE_URL}/doctors/:id/specialties`, () => {
        return HttpResponse.json([
            { name: 'Cardiologia', self: `${BASE_URL}/specialties/1` },
            { name: 'Pediatria', self: `${BASE_URL}/specialties/2` },
            { name: 'Neurologia', self: `${BASE_URL}/specialties/5` }
        ]);
    }),

    http.get(`${BASE_URL}/doctors/:id/coverages`, () => {
        return HttpResponse.json([
            { name: 'Galeno', self: `${BASE_URL}/coverages/1` },
            { name: 'Medife', self: `${BASE_URL}/coverages/2` }
        ]);
    }),

    http.get(`${BASE_URL}/doctors/:id/experiences`, ({ params }) => {
        const { id } = params;
        return HttpResponse.json([
            {
                positionTitle: 'Doctor',
                organizationName: 'Princeton University',
                startDate: '2010-01-01',
                endDate: '2021-01-01',
                doctor: `${BASE_URL}/doctors/${id}`
            }
        ]);
    }),

    http.get(`${BASE_URL}/doctors/:id/certifications`, ({ params }) => {
        const { id } = params;
        return HttpResponse.json([
            {
                certificateName: 'Certificacion 1',
                issuingEntity: 'Hospital General de Princeton',
                issueDate: '2021-01-01',
                doctor: `${BASE_URL}/doctors/${id}`
            },
            {
                certificateName: 'Certificacion 2',
                issuingEntity: 'Hospital General de Princeton',
                issueDate: '2021-01-01',
                doctor: `${BASE_URL}/doctors/${id}`
            }
        ]);
    }),

    http.get(`${BASE_URL}/doctors/:id/profile`, ({ params }) => {
        const { id } = params;
        return HttpResponse.json({
            bio: 'General Doctor in Princeton University',
            description: 'Been a doctor for 10 years.',
            doctor: `${BASE_URL}/doctors/${id}`
        });
    }),

    http.get(`${BASE_URL}/doctors/:id/unavailability`, ({ params }) => {
        const { id } = params;
        return HttpResponse.json([
            {
                startDate: '2021-01-01',
                endDate: '2021-01-02',
                doctor: `${BASE_URL}/doctors/${id}`
            },
            {
                startDate: '2021-01-03',
                endDate: '2021-01-04',
                doctor: `${BASE_URL}/doctors/${id}`
            }
        ], {
            headers: {
                'x-total-count': '2'
            }
        });
    }),

    http.post(`${BASE_URL}/doctors`, () => {
        return HttpResponse.json(
            { status: 'created' },
            {
                status: 201,
                headers: {
                    'Location': `${BASE_URL}/doctors/999`
                }
            }
        );
    }),

    http.patch(`${BASE_URL}/doctors/:id`, () => {
        return new HttpResponse(null, { status: 204 });
    }),

    http.put(`${BASE_URL}/doctors/:id/certifications`, () => {
        return new HttpResponse(null, { status: 204 });
    }),

    http.put(`${BASE_URL}/doctors/:id/experiences`, () => {
        return new HttpResponse(null, { status: 204 });
    }),

    http.put(`${BASE_URL}/doctors/:id/biography`, () => {
        return new HttpResponse(null, { status: 204 });
    }),

    http.put(`${BASE_URL}/doctors/:id/image`, () => {
        return new HttpResponse(null, { status: 204 });
    }),

    http.put(`${BASE_URL}/doctors/:id/unavailability`, () => {
        return new HttpResponse(null, { status: 204 });
    })
];