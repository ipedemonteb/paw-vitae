package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.DoctorOfficeForm;

import javax.validation.ConstraintValidator;
import java.util.List;

public class ValidOfficeTimeSlotValidator implements ConstraintValidator<ValidOfficeTimeSlot, List<DoctorOfficeForm>> {
    @Override
    public boolean isValid(List<DoctorOfficeForm> officeForms, javax.validation.ConstraintValidatorContext context) {
        if (officeForms == null || officeForms.isEmpty()) {
            return false; // Invalid if the list is null or empty
        }
        for (DoctorOfficeForm form : officeForms) {
            if (form.getOfficeAvailabilitySlotForms() == null || form.getOfficeAvailabilitySlotForms().isEmpty()) {
                return false; // Invalid if any office has no time slots
            }
            for (var slot : form.getOfficeAvailabilitySlotForms()) {
                if (slot.getOfficeId() != null || slot.getId() != null) {
                    return false;
                } else if (slot.getStartTime() == null || slot.getEndTime() == null || slot.getStartTime().isAfter(slot.getEndTime())) {
                    return false; // Invalid if any time slot is malformed
                } else if (slot.getStartTime().getHour() < 8 || slot.getEndTime().getHour() > 20) {
                    return false; // Invalid if any time slot is outside of office hours
                }
            }
        }
        return true; // All forms are valid
    }
}
