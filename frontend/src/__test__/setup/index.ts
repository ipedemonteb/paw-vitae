import { appointmentHandlers } from '../mocks/AppointmentsApiMock';
import {authHandlers} from "@/__test__/mocks/AuthApiMock.ts";
import {coveragesHandlers} from "@/__test__/mocks/CoveragesApiMock.ts";

export const handlers = [
    ...appointmentHandlers,
    ...authHandlers,
    ...coveragesHandlers
];