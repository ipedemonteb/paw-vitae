package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "Occupied_slots")
public class OccupiedSlots {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "occupied_slots_id_seq")
    @SequenceGenerator(sequenceName = "occupied_slots_id_seq", name = "occupied_slots_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "slot_date", nullable = false)
    private LocalDate slotDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    public OccupiedSlots() {
    }

    public OccupiedSlots(Doctor doctor, LocalDate slotDate, LocalTime startTime) {
        this.doctor = doctor;
        this.slotDate = slotDate;
        this.startTime = startTime;
    }

    public enum SlotStatus {
        AVAILABLE,
        UNAVAILABLE
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDate getSlotDate() {
        return slotDate;
    }

    public void setSlotDate(LocalDate slotDate) {
        this.slotDate = slotDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OccupiedSlots that = (OccupiedSlots) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(doctor, that.doctor) &&
                Objects.equals(slotDate, that.slotDate) &&
                Objects.equals(startTime, that.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, doctor, slotDate, startTime);
    }
}