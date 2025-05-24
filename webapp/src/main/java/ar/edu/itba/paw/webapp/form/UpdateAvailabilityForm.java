package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.AvailabilitySlot;
import ar.edu.itba.paw.models.AvailabilitySlotForm;
import ar.edu.itba.paw.models.UnavailabilitySlotForm;
import ar.edu.itba.paw.webapp.validation.TimeSlotIntersection;
import ar.edu.itba.paw.webapp.validation.UnavailabilityIntersection;
import ar.edu.itba.paw.webapp.validation.ValidTimeSlot;
import ar.edu.itba.paw.webapp.validation.ValidUnavailability;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;


public class UpdateAvailabilityForm {

    @NotEmpty
    @ValidTimeSlot(message = "{slots.invalid}")
    @TimeSlotIntersection(message = "{slots.overlap}")
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