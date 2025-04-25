package ar.edu.itba.paw.models;

import java.time.LocalDateTime;

public class Appointment {

    private final long id;
    private LocalDateTime date;
    private String status;
    private String reason;
    private Specialty specialty;
    private Doctor doctor;
    private Patient patient;
    public Appointment(LocalDateTime date, String status, String reason, long id, Specialty specialty, Doctor doctor, Patient patient) {
        this.date = date;
        this.status = status;
        this.reason = reason;
        this.id = id;
        this.specialty = specialty;
        this.doctor = doctor;
        this.patient = patient;
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
    public Patient getPatient() {
        return patient;
    }
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
