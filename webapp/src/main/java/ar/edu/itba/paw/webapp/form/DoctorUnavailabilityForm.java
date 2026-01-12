package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.UnavailabilitySlotForm;
import ar.edu.itba.paw.webapp.validation.UnavailabilityIntersection;
import ar.edu.itba.paw.webapp.validation.ValidUnavailability;

import java.util.List;

public class DoctorUnavailabilityForm {
    @ValidUnavailability(message = "unavailable.slots.invalid")
    @UnavailabilityIntersection(message = "unavailable.slots.overlap")
    private List<UnavailabilitySlotForm> unavailabilitySlots;

    public List<UnavailabilitySlotForm> getUnavailabilitySlots() {
        return unavailabilitySlots;
    }

    public void setUnavailabilitySlots(List<UnavailabilitySlotForm> unavailabilitySlots) {
        this.unavailabilitySlots = unavailabilitySlots;
    }
}
