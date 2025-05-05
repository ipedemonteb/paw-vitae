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
    private String doctorIdFieldName;
    private String dateFieldName;
    private String startTimeFieldName;

    @Autowired
    private AppointmentService as;

    @Override
    public void initialize(AppointmentExistence constraintAnnotation) {
        this.doctorIdFieldName = constraintAnnotation.doctorId();
        this.dateFieldName = constraintAnnotation.date();
        this.startTimeFieldName = constraintAnnotation.startTime();
    }

    @Override
    public boolean isValid(Object value, javax.validation.ConstraintValidatorContext context) {
        try {
            Field doctorIdField = value.getClass().getDeclaredField(doctorIdFieldName);
            Field dateField = value.getClass().getDeclaredField(dateFieldName);
            Field startTimeField = value.getClass().getDeclaredField(startTimeFieldName);

            doctorIdField.setAccessible(true);
            dateField.setAccessible(true);
            startTimeField.setAccessible(true);

            long doctorId = (long) doctorIdField.get(value);
            LocalDate date = (LocalDate) dateField.get(value);
            Integer startTime = (Integer) startTimeField.get(value);

            if (date == null || startTime == null) {
                return true; // If date or start time is null, skip validation
            }

            List<Appointment> appointments = as.getAppointmentByUserAndDate(doctorId, date, startTime);


            if (appointments.isEmpty()) {
                return true;
            }


            boolean isValid = appointments.size() == 1 &&
                    !appointments.get(0).getStatus().equals(AppointmentStatus.CONFIRMADO.getValue());

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
