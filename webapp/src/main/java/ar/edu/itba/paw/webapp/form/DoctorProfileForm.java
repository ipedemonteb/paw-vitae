package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.CertificateForm;
import ar.edu.itba.paw.models.ExperienceForm;
import ar.edu.itba.paw.webapp.validation.ValidCertificate;
import ar.edu.itba.paw.webapp.validation.ValidExperience;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class DoctorProfileForm {

    @Size(max = 220)
    private String biography;

    @Size(max = 2600)
    private String description;
    @ValidExperience(message = "{experiences.invalid}")

    @Size(max = 10)
    private List<ExperienceForm> experiences;
    @ValidCertificate(message = "{certificates.invalid}")
    @Size(max = 8)
    private List<CertificateForm> certificates;

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

    public List<ExperienceForm> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<ExperienceForm> experiences) {
        this.experiences = experiences;
    }

    public List<CertificateForm> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<CertificateForm> certificates) {
        this.certificates = certificates;
    }
}
