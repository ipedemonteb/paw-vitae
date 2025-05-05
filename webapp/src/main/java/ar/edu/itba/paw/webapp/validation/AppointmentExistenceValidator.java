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
        System.out.println("I AM LOST");
        try {
            System.out.println("NIGGA WHY 1");
            Field doctorIdField = value.getClass().getDeclaredField(doctorIdFieldName);
            System.out.println("NIGGA WHY 2");
            Field dateField = value.getClass().getDeclaredField(dateFieldName);
            System.out.println("NIGGA WHY 3");
            Field startTimeField = value.getClass().getDeclaredField(startTimeFieldName);

            System.out.println("NIGGA WHY 4");
            doctorIdField.setAccessible(true);
            System.out.println("NIGGA WHY 5");
            dateField.setAccessible(true);
            System.out.println("NIGGA WHY 6");
            startTimeField.setAccessible(true);

            System.out.println("NIGGA WHY 7");
            long doctorId = (long) doctorIdField.get(value);
            System.out.println("NIGGA WHY 8");
            LocalDate date = (LocalDate) dateField.get(value);
            System.out.println("NIGGA WHY 9");
            Integer startTime = (Integer) startTimeField.get(value);
            System.out.println("NIGGA WHY 10");

            if (date == null || startTime == null) {
                System.out.println("NIGGA 1");
                return true; // If date or start time is null, skip validation
            }

            List<Appointment> appointments = as.getAppointmentByDoctorAndDate(doctorId, date, startTime);

            if (appointments.isEmpty()) {
                System.out.println("NIGGA 2");
                return true;
            }

            System.out.println("NIGGA 3: " + (appointments.size() == 1 && !appointments.stream().findFirst().get().getStatus().equals(AppointmentStatus.CONFIRMADO.getValue())));

            return appointments.size() == 1 && !appointments.stream().findFirst().get().getStatus().equals(AppointmentStatus.CONFIRMADO.getValue());

        } catch (Exception e) {
            System.out.println("NIGGA WHY final");
            return false;
        }
    }
}
