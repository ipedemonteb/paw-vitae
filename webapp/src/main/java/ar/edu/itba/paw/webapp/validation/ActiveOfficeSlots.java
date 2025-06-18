package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ActiveOfficeSlotsValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ActiveOfficeSlots {
    String message() default "Active office slots are required.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
