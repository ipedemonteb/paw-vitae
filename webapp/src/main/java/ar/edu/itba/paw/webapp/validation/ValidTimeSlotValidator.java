package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.AvailabilitySlot;
import ar.edu.itba.paw.models.AvailabilitySlotForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ValidTimeSlotValidator implements ConstraintValidator<ValidTimeSlot, List<AvailabilitySlotForm>> {

    @Override
    public boolean isValid(List<AvailabilitySlotForm> availabilitySlotList, ConstraintValidatorContext constraintValidatorContext) {
        if (availabilitySlotList == null || availabilitySlotList.isEmpty()) {
            return true;
        }
        for (AvailabilitySlotForm slot : availabilitySlotList) {
            if( slot.getStartTime() == null && slot.getEndTime() == null){
                continue;
            }
            if (slot.getStartTime() == null || slot.getEndTime() == null || slot.getStartTime().isAfter(slot.getEndTime())
                    || slot.getDayOfWeek() > 6 || slot.getDayOfWeek() < 0 || slot.getStartTime().getHour() < 8
                    || slot.getStartTime().getHour() > 20 || slot.getEndTime().getHour() < 8 || slot.getEndTime().getHour() > 20
                    || slot.getStartTime().getMinute() != 0 || slot.getEndTime().getMinute() != 0 ) {
                return false;
            }
        }
        return true;
    }
}
