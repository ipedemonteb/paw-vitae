package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {OfficeAvailabilitySlotIntersectionValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OfficeAvailabilitySlotIntersection {
    String message() default "Office availability slots intersect.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
