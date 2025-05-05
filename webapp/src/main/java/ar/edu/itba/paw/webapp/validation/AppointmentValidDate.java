package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Documented
@Constraint(validatedBy = { AppointmentValidDateValidator.class })
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AppointmentValidDate {
    String message() default "Time slots must not overlap.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String date();
    String startTime();

}
