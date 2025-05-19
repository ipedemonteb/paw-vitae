package ar.edu.itba.paw.models;

import java.time.LocalTime;

public class AvailabilitySlotForm {
    private int dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    public AvailabilitySlotForm(int dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public AvailabilitySlotForm() {

    }
    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public static AvailabilitySlotForm fromEntity(AvailabilitySlot slot) {
        return new AvailabilitySlotForm(
                slot.getId().getDayOfWeek(),
                slot.getId().getStartTime(),
                slot.getEndTime()
        );
    }

    public AvailabilitySlot toEntity(Doctor doctor) {
        AvailabilitySlot slot = new AvailabilitySlot();
        slot.setId(new AvailabilitySlotId(doctor.getId(), dayOfWeek, startTime));
        slot.setDoctor(doctor);
        slot.setEndTime(endTime);
        return slot;
    }
}
