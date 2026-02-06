import '@testing-library/jest-dom';
import { cleanup } from '@testing-library/react';
import { beforeAll, afterEach, afterAll } from 'vitest';
import { setupServer } from 'msw/node';
import {handlers} from "./index";


export const server = setupServer(...handlers); //Aca van todos los handlers que vayamos haciendo en mocks

beforeAll(() => server.listen({ onUnhandledRequest: 'error' }));

afterEach(() => {
    server.resetHandlers();
    cleanup(); // failsafe por si vitest no limpia bien los DOMS
});

afterAll(() => server.close());