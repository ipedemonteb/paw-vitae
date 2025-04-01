package ar.edu.itba.paw.models;

import java.time.LocalDateTime;
import java.util.Date;

public class Appointment {

    private final long id;
    private final long clientId;
    private final long doctorId;
    private LocalDateTime date;
    private String status; //will be enum
    private String reason;

    public Appointment(long clientId, long doctorId, LocalDateTime date, String status, String reason, long id) {
        this.clientId = clientId;
        this.doctorId = doctorId;
        this.date = date;
        this.status = status;
        this.reason = reason;
        this.id = id;
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
}
