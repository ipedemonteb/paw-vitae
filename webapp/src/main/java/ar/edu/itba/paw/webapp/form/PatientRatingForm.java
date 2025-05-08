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

    // Getters and Setters
    public Integer getRating() {
        return rating;
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
}
