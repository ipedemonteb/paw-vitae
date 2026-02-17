package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.DoctorOfficeAvailabilityForm;
import ar.edu.itba.paw.webapp.validation.ActiveOfficeSlots;
import ar.edu.itba.paw.webapp.validation.OfficeAvailabilitySlotIntersection;
import ar.edu.itba.paw.webapp.validation.OfficeOwnedByDoctor;
import ar.edu.itba.paw.webapp.validation.ValidOfficeTimeSlot;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


public class DoctorAvailabilityForm {
    @NotNull
    @ValidOfficeTimeSlot(message = "office.invalid.timeSlot")
    @OfficeAvailabilitySlotIntersection(message = "office.availabilitySlot.intersection")
    private List<DoctorOfficeAvailabilityForm> doctorOfficeAvailabilities;

    public List<DoctorOfficeAvailabilityForm> getDoctorOfficeAvailabilities() {
        return doctorOfficeAvailabilities;
    }

    public void setDoctorOfficeAvailabilities(List<DoctorOfficeAvailabilityForm> doctorOfficeAvailabilities) {
        this.doctorOfficeAvailabilities = doctorOfficeAvailabilities;
    }


}
