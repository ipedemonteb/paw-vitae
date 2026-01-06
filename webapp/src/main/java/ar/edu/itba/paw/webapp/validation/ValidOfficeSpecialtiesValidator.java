package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.models.DoctorOfficeForm;
import ar.edu.itba.paw.models.Specialty;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ValidOfficeSpecialtiesValidator implements ConstraintValidator<ValidOfficeSpecialties, DoctorOfficeForm> {
    private final SpecialtyService specialtyService;

    @Autowired
    public ValidOfficeSpecialtiesValidator(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }
    Set<Long> seenIds = new HashSet<>();

    @Override
    public boolean isValid(DoctorOfficeForm value, javax.validation.ConstraintValidatorContext context) {
        if (value == null ) {
            return true;
        }

            if (value.getSpecialtyIds() == null || value.getSpecialtyIds().isEmpty()) {

                return false;
            }
            for (Long id : value.getSpecialtyIds()) {
                if (id == null || id < 0) {
                    return false;
                }
                if (!seenIds.add(id)) {
                    return false;
                }
            }
            seenIds.clear();
        return true;
    }
}
