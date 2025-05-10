package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.models.Specialty;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.List;

public class AppointmentValidSpecialtyForDoctorValidator implements ConstraintValidator<AppointmentValidSpecialtyForDoctor, Object> {

    private final SpecialtyService specialtyService;
    private String doctorIdFieldName;
    private String specialtyIdFieldName;

    @Autowired
    public AppointmentValidSpecialtyForDoctorValidator(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @Override
    public void initialize(AppointmentValidSpecialtyForDoctor constraintAnnotation) {
        this.doctorIdFieldName = constraintAnnotation.doctorId();
        this.specialtyIdFieldName = constraintAnnotation.specialtyId();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field doctorIdField = value.getClass().getDeclaredField(doctorIdFieldName);
            Field specialtyIdField = value.getClass().getDeclaredField(specialtyIdFieldName);
            doctorIdField.setAccessible(true);
            specialtyIdField.setAccessible(true);
            long doctorId = (long) doctorIdField.get(value);
            long specialtyId = (long) specialtyIdField.get(value);
            List<Specialty> specialtyList = specialtyService.getByDoctorId(doctorId);
            for (Specialty specialty : specialtyList) {
                if (specialty.getId() == specialtyId) {
                    return true;
                }
            }
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{appointment.specialty.valid}")
                    .addPropertyNode(specialtyIdFieldName)
                    .addConstraintViolation();
            return false;
        } catch (Exception e) {
            return false;
        }

    }
}
