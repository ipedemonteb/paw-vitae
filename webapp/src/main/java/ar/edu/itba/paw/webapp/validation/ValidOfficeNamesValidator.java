package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.DoctorOfficeForm;

import javax.validation.ConstraintValidator;
import java.util.List;

public class ValidOfficeNamesValidator implements ConstraintValidator<ValidOfficeNames, DoctorOfficeForm> {
    @Override
    public boolean isValid(DoctorOfficeForm value, javax.validation.ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return value.getOfficeName() != null && !value.getOfficeName().isEmpty();
    }
}
