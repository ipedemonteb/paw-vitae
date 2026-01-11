export const ContentTypes = {
    HOME: "application/vnd.vitae.home.v1+json",
    DOCTOR: "application/vnd.vitae.doctor.v1+json",
    DOCTOR_LIST: "application/vnd.vitae.doctor-list.v1+json",
    DOCTOR_PROFILE: "application/vnd.vitae.doctor-profile.v1+json",
    DOCTOR_EXPERIENCE_LIST: "application/vnd.vitae.doctor-experience-list.v1+json",
    DOCTOR_CERTIFICATION_LIST: "application/vnd.vitae.doctor-certification-list.v1+json",
    PATIENT: "application/vnd.vitae.patient.v1+json",
    PATIENT_LIST: "application/vnd.vitae.patient-list.v1+json",
    APPOINTMENT: "application/vnd.vitae.appointment.v1+json",
    APPOINTMENT_LIST: "application/vnd.vitae.appointment-list.v1+json",
    APPOINTMENT_REPORT: "application/vnd.vitae.appointment-report.v1+json",
    APPOINTMENT_CANCEL: "application/vnd.vitae.appointment-cancel.v1+json",
    APPOINTMENT_FILE: "application/vnd.vitae.appointment-file.v1+json",
    APPOINTMENT_FILE_LIST: "application/vnd.vitae.appointment-file-list.v1+json",
    OFFICE: "application/vnd.vitae.office.v1+json",
    OFFICE_LIST: "application/vnd.vitae.office-list.v1+json",
    AVAILABILITY_LIST: "application/vnd.vitae.availability-list.v1+json",
    UNAVAILABILITY_LIST: "application/vnd.vitae.unavailability-list.v1+json",
    SPECIALTY: "application/vnd.vitae.specialty.v1+json",
    SPECIALTY_LIST: "application/vnd.vitae.specialty-list.v1+json",
    OFFICE_SPECIALTY_LIST: "application/vnd.vitae.office-specialty-list.v1+json",
    NEIGHBORHOOD: "application/vnd.vitae.neighborhood.v1+json",
    NEIGHBORHOOD_LIST: "application/vnd.vitae.neighborhood-list.v1+json",
    COVERAGE: "application/vnd.vitae.coverage.v1+json",
    COVERAGE_LIST: "application/vnd.vitae.coverage-list.v1+json",
    RATING: "application/vnd.vitae.rating.v1+json",
    RATING_LIST: "application/vnd.vitae.rating-list.v1+json",
    USER_PASSWORD: "application/vnd.vitae.user-password.v1+json",
    JSON: "application/json",
    MULTIPART: "multipart/form-data",
    OCTET_STREAM: "application/octet-stream",
    PDF: "application/pdf",
    IMAGE_PNG: "image/png",
    IMAGE_JPEG: "image/jpeg",
    IMAGE_JPG: "image/image/jpg",

} as const;

export type ContentType = (typeof ContentTypes)[keyof typeof ContentTypes];

export function buildHeaders(
    contentType?: string | null,
    acceptType?: string | null,
    token: string | null = null
): Record<string, string> {
    const headers: Record<string, string> = {
        Accept: acceptType || ContentTypes.JSON,
    };

    if (contentType && contentType !== ContentTypes.MULTIPART) {
        headers["Content-Type"] = contentType;
    }

    if (token) {
        headers["Authorization"] = `Bearer ${token}`;
    }

    return headers;
}
