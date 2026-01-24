package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SlotAvailableValidator.class)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SlotAvailable {
    String message() default "Invalid date ";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
