package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.ExperienceForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class ValidExperienceValidator implements ConstraintValidator<ValidExperience,List<ExperienceForm>> {


    @Override
    public boolean isValid(List<ExperienceForm> experienceFormList, ConstraintValidatorContext constraintValidatorContext) {
        if (experienceFormList == null || experienceFormList.isEmpty()) {
            return true;
        }

        for (ExperienceForm experience : experienceFormList) {
            if (experience.getOrganizationName() == null || experience.getOrganizationName().isEmpty() ||
                experience.getPositionTitle() == null || experience.getPositionTitle().isEmpty() ||
                experience.getStartDate() == null) {
                return false;
            }
            if ((experience.getEndDate() != null && experience.getEndDate().isBefore(experience.getStartDate())) || (experience.getEndDate() == null && experience.getStartDate().isAfter(LocalDate.now()))) {
                return false;
            }
        }
        return true;
    }


}
