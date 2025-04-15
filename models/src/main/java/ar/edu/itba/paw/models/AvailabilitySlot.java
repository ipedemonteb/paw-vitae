package ar.edu.itba.paw.models;

import java.time.LocalTime;

public class AvailabilitySlot {

    private int dayOfWeek; // 0 = Monday, 6 = Sunday
    private LocalTime startTime;
    private LocalTime endTime;

    // Add a default constructor for Spring data binding
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
}