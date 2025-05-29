package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.DoctorOfficeService;
import ar.edu.itba.paw.models.DoctorOffice;
import ar.edu.itba.paw.models.DoctorOfficeForm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import java.lang.reflect.Field;
import java.util.List;

public class ValidOfficeIdValidator implements ConstraintValidator<ValidOfficeId, Object> {
    private final DoctorOfficeService doctorOfficeService;
    private String offices;
    private String doctorId;

    @Autowired
    public ValidOfficeIdValidator(DoctorOfficeService doctorOfficeService) {
        this.doctorOfficeService = doctorOfficeService;
    }

    @Override
    public void initialize(ValidOfficeId constraintAnnotation) {
        this.offices = constraintAnnotation.offices();
        this.doctorId = constraintAnnotation.doctorId();
    }

    @Override
    public boolean isValid(Object value, javax.validation.ConstraintValidatorContext context) {
        try {
            Field officesField = value.getClass().getDeclaredField(offices);
            Field doctorIdField = value.getClass().getDeclaredField(doctorId);

            officesField.setAccessible(true);
            doctorIdField.setAccessible(true);

            List<DoctorOfficeForm> officeForms = (List<DoctorOfficeForm>) officesField.get(value);
            long doctorIdValue = (long) doctorIdField.get(value);

            List<DoctorOffice> offices = doctorOfficeService.getAllByDoctorId(doctorIdValue);

            return officeForms.stream().allMatch(officeForm ->
                    officeForm.getId() == null ||
                    offices.stream().anyMatch(office -> office.getId() == officeForm.getId())
            );
        } catch (Exception e) {
            return false;
        }
    }





}
