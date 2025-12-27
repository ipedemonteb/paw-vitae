package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.DoctorExperience;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDate;
import java.util.Date;

public class ExperienceDTO {
    private String positionTitle;
    private String organizationName;
    private LocalDate startDate;
    private LocalDate endDate;

    private URI doctor;

    public static ExperienceDTO fromDoctorExperience(DoctorExperience experience, UriInfo uriInfo) {
        ExperienceDTO dto = new ExperienceDTO();

        dto.positionTitle = experience.getPositionTitle();
        dto.organizationName = experience.getOrganizationName();
        dto.startDate = experience.getStartDate();
        dto.endDate = experience.getEndDate();

        dto.doctor = uriInfo.getBaseUriBuilder().path("doctors").path(String.valueOf(experience.getDoctor().getId())).build();

        return dto;
    }

    public String getPositionTitle() {
        return positionTitle;
    }

    public void setPositionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public URI getDoctor() {
        return doctor;
    }

    public void setDoctor(URI doctor) {
        this.doctor = doctor;
    }
}
