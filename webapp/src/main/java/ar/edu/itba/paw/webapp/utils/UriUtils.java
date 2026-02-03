package ar.edu.itba.paw.webapp.utils;

public final class UriUtils {

    private UriUtils() {
        throw new AssertionError();
    }
    public final static String API_BASE_URL = "/api/";
    public final static String DOCTORS = API_BASE_URL + "doctors";
    public final static String PATIENTS = API_BASE_URL + "patients";
    public final static String APPOINTMENTS = API_BASE_URL + "appointments";
    public final static String RATINGS = API_BASE_URL + "ratings";
    public final static String SPECIALTIES = API_BASE_URL + "specialties";
    public final static String COVERAGES = API_BASE_URL + "coverages";
    public final static String NEIGHBORHOODS = API_BASE_URL + "neighborhoods";
    public final static String USERS = API_BASE_URL + "users";
    public final static String DOCTOR_OFFICES = DOCTORS + "/{id}/offices";
    public final static String DOCTOR_AVAILABILITY = DOCTORS + "/{id}/availability";
    public final static String DOCTOR_UNAVAILABILITY = DOCTORS + "/{id}/unavailability";

    public final static String APPOINTMENT_FILES = APPOINTMENTS + "/{appointmentId}/files";
}