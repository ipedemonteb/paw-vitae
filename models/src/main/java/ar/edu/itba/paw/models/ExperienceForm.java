package ar.edu.itba.paw.models;

import java.time.LocalDate;

public class ExperienceForm {

    private String positionTitle;
    private String organizationName;
    private LocalDate startDate;
    private LocalDate endDate;

    public ExperienceForm() {
    }

    public ExperienceForm(String positionTitle, String organizationName, LocalDate startDate, LocalDate endDate) {
        this.positionTitle = positionTitle;
        this.organizationName = organizationName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public ExperienceForm fromEntity(DoctorExperience experience) {
        return new ExperienceForm(
                experience.getPositionTitle(),
                experience.getOrganizationName(),
                experience.getStartDate(),
                experience.getEndDate()
        );
    }

    public String getPositionTitle() {
        return positionTitle;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setPositionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
