package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.DoctorCertification;
import ar.edu.itba.paw.models.DoctorExperience;
import ar.edu.itba.paw.models.DoctorProfile;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

public class ProfileDTO {
    private String bio;
    private String description;
    private URI doctor;

    public static ProfileDTO fromDoctorProfile(DoctorProfile profile, UriInfo uriInfo) {
        ProfileDTO dto = new ProfileDTO();
        dto.bio = profile.getBio();
        dto.description = profile.getDescription();

        dto.doctor = uriInfo.getBaseUriBuilder().path("api").path("doctors").path(String.valueOf(profile.getDoctorId())).build();

        return dto;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URI getDoctor() {
        return doctor;
    }

    public void setDoctor(URI doctor) {
        this.doctor = doctor;
    }
}
