package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.AvailabilitySlot;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class UpdateAvailabilityForm {

    @NotEmpty
    private List<AvailabilitySlot> availabilitySlots;

    public List<AvailabilitySlot> getAvailabilitySlots() {
        return availabilitySlots;
    }

    public void setAvailabilitySlots(List<AvailabilitySlot> availabilitySlots) {
        this.availabilitySlots = availabilitySlots;
    }
}