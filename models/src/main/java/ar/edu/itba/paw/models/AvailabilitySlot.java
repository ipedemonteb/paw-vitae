package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "doctor_availability")
public class AvailabilitySlot {

    @EmbeddedId
    private AvailabilitySlotId id;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @MapsId("doctorId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;




    public AvailabilitySlot() {}

    public AvailabilitySlot(Doctor doctor, int dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.doctor = doctor;
        this.id = new AvailabilitySlotId(doctor.getId(), dayOfWeek, startTime);
        this.endTime = endTime;
    }

    public AvailabilitySlotId getId() {
        return id;
    }

    public int getDayOfWeek() {
        return id.getDayOfWeek();
    }

    public LocalTime getStartTime() {
        return id.getStartTime();
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AvailabilitySlot that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void setId(AvailabilitySlotId availabilitySlotId) {
        this.id = availabilitySlotId;
    }
}
