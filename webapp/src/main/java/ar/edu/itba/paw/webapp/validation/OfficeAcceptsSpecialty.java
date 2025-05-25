package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {OfficeAcceptsSpecialtyValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OfficeAcceptsSpecialty {
    String message() default "Office does not accept this specialty.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String officeId();

    String specialtyId();

}
