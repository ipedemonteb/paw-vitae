import { describe, it, expect, vi, beforeAll, afterAll } from 'vitest';
import { renderHook, waitFor } from "@/__test__/setup/utils.tsx";
import { BASE_URL } from "@/__test__/utils/utils.ts";
import {
    useDoctors,
    useDoctor,
    useDoctorImageUrl,
    // useDoctorSpecialties,
    // useDoctorCoverages,
    useDoctorExperience,
    useDoctorCertifications,
    useDoctorBiography,
    useDoctorUnavailability,
    useDoctorsCount,
    useRegisterDoctorMutation,
    useUpdateDoctorProfileMutation,
    useUpdateDoctorImageMutation,
    useUpdateDoctorMutation, useCreateDoctorUnavailabilityMutation
} from "@/hooks/useDoctors.ts";
import * as doctorService from "@/data/doctors";
import type {UnavailabilityForm} from "@/data/doctors";


beforeAll(() => {
    globalThis.URL.createObjectURL = vi.fn(() => 'blob:mock-preview-url');
    globalThis.URL.revokeObjectURL = vi.fn();
});

afterAll(() => {
    vi.clearAllMocks();
});

describe('Doctors Hooks Integration Tests', () => {

    describe('useDoctors (List)', () => {
        it('should return the list of doctors paginated', async () => {
            const { result } = renderHook(() => useDoctors({ page: 1, pageSize: 10 }));

            expect(result.current.isLoading).toBe(true);
            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data?.data).toHaveLength(3);
            expect(result.current.data?.data[0].lastName).toBe('House');

            expect(result.current.data?.pagination.total).toBe(3);
        });
    });

    describe('useDoctor (Detail)', () => {
        it('should bring the details of a specific doctor', async () => {
            const { result } = renderHook(() => useDoctor('1'));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(result.current.data?.name).toBe('Gregory');
            expect(result.current.data?.email).toBe('house@princeton.edu');
        });
    });

    describe('useDoctorsCount', () => {
        it('should return the amount of total doctors (HEAD request)', async () => {
            const { result } = renderHook(() => useDoctorsCount());

            await waitFor(() => expect(result.current.isSuccess).toBe(true));
            expect(result.current.data).toBe(3);
        });
    });



    describe('useDoctorImageUrl', () => {
        it('should download the image and generate an object URl', async () => {

            const { result } = renderHook(() => useDoctorImageUrl('1'));

            await waitFor(() => {
                expect(result.current.url).toBe('blob:mock-preview-url');
            }, { timeout: 5000 });
        });



        it('should not execute if the ID is not numeric', async () => {
            const { result } = renderHook(() => useDoctorImageUrl('invalid'));
            expect(result.current.fetchStatus).toBe('idle');
            expect(result.current.url).toBeNull();
        });
    });


    describe('Sub-resource Hooks', () => {

        // it('should bring the doctor specialties', async () => {
        //     const url = `${BASE_URL}/doctors/1/specialties`;
        //     const { result } = renderHook(() => useDoctorSpecialties(url));
        //
        //     await waitFor(() => expect(result.current.isSuccess).toBe(true));
        //     expect(result.current.data).toHaveLength(3);
        //     expect(result.current.data![0].name).toBe('Cardiologia');
        // });

        // it('should bring the coverages', async () => {
        //     const url = `${BASE_URL}/doctors/1/coverages`;
        //     const { result } = renderHook(() => useDoctorCoverages(url));
        //
        //     await waitFor(() => expect(result.current.isSuccess).toBe(true));
        //     expect(result.current.data![0].name).toBe('Galeno');
        // });

        it('should bring the doctor profile/bio', async () => {
            const url = `${BASE_URL}/doctors/1/profile`;
            const { result } = renderHook(() => useDoctorBiography(url));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));
            expect(result.current.data?.bio).toContain('General Doctor');
        });

        it('should bring the experiences', async () => {
            const url = `${BASE_URL}/doctors/1/experiences`;
            const { result } = renderHook(() => useDoctorExperience(url));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));
            expect(result.current.data![0].organizationName).toBe('Princeton University');
        });

        it('should bring the certifications', async () => {
            const url = `${BASE_URL}/doctors/1/certifications`;
            const { result } = renderHook(() => useDoctorCertifications(url));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));
            expect(result.current.data![0].certificateName).toBe('Certificacion 1');
        });

        it('should bring unavailability dates', async () => {
            const url = `${BASE_URL}/doctors/1/unavailability`;
            const { result } = renderHook(() => useDoctorUnavailability(url));

            await waitFor(() => expect(result.current.isSuccess).toBe(true));
            expect(result.current.data?.data).toHaveLength(2);
        });
    });

    describe('Mutations', () => {

        it('should register a doctor', async () => {
            const { result } = renderHook(() => useRegisterDoctorMutation());

            const newDoctor = {
                name: 'Wilson',
                lastName: 'James',
                email: 'wilson@princeton.edu',
                password: 'password',
                repeatPassword: 'password',
                phone: '123456',
                selectedSpecialties: [`${BASE_URL}/specialties/1`],
                selectedCoverages: [`${BASE_URL}/coverages/1`]
            };

            result.current.mutate(newDoctor);
            await waitFor(() => expect(result.current.isSuccess).toBe(true));
        });

        it('should update doctor bio', async () => {
            const url = `${BASE_URL}/doctors/1/biography`;
            const { result } = renderHook(() => useUpdateDoctorProfileMutation(url));

            result.current.mutate({ biography: 'New Bio', description: 'New Desc' });
            await waitFor(() => expect(result.current.isSuccess).toBe(true));
        });

        it('should call the image service', async () => {
            //ACLARACIÓN:
            // ---------------------------------------------------------------------------
            // Mockeamos la función del servicio 'putDoctorImage' para evitar enviarla por la red simulada (MSW).
            //
            // EXPLICACIÓN:
            // En entornos de test (JSDOM/Node), la combinación de Axios + FormData a menudo falla al
            // generar correctamente los headers de 'multipart/form-data' (específicamente el 'boundary').
            // Esto causa que MSW rechace la petición, haciendo fallar el test aunque el código esté bien.
            // Al usar spyOn().mockResolvedValue(), saltamos la capa de red defectuosa y probamos
            // lo que realmente importa: que el hook llame a la función con los parámetros correctos.
            // ---------------------------------------------------------------------------
            const spy = vi.spyOn(doctorService, 'putDoctorImage').mockResolvedValue({ status: 'ok' });

            const url = `${BASE_URL}/doctors/1/image`;
            const { result } = renderHook(() => useUpdateDoctorImageMutation(url));

            const file = new File(['(⌐□_□)'], 'avatar.png', { type: 'image/png' });

            result.current.mutate(file);

            await waitFor(() => expect(result.current.isSuccess).toBe(true));

            expect(spy).toHaveBeenCalledWith(url, file);
        });

        it('should update the list of data and images at the same time', async () => {
            const spyUpdate = vi.spyOn(doctorService, 'updateDoctorProfileComplete').mockResolvedValue({ status: 'ok' });

            const { result } = renderHook(() => useUpdateDoctorMutation());
            const fakeFile = new File(['content'], 'test.jpg', { type: 'image/jpeg' });

            const params = {
                doctorUrl: `${BASE_URL}/doctors/1`,
                imageUrl: `${BASE_URL}/doctors/1/image`,
                doctorId: '1',
                data: { name: 'Gregory Updated' },
                imageFile: fakeFile
            };

            result.current.mutate(params);

            await waitFor(() => expect(result.current.isSuccess).toBe(true));
            expect(spyUpdate).toHaveBeenCalled();
        });

        it('should update the unavailability dates', async () => {
            const url = `${BASE_URL}/doctors/1/unavailability`;
            const { result } = renderHook(() => useCreateDoctorUnavailabilityMutation(url));

            const payload: UnavailabilityForm = {
                startDate: '2026-05-01',
                endDate: '2026-05-10'
            }

            result.current.mutate(payload);
            await waitFor(() => expect(result.current.isSuccess).toBe(true));
        });
    });
});