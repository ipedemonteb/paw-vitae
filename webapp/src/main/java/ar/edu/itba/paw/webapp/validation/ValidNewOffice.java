package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {ValidNewOfficeValidator.ForOfficeForm.class,
                          ValidNewOfficeValidator.ForDoctorForm.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNewOffice {
    String message() default "{offices.new.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
