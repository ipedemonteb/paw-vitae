package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {AppointmentExistenceValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(AppointmentExistences.class)
public @interface AppointmentExistence {

    String message() default "Appointment already exists at this time.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String userId();

    String date();

    String startTime();
}


