package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.AppointmentFileService;
import ar.edu.itba.paw.models.AppointmentFile;
import ar.edu.itba.paw.webapp.form.DoctorFileForm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class InvalidFileQuantityValidator implements ConstraintValidator<InvalidFileQuantity, DoctorFileForm> {
    private final AppointmentFileService appointmentFileService;

    @Autowired
    public InvalidFileQuantityValidator(AppointmentFileService appointmentFileService) {
        this.appointmentFileService = appointmentFileService;
    }

    @Override
    public boolean isValid(DoctorFileForm form, ConstraintValidatorContext context) {
        List<AppointmentFile> files = appointmentFileService.getByAppointmentIdForDoctor(form.getAppointmentId());
        if(files.size() + form.getFiles().length > 5) {
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate("{InvalidFileQuantity.message}")
                    .addPropertyNode("patientFiles")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

}