package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;

public class SpecialtyValidator implements ConstraintValidator<Specialty, Long> {

    private final SpecialtyService specialtyService;

    @Autowired
    public SpecialtyValidator(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @Override
    public boolean isValid(Long value, javax.validation.ConstraintValidatorContext context) {
        return value == null || specialtyService.getById(value).isPresent();
    }
}
