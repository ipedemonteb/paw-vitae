package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.DoctorOfficeForm;
import ar.edu.itba.paw.webapp.form.DoctorForm;
import ar.edu.itba.paw.webapp.form.OfficeForm;
import ar.edu.itba.paw.webapp.form.UpdateAvailabilityForm;

import javax.print.Doc;
import javax.validation.ConstraintValidator;
import java.util.List;

public class ValidNewOfficeValidator {


    public static class ForOfficeForm implements ConstraintValidator<ValidNewOffice, OfficeForm> {
        @Override
        public boolean isValid(OfficeForm form, javax.validation.ConstraintValidatorContext context) {
            DoctorOfficeForm doctorOfficeForms = form.getDoctorOfficeForm();
            if (doctorOfficeForms == null ) {
                return true;
            }
            boolean flag =  !doctorOfficeForms.getRemoved() || doctorOfficeForms.getId() != null;
            if (!flag) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("offices.new.invalid")
                        .addPropertyNode("doctorOfficeForm")
                        .addConstraintViolation();
            }
            return flag;
        }
    }

}
