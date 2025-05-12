package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = InvalidRatingFormValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface InvalidRatingForm {
    String message() default "Invalid rating form";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
