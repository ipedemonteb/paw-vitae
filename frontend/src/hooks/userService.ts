import type { AxiosInstance } from "axios";

export function useUserService(api: AxiosInstance) {

    const uploadDoctorImage = async (uploadUrl: string, image: File) => {
        const formData = new FormData();
        formData.append('image', image);
        return api.put(uploadUrl,formData);
    }

    return {
        uploadDoctorImage
    };
}