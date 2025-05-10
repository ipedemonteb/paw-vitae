package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.models.Coverage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CoverageListValidator implements javax.validation.ConstraintValidator<CoverageList, List<Long>> {

    private final CoverageService cs;

    @Autowired
    public CoverageListValidator(CoverageService cs) {
        this.cs = cs;
    }

    @Override
    public boolean isValid(List<Long> values, javax.validation.ConstraintValidatorContext context) {
        if (values == null) {
            return true;
        }
        for (Long value : values) {
            if (cs.findById(value).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
