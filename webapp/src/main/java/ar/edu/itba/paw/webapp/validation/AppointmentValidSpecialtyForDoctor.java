package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = { AppointmentValidSpecialtyForDoctorValidator.class })
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AppointmentValidSpecialtyForDoctor {
    String message() default "Appointment already exists at this time.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String specialtyId();

    String doctorId();
}
