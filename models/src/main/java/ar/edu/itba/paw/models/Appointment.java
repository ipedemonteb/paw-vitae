package ar.edu.itba.paw.models;

import java.util.Date;

public class Appointment {

    private final long clientId;
    private final long doctorId;
    private Date date;
    private String status; //will be enum
    private String reason;

    public Appointment(long clientId, long doctorId, Date date, String status, String reason) {
        this.clientId = clientId;
        this.doctorId = doctorId;
        this.date = date;
        this.status = status;
        this.reason = reason;
    }

    public long getClientId() {
        return clientId;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
