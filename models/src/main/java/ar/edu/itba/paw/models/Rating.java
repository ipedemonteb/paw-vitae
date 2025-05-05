package ar.edu.itba.paw.models;

public class Rating {

    private final long id;
    private long rating;
    private long doctorId;
    private long patientId;
    private long appointmentId;
    private String comment;

    public Rating(long id, long rating, long doctorId, long patientId, long appointmentId, String comment) {
        this.id = id;
        this.rating = rating;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
