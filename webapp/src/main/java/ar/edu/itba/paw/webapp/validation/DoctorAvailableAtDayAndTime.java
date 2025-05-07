package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {DoctorAvailableAtDayAndTimeValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DoctorAvailableAtDayAndTime {
    String message() default "Doctor is not available at this time.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String doctorId();

    String date();

    String startTime();
}
