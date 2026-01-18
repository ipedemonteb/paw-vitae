package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.interfaceServices.DoctorOfficeService;
import ar.edu.itba.paw.models.DoctorOffice;
import ar.edu.itba.paw.models.DoctorOfficeAvailabilityForm;
import ar.edu.itba.paw.webapp.form.AppointmentForm;
import ar.edu.itba.paw.webapp.form.UpdateAvailabilityForm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import java.util.List;

public class OfficeOwnedByDoctorValidator {

    public static class ForDoctorOfficeForm implements ConstraintValidator<OfficeOwnedByDoctor, AppointmentForm> {

        private final DoctorOfficeService doctorOfficeService;

        @Autowired
        public ForDoctorOfficeForm(DoctorOfficeService doctorOfficeService) {
            this.doctorOfficeService = doctorOfficeService;
        }

        @Override
        public boolean isValid(AppointmentForm form, javax.validation.ConstraintValidatorContext context) {
            List<DoctorOffice> offices = doctorOfficeService.getActiveByDoctorId(form.getDoctorId());
            if (offices.stream().noneMatch(office -> office.getId() == form.getOfficeId())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("appointment.office.valid")
                        .addPropertyNode("officeId")
                        .addConstraintViolation();
                return false;
            }
            return true;
        }

    }

    public static class ForCreateAppointmentForm implements ConstraintValidator<OfficeOwnedByDoctor, AppointmentForm> {

        private final DoctorOfficeService doctorOfficeService;

        @Autowired
        public ForCreateAppointmentForm(DoctorOfficeService doctorOfficeService) {
            this.doctorOfficeService = doctorOfficeService;
        }

        @Override
        public boolean isValid(AppointmentForm form, javax.validation.ConstraintValidatorContext context) {
            List<DoctorOffice> offices = doctorOfficeService.getActiveByDoctorId(form.getDoctorId());
            if (offices.stream().noneMatch(office -> office.getId() == form.getOfficeId())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("{appointment.office.valid}")
                        .addPropertyNode("officeId")
                        .addConstraintViolation();
                return false;
            }
            return true;
        }
    }

    public static class ForDoctorOfficeAvailabilityForm implements ConstraintValidator<OfficeOwnedByDoctor, UpdateAvailabilityForm> {

        private final DoctorOfficeService doctorOfficeService;

        @Autowired
        public ForDoctorOfficeAvailabilityForm(DoctorOfficeService doctorOfficeService) {
            this.doctorOfficeService = doctorOfficeService;
        }

        @Override
        public boolean isValid(UpdateAvailabilityForm form, javax.validation.ConstraintValidatorContext context) {
            List<DoctorOffice> offices = doctorOfficeService.getAllByDoctorId(form.getDoctorId());
            if (form.getDoctorOfficeAvailabilities() != null) {
                for (DoctorOfficeAvailabilityForm slot : form.getDoctorOfficeAvailabilities()) {
                    if (offices.stream().noneMatch(office -> office.getId().equals(slot.getOfficeId()))) {
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate("{appointment.office.valid}")
                                .addPropertyNode("doctorOfficeAvailabilities")
                                .addConstraintViolation();
                        return false;
                    }
                }
            }
            return true;
        }
    }

}
