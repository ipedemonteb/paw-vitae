package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.DoctorOfficeService;
import ar.edu.itba.paw.models.DoctorOffice;
import ar.edu.itba.paw.models.DoctorOfficeAvailabilityForm;
import ar.edu.itba.paw.models.exception.DoctorOfficeNotFoundException;
import ar.edu.itba.paw.webapp.form.UpdateAvailabilityForm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.*;
import java.util.stream.Collectors;

public class ActiveOfficeSlotsValidator implements ConstraintValidator<ActiveOfficeSlots, UpdateAvailabilityForm> {

    private final DoctorOfficeService doctorOfficeService;

    @Autowired
    public ActiveOfficeSlotsValidator(DoctorOfficeService doctorOfficeService) {
        this.doctorOfficeService = doctorOfficeService;
    }

    @Override
    public boolean isValid(UpdateAvailabilityForm form, ConstraintValidatorContext context) {

        Set<Long> ids = form.getDoctorOfficeAvailabilities().stream().map(DoctorOfficeAvailabilityForm::getOfficeId).collect(Collectors.toSet());

        List<DoctorOffice> offices = doctorOfficeService.getActiveByDoctorId(form.getDoctorId());

        for (DoctorOffice office : offices) {
            if (!ids.contains(office.getId())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("office.availabilitySlot.active")
                        .addPropertyNode("doctorOfficeAvailabilities")
                        .addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}
