package ar.edu.itba.paw.models;

import java.time.LocalTime;

public class DoctorOfficeAvailabilityForm {
    private Long id;
    private Long officeId;
    private Integer dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public DoctorOfficeAvailabilityForm(Integer dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public DoctorOfficeAvailabilityForm(Long id, Long officeId, Integer dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.officeId = officeId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public DoctorOfficeAvailabilityForm() {
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Long officeId) {
        this.officeId = officeId;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public DoctorOfficeAvailability toEntity(DoctorOffice office) {
        return new DoctorOfficeAvailability(office, endTime, startTime, dayOfWeek);
    }
}
