package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.DoctorOfficeService;
import ar.edu.itba.paw.models.DoctorOffice;
import ar.edu.itba.paw.models.DoctorOfficeForm;
import ar.edu.itba.paw.webapp.form.OfficeForm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.List;

public class ValidOfficeIdValidator implements ConstraintValidator<ValidOfficeId, OfficeForm> {
    private final DoctorOfficeService doctorOfficeService;

    @Autowired
    public ValidOfficeIdValidator(DoctorOfficeService doctorOfficeService) {
        this.doctorOfficeService = doctorOfficeService;
    }

    @Override
    public boolean isValid(OfficeForm officeForm, ConstraintValidatorContext constraintValidatorContext) {
        if (officeForm.getDoctorOfficeForm() == null || officeForm.getDoctorId() == null) {
            return true; // validated elsewhere
        }
        List<DoctorOffice> offices = doctorOfficeService.getAllByDoctorId(officeForm.getDoctorId());

        boolean flag = officeForm.getDoctorOfficeForm().stream().allMatch(of ->
            of.getId() == null || offices.stream().anyMatch(o -> o.getId().equals(of.getId()))
        );
        if (!flag) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("{offices.invalid.id}")
                    .addPropertyNode("doctorOfficeForm")
                    .addConstraintViolation();
            return false; // Invalid if any office ID does not match
        }
        return true;
    }
}
