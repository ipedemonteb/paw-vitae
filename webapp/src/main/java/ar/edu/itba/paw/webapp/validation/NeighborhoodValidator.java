package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.NeighborhoodService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;

public class NeighborhoodValidator implements ConstraintValidator<Neighborhood, Long> {
    private final NeighborhoodService ns;

    @Autowired
    public NeighborhoodValidator(NeighborhoodService ns) {
        this.ns = ns;
    }

    @Override
    public boolean isValid(Long value, javax.validation.ConstraintValidatorContext context) {
        return value == null || ns.getById(value).isPresent();
    }
}
