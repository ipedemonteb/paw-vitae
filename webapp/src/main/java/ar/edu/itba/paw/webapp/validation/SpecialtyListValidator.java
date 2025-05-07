package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class SpecialtyListValidator implements ConstraintValidator<SpecialtyList, List<String>> {

    private SpecialtyService specialtyService;

    @Autowired
    public SpecialtyListValidator(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @Override
    public boolean isValid(List<String> strings, ConstraintValidatorContext constraintValidatorContext) {
        if (strings == null) {
            return true;
        }
        for (String value : strings) {
            if (specialtyService.getById(Integer.parseInt(value)).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
