package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.AvailabilitySlot;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.webapp.form.UpdateAvailabilityForm;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TimeSlotIntersectionValidator implements ConstraintValidator<TimeSlotIntersection, List<AvailabilitySlot>> {

    @Override
    public boolean isValid(List<AvailabilitySlot> slots, ConstraintValidatorContext context) {
        if (slots == null || slots.isEmpty()) return true;

        Map<Integer, List<AvailabilitySlot>> groupedByDay = slots.stream()
                .filter(slot -> slot != null && slot.getStartTime() != null && slot.getEndTime() != null)
                .collect(Collectors.groupingBy(AvailabilitySlot::getDayOfWeek));
        for (Map.Entry<Integer, List<AvailabilitySlot>> entry : groupedByDay.entrySet()) {
            List<AvailabilitySlot> daySlots = entry.getValue();
            daySlots.sort(Comparator.comparing(AvailabilitySlot::getStartTime));
            for (int i = 0; i < daySlots.size() - 1; i++) {
                AvailabilitySlot current = daySlots.get(i);
                AvailabilitySlot next = daySlots.get(i + 1);

                if (!current.getEndTime().isBefore(next.getStartTime())) {
                    return false;
                }
            }
        }
        return true;
    }
}