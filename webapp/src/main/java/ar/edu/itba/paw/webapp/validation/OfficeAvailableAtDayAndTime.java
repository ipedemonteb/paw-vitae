package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {OfficeAvailableAtDayAndTimeValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OfficeAvailableAtDayAndTime {
    String message() default "Doctor is not available at this time.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String officeId();

    String date();

    String appointmentHour();
}
