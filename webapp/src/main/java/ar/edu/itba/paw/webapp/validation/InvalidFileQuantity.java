package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = InvalidFileQuantityValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface InvalidFileQuantity{
    String message() default "Invalid doctor file form";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
