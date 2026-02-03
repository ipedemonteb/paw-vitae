package ar.edu.itba.paw.models;

import java.time.LocalDateTime;

public class MailDTO {
    private final long appointmentId;
    private final String doctorName;
    private final String doctorEmail;
    private final String doctorLanguage;
    private final String patientName;
    private final String patientEmail;
    private final String patientLanguage;
    private final LocalDateTime appointmentDateTime;
    private final String officeName;
    private final String officeNeighborhood;
    private final String reason;
    private final String status;
    private final String report;

    public MailDTO(Appointment appointment) {
        this.appointmentId = appointment.getId();
        this.doctorName = appointment.getDoctor().getName() + " " + appointment.getDoctor().getLastName();
        this.doctorEmail = appointment.getDoctor().getEmail();
        this.doctorLanguage = appointment.getDoctor().getLanguage();
        this.patientName = appointment.getPatient().getName() + " " + appointment.getPatient().getLastName();
        this.patientEmail = appointment.getPatient().getEmail();
        this.patientLanguage = appointment.getPatient().getLanguage();
        this.appointmentDateTime = appointment.getDate();
        this.officeName = appointment.getDoctorOffice().getOfficeName();
        this.officeNeighborhood = appointment.getDoctorOffice().getNeighborhood().getName();
        this.reason = appointment.getReason();
        this.status = appointment.getStatus();
        this.report = appointment.getReport();
    }

    public long getId() {
        return appointmentId;
    }

    public LocalDateTime getDate() {
        return appointmentDateTime;
    }

    public String getStatus() {
        return status;
    }

    public String getDoctorLanguage() {
        return doctorLanguage;
    }

    public String getPatientLanguage() {
        return patientLanguage;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public String getOfficeName() {
        return officeName;
    }

    public String getOfficeNeighborhood() {
        return officeNeighborhood;
    }

    public String getReason() {
        return reason;
    }

    public String getReport() {
        return report;
    }



}
