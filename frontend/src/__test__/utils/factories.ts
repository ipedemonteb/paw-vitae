import type {DoctorDTO} from "@/data/doctors.ts";
import type {PatientDTO} from "@/data/patients.ts";
import {BASE_URL} from "@/__test__/utils/utils.ts";

// 1. FACTORY DE DOCTOR
// Acepta "overrides"  parciales para cambiar solo lo que necesitemos
export function createMockDoctor(overrides: Partial<DoctorDTO> = {}): DoctorDTO {
    return {
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
        self: `${BASE_URL}/doctors/1`,
        ...overrides
    };
}

// 2. FACTORY DE PACIENTE
export function createMockPatient(overrides: Partial<PatientDTO> = {}): PatientDTO {
    return {
        name: 'Pepe',
        lastName: 'Argento',
        email: 'pepe@racing.com',
        phone: '555-1234',
        coverages: `${BASE_URL}/patients/1/coverages`,
        neighborhood: `${BASE_URL}/neighborhoods/1`,
        appointments: `${BASE_URL}/patients/1/appointments`,
        self: `${BASE_URL}/patients/1`,
        ...overrides
    };
}