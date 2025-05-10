package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.CoverageService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;

public class CoverageValidator implements ConstraintValidator<Coverage, Long> {

    private final CoverageService cs;

    @Autowired
    public CoverageValidator(CoverageService cs) {
        this.cs = cs;
    }

    @Override
    public boolean isValid(Long value, javax.validation.ConstraintValidatorContext context) {
        return value == null || cs.findById(value).isPresent();
    }
}
