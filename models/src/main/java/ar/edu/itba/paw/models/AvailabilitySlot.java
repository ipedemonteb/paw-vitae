package ar.edu.itba.paw.models;

import java.time.LocalTime;
import java.util.Objects;

public class AvailabilitySlot {

    private int dayOfWeek; // 0 = Monday, 6 = Sunday

    private LocalTime startTime;

    private LocalTime endTime;

    public AvailabilitySlot() {
        // Default constructor required for Spring data binding
    }

    public AvailabilitySlot(int dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AvailabilitySlot that = (AvailabilitySlot) o;
        return dayOfWeek == that.dayOfWeek && Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayOfWeek, startTime, endTime);
    }
}