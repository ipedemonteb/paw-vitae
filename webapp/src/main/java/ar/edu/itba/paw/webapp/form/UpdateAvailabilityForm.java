package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.AvailabilitySlotForm;
import ar.edu.itba.paw.models.UnavailabilitySlotForm;
import ar.edu.itba.paw.webapp.validation.UnavailabilityIntersection;
import ar.edu.itba.paw.webapp.validation.ValidUnavailability;

import javax.validation.constraints.NotEmpty;
import java.util.List;


public class UpdateAvailabilityForm {

    @NotEmpty
    private List<AvailabilitySlotForm> availabilitySlots;

    @ValidUnavailability(message = "{unavailable.slots.invalid}")
    @UnavailabilityIntersection(message = "{unavailable.slots.overlap}")
    private List<UnavailabilitySlotForm> unavailabilitySlots;

    public List<UnavailabilitySlotForm> getUnavailabilitySlots() {
        return unavailabilitySlots;
    }

    public void setUnavailabilitySlots(List<UnavailabilitySlotForm> unavailabilitySlots) {
        this.unavailabilitySlots = unavailabilitySlots;
    }

    public List<AvailabilitySlotForm> getAvailabilitySlots() {
        return availabilitySlots;
    }

    public void setAvailabilitySlots(List<AvailabilitySlotForm> availabilitySlots) {
        this.availabilitySlots = availabilitySlots;
    }
}