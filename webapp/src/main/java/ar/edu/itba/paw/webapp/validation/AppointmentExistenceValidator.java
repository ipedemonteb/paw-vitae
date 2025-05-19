package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

public class AppointmentExistenceValidator implements ConstraintValidator<AppointmentExistence, Object> {

    private String userIdFieldName;
    private String dateFieldName;
    private String startTimeFieldName;
    private final AppointmentService as;

    @Autowired
    public AppointmentExistenceValidator(AppointmentService as) {
        this.as = as;
    }

    @Override
    public void initialize(AppointmentExistence constraintAnnotation) {
        this.userIdFieldName = constraintAnnotation.userId();
        this.dateFieldName = constraintAnnotation.date();
        this.startTimeFieldName = constraintAnnotation.startTime();
    }

    @Override
    public boolean isValid(Object value, javax.validation.ConstraintValidatorContext context) {
        try {
            Field userIdField = value.getClass().getDeclaredField(userIdFieldName);
            Field dateField = value.getClass().getDeclaredField(dateFieldName);
            Field startTimeField = value.getClass().getDeclaredField(startTimeFieldName);

            userIdField.setAccessible(true);
            dateField.setAccessible(true);
            startTimeField.setAccessible(true);

            long userId = (long) userIdField.get(value);
            LocalDate date = (LocalDate) dateField.get(value);
            Integer startTime = (Integer) startTimeField.get(value);

            if (date == null || startTime == null) {
                return true;
            }

            List<Appointment> appointments = as.getAppointmentByUserAndDate(userId, date, startTime);


            if (appointments.isEmpty()) {
                return true;
            }
            boolean isValid = appointments.size() == 1 && !appointments.getFirst().getStatus().equals(AppointmentStatus.CONFIRMADO.getValue());

            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("{appointment.date.existence}")
                        .addPropertyNode(dateFieldName)
                        .addConstraintViolation();
            }

            return isValid;
        } catch (Exception e) {
            return false;
        }
    }
}
