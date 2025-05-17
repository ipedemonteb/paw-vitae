package ar.edu.itba.paw.models;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

@Embeddable
public class AvailabilitySlotId implements Serializable {

    private long doctorId;
    private int dayOfWeek;
    private LocalTime startTime;

    public AvailabilitySlotId() {}

    public AvailabilitySlotId(long doctorId, int dayOfWeek, LocalTime startTime) {
        this.doctorId = doctorId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AvailabilitySlotId)) return false;
        AvailabilitySlotId that = (AvailabilitySlotId) o;
        return doctorId == that.doctorId &&
                dayOfWeek == that.dayOfWeek &&
                Objects.equals(startTime, that.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctorId, dayOfWeek, startTime);
    }
}
