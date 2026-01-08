
export const ContentTypes = {
    // --- HOME ---
    HOME: "application/vnd.vitae.home.v1+json",

    // --- DOCTORS ---
    DOCTOR: "application/vnd.vitae.doctor.v1+json",
    DOCTOR_LIST: "application/vnd.vitae.doctor-list.v1+json",

    // Doctor Sub-resources
    DOCTOR_PROFILE: "application/vnd.vitae.doctor-profile.v1+json",
    DOCTOR_EXPERIENCE_LIST: "application/vnd.vitae.doctor-experience-list.v1+json",
    DOCTOR_CERTIFICATION_LIST: "application/vnd.vitae.doctor-certification-list.v1+json",

    // --- PATIENTS ---
    PATIENT: "application/vnd.vitae.patient.v1+json",
    PATIENT_LIST: "application/vnd.vitae.patient-list.v1+json",

    // --- APPOINTMENTS ---
    APPOINTMENT: "application/vnd.vitae.appointment.v1+json",
    APPOINTMENT_LIST: "application/vnd.vitae.appointment-list.v1+json",
    APPOINTMENT_REPORT: "application/vnd.vitae.appointment-report.v1+json",

    // --- APPOINTMENT FILES ---
    APPOINTMENT_FILE: "application/vnd.vitae.appointment-file.v1+json",      // Respuesta de subida
    APPOINTMENT_FILE_LIST: "application/vnd.vitae.appointment-file-list.v1+json", // Lista de archivos

    // --- OFFICES ---
    OFFICE: "application/vnd.vitae.office.v1+json",
    OFFICE_LIST: "application/vnd.vitae.office-list.v1+json",

    // --- AVAILABILITY ---
    AVAILABILITY_LIST: "application/vnd.vitae.availability-list.v1+json",
    UNAVAILABILITY_LIST: "application/vnd.vitae.unavailability-list.v1+json",

    // --- SPECIALTIES ---
    SPECIALTY: "application/vnd.vitae.specialty.v1+json",
    SPECIALTY_LIST: "application/vnd.vitae.specialty-list.v1+json",
    OFFICE_SPECIALTY_LIST: "application/vnd.vitae.office-specialty-list.v1+json",

    // --- NEIGHBORHOODS ---
    NEIGHBORHOOD: "application/vnd.vitae.neighborhood.v1+json",
    NEIGHBORHOOD_LIST: "application/vnd.vitae.neighborhood-list.v1+json",

    // --- COVERAGES ---
    COVERAGE: "application/vnd.vitae.coverage.v1+json",
    COVERAGE_LIST: "application/vnd.vitae.coverage-list.v1+json",

    // --- RATINGS ---
    RATING: "application/vnd.vitae.rating.v1+json",
    RATING_LIST: "application/vnd.vitae.rating-list.v1+json",

    // --- AUTH / SECURITY ---
    USER_PASSWORD: "application/vnd.vitae.user-password.v1+json",

    // --- ESTÁNDARES (Usados en descargas/subidas) ---
    JSON: "application/json",
    MULTIPART: "multipart/form-data",
    OCTET_STREAM: "application/octet-stream",
    PDF: "application/pdf",
    IMAGE_PNG: "image/png",
    IMAGE_JPEG: "image/jpeg",
    IMAGE_JPG: "image/jpg",
};

/**
 * @param {string} contentType - El tipo de contenido que envías (body).
 * @param {string} acceptType - El tipo de contenido que esperas recibir.
 * @param {string} [token] - (Opcional) Token JWT.
 */
export const buildHeaders = (contentType, acceptType, token = null) => {
    const headers = {
        'Accept': acceptType || ContentTypes.JSON,
    };

    // Solo agregamos Content-Type si no es Multipart (el navegador lo pone solo con el boundary)
    if (contentType && contentType !== ContentTypes.MULTIPART) {
        headers['Content-Type'] = contentType;
    }

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    return headers;
};