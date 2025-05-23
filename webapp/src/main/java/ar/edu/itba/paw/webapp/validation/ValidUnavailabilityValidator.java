package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.UnavailabilitySlotForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.List;

public class ValidUnavailabilityValidator implements ConstraintValidator<ValidUnavailability, List<UnavailabilitySlotForm>> {

    @Override
    public boolean isValid(List<UnavailabilitySlotForm> unavailabilitySlotFormList, ConstraintValidatorContext context) {
        if (unavailabilitySlotFormList == null || unavailabilitySlotFormList.isEmpty()) {
            return true;
        }

        List<UnavailabilitySlotForm> validSlots = unavailabilitySlotFormList.stream()
                .filter(slot -> slot.getStartDate() != null && slot.getEndDate() != null)
                .toList();

        for (UnavailabilitySlotForm slot : validSlots) {
            LocalDate start = slot.getStartDate();
            LocalDate end = slot.getEndDate();

            // Validación de fechas inválidas
            if (start.isAfter(end)) {
                return false;
            }

            // Validación de fechas pasadas (termina antes de hoy)
            if (end.isBefore(LocalDate.now())) {
                return false;
            }
        }
        return true;
    }

}
