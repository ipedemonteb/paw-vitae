package ar.edu.itba.paw.webapp.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MaxNumberOfOfficesValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxNumberOfOffices {
    String message() default "{offices.max}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
