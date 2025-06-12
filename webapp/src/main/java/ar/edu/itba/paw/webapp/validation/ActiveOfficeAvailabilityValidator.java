package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.DoctorOfficeAvailabilityService;
import ar.edu.itba.paw.models.DoctorOfficeAvailability;
import ar.edu.itba.paw.models.DoctorOfficeForm;
import ar.edu.itba.paw.webapp.form.OfficeForm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ActiveOfficeAvailabilityValidator implements ConstraintValidator<ActiveOfficeAvailability, OfficeForm> {

    private final DoctorOfficeAvailabilityService doctorOfficeAvailabilityService;

    @Autowired
    public ActiveOfficeAvailabilityValidator(DoctorOfficeAvailabilityService doctorOfficeAvailabilityService) {
        this.doctorOfficeAvailabilityService = doctorOfficeAvailabilityService;
    }

    @Override
    public boolean isValid(OfficeForm officeForm, ConstraintValidatorContext context) {
        if (officeForm.getDoctorOfficeForm() == null) {
            return true; // validated elsewhere
        }

        List<DoctorOfficeAvailability> existingSlots = doctorOfficeAvailabilityService.getByDoctorId(officeForm.getDoctorId());

        for (DoctorOfficeForm form : officeForm.getDoctorOfficeForm()) {
             if ((form.getId() == null && form.getActive()) || (form.getId() != null && form.getActive() && existingSlots.stream().noneMatch(s -> s.getOffice().getId().equals(form.getId())))) {
                 context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("{offices.active.availability}")
                            .addPropertyNode("doctorOfficeForm")
                            .addConstraintViolation();
                 return false; // Invalid if a new office is active because it will not have availability
             }
        }

        return true; // All slots are valid
    }
}
