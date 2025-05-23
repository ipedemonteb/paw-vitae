package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UnavailabilityIntersectionValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UnavailabilityIntersection {

    String message() default "Unavailable dates must not overlap.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
