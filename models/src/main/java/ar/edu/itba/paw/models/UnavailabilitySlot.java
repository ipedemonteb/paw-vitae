package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "Doctor_Unavailability")
public class UnavailabilitySlot {

    @EmbeddedId
    private UnavailabilitySlotId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("doctorId")
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    public UnavailabilitySlot() {}

    public UnavailabilitySlot(Doctor doctor, LocalDate start_date, LocalDate end_date) {
        this.doctor = doctor;
        this.id = new UnavailabilitySlotId(doctor.getId(), start_date, end_date);

    }

    // Getters y Setters
    public UnavailabilitySlotId getId() {
        return id;
    }
    public void setId(UnavailabilitySlotId id) {
        this.id = id;
    }
    public Doctor getDoctor() {
        return doctor;
    }
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDate getStartDate() {
        return id.getStartDate();
    }
    public LocalDate getEndDate() {
        return id.getEndDate();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UnavailabilitySlot that = (UnavailabilitySlot) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

