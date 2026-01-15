package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Size;

public class DoctorBioForm {
    @Size(max = 2600)
    private String biography;

    @Size(max = 220)
    private String description;

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
