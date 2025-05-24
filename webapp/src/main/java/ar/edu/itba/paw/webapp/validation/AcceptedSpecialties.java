package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {AcceptedSpecialtiesValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AcceptedSpecialties {
    String message() default "Specialty not accepted.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String specialties();

    String offices();
}
