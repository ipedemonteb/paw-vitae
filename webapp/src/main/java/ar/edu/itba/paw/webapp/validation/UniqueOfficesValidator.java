package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.DoctorOfficeForm;

import javax.validation.ConstraintValidator;
import java.util.List;

public class UniqueOfficesValidator implements ConstraintValidator<UniqueOffices, List<DoctorOfficeForm>> {
    @Override
    public boolean isValid(List<DoctorOfficeForm> value, javax.validation.ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        for (int i = 0; i < value.size(); i++) {
            for (int j = i + 1; j < value.size(); j++) {
                if (value.get(i).getOfficeName().equals(value.get(j).getOfficeName()) && value.get(i).getNeighborhoodId().equals(value.get(j).getNeighborhoodId())) {
                    return false;
                }
            }
        }
        return true;
    }
}
