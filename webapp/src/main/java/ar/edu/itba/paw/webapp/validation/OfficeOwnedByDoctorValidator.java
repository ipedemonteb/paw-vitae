package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.DoctorOfficeService;
import ar.edu.itba.paw.models.DoctorOffice;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import java.lang.reflect.Field;
import java.util.List;

public class OfficeOwnedByDoctorValidator implements ConstraintValidator<OfficeOwnedByDoctor, Object> {
    private String officeId;
    private String doctorId;
    private final DoctorOfficeService doctorOfficeService;

    @Autowired
    public OfficeOwnedByDoctorValidator(DoctorOfficeService doctorOfficeService) {
        this.doctorOfficeService = doctorOfficeService;
    }

    @Override
    public void initialize(OfficeOwnedByDoctor constraintAnnotation) {
        this.officeId = constraintAnnotation.officeId();
        this.doctorId = constraintAnnotation.doctorId();
    }

    @Override
    public boolean isValid(Object value, javax.validation.ConstraintValidatorContext context) {
        try {
            Field officeIdField = value.getClass().getDeclaredField(officeId);
            Field doctorIdField = value.getClass().getDeclaredField(doctorId);

            officeIdField.setAccessible(true);
            doctorIdField.setAccessible(true);

            long officeIdValue = (long) officeIdField.get(value);
            long doctorIdValue = (long) doctorIdField.get(value);

            List<DoctorOffice> offices = doctorOfficeService.getByDoctorId(doctorIdValue);
            return offices.stream().anyMatch(office -> office.getId() == officeIdValue);
        } catch (Exception e) {
            return false;
        }
    }
}
