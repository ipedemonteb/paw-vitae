package ar.edu.itba.paw.webapp.dto;

import javax.ws.rs.core.UriInfo;
import java.io.Serializable;

public class HomeDTO  {


    private String usersUrl;
    private String doctorsUrl;
    private String patientsUrl;
    private String appointmentsUrl;
    private String ratingsUrl;
    private String specialtiesUrl;
    private String coveragesUrl;
    private String neighborhoodUrl;


    private String doctorByIdUrl;
    private String doctorProfileUrl;
    private String doctorExperiencesUrl;
    private String doctorCertificationsUrl;
    private String doctorImageUrl;
    private String doctorSpecialtiesUrl;
    private String doctorCoveragesUrl;


    private String doctorOfficesUrl;
    private String doctorOfficeByIdUrl;
    private String doctorOfficeAvailabilityUrl;
    private String doctorOfficeSpecialtiesUrl;

    private String doctorUnavailabilityUrl;
    private String doctorOccupiedSlotsUrl;
    private String doctorOccupiedSlotByIdUrl;

    private String appointmentByIdUrl;
    private String appointmentReportUrl;
    private String appointmentFilesUrl;
    private String appointmentFileByIdUrl;
    private String appointmentFileViewUrl;

    private String patientByIdUrl;
    private String specialtyByIdUrl;
    private String coverageByIdUrl;
    private String neighborhoodByIdUrl;
    private String ratingByIdUrl;

    public HomeDTO() {
    }

    public static HomeDTO fromUriInfo(final UriInfo uriInfo) {
        final HomeDTO dto = new HomeDTO();
        final String baseUri = uriInfo.getBaseUri().toString() + "api/";

        dto.usersUrl = baseUri + "users";

        dto.doctorsUrl = baseUri + "doctors{?specialty,coverage,weekdays,keyword,orderBy,direction,page,pageSize}";

        dto.appointmentsUrl = baseUri + "appointments{?doctorId,collection,filter,sort,page,pageSize}";

        dto.ratingsUrl = baseUri + "ratings{?doctorId,page,pageSize}";

        dto.patientsUrl = baseUri + "patients";
        dto.specialtiesUrl = baseUri + "specialties";
        dto.coveragesUrl = baseUri + "coverages";
        dto.neighborhoodUrl = baseUri + "neighborhoods";

        dto.doctorByIdUrl = baseUri + "doctors/{id}";
        dto.doctorProfileUrl = baseUri + "doctors/{id}/biography";
        dto.doctorExperiencesUrl = baseUri + "doctors/{id}/experiences";
        dto.doctorCertificationsUrl = baseUri + "doctors/{id}/certifications";
        dto.doctorImageUrl = baseUri + "doctors/{id}/image";
        dto.doctorSpecialtiesUrl = baseUri + "doctors/{id}/specialties";
        dto.doctorCoveragesUrl = baseUri + "doctors/{id}/coverages";


        dto.doctorOfficesUrl = baseUri + "doctors/{id}/offices{?status}";
        dto.doctorOfficeByIdUrl = baseUri + "doctors/{doctorId}/offices/{officeId}";
        dto.doctorOfficeSpecialtiesUrl = baseUri + "doctors/{doctorId}/offices/{officeId}/specialties";


        dto.doctorOfficeAvailabilityUrl = baseUri + "doctors/{id}/availability{?officeId}";

        dto.doctorUnavailabilityUrl = baseUri + "doctors/{id}/unavailability{?from,to,page,pageSize}";

        dto.doctorOccupiedSlotsUrl = baseUri + "doctors/{id}/slots{?from,to}";
        dto.doctorOccupiedSlotByIdUrl = baseUri + "doctors/{id}/slots/{slotId}";

        dto.appointmentByIdUrl = baseUri + "appointments/{id}";
        dto.appointmentReportUrl = baseUri + "appointments/{id}/report";
        dto.appointmentFilesUrl = baseUri + "appointments/{id}/files";
        dto.appointmentFileByIdUrl = baseUri + "appointments/{appointmentId}/files/{fileId}";
        dto.appointmentFileViewUrl = baseUri + "appointments/{appointmentId}/files/{fileId}/view";


        dto.patientByIdUrl = baseUri + "patients/{id}";
        dto.specialtyByIdUrl = baseUri + "specialties/{id}";
        dto.coverageByIdUrl = baseUri + "coverages/{id}";
        dto.neighborhoodByIdUrl = baseUri + "neighborhoods/{id}";
        dto.ratingByIdUrl = baseUri + "ratings/{id}";

        return dto;
    }

    public String getUsersUrl() { return usersUrl; }
    public void setUsersUrl(String usersUrl) { this.usersUrl = usersUrl; }

    public String getDoctorsUrl() { return doctorsUrl; }
    public void setDoctorsUrl(String doctorsUrl) { this.doctorsUrl = doctorsUrl; }

    public String getPatientsUrl() { return patientsUrl; }
    public void setPatientsUrl(String patientsUrl) { this.patientsUrl = patientsUrl; }

    public String getAppointmentsUrl() { return appointmentsUrl; }
    public void setAppointmentsUrl(String appointmentsUrl) { this.appointmentsUrl = appointmentsUrl; }

    public String getRatingsUrl() { return ratingsUrl; }
    public void setRatingsUrl(String ratingsUrl) { this.ratingsUrl = ratingsUrl; }

    public String getSpecialtiesUrl() { return specialtiesUrl; }
    public void setSpecialtiesUrl(String specialtiesUrl) { this.specialtiesUrl = specialtiesUrl; }

    public String getCoveragesUrl() { return coveragesUrl; }
    public void setCoveragesUrl(String coveragesUrl) { this.coveragesUrl = coveragesUrl; }

    public String getNeighborhoodUrl() { return neighborhoodUrl; }
    public void setNeighborhoodUrl(String neighborhoodUrl) { this.neighborhoodUrl = neighborhoodUrl; }

    public String getDoctorByIdUrl() { return doctorByIdUrl; }
    public void setDoctorByIdUrl(String doctorByIdUrl) { this.doctorByIdUrl = doctorByIdUrl; }

    public String getDoctorProfileUrl() { return doctorProfileUrl; }
    public void setDoctorProfileUrl(String doctorProfileUrl) { this.doctorProfileUrl = doctorProfileUrl; }

    public String getDoctorExperiencesUrl() { return doctorExperiencesUrl; }
    public void setDoctorExperiencesUrl(String doctorExperiencesUrl) { this.doctorExperiencesUrl = doctorExperiencesUrl; }

    public String getDoctorCertificationsUrl() { return doctorCertificationsUrl; }
    public void setDoctorCertificationsUrl(String doctorCertificationsUrl) { this.doctorCertificationsUrl = doctorCertificationsUrl; }

    public String getDoctorImageUrl() { return doctorImageUrl; }
    public void setDoctorImageUrl(String doctorImageUrl) { this.doctorImageUrl = doctorImageUrl; }

    public String getDoctorSpecialtiesUrl() { return doctorSpecialtiesUrl; }
    public void setDoctorSpecialtiesUrl(String doctorSpecialtiesUrl) { this.doctorSpecialtiesUrl = doctorSpecialtiesUrl; }

    public String getDoctorCoveragesUrl() { return doctorCoveragesUrl; }
    public void setDoctorCoveragesUrl(String doctorCoveragesUrl) { this.doctorCoveragesUrl = doctorCoveragesUrl; }

    public String getDoctorOfficesUrl() { return doctorOfficesUrl; }
    public void setDoctorOfficesUrl(String doctorOfficesUrl) { this.doctorOfficesUrl = doctorOfficesUrl; }

