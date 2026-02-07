import { appointmentHandlers } from '../mocks/AppointmentsApiMock';
import {authHandlers} from "@/__test__/mocks/AuthApiMock.ts";

export const handlers = [
    ...appointmentHandlers,
    ...authHandlers
];