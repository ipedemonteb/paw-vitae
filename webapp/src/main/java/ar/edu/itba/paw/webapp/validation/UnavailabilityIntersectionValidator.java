package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.UnavailabilitySlotForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.List;

public class UnavailabilityIntersectionValidator implements ConstraintValidator<UnavailabilityIntersection, List<UnavailabilitySlotForm>> {

    @Override
    public boolean isValid(List<UnavailabilitySlotForm> slots, ConstraintValidatorContext context) {
        if (slots == null || slots.isEmpty()) {
            return true;
        }

        List<UnavailabilitySlotForm> validSlots = slots.stream()
                .filter(slot -> slot.getStartDate() != null && slot.getEndDate() != null)
                .toList();

        for (int i = 0; i < validSlots.size(); i++) {
            UnavailabilitySlotForm slot1 = validSlots.get(i);
            LocalDate start1 = slot1.getStartDate();
            LocalDate end1 = slot1.getEndDate();

            for (int j = i + 1; j < validSlots.size(); j++) {
                UnavailabilitySlotForm slot2 = validSlots.get(j);
                LocalDate start2 = slot2.getStartDate();
                LocalDate end2 = slot2.getEndDate();

                if (rangesOverlap(start1, end1, start2, end2)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean rangesOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !end1.isBefore(start2) && !end2.isBefore(start1);
    }
}
