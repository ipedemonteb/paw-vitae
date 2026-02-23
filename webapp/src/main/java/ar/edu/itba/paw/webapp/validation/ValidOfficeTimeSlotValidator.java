package ar.edu.itba.paw.webapp.validation;
import ar.edu.itba.paw.models.DoctorOfficeAvailabilityForm;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ValidOfficeTimeSlotValidator {

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
