package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.AvailabilitySlotsService;
import ar.edu.itba.paw.interfaceServices.UnavailabilitySlotsService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import java.lang.reflect.Field;
import java.time.LocalDate;

public class DoctorAvailableAtDayAndTimeValidator implements ConstraintValidator<DoctorAvailableAtDayAndTime, Object> {

    private String doctorIdFieldName;
    private String dateFieldName;
    private String startTimeFieldName;
    private final AvailabilitySlotsService availabilitySlotsService;
    private final UnavailabilitySlotsService unavailabilitySlotsService;

    @Autowired
    public DoctorAvailableAtDayAndTimeValidator(AvailabilitySlotsService availabilitySlotsService, UnavailabilitySlotsService unavailabilitySlotsService) {
        this.availabilitySlotsService = availabilitySlotsService;
        this.unavailabilitySlotsService = unavailabilitySlotsService;;
    }

    @Override
    public void initialize(DoctorAvailableAtDayAndTime constraintAnnotation) {
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
                return true;
            }
            boolean flag = availabilitySlotsService.isAvailableAtDateAndTime(doctorId, date, startTime)
                    && !unavailabilitySlotsService.isUnavailableAtDate(doctorId, date);
            if (!flag) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("{doctor.available.at.day.and.time.invalid}")
                        .addPropertyNode(dateFieldName
                        )
                        .addConstraintViolation();
            }
            return flag;
        }catch (Exception e) {
            return false;
        }
    }
}
