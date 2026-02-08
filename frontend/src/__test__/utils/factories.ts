import type {DoctorDTO} from "@/data/doctors.ts";
import type {PatientDTO} from "@/data/patients.ts";

// 1. FACTORY DE DOCTOR
// Acepta "overrides"  parciales para cambiar solo lo que necesitemos
export function createMockDoctor(overrides: Partial<DoctorDTO> = {}): DoctorDTO {
    return {
        name: 'Gregory',
        lastName: 'House',
        email: 'house@princeton.edu',
        phone: '555-0199',
        rating: 4.5,
        ratingCount: 150,
        specialties: 'http://api/specialties/1',
        coverages: 'http://api/coverages/1',
        offices: 'http://api/offices/1',
        profile: 'http://api/doctors/1/profile',
        experiences: 'http://api/doctors/1/experiences',
        certifications: 'http://api/doctors/1/certifications',
        ratings: 'http://api/doctors/1/ratings',
        appointments: 'http://api/doctors/1/appointments',
        image: 'http://api/images/doctor1.jpg',
        unavailability: 'http://api/doctors/1/unavailability',
        self: 'http://api/doctors/1',
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
        coverages: 'http://api/patients/1/coverages',
        neighborhood: 'http://api/neighborhoods/1',
        appointments: 'http://api/patients/1/appointments',
        self: 'http://api/patients/1',
        ...overrides
    };
}