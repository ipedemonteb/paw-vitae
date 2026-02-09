import { appointmentHandlers } from '../mocks/AppointmentsApiMock';
import {authHandlers} from "@/__test__/mocks/AuthApiMock.ts";
import {createCatalogHandlers} from "@/__test__/mocks/factories.ts";
import {userHandlers} from "@/__test__/mocks/UserApiMock.ts";
import {slotsHandler} from "@/__test__/mocks/SlotsApiMock.ts";
import {ratingHandler} from "@/__test__/mocks/RatingApiMock.ts";

export const handlers = [
    ...appointmentHandlers,
    ...authHandlers,
    ...createCatalogHandlers('coverages', [
        { id: '1', name: 'Medifé' },
        { id: '2', name: 'Galeno' },
        { id: '3', name: 'OSDE' }
    ]),

    ...createCatalogHandlers('neighborhoods', [
        { id: '1', name: 'Belgrano' },
        { id: '2', name: 'Recoleta' },
        { id: '3', name: 'Palermo' }
    ]),
    ...createCatalogHandlers('specialties', [
        { id: '1', name: 'Cardiología' },
        { id: '2', name: 'Dermatología' },
        { id: '3', name: 'Pediatría' },
        { id: '4', name: 'Neurología' }
    ]),
    ...userHandlers,
    ...slotsHandler,
    ...ratingHandler
];