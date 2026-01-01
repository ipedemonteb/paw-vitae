package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.ExperienceForm;
import ar.edu.itba.paw.webapp.validation.ValidExperience;

import javax.validation.constraints.Size;
import java.util.List;

public class DoctorExperiencesForm {
    @ValidExperience(message = "{experiences.invalid}")
    @Size(max = 10)
    private List<ExperienceForm> experiences;

    public List<ExperienceForm> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<ExperienceForm> experiences) {
        this.experiences = experiences;
    }
}
