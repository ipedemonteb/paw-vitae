package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.webapp.validation.AppointmentValidDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class AppointmentValidDateValidator implements ConstraintValidator<AppointmentValidDate, Object> {

    private String dateFieldName;
    private String timeFieldName;

    @Override
    public void initialize(AppointmentValidDate constraintAnnotation) {
        this.dateFieldName = constraintAnnotation.date();
        this.timeFieldName = constraintAnnotation.startTime();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field dateField = value.getClass().getDeclaredField(dateFieldName);
            Field timeField = value.getClass().getDeclaredField(timeFieldName);

            dateField.setAccessible(true);
            timeField.setAccessible(true);

            LocalDate date = (LocalDate) dateField.get(value);
            Integer hour = (Integer) timeField.get(value);

            if (date == null || hour == null) {
                return true;
            }

            LocalDateTime appointmentDateTime = LocalDateTime.of(date, LocalTime.of(hour, 0));
            LocalDateTime now = LocalDateTime.now();


            if (!appointmentDateTime.isAfter(now)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("{appointment.date.invalid}")
                        .addPropertyNode(dateFieldName)
                        .addConstraintViolation();
                return false;
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
