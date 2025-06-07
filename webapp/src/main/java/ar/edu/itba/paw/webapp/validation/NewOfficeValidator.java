package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.DoctorOfficeForm;

import javax.validation.ConstraintValidator;
import java.util.List;

public class NewOfficeValidator implements ConstraintValidator<NewOffice, List<DoctorOfficeForm>> {

    @Override
    public boolean isValid(List<DoctorOfficeForm> officeForms, javax.validation.ConstraintValidatorContext context) {
        if (officeForms == null || officeForms.isEmpty()) {
            return false; // Invalid if the list is null or empty
        }
        for (DoctorOfficeForm form : officeForms) {
            if (form.getId() != null) {
                return false; // it is a new office, as such the id should be null
            }
        }
        return true; // All forms are valid
    }
}
