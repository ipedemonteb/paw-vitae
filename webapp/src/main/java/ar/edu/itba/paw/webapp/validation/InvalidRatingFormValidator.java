package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.webapp.form.PatientRatingForm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class InvalidRatingFormValidator implements ConstraintValidator<InvalidRatingForm, PatientRatingForm> {
    private final AppointmentService as;
    @Autowired
    public InvalidRatingFormValidator(AppointmentService as) {
        this.as = as;
    }
    @Override
    public boolean isValid(PatientRatingForm form, ConstraintValidatorContext context) {
        Optional<Appointment> optAppt = as.getById(form.getAppointmentId());
        return optAppt.map(appointment -> appointment.getStatus().equals(AppointmentStatus.COMPLETO.getValue())).orElse(false);

    }
}