    public String getDoctorOfficeByIdUrl() { return doctorOfficeByIdUrl; }
    public void setDoctorOfficeByIdUrl(String doctorOfficeByIdUrl) { this.doctorOfficeByIdUrl = doctorOfficeByIdUrl; }

    public String getDoctorOfficeAvailabilityUrl() { return doctorOfficeAvailabilityUrl; }
    public void setDoctorOfficeAvailabilityUrl(String doctorOfficeAvailabilityUrl) { this.doctorOfficeAvailabilityUrl = doctorOfficeAvailabilityUrl; }

    public String getDoctorOfficeSpecialtiesUrl() { return doctorOfficeSpecialtiesUrl; }
    public void setDoctorOfficeSpecialtiesUrl(String doctorOfficeSpecialtiesUrl) { this.doctorOfficeSpecialtiesUrl = doctorOfficeSpecialtiesUrl; }

    public String getDoctorUnavailabilityUrl() { return doctorUnavailabilityUrl; }
    public void setDoctorUnavailabilityUrl(String doctorUnavailabilityUrl) { this.doctorUnavailabilityUrl = doctorUnavailabilityUrl; }

    public String getDoctorOccupiedSlotsUrl() { return doctorOccupiedSlotsUrl; }
    public void setDoctorOccupiedSlotsUrl(String doctorOccupiedSlotsUrl) { this.doctorOccupiedSlotsUrl = doctorOccupiedSlotsUrl; }

    public String getDoctorOccupiedSlotByIdUrl() { return doctorOccupiedSlotByIdUrl; }
    public void setDoctorOccupiedSlotByIdUrl(String doctorOccupiedSlotByIdUrl) { this.doctorOccupiedSlotByIdUrl = doctorOccupiedSlotByIdUrl; }

    public String getAppointmentByIdUrl() { return appointmentByIdUrl; }
    public void setAppointmentByIdUrl(String appointmentByIdUrl) { this.appointmentByIdUrl = appointmentByIdUrl; }

    public String getAppointmentReportUrl() { return appointmentReportUrl; }
    public void setAppointmentReportUrl(String appointmentReportUrl) { this.appointmentReportUrl = appointmentReportUrl; }

    public String getAppointmentFilesUrl() { return appointmentFilesUrl; }
    public void setAppointmentFilesUrl(String appointmentFilesUrl) { this.appointmentFilesUrl = appointmentFilesUrl; }

    public String getAppointmentFileByIdUrl() { return appointmentFileByIdUrl; }
    public void setAppointmentFileByIdUrl(String appointmentFileByIdUrl) { this.appointmentFileByIdUrl = appointmentFileByIdUrl; }

    public String getAppointmentFileViewUrl() { return appointmentFileViewUrl; }
    public void setAppointmentFileViewUrl(String appointmentFileViewUrl) { this.appointmentFileViewUrl = appointmentFileViewUrl; }

    public String getPatientByIdUrl() { return patientByIdUrl; }
    public void setPatientByIdUrl(String patientByIdUrl) { this.patientByIdUrl = patientByIdUrl; }

    public String getSpecialtyByIdUrl() { return specialtyByIdUrl; }
    public void setSpecialtyByIdUrl(String specialtyByIdUrl) { this.specialtyByIdUrl = specialtyByIdUrl; }

    public String getCoverageByIdUrl() { return coverageByIdUrl; }
    public void setCoverageByIdUrl(String coverageByIdUrl) { this.coverageByIdUrl = coverageByIdUrl; }

    public String getNeighborhoodByIdUrl() { return neighborhoodByIdUrl; }
    public void setNeighborhoodByIdUrl(String neighborhoodByIdUrl) { this.neighborhoodByIdUrl = neighborhoodByIdUrl; }

    public String getRatingByIdUrl() { return ratingByIdUrl; }
    public void setRatingByIdUrl(String ratingByIdUrl) { this.ratingByIdUrl = ratingByIdUrl; }
}