package ar.edu.itba.paw.models;

import java.time.LocalDateTime;
import java.util.Date;

public class Appointment {

    private final long id;
    private final long clientId;
    private final long doctorId;
    private LocalDateTime date;
    private String status;
    private String reason;
    private Specialty specialty;
    private Doctor doctor;
    private Client client;
    public Appointment(long clientId, long doctorId, LocalDateTime date, String status, String reason, long id, Specialty specialty) {
        this.clientId = clientId;
        this.doctorId = doctorId;
        this.date = date;
        this.status = status;
        this.reason = reason;
        this.id = id;
        this.specialty = specialty;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public long getId() {
        return id;
    }

    public long getClientId() {
        return clientId;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    public Doctor getDoctor() {
        return doctor;
    }
    public Client getClient() {
        return client;
    }
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    public void setClient(Client client) {
        this.client = client;
    }
}
