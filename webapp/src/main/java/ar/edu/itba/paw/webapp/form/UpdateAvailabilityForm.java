package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.AvailabilitySlot;
import ar.edu.itba.paw.models.AvailabilitySlotForm;
import ar.edu.itba.paw.webapp.validation.TimeSlotIntersection;
import ar.edu.itba.paw.webapp.validation.ValidTimeSlot;
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
    public List<AvailabilitySlotForm> getAvailabilitySlots() {
        return availabilitySlots;
    }

    public void setAvailabilitySlots(List<AvailabilitySlotForm> availabilitySlots) {
        this.availabilitySlots = availabilitySlots;
    }
}