package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.DoctorOfficeAvailabilityForm;
import ar.edu.itba.paw.models.DoctorOfficeForm;
import ar.edu.itba.paw.webapp.form.DoctorForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ValidOfficeTimeSlotValidator {


    public static class ForDoctorOfficeForm implements ConstraintValidator<ValidOfficeTimeSlot, DoctorForm> {
        @Override
        public boolean isValid(DoctorForm doctorForm, ConstraintValidatorContext context) {
            List<DoctorOfficeForm> officeForms = doctorForm.getDoctorOfficeForm();
            if (officeForms == null || officeForms.isEmpty()) {
                return false;
            }
            for (DoctorOfficeForm form : officeForms) {
                if (form.getOfficeAvailabilitySlotForms() == null || form.getOfficeAvailabilitySlotForms().isEmpty()) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("{office.timeSlot.empty")
                            .addPropertyNode("officeAvailabilitySlotForms")
                            .addConstraintViolation();
                    return false;
                }
                for (DoctorOfficeAvailabilityForm slot : form.getOfficeAvailabilitySlotForms()) {
                    if (slot.getOfficeId() != null || slot.getId() != null || slot.getStartTime() == null || slot.getEndTime() == null || slot.getStartTime().isAfter(slot.getEndTime()) || slot.getStartTime().getHour() < 8 || slot.getEndTime().getHour() > 20) {
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate("{office.invalid.timeSlot}")
                                .addPropertyNode("officeAvailabilitySlotForms")
                                .addConstraintViolation();
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public static class ForDoctorOfficeAvailabilitySlotForm implements ConstraintValidator<ValidOfficeTimeSlot, List<DoctorOfficeAvailabilityForm>> {
        @Override
        public boolean isValid(List<DoctorOfficeAvailabilityForm> officeAvailabilitySlotForms, ConstraintValidatorContext context) {
            if (officeAvailabilitySlotForms == null || officeAvailabilitySlotForms.isEmpty()) {
                return true;
            }
            for (DoctorOfficeAvailabilityForm slot : officeAvailabilitySlotForms) {
                if (slot.getOfficeId() == null || slot.getStartTime() == null || slot.getEndTime() == null || slot.getStartTime().isAfter(slot.getEndTime()) || slot.getStartTime().getHour() < 8 || slot.getEndTime().getHour() > 20) {
                    return false;
                }
            }
            return true;
        }
    }
}
