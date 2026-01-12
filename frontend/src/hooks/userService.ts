import type { AxiosInstance } from "axios";

export interface PatientRegisterData {
    name: string;
    lastName: string;
    email: string;
    phone: string;
    password: string;
    repeatPassword: string;
    coverageUrl: string;
    neighborhoodUrl: string;
}

export interface DoctorRegisterData {
    name: string;
    lastName: string;
    email: string;
    phone: string;
    password: string;
    repeatPassword: string;
    image: File | null;
    selectedSpecialties: string[];
    selectedCoverages: string[];
}

const extractIdFromUrl = (url: string): string => {
    if (!url) return "";
    const segments = url.replace(/\/$/, "").split("/");
    return segments.pop() || "";
}

export function useUserService(api: AxiosInstance) {

    const login = async (url: string, email: string, password: string): Promise<{ success: boolean; jwt?: string; refreshToken?: string }> => {
        const credentials = btoa(`${email}:${password}`);

        try {
            const response = await api.head(url, {
                headers: {
                    "Accept": "application/json",
                    "Authorization": `Basic ${credentials}`
                }
            });
            const jwt = response.headers["x-vitae-authtoken"];
            const refreshToken = response.headers["x-vitae-refreshtoken"];

            if (jwt && refreshToken) {
                return { success: true, jwt, refreshToken };
            } else {
                return { success: false };
            }
        } catch (error) {
            console.error(error);
            return { success: false };
        }
    };

    const registerPatient = async (data: PatientRegisterData) => {
        const coverageId = extractIdFromUrl(data.coverageUrl);
        const neighborhoodId = extractIdFromUrl(data.neighborhoodUrl);

        if (!coverageId || !neighborhoodId) {
            throw new Error("Invalid IDs for coverage or neighborhood");
        }

        const payload = {
            name: data.name,
            lastName: data.lastName,
            email: data.email,
            password: data.password,
            repeatPassword: data.repeatPassword,
            phone: data.phone,
            coverage: parseInt(coverageId),       // Backend espera Long
            neighborhoodId: parseInt(neighborhoodId) // Backend espera Long
        };

        return api.post('/patients', payload,
            { headers: { 'Content-Type': 'application/vnd.vitae.patient.v1+json' } });
    };

    const registerDoctor = async (data: DoctorRegisterData) => {
        const specialtyIds: number[] = [];
        data.selectedSpecialties.forEach(url => {
            const id = extractIdFromUrl(url);
            if (id) specialtyIds.push(parseInt(id));
        });

        const coverageIds: number[] = [];
        data.selectedCoverages.forEach(url => {
            const id = extractIdFromUrl(url);
            if (id) coverageIds.push(parseInt(id));
        });

        const payload = {
            name: data.name,
            lastName: data.lastName,
            email: data.email,
            phone: data.phone,
            password: data.password,
            repeatPassword: data.repeatPassword,
            specialties: specialtyIds,
            coverages: coverageIds
        };


        return api.post('/doctors', payload, {
            headers: {
                'Content-Type': 'application/vnd.vitae.doctor.v1+json',
            }
        });
    };

    const uploadDoctorImage = async (uploadUrl: string, image: File) => {
        const formData = new FormData();
        formData.append('image', image);
        return api.put(uploadUrl, formData);
    }

    return {
        login,
        registerPatient,
        registerDoctor,
        uploadDoctorImage
    };
}