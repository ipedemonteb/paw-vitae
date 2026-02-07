import { appointmentHandlers } from '../mocks/AppointmentsApiMock';
import {authHandlers} from "@/__test__/mocks/AuthApiMock.ts";
import {coveragesHandlers} from "@/__test__/mocks/CoveragesApiMock.ts";
import {neighborhoodHandlers} from "@/__test__/mocks/NeighborhoodsApiMock.ts";

export const handlers = [
    ...appointmentHandlers,
    ...authHandlers,
    ...coveragesHandlers,
    ...neighborhoodHandlers
];