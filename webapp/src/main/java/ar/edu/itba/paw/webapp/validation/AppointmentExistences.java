package ar.edu.itba.paw.webapp.validation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AppointmentExistences {

    AppointmentExistence[] value();
}
