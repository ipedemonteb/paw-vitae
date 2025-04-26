package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.AvailabilitySlot;
import ar.edu.itba.paw.webapp.validation.TimeSlotIntersection;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;


public class UpdateAvailabilityForm {

    @NotEmpty
    @TimeSlotIntersection(message = "{slots.overlap}")
    private List<AvailabilitySlot> availabilitySlots;
    public List<AvailabilitySlot> getAvailabilitySlots() {
        return availabilitySlots;
    }

    public void setAvailabilitySlots(List<AvailabilitySlot> availabilitySlots) {
        this.availabilitySlots = availabilitySlots;
    }
    public void setForm(List<AvailabilitySlot> availabilitySlots) {
        this.availabilitySlots = availabilitySlots.stream().filter(slot -> slot != null && slot.getStartTime() != null && slot.getEndTime() != null).collect(Collectors.toList());
    }
}