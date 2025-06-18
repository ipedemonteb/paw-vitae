package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.DoctorOfficeForm;

import javax.validation.ConstraintValidator;
import java.util.List;

public class NewOfficeValidator implements ConstraintValidator<NewOffice, List<DoctorOfficeForm>> {

    @Override
    public boolean isValid(List<DoctorOfficeForm> officeForms, javax.validation.ConstraintValidatorContext context) {
        if (officeForms == null || officeForms.isEmpty()) {
            return false;
        }
        for (DoctorOfficeForm form : officeForms) {
            if (form.getId() != null) {
                return false;
            }
        }
        return true;
    }
}
