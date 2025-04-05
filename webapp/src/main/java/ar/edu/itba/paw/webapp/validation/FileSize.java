package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FileSizeValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface FileSize {
    String message() default "The file is too large. Maximum size is {max} bytes.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    // Maximum file size in bytes
    long max();
}
