package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AppointmentFileValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public  @interface AppointmentFileValid {
    String message() default "The file is not valid. Please upload a valid file. The file must be less than 5MB and of type PDF";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
