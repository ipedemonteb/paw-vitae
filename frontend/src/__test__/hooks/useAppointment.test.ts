import { describe, it, expect, vi, afterEach } from 'vitest';
import { renderHook, waitFor } from '../setup/utils';
import {
    useAppointment,
    useAppointmentFiles,
    useAppointments,
    useBookAppointmentMutation, useCancelAppointmentMutation, useUpdateReportMutation, useUploadDoctorFilesMutation
} from "@/hooks/useAppointments.ts";
import {act} from "react";
import * as appointmentsApi from '@/data/appointments';
//TODO: La idea es que hagamos un describe grande por archivo de hooks. Uno describe por hook y despues un it por caso

describe('useAppointments File', () => {

    afterEach(() => {
        vi.clearAllMocks(); // Reinicia los contadores de llamadas a 0
    });

    describe('useAppointment Hook', () => {

        it('debería retornar el DTO con las URLs HATEOAS correctas', async () => {
            const {result} = renderHook(() => useAppointment('1'));

            expect(result.current.isLoading).toBe(true);

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            const data = result.current.data;

            expect(data?.status).toBe('completo');
            expect(data?.reason).toBe('Consulta general');

            expect(data?.doctor).toBeTypeOf('string');
            expect(data?.doctor).toContain('/doctors/1');
        });

        it('debería manejar errores del servidor (500)', async () => {
            const {result} = renderHook(() => useAppointment('error'));
            await waitFor(() => expect(result.current.isError).toBe(true));
        });

        it('debería incluir la URL de rating si el turno ya fue calificado', async () => {
            const {result} = renderHook(() => useAppointment('rated-1'));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data?.rating).toBeDefined();
            expect(result.current.data?.rating).toContain('/ratings/99');
        });

        it('no debería tener rating si el turno está completo pero sin calificar', async () => {
            const {result} = renderHook(() => useAppointment('unrated-1'));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data?.rating).toBeFalsy();
        });

        it('no debería tener rating si el turno no está completo', async () => {
            const {result} = renderHook(() => useAppointment('uncompleted-1'));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data?.rating).toBeFalsy();
        });
    });

    //TODO: mas casos?
    describe('useAppointments Hook', () => {

        it('debería retornar una lista de turnos y la metadata de paginación', async () => {
            const {result} = renderHook(() => useAppointments({page: 1, filter: 'all', userId: '1'}));

            await waitFor(() => expect(result.current.isPending).toBe(false));

            expect(result.current.isSuccess).toBe(true);

            const appointments = result.current.data?.data;
            expect(appointments).toHaveLength(2);
            expect(appointments?.[0].reason).toBe('Dolor de cabeza');

            const pagination = result.current.data?.pagination;
            expect(pagination?.total).toBe(50);
            expect(pagination?.next).toBeDefined();
            expect(pagination?.next).toContain('page=2');
        });

        it('debería retornar lista vacía si aplicamos un filtro sin resultados', async () => {
            //Pedimos filtro 'cancelled'
            const {result} = renderHook(() => useAppointments({page: 1, filter: 'cancelled', userId: '1'}));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            const appointments = result.current.data?.data;
            expect(appointments).toHaveLength(0);
            expect(result.current.data?.pagination.total).toBe(50);
        });

    });


    describe('useAppointmentsFiles Hook', () => {

        it('deberia retornar un array vacio si no hay archivos', async () => {
            const {result} = renderHook(() => useAppointmentFiles('appointments/empty/files'));
            await waitFor(() => expect(result.current.isSuccess).toBe(true));
            expect(result.current.data).toHaveLength(0);
        })

        it('debería manejar errores del servidor (500)', async () => {
            const {result} = renderHook(() => useAppointmentFiles('appointments/error/files'));
            await waitFor(() => expect(result.current.isError).toBe(true));
        });

        it('deberia retornar un array con archivos de un turno ', async () => {
            const {result} = renderHook(() => useAppointmentFiles('appointments/1/files'));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));
            expect(result.current.data).toHaveLength(2);

            const files = result.current.data;

            expect(files?.[0].fileName).toBe('Radiografia_Torax.pdf');
            expect(files?.[1].id).toBe('file-101');


        });

        it('NO debería ejecutarse si la URL es undefined o vacía', async () => {
            const {result} = renderHook(() => useAppointmentFiles(undefined));


            expect(result.current.isPending).toBe(true);
            expect(result.current.fetchStatus).toBe('idle');

            expect(result.current.data).toBeUndefined();
        });

    });

    describe('useBookAppointmentMutation', () => {

        it('debería crear un turno sin archivos y retornar el nuevo ID', async () => {
            const {result} = renderHook(() => useBookAppointmentMutation());

            const mockForm = {
                appointmentDate: '2026-02-10',
                appointmentHour: '10:00',
                reason: 'Dolor de muela',
                doctorId: '1',
                patientId: '1',
                specialtyId: '1',
                officeId: '1',
                allowFullHistory: true

            } as any;


            act(() => {
                result.current.mutate({form: mockForm, files: []});
            });

            await waitFor(() => {expect(result.current.isSuccess).toBe(true);});


            expect(result.current.data).toBe('123');
        });

        it('debería crear un turno, subir archivos y retornar el nuevo ID', async () => {
            const {result} = renderHook(() => useBookAppointmentMutation());


            const uploadSpy = vi.spyOn(appointmentsApi, 'uploadAppointmentFile')
                .mockResolvedValue({} as any);

            const mockForm = {
                appointmentDate: '2026-02-10',
                appointmentHour: '10:00',
                reason: 'Dolor de muela',
                doctorId: '1',
                patientId: '1',
                specialtyId: '1',
                officeId: '1',
                allowFullHistory: true
            } as any;

            const files = [new File(['dummy'], 'test.pdf', { type: 'application/pdf' })];

            act(() => {
                result.current.mutate({form: mockForm, files: files});
            });

            await waitFor(() => {
                expect(result.current.isSuccess).toBe(true);
            });

            expect(result.current.data).toBe('123');

            //TODO: Explota cuando subo un archivo por alguina razon la solucioon es usar el spy
            // Esto confirma que tu loop "for (const file of files)" funcionó
            expect(uploadSpy).toHaveBeenCalledTimes(1);
            expect(uploadSpy).toHaveBeenCalledWith('123', files[0], 'patient');
        });

        it('debería fallar si el servidor no devuelve el header Location', async () => {
            const {server} = await import('../setup/setup');
            const {http, HttpResponse} = await import('msw');

            server.use(
                http.post('http://localhost:8080/api/appointments', () => {
                    return HttpResponse.json({}, {status: 201}); // Sin headers
                })
            );

            const {result} = renderHook(() => useBookAppointmentMutation());

            const mockForm = {doctorId: '1'} as any;

            act(() => {
                result.current.mutate({form: mockForm, files: []});
            });

            await waitFor(() => expect(result.current.isError).toBe(true));

            expect(result.current.error?.message).toBe("No se pudo obtener el ID del turno creado");
        });
    });


    describe('useUpdateReportMutation',  () => {

        it('Deberia updatear correctamente el reporte y devolver updated', async () => {
            const { result } = renderHook(() => useUpdateReportMutation());

            const updateData = {
                id: '1',
                report: 'El paciente presenta leve mejoría.'
            };

            act(() => {
                result.current.mutate(updateData);
            });

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

        });

        it('Deberia fallar y manejar el 500 si el id es error', async () => {
            const { result } = renderHook(() => useUpdateReportMutation());

            act(() => {
                result.current.mutate({ id: 'error', report: 'Test fail' });
            });

            await waitFor(() => expect(result.current.isError).toBe(true));
        });

    });


    //TODO: revisar
    describe('useUploadDoctorFilesMutation', () => {

        it('Deberia subir correctamente los archivos con el rol de DOCTOR', async () => {
            const uploadSpy = vi.spyOn(appointmentsApi, 'uploadAppointmentFile')
                .mockResolvedValue({} as any);

            const { result } = renderHook(() => useUploadDoctorFilesMutation());

            const files = [new File(['dummy'], 'test.pdf', { type: 'application/pdf' })];
            const appointmentId = '123';

            act(() => {
                result.current.mutate({ id: appointmentId, files: files });
            });

            await waitFor(() => {
                expect(result.current.isSuccess).toBe(true);
            });

            expect(uploadSpy).toHaveBeenCalledTimes(1);

            expect(uploadSpy).toHaveBeenCalledWith(appointmentId, files[0], 'doctor');
        });

    });

    describe('useCancelAppointmentMutation', () => {

        it('Deberia cancelar correctamente el turno', async () => {
            const { result } = renderHook(() => useCancelAppointmentMutation());

            const variables = { id: '1', userId: '1' };

            act(() => {
                result.current.mutate(variables);
            });

            await waitFor(() => expect(result.current.isSuccess).toBe(true));


        })
        it('Debería manejar errores del servidor al cancelar (500)', async () => {
            const { result } = renderHook(() => useCancelAppointmentMutation());

            // Usamos ID 'error' para detonar el fallo en MSW
            act(() => {
                result.current.mutate({ id: 'error', userId: '1' });
            });

            await waitFor(() => expect(result.current.isError).toBe(true));
        });



    })

    //TODO: Falto testear useAppointmentFileHandlerMutation pero no le veo uso




})