package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {ValidOfficeIdValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOfficeId {
    String message() default "{offices.invalid.id}";

    Class<?>[] groups() default {};

    Class<? extends javax.validation.Payload>[] payload() default {};

    String offices() default "doctorOfficeForm"; // Default field name for office ID

    String doctorId() default "doctorId"; // Default field name for doctor ID
}
