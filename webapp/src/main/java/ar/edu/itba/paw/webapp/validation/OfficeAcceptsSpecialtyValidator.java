package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.DoctorOfficeService;
import ar.edu.itba.paw.models.DoctorOffice;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import java.lang.reflect.Field;

public class OfficeAcceptsSpecialtyValidator implements ConstraintValidator<OfficeAcceptsSpecialty, Object> {
    private String officeId;
    private String specialtyId;
    private final DoctorOfficeService doctorOfficeService;

    @Autowired
    public OfficeAcceptsSpecialtyValidator(DoctorOfficeService doctorOfficeService) {
        this.doctorOfficeService = doctorOfficeService;
    }

    @Override
    public void initialize(OfficeAcceptsSpecialty constraintAnnotation) {
        this.officeId = constraintAnnotation.officeId();
        this.specialtyId = constraintAnnotation.specialtyId();
    }

    @Override
    public boolean isValid(Object value, javax.validation.ConstraintValidatorContext context) {
        try {
            Field officeField = value.getClass().getDeclaredField(officeId);
            Field specialtyIdField = value.getClass().getDeclaredField(specialtyId);

            officeField.setAccessible(true);
            specialtyIdField.setAccessible(true);

            long officeIdValue = (long) officeField.get(value);
            long specialtyIdValue = (long) specialtyIdField.get(value);

            DoctorOffice doctorOffice = doctorOfficeService.getById(officeIdValue)
                    .orElse(null);
            if (doctorOffice == null) {
                return true;
            }
            return doctorOffice.getSpecialties().stream().anyMatch(specialty -> specialty.getId() == specialtyIdValue);
        } catch (Exception e) {
            return false;
        }
    }
}
