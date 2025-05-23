package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.DoctorOfficeForm;

import javax.validation.ConstraintValidator;
import java.util.List;

public class ValidOfficeNamesValidator implements ConstraintValidator<ValidOfficeNames, List<DoctorOfficeForm>> {
    @Override
    public boolean isValid(List<DoctorOfficeForm> value, javax.validation.ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        for (DoctorOfficeForm office : value) {
            if (office.getOfficeName() == null || office.getOfficeName().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
