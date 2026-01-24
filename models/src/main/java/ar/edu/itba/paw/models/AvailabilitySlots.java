package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "Doctor_Availability_Slots")
public class AvailabilitySlots {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doctor_availability_slots_id_seq")
    @SequenceGenerator(sequenceName = "doctor_availability_slots_id_seq", name = "doctor_availability_slots_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "slot_date", nullable = false)
    private LocalDate slotDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SlotStatus status;

    public AvailabilitySlots() {
    }

    public AvailabilitySlots(Doctor doctor, LocalDate slotDate, LocalTime startTime, SlotStatus status) {
        this.doctor = doctor;
        this.slotDate = slotDate;
        this.startTime = startTime;
        this.status = status;
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

    public SlotStatus getStatus() {
        return status;
    }

    public void setStatus(SlotStatus status) {
        this.status = status;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailabilitySlots that = (AvailabilitySlots) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(doctor, that.doctor) &&
                Objects.equals(slotDate, that.slotDate) &&
                Objects.equals(startTime, that.startTime) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, doctor, slotDate, startTime, status);
    }
}