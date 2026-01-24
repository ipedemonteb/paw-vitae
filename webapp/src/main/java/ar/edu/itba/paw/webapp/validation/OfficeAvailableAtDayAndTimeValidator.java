package ar.edu.itba.paw.webapp.validation;


import ar.edu.itba.paw.interfaceServices.AvailabilitySlotsService;
import ar.edu.itba.paw.interfaceServices.DoctorOfficeAvailabilityService;
import ar.edu.itba.paw.interfaceServices.UnavailabilitySlotsService;
import ar.edu.itba.paw.models.AvailabilitySlots;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Optional;
import java.util.OptionalLong;

public class OfficeAvailableAtDayAndTimeValidator implements ConstraintValidator<OfficeAvailableAtDayAndTime, Object> {

    private String officeIdFieldName;
    private String dateFieldSlotId;

    private final DoctorOfficeAvailabilityService doctorOfficeAvailabilityService;
    private final UnavailabilitySlotsService unavailabilitySlotsService;
    private final AvailabilitySlotsService availabilitySlotsService;

    @Autowired
    public OfficeAvailableAtDayAndTimeValidator(UnavailabilitySlotsService unavailabilitySlotsService, DoctorOfficeAvailabilityService doctorOfficeAvailabilityService,AvailabilitySlotsService availabilitySlotsService) {
        this.unavailabilitySlotsService = unavailabilitySlotsService;
        this.doctorOfficeAvailabilityService = doctorOfficeAvailabilityService;
        this.availabilitySlotsService = availabilitySlotsService;
    }

    @Override
    public void initialize(OfficeAvailableAtDayAndTime constraintAnnotation) {
        this.officeIdFieldName = constraintAnnotation.officeId();

        this.dateFieldSlotId = constraintAnnotation.slotId();
    }

    @Override
    public boolean isValid(Object value, javax.validation.ConstraintValidatorContext context) {
        try {
            Field officeIdField = value.getClass().getDeclaredField(officeIdFieldName);
            Field slotIdField = value.getClass().getDeclaredField(dateFieldSlotId);

            officeIdField.setAccessible(true);
            slotIdField.setAccessible(true);

            long officeId = (long) officeIdField.get(value);
            Long slotId = (Long) slotIdField.get(value);

            if (slotId == null) {
                return true;
            }
            Optional<AvailabilitySlots> slot = availabilitySlotsService.getById(slotId);
            if (slot.isEmpty()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("appointment.office.date.valid")
                        .addPropertyNode(dateFieldSlotId)
                        .addConstraintViolation();
                return false;
            }
            LocalDate date = slot.get().getSlotDate();
            int hour = slot.get().getStartTime().getHour();
            if (!doctorOfficeAvailabilityService.isAvailableAtDayAndTime(officeId, date, hour)
                    || unavailabilitySlotsService.isUnavailableAtDate(officeId, date)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("appointment.office.date.valid")
                        .addPropertyNode(dateFieldSlotId)
                        .addConstraintViolation();
                return false;
            }
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
