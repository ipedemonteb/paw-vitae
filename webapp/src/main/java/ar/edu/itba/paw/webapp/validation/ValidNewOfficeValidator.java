package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.DoctorOfficeForm;
import ar.edu.itba.paw.webapp.form.DoctorForm;
import ar.edu.itba.paw.webapp.form.OfficeForm;
import ar.edu.itba.paw.webapp.form.UpdateAvailabilityForm;

import javax.print.Doc;
import javax.validation.ConstraintValidator;
import java.util.List;

public class ValidNewOfficeValidator {

    private static boolean helperFunction(List<DoctorOfficeForm> doctorOfficeForms) {
        for (DoctorOfficeForm office : doctorOfficeForms) {
            if (office.getRemoved() && office.getId() == null) {
                return false; // Invalid office found
            }
        }
        return true; // All offices are valid
    }

    public static class ForOfficeForm implements ConstraintValidator<ValidNewOffice, OfficeForm> {
        @Override
        public boolean isValid(OfficeForm form, javax.validation.ConstraintValidatorContext context) {
            List<DoctorOfficeForm> doctorOfficeForms = form.getDoctorOfficeForm();
            if (doctorOfficeForms == null || doctorOfficeForms.isEmpty()) {
                return true; // validated elsewhere
            }
            boolean flag = helperFunction(doctorOfficeForms);
            if (!flag) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("{offices.new.invalid}")
                        .addPropertyNode("doctorOfficeForm")
                        .addConstraintViolation();
            }
            return flag;
        }
    }

    public static class ForDoctorForm implements ConstraintValidator<ValidNewOffice, DoctorForm> {
        @Override
        public boolean isValid(DoctorForm form, javax.validation.ConstraintValidatorContext context) {
            List<DoctorOfficeForm> doctorOfficeForms = form.getDoctorOfficeForm();
            if (doctorOfficeForms == null || doctorOfficeForms.isEmpty()) {
                return true; // validated elsewhere
            }

            boolean flag = true;

            for (DoctorOfficeForm office : doctorOfficeForms) {
                if (!office.getActive()) {
                    flag = false;
                    break;
                }
            }

            if (!flag || !helperFunction(doctorOfficeForms)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("{offices.new.invalid}")
                        .addPropertyNode("doctorOfficeForm")
                        .addConstraintViolation();
                return false;
            }
            return true;
        }
    }
}
