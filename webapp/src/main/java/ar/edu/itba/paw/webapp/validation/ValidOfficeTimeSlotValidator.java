package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.DoctorOfficeAvailabilitySlotForm;
import ar.edu.itba.paw.models.DoctorOfficeForm;

import javax.validation.ConstraintValidator;
import java.util.List;

public class ValidOfficeTimeSlotValidator implements ConstraintValidator<ValidOfficeTimeSlot, List<DoctorOfficeForm>> {
    @Override
    public boolean isValid(List<DoctorOfficeForm> officeForms, javax.validation.ConstraintValidatorContext context) {
        if (officeForms == null || officeForms.isEmpty()) {
            return false; // Invalid if the list is null or empty
        }
        for (int i = 0; i < officeForms.size(); i++) {
            DoctorOfficeForm form = officeForms.get(i);
            if (form.getOfficeAvailabilitySlotForms() == null || form.getOfficeAvailabilitySlotForms().isEmpty()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("{office.timeSlot.empty")
                        .addPropertyNode("officeAvailabilitySlotForms")
                        .inIterable().atIndex(i)
                        .addConstraintViolation();
                return false; // Invalid if any office has no time slots
            }
            for (int j = 0; j < form.getOfficeAvailabilitySlotForms().size(); j++) {
                DoctorOfficeAvailabilitySlotForm slot = form.getOfficeAvailabilitySlotForms().get(j);
                if (slot.getOfficeId() != null || slot.getId() != null || slot.getStartTime() == null || slot.getEndTime() == null || slot.getStartTime().isAfter(slot.getEndTime()) || slot.getStartTime().getHour() < 8 || slot.getEndTime().getHour() > 21) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("{office.invalid.timeSlot}")
                            .addPropertyNode("officeAvailabilitySlotForms")
                            .inIterable().atIndex(i)
                            .addPropertyNode("startTime")
                            .inIterable().atIndex(j)
                            .addConstraintViolation();
                    return false;
                }
            }
        }
        return true; // All forms are valid
    }
}
