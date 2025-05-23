package ar.edu.itba.paw.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class UnavailabilitySlotForm {
    private LocalDate startDate;
    private LocalDate endDate;

    public UnavailabilitySlotForm() {}

    public UnavailabilitySlotForm(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public void  setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public static UnavailabilitySlotForm fromEntity(UnavailabilitySlot slot) {
        return new UnavailabilitySlotForm(
                slot.getStartDate(),
                slot.getEndDate()
        );
    }
    public UnavailabilitySlot toEntity(Doctor doctor) {
        UnavailabilitySlot slot = new UnavailabilitySlot();
        slot.setId(new UnavailabilitySlotId(doctor.getId(), startDate, endDate));
        slot.setDoctor(doctor);
        return slot;
    }

}
