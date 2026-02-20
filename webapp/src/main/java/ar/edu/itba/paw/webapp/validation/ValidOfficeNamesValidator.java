package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.DoctorOfficeForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidOfficeNamesValidator implements ConstraintValidator<ValidOfficeNames, DoctorOfficeForm> {

    @Override
    public boolean isValid(DoctorOfficeForm value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String officeName = value.getOfficeName();

        if (officeName == null || officeName.trim().isEmpty()) {
            return false;
        }

        return officeName.length() <= 50;
    }
}