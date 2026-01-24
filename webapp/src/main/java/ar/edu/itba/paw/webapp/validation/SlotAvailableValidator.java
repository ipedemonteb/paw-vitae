package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.AvailabilitySlotsService;
import ar.edu.itba.paw.models.AvailabilitySlots;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import java.util.Optional;

public class SlotAvailableValidator implements ConstraintValidator<SlotAvailable, Long> {

    private final  AvailabilitySlotsService availabilitySlotsService;

    @Autowired
    public SlotAvailableValidator(AvailabilitySlotsService availabilitySlotsService) {
        this.availabilitySlotsService = availabilitySlotsService;
    }

    @Override
    public boolean isValid(Long value, javax.validation.ConstraintValidatorContext context) {
      if (value == null) {
          return true;
      }
        Optional<AvailabilitySlots> availabilitySlot = availabilitySlotsService.getById(value);
        return availabilitySlot.map(availabilitySlots -> availabilitySlots.getStatus().toString().equals("AVAILABLE")).orElse(false);
    }
}
