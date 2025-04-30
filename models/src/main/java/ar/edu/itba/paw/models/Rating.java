package ar.edu.itba.paw.models;

public class Rating {
    private Double rating;
    private long doctorId;
    private long patientId;
    private long appointmentId;
    private String comment;
    private long id;
    public Rating(Double rating, long doctorId, long patientId, long appointmentId, String comment, long id) {
        this.rating = rating;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.comment = comment;
        this.id = id;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public void setAppointmentId(long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getRating() {
        return rating;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public long getPatientId() {
        return patientId;
    }

    public long getAppointmentId() {
        return appointmentId;
    }

    public String getComment() {
        return comment;
    }

    public long getId() {
        return id;
    }
}
