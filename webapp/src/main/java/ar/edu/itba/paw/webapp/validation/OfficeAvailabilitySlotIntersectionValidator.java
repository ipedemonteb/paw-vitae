package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.DoctorOfficeAvailabilitySlotForm;
import ar.edu.itba.paw.models.DoctorOfficeForm;
import ar.edu.itba.paw.webapp.form.DoctorForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.*;

public class OfficeAvailabilitySlotIntersectionValidator implements ConstraintValidator<OfficeAvailabilitySlotIntersection, DoctorForm> {

    @Override
    public boolean isValid(DoctorForm form, ConstraintValidatorContext context) {
        List<DoctorOfficeForm> offices = form.getDoctorOfficeForm();
        if (offices == null || offices.isEmpty()) {
            return true; // No offices to validate
        }

        // Combine all slots into a single map
        Map<Integer, List<DoctorOfficeAvailabilitySlotForm>> slotsByDay = new TreeMap<>();
        for (DoctorOfficeForm office : offices) {
            if (office.getOfficeAvailabilitySlotForms() != null) {
                for (DoctorOfficeAvailabilitySlotForm slot : office.getOfficeAvailabilitySlotForms()) {
                    slotsByDay
                            .computeIfAbsent(slot.getDayOfWeek(), k -> new ArrayList<>())
                            .add(slot);
                }
            }
        }

        // Validate slots for each day
        for (List<DoctorOfficeAvailabilitySlotForm> slots : slotsByDay.values()) {
            slots.sort(Comparator.comparing(DoctorOfficeAvailabilitySlotForm::getStartTime));

            for (int i = 0; i < slots.size() - 1; i++) {
                DoctorOfficeAvailabilitySlotForm current = slots.get(i);
                DoctorOfficeAvailabilitySlotForm next = slots.get(i + 1);

                if (current.getEndTime().plusHours(1).isAfter(next.getStartTime())) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("{office.availabilitySlot.intersection}")
                            .addPropertyNode("officeAvailabilitySlotForms")
                            .addConstraintViolation();
                    return false; // Intersection found
                }
            }
        }

        return true; // No intersections found
    }
}
