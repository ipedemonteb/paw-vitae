package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.webapp.form.DoctorFileForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class InvalidDoctorFileFormValidator implements ConstraintValidator<InvalidDoctorFileForm, DoctorFileForm> {
    private final AppointmentService as;
    @Autowired
    public InvalidDoctorFileFormValidator(AppointmentService as) {
        this.as = as;
    }
    @Override
    public boolean isValid(DoctorFileForm form, ConstraintValidatorContext context) {
        Optional<Appointment> optAppt = as.getById(form.getAppointmentId());
        return optAppt.map(appointment -> appointment.getStatus().equals(AppointmentStatus.COMPLETO.getValue())).orElse(false);

    }
}
