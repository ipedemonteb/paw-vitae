package ar.edu.itba.paw.models;

public class Rating {
    private Double rating;
    private int doctorId;
    private int patientId;
    private int appointmentId;
    private String comment;
    private long id;
    public Rating(Double rating, int doctorId, int patientId, int appointmentId, String comment, long id) {
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

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setAppointmentId(int appointmentId) {
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

    public int getDoctorId() {
        return doctorId;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public String getComment() {
        return comment;
    }

    public long getId() {
        return id;
    }
}
