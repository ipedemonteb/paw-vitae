package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.DoctorOfficeAvailabilitySlotForm;
import ar.edu.itba.paw.models.DoctorOfficeForm;
import ar.edu.itba.paw.webapp.form.DoctorForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ValidOfficeTimeSlotValidator implements ConstraintValidator<ValidOfficeTimeSlot, DoctorForm> {
    @Override
    public boolean isValid(DoctorForm doctorForm, ConstraintValidatorContext context) {
        List<DoctorOfficeForm> officeForms = doctorForm.getDoctorOfficeForm();
        if (officeForms == null || officeForms.isEmpty()) {
            return false; // Invalid if the list is null or empty
        }
        for (DoctorOfficeForm form : officeForms) {
            if (form.getOfficeAvailabilitySlotForms() == null || form.getOfficeAvailabilitySlotForms().isEmpty()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("{office.timeSlot.empty")
                        .addPropertyNode("officeAvailabilitySlotForms")
                        .addConstraintViolation();
                return false; // Invalid if any office has no time slots
            }
            for (DoctorOfficeAvailabilitySlotForm slot : form.getOfficeAvailabilitySlotForms()) {
                if (slot.getOfficeId() != null || slot.getId() != null || slot.getStartTime() == null || slot.getEndTime() == null || slot.getStartTime().isAfter(slot.getEndTime()) || slot.getStartTime().getHour() < 8 || slot.getEndTime().getHour() > 21) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("{office.invalid.timeSlot}")
                            .addPropertyNode("officeAvailabilitySlotForms")
                            .addConstraintViolation();
                    return false;
                }
            }
        }
        return true; // All forms are valid
    }
}
