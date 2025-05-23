package ar.edu.itba.paw.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Embeddable
public class UnavailabilitySlotId implements Serializable {

    @Column(name = "doctor_id", nullable = false)
    private long doctorId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    public UnavailabilitySlotId() {
        // For Hibernate
    }

    public UnavailabilitySlotId(long doctorId, LocalDate startDate, LocalDate endDate) {
        this.doctorId = doctorId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getDoctorId() {
        return doctorId;
    }


    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnavailabilitySlotId)) return false;
        UnavailabilitySlotId that = (UnavailabilitySlotId) o;
        return doctorId == that.doctorId && startDate.equals(that.startDate) && endDate.equals(that.endDate);
    }
    @Override
    public int hashCode() {
        return Objects.hash(doctorId, startDate, endDate);
    }
}
