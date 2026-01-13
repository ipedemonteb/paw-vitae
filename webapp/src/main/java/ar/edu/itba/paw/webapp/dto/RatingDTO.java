package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Rating;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

public class RatingDTO {

    private long rating;
    private String comment;

    private String patientName;
    private String patientLastName;

    private URI self;
    private URI doctor;
    private URI patient;
    private URI appointment;

    public static RatingDTO fromRating(Rating rating, UriInfo uriInfo){
        RatingDTO res = new RatingDTO();
        res.rating = rating.getRating();
        res.comment = rating.getComment();

        res.patientName = rating.getPatient().getName();
        res.patientLastName = rating.getPatient().getLastName();

        res.doctor = uriInfo.getBaseUriBuilder().path("doctors").path(String.valueOf(rating.getDoctor().getId())).build();
        res.patient = uriInfo.getBaseUriBuilder().path("patients").path(String.valueOf(rating.getPatient().getId())).build();
        res.appointment = uriInfo.getBaseUriBuilder().path("appointments").path(String.valueOf(rating.getAppointment().getId())).build();
        res.self = uriInfo.getBaseUriBuilder().path("ratings").path(String.valueOf(rating.getId())).build();

        return res;
    }

    public static List<RatingDTO> fromRating(List<Rating> ratings, UriInfo uriInfo) {
        return ratings.stream().map(r -> fromRating(r, uriInfo)).toList();
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getDoctor() {
        return doctor;
    }

    public void setDoctor(URI doctor) {
        this.doctor = doctor;
    }

    public URI getPatient() {
        return patient;
    }

    public void setPatient(URI patient) {
        this.patient = patient;
    }

    public URI getAppointment() {
        return appointment;
    }

    public void setAppointment(URI appointment) {
        this.appointment = appointment;
    }

    public String getPatientName() {
        return patientName;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
    public String getPatientLastName() {
        return patientLastName;
    }
    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

}
