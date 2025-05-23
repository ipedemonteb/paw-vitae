package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.NeighborhoodService;
import ar.edu.itba.paw.models.DoctorOfficeForm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import java.util.List;

public class ValidOfficeNeighborhoodValidator implements ConstraintValidator<ValidOfficeNeighborhood, List<DoctorOfficeForm>> {

    private final NeighborhoodService neighborhoodService;

    @Autowired
    public ValidOfficeNeighborhoodValidator(NeighborhoodService neighborhoodService) {
        this.neighborhoodService = neighborhoodService;
    }

    @Override
    public boolean isValid(List<DoctorOfficeForm> value, javax.validation.ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        for (DoctorOfficeForm office : value) {
            if (office.getNeighborhoodId() == null || neighborhoodService.getById(office.getNeighborhoodId()).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
