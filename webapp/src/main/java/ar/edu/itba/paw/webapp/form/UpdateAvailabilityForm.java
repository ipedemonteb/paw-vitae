package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.AvailabilitySlotForm;
import ar.edu.itba.paw.models.DoctorOfficeAvailabilitySlotForm;
import ar.edu.itba.paw.models.DoctorOfficeForm;
import ar.edu.itba.paw.models.UnavailabilitySlotForm;
import ar.edu.itba.paw.webapp.validation.UnavailabilityIntersection;
import ar.edu.itba.paw.webapp.validation.ValidUnavailability;

import javax.validation.constraints.NotEmpty;
import java.util.List;


public class UpdateAvailabilityForm {

    @NotEmpty
    private List<DoctorOfficeAvailabilitySlotForm> doctorOfficeAvailabilitySlots;

    @ValidUnavailability(message = "{unavailable.slots.invalid}")
    @UnavailabilityIntersection(message = "{unavailable.slots.overlap}")
    private List<UnavailabilitySlotForm> unavailabilitySlots;

    public List<UnavailabilitySlotForm> getUnavailabilitySlots() {
        return unavailabilitySlots;
    }

    public void setUnavailabilitySlots(List<UnavailabilitySlotForm> unavailabilitySlots) {
        this.unavailabilitySlots = unavailabilitySlots;
    }

    public List<DoctorOfficeAvailabilitySlotForm> getDoctorOfficeAvailabilitySlots() {
        return doctorOfficeAvailabilitySlots;
    }

    public void setDoctorOfficeAvailabilitySlots(List<DoctorOfficeAvailabilitySlotForm> doctorOfficeAvailabilitySlots) {
        this.doctorOfficeAvailabilitySlots = doctorOfficeAvailabilitySlots;
    }
}