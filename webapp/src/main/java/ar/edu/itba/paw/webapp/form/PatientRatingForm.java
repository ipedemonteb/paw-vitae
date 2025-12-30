package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validation.InvalidRatingForm;

import javax.validation.constraints.*;
@InvalidRatingForm(message = "{InvalidRatingForm.message}")
public class PatientRatingForm {

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    @Size(max = 255)
    private String comment;

    @NotNull
    private Long appointmentId;

    @NotNull
    private Long doctorId;

    @NotNull
    private Long patientId;

    public Long getDoctorId() {
        return doctorId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}
