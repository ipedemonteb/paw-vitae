package ar.edu.itba.paw.models;

import java.util.Date;

public class Appointment {

    private final String clientId;
    private final String doctorId;
    private Date startDate;
    private Date endDate;
    private String status;
    private String reason;

    public Appointment(String clientId, String doctorId, Date startDate, Date endDate, String status, String reason) {
        this.clientId = clientId;
        this.doctorId = doctorId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.reason = reason;
    }

    public String getClientId() {
        return clientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
