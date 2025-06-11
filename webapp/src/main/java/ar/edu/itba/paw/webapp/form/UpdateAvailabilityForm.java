package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.DoctorOfficeAvailabilityForm;
import ar.edu.itba.paw.models.UnavailabilitySlotForm;
import ar.edu.itba.paw.webapp.validation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@OfficeOwnedByDoctor(message = "{office.invalid}")
public class UpdateAvailabilityForm {

    @NotEmpty
    @NotNull
    @ValidOfficeTimeSlot(message = "{office.invalid.timeSlot}")
    @OfficeAvailabilitySlotIntersection(message = "{office.availabilitySlot.intersection}")
    private List<DoctorOfficeAvailabilityForm> doctorOfficeAvailabilities;

    @NotNull
    private Long doctorId;

    @ValidUnavailability(message = "{unavailable.slots.invalid}")
    @UnavailabilityIntersection(message = "{unavailable.slots.overlap}")
    private List<UnavailabilitySlotForm> unavailabilitySlots;

    public List<UnavailabilitySlotForm> getUnavailabilitySlots() {
        return unavailabilitySlots;
    }

    public void setUnavailabilitySlots(List<UnavailabilitySlotForm> unavailabilitySlots) {
        this.unavailabilitySlots = unavailabilitySlots;
    }

    public List<DoctorOfficeAvailabilityForm> getDoctorOfficeAvailabilities() {
        return doctorOfficeAvailabilities;
    }

    public void setDoctorOfficeAvailabilities(List<DoctorOfficeAvailabilityForm> doctorOfficeAvailabilities) {
        this.doctorOfficeAvailabilities = doctorOfficeAvailabilities;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }
}