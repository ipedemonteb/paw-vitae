package ar.edu.itba.paw.webapp.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = { OfficeOwnedByDoctorValidator.ForDoctorOfficeForm.class,
                            OfficeOwnedByDoctorValidator.ForDoctorOfficeAvailabilitySlotForm.class
                            })
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OfficeOwnedByDoctor {
    String message() default "Office does not belong to the doctor.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
