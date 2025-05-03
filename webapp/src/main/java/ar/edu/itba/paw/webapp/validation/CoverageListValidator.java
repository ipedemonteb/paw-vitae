package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.models.Coverage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CoverageListValidator implements javax.validation.ConstraintValidator<CoverageList, List<String>> {
    @Autowired
    private CoverageService cs;
    @Override
    public boolean isValid(List<String> values, javax.validation.ConstraintValidatorContext context) {
        if (values == null) {
            return true;
        }
        for (String value : values) {
            if (cs.findById(Integer.parseInt(value)).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
