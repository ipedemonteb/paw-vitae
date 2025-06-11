package ar.edu.itba.paw.models;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "doctor_office_availability_slots")
public class DoctorOfficeAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doctor_office_availability_slots_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "doctor_office_availability_slots_id_seq", name = "doctor_office_availability_slots_id_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "office_id", nullable = false)
    @JsonView(Views.Private.class)
    private DoctorOffice office;

    @Column(name = "end_time", nullable = false)
    @JsonSerialize(using = JsonUtils.LocalTimeHourSerializer.class)
    private LocalTime endTime;

    @Column(name = "start_time", nullable = false)
    @JsonSerialize(using = JsonUtils.LocalTimeHourSerializer.class)
    private LocalTime startTime;

    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    public DoctorOfficeAvailability() {}

    public DoctorOfficeAvailability(DoctorOffice office, LocalTime endTime, LocalTime startTime, Integer dayOfWeek) {
        this.office = office;
        this.endTime = endTime;
        this.startTime = startTime;
        this.dayOfWeek = dayOfWeek;
    }

    // Views definition
    public static class Views {
        public static class Public {}
        public static class Private extends Doctor.Views.Public {}
    }

    public DoctorOffice getOffice() {
        return office;
    }

    public void setOffice(DoctorOffice office) {
        this.office = office;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public Long getId() {
        return id;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
