package ar.edu.itba.paw.webapp.validation;

import javax.validation.Payload;
import java.time.LocalDate;
import java.time.LocalDateTime;

public @interface AppointmentValidDate {
    String message() default "Time slots must not overlap.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String date();
    String startTime();

}
