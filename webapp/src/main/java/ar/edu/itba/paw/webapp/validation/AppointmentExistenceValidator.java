package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.AvailabilitySlotsService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.AvailabilitySlots;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

public class AppointmentExistenceValidator implements ConstraintValidator<AppointmentExistence, Object> {

    private String userIdFieldName;
    private final AvailabilitySlotsService availabilitySlotsService;
    private final AppointmentService as;
    private String slotIdFieldName;

    @Autowired
    public AppointmentExistenceValidator(AppointmentService as, AvailabilitySlotsService availabilitySlotsService) {
        this.as = as;
        this.availabilitySlotsService = availabilitySlotsService;
    }

    @Override
    public void initialize(AppointmentExistence constraintAnnotation) {
        this.userIdFieldName = constraintAnnotation.userId();
        this.slotIdFieldName = constraintAnnotation.slotId();
    }

    @Override
    public boolean isValid(Object value, javax.validation.ConstraintValidatorContext context) {
        try {
            Field userIdField = value.getClass().getDeclaredField(userIdFieldName);
            Field slotIdField = value.getClass().getDeclaredField(slotIdFieldName);

            userIdField.setAccessible(true);
            slotIdField.setAccessible(true);

            long userId = (long) userIdField.get(value);
            Long slotId = (Long) slotIdField.get(value);

            if (slotId == null) {
                return true;
            }
            AvailabilitySlots slot = availabilitySlotsService.getById(slotId).orElse(null);
            if (slot == null) {
                return true;
            }
            List<Appointment> appointments = as.getAppointmentByUserAndDate(userId, slot.getSlotDate(), slot.getStartTime().getHour());


            if (appointments.isEmpty()) {
                return true;
            }
            boolean isValid = appointments.size() == 1 && !appointments.getFirst().getStatus().equals(AppointmentStatus.CONFIRMADO.getValue());

            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("appointment.date.existence")
                        .addPropertyNode(slotIdFieldName)
                        .addConstraintViolation();
            }

            return isValid;
        } catch (Exception e) {
            return false;
        }
    }
}
