package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.DoctorOfficeForm;

import javax.validation.ConstraintValidator;
import java.util.List;

public class ActiveOfficeValidator implements ConstraintValidator<ActiveOffice, List<DoctorOfficeForm>> {

    @Override
    public boolean isValid(List<DoctorOfficeForm> doctorOfficeForms, javax.validation.ConstraintValidatorContext context) {
        if (doctorOfficeForms == null || doctorOfficeForms.isEmpty()) {
            return true;
        }
        return doctorOfficeForms.stream()
                .anyMatch(DoctorOfficeForm::getActive);
    }

}
