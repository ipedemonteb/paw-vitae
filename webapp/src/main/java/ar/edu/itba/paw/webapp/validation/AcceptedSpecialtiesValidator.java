package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.DoctorOfficeForm;

import javax.validation.ConstraintValidator;
import java.lang.reflect.Field;
import java.util.List;

public class AcceptedSpecialtiesValidator implements ConstraintValidator<AcceptedSpecialties, Object> {
    private String specialtyField;
    private String officesField;

    @Override
    public void initialize(AcceptedSpecialties constraintAnnotation) {
        this.specialtyField = constraintAnnotation.specialties();
        this.officesField = constraintAnnotation.offices();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isValid(Object value, javax.validation.ConstraintValidatorContext context) {
        try{
            Field specialtyField = value.getClass().getDeclaredField(this.specialtyField);
            Field officesField = value.getClass().getDeclaredField(this.officesField);

            specialtyField.setAccessible(true);
            officesField.setAccessible(true);

            List<Long> specialties = (List<Long>) specialtyField.get(value);
            List<DoctorOfficeForm> offices = (List<DoctorOfficeForm>) officesField.get(value);

            if (specialties == null || specialties.isEmpty() || offices == null || offices.isEmpty()) {
                return true;
            }

            for (DoctorOfficeForm office : offices) {
                if (office.getSpecialtyIds() == null || office.getSpecialtyIds().isEmpty()) {
                    return true;
                }
                for (Long id : office.getSpecialtyIds()) {
                    if (!specialties.contains(id)) {
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate("{specialties.not.accepted}")
                                .addPropertyNode(this.officesField)
                                .addConstraintViolation();
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
