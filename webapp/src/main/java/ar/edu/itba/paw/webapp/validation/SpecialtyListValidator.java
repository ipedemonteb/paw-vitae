package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class SpecialtyListValidator implements ConstraintValidator<SpecialtyList, List<Long>> {

    private final SpecialtyService specialtyService;

    @Autowired
    public SpecialtyListValidator(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @Override
    public boolean isValid(List<Long> specialties, ConstraintValidatorContext constraintValidatorContext) {
        if (specialties == null) {
            return true;
        }
        if (specialties.isEmpty()) {
            return false;
        }

        for (Long value : specialties) {
            if (specialtyService.getById(value).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
