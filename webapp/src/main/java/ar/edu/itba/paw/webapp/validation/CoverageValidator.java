package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.CoverageService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;

public class CoverageValidator implements ConstraintValidator<Coverage, String> {
    @Autowired
    private CoverageService cs;
    @Override
    public boolean isValid(String value, javax.validation.ConstraintValidatorContext context) {
        return value == null || cs.findById(Integer.parseInt(value)).isPresent();
    }
}
