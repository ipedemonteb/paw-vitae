package ar.edu.itba.paw.webapp;

public final class CustomMediaType {

    private CustomMediaType() {}

    public static final String APPLICATION_HOME = "application/vnd.vitae.home.v1+json";
    // --- DOCTORS (Principal) ---
    public static final String APPLICATION_DOCTOR = "application/vnd.vitae.doctor.v1+json";
    public static final String APPLICATION_DOCTOR_LIST = "application/vnd.vitae.doctor-list.v1+json";

    // --- DOCTOR PROFILE SUB-RESOURCES (Lo que faltaba) ---
    // Para /doctors/{id}/biography
    public static final String APPLICATION_DOCTOR_PROFILE = "application/vnd.vitae.doctor-profile.v1+json";

    // Para /doctors/{id}/experiences
    public static final String APPLICATION_DOCTOR_EXPERIENCE_LIST = "application/vnd.vitae.doctor-experience-list.v1+json";

    // Para /doctors/{id}/certifications
    public static final String APPLICATION_DOCTOR_CERTIFICATION_LIST = "application/vnd.vitae.doctor-certification-list.v1+json";

    // --- PATIENTS ---
    public static final String APPLICATION_PATIENT = "application/vnd.vitae.patient.v1+json";
    // Si tuvieras lista de pacientes en algún lado:
    public static final String APPLICATION_PATIENT_LIST = "application/vnd.vitae.patient-list.v1+json";

    // --- APPOINTMENTS ---
    public static final String APPLICATION_APPOINTMENT = "application/vnd.vitae.appointment.v1+json";
    public static final String APPLICATION_APPOINTMENT_LIST = "application/vnd.vitae.appointment-list.v1+json";
    public static final String APPLICATION_APPOINTMENT_REPORT = "application/vnd.vitae.appointment-report.v1+json";

    // --- APPOINTMENT FILES ---
    // Para /appointments/{id}/files (devuelve lista)
    public static final String APPLICATION_APPOINTMENT_FILE_LIST = "application/vnd.vitae.appointment-file-list.v1+json";

    // Singular (para respuesta de Upload)
    public static final String APPLICATION_APPOINTMENT_FILE = "application/vnd.vitae.appointment-file.v1+json";
    // --- OFFICES ---
    public static final String APPLICATION_OFFICE = "application/vnd.vitae.office.v1+json";
    public static final String APPLICATION_OFFICE_LIST = "application/vnd.vitae.office-list.v1+json";

    // --- AVAILABILITY / UNAVAILABILITY ---
    public static final String APPLICATION_AVAILABILITY_LIST = "application/vnd.vitae.availability-list.v1+json";
    public static final String APPLICATION_UNAVAILABILITY_LIST = "application/vnd.vitae.unavailability-list.v1+json";

    // --- SPECIALTIES ---
    public static final String APPLICATION_SPECIALTY = "application/vnd.vitae.specialty.v1+json";
    public static final String APPLICATION_SPECIALTY_LIST = "application/vnd.vitae.specialty-list.v1+json";

    // Para /doctors/{id}/offices/{officeId}/specialties (si devuelve OfficeSpecialtyDTO que es distinto a SpecialtyDTO)
    public static final String APPLICATION_OFFICE_SPECIALTY_LIST = "application/vnd.vitae.office-specialty-list.v1+json";

    // --- NEIGHBORHOODS ---
    public static final String APPLICATION_NEIGHBORHOOD = "application/vnd.vitae.neighborhood.v1+json";
    public static final String APPLICATION_NEIGHBORHOOD_LIST = "application/vnd.vitae.neighborhood-list.v1+json";

    // --- COVERAGES ---
    public static final String APPLICATION_COVERAGE = "application/vnd.vitae.coverage.v1+json";
    public static final String APPLICATION_COVERAGE_LIST = "application/vnd.vitae.coverage-list.v1+json";

    // --- RATINGS ---
    public static final String APPLICATION_RATING = "application/vnd.vitae.rating.v1+json";
    public static final String APPLICATION_RATING_LIST = "application/vnd.vitae.rating-list.v1+json";

    // --- ACTIONS / FORMS ESPECÍFICOS ---
    // Para cambio de contraseña (PATCH)
    public static final String APPLICATION_USER_PASSWORD = "application/vnd.vitae.user-password.v1+json";
}