package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.DoctorOfficeAvailabilityForm;
import ar.edu.itba.paw.models.DoctorOfficeForm;
import ar.edu.itba.paw.webapp.form.DoctorForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.*;

public class OfficeAvailabilitySlotIntersectionValidator {

    public static class ForDoctorOfficeForm implements ConstraintValidator<OfficeAvailabilitySlotIntersection, DoctorForm> {
        @Override
        public boolean isValid(DoctorForm form, ConstraintValidatorContext context) {
            List<DoctorOfficeForm> offices = form.getDoctorOfficeForm();
            if (offices == null || offices.isEmpty()) {
                return true; // No offices to validate
            }

            // Combine all slots into a single map
            Map<Integer, List<DoctorOfficeAvailabilityForm>> slotsByDay = new TreeMap<>();
            for (DoctorOfficeForm office : offices) {
                if (office.getOfficeAvailabilitySlotForms() != null) {
                    for (DoctorOfficeAvailabilityForm slot : office.getOfficeAvailabilitySlotForms()) {
                        slotsByDay
                                .computeIfAbsent(slot.getDayOfWeek(), k -> new ArrayList<>())
                                .add(slot);
                    }
                }
            }

            // Validate slots for each day
            for (List<DoctorOfficeAvailabilityForm> slots : slotsByDay.values()) {
                slots.sort(Comparator.comparing(DoctorOfficeAvailabilityForm::getStartTime));

                for (int i = 0; i < slots.size() - 1; i++) {
                    DoctorOfficeAvailabilityForm current = slots.get(i);
                    DoctorOfficeAvailabilityForm next = slots.get(i + 1);

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

    public static class ForDoctorOfficeAvailabilityForm implements ConstraintValidator<OfficeAvailabilitySlotIntersection, List<DoctorOfficeAvailabilityForm>> {
        @Override
        public boolean isValid(List<DoctorOfficeAvailabilityForm> officeAvailabilitySlotForms, ConstraintValidatorContext context) {
            if (officeAvailabilitySlotForms == null || officeAvailabilitySlotForms.isEmpty()) {
                return true; // No slots to validate
            }

            Map<Integer, List<DoctorOfficeAvailabilityForm>> slotsByDay = new TreeMap<>();
            for (DoctorOfficeAvailabilityForm slot : officeAvailabilitySlotForms) {
                slotsByDay
                        .computeIfAbsent(slot.getDayOfWeek(), k -> new ArrayList<>())
                        .add(slot);
            }

            for (List<DoctorOfficeAvailabilityForm> slots : slotsByDay.values()) {
                slots.sort(Comparator.comparing(DoctorOfficeAvailabilityForm::getStartTime));

                for (int i = 0; i < slots.size() - 1; i++) {
                    DoctorOfficeAvailabilityForm current = slots.get(i);
                    DoctorOfficeAvailabilityForm next = slots.get(i + 1);

                    if (current.getEndTime().plusHours(1).isAfter(next.getStartTime())) {
                        return false; // Intersection found
                    }
                }
            }

            return true; // No intersections found
        }
    }
}
