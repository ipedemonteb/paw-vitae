package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.AvailabilitySlot;
import ar.edu.itba.paw.models.AvailabilitySlotForm;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.webapp.form.UpdateAvailabilityForm;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TimeSlotIntersectionValidator implements ConstraintValidator<TimeSlotIntersection, List<AvailabilitySlotForm>> {

    @Override
    public boolean isValid(List<AvailabilitySlotForm> slots, ConstraintValidatorContext context) {
        if (slots == null || slots.isEmpty()) return true;

        Map<Integer, List<AvailabilitySlotForm>> groupedByDay = slots.stream()
                .filter(slot -> slot != null && slot.getStartTime() != null && slot.getEndTime() != null)
                .collect(Collectors.groupingBy(AvailabilitySlotForm::getDayOfWeek));
        for (Map.Entry<Integer, List<AvailabilitySlotForm>> entry : groupedByDay.entrySet()) {
            List<AvailabilitySlotForm> daySlots = entry.getValue();
            daySlots.sort(Comparator.comparing(AvailabilitySlotForm::getStartTime));
            for (int i = 0; i < daySlots.size() - 1; i++) {
                AvailabilitySlotForm current = daySlots.get(i);
                AvailabilitySlotForm next = daySlots.get(i + 1);

                if (!current.getEndTime().isBefore(next.getStartTime())) {
                    return false;
                }
            }
        }
        return true;
    }
}