package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {ValidOfficeTimeSlotValidator.ForDoctorOfficeAvailabilitySlotForm.class
                            })
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOfficeTimeSlot {
    String message() default "Invalid office time slot.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
