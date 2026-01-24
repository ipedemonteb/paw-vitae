package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.AvailabilitySlotsService;
import ar.edu.itba.paw.models.AvailabilitySlots;
import ar.edu.itba.paw.webapp.validation.AppointmentValidDate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class AppointmentValidDateValidator implements ConstraintValidator<AppointmentValidDate, Object> {

    private String slotIdFieldName;
    private final AvailabilitySlotsService availabilitySlotsService;

    @Autowired
    public AppointmentValidDateValidator(AvailabilitySlotsService availabilitySlotsService) {
        this.availabilitySlotsService = availabilitySlotsService;
    }

    @Override
    public void initialize(AppointmentValidDate constraintAnnotation) {
        this.slotIdFieldName = constraintAnnotation.slotId();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field slotIdField = value.getClass().getDeclaredField(slotIdFieldName);

            slotIdField.setAccessible(true);

            Long slotId = (Long) slotIdField.get(value);

            if (slotId == null) {
                return true;
            }
            AvailabilitySlots slot = availabilitySlotsService.getById(slotId).orElse(null);
            if (slot == null) {
                return true;
            }
            LocalDateTime appointmentDateTime = LocalDateTime.of(slot.getSlotDate(), LocalTime.of(slot.getStartTime().getHour(), 0));
            LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());


            if (!appointmentDateTime.isAfter(now)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("appointment.date.invalid")
                        .addPropertyNode(slotIdFieldName)
                        .addConstraintViolation();
                return false;
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
