package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentFile;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentDTO {

    private long id;
    private LocalDateTime date;
    private String status;
    private String reason;
    private boolean allowFullHistory;
    private String report;
    private Boolean cancellable;

    private URI self;
    private URI doctor;
    private URI patient;
    private URI specialty;
    private URI doctorOffice;
    private URI appointmentFiles;
    private URI rating;

    public static AppointmentDTO fromAppointment(Appointment appointment, UriInfo uriInfo) {
        AppointmentDTO dto = new AppointmentDTO();

        dto.id = appointment.getId();
        dto.date = appointment.getDate();
        dto.status = appointment.getStatus();
        dto.reason = appointment.getReason();
        dto.allowFullHistory = appointment.isAllowFullHistory();
        dto.report = appointment.getReport();
        dto.cancellable = appointment.getCancellable();

        dto.self = uriInfo.getBaseUriBuilder().path("api").path("appointments").path(String.valueOf(appointment.getId())).build();
        dto.doctor = uriInfo.getBaseUriBuilder().path("api").path("doctors").path(String.valueOf(appointment.getDoctor().getId())).build();
        dto.patient = uriInfo.getBaseUriBuilder().path("api").path("patients").path(String.valueOf(appointment.getPatient().getId())).build();
        dto.specialty = uriInfo.getBaseUriBuilder().path("api").path("specialties").path(String.valueOf(appointment.getSpecialty().getId())).build();
        dto.doctorOffice = uriInfo.getBaseUriBuilder().path("api")
                .path("doctors")
                .path(String.valueOf(appointment.getDoctor().getId()))
                .path("offices")
                .path(String.valueOf(appointment.getDoctorOffice().getId()))
                .build();
        dto.appointmentFiles = uriInfo.getBaseUriBuilder().path("api").path("appointments").path(String.valueOf(appointment.getId())).path("files").build();
        if (appointment.getRating() != null) {
            dto.rating = uriInfo.getBaseUriBuilder().path("api")
                    .path("ratings")
                    .path(String.valueOf(appointment.getRating().getId()))
                    .build();
        }
        return dto;
    }

    public static List<AppointmentDTO> fromAppointment(List<Appointment> appointments, UriInfo uriInfo) {
        return appointments.stream().map(a -> fromAppointment(a, uriInfo)).toList();
    }


    public long getId() {
        return id;
    }

    public URI getSelf() {
        return self;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public boolean isAllowFullHistory() {
        return allowFullHistory;
    }

    public String getReport() {
        return report;
    }

    public Boolean getCancellable() {
        return cancellable;
    }

    public URI getDoctor() {
        return doctor;
    }

    public URI getPatient() {
        return patient;
    }

    public URI getSpecialty() {
        return specialty;
    }

    public URI getDoctorOffice() {
        return doctorOffice;
    }

    public URI getAppointmentFiles() {
        return appointmentFiles;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setAllowFullHistory(boolean allowFullHistory) {
        this.allowFullHistory = allowFullHistory;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public void setCancellable(Boolean cancellable) {
        this.cancellable = cancellable;
    }

    public void setDoctor(URI doctor) {
        this.doctor = doctor;
    }

    public void setPatient(URI patient) {
        this.patient = patient;
    }

    public void setSpecialty(URI specialty) {
        this.specialty = specialty;
    }

    public void setDoctorOffice(URI doctorOffice) {
        this.doctorOffice = doctorOffice;
    }

    public void setAppointmentFiles(URI appointmentFiles) {
        this.appointmentFiles = appointmentFiles;
    }

    public URI getRating() {
        return rating;
    }

    public void setRating(URI rating) {
        this.rating = rating;
    }
}
