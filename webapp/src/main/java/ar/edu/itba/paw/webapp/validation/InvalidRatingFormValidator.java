package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.RatingService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.Rating;
import ar.edu.itba.paw.webapp.form.PatientRatingForm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class InvalidRatingFormValidator implements ConstraintValidator<InvalidRatingForm, PatientRatingForm> {
    private final AppointmentService appointmentService;
    private final RatingService ratingService;
    @Autowired
    public InvalidRatingFormValidator(AppointmentService appointmentService, RatingService ratingService) {
        this.appointmentService = appointmentService;
        this.ratingService = ratingService;
    }
    @Override
    public boolean isValid(PatientRatingForm form, ConstraintValidatorContext context) {
        Optional<Appointment> optAppt = appointmentService.getById(form.getAppointmentId());
        Optional<Rating> rating = ratingService.getRatingByAppointmentId(form.getAppointmentId());
        return optAppt.map(appointment -> appointment.getStatus().equals(AppointmentStatus.COMPLETO.getValue())).orElse(false) && rating.isEmpty();
    }
}
