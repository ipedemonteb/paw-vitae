package ar.edu.itba.paw.webapp.validation;


import ar.edu.itba.paw.interfaceServices.DoctorOfficeAvailabilityService;
import ar.edu.itba.paw.interfaceServices.UnavailabilitySlotsService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import java.lang.reflect.Field;
import java.time.LocalDate;

public class OfficeAvailableAtDayAndTimeValidator implements ConstraintValidator<OfficeAvailableAtDayAndTime, Object> {

    private String officeIdFieldName;
    private String dateFieldName;
    private String hourFieldName;
    private final DoctorOfficeAvailabilityService doctorOfficeAvailabilityService;
    private final UnavailabilitySlotsService unavailabilitySlotsService;

    @Autowired
    public OfficeAvailableAtDayAndTimeValidator(UnavailabilitySlotsService unavailabilitySlotsService, DoctorOfficeAvailabilityService doctorOfficeAvailabilityService) {
        this.unavailabilitySlotsService = unavailabilitySlotsService;
        this.doctorOfficeAvailabilityService = doctorOfficeAvailabilityService;
    }

    @Override
    public void initialize(OfficeAvailableAtDayAndTime constraintAnnotation) {
        this.officeIdFieldName = constraintAnnotation.officeId();
        this.dateFieldName = constraintAnnotation.date();
        this.hourFieldName = constraintAnnotation.appointmentHour();
    }

    @Override
    public boolean isValid(Object value, javax.validation.ConstraintValidatorContext context) {
        try {
            Field officeIdField = value.getClass().getDeclaredField(officeIdFieldName);
            Field dateField = value.getClass().getDeclaredField(dateFieldName);
            Field hourField = value.getClass().getDeclaredField(hourFieldName);

            officeIdField.setAccessible(true);
            dateField.setAccessible(true);
            hourField.setAccessible(true);

            long officeId = (long) officeIdField.get(value);
            LocalDate date = (LocalDate) dateField.get(value);
            Integer hour = (Integer) hourField.get(value);

            if (date == null || hour == null) {
                return true;
            }

            if (!doctorOfficeAvailabilityService.isAvailableAtDayAndTime(officeId, date, hour)
                    || unavailabilitySlotsService.isUnavailableAtDate(officeId, date)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("appointment.office.date.valid")
                        .addPropertyNode(dateFieldName)
                        .addConstraintViolation();
                return false;
            }
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
