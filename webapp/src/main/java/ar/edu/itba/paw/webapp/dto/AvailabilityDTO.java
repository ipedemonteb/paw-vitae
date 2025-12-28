package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.DoctorOfficeAvailability;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalTime;

public class AvailabilityDTO {
    private int dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    private URI office;

    public static AvailabilityDTO fromDoctorOfficeAvailability(DoctorOfficeAvailability availability, UriInfo uriInfo) {
        AvailabilityDTO dto = new AvailabilityDTO();

        dto.dayOfWeek = availability.getDayOfWeek();
        dto.startTime = availability.getStartTime();
        dto.endTime = availability.getEndTime();

        dto.office = uriInfo.getBaseUriBuilder().path("doctors").path(String.valueOf(availability.getOffice().getDoctor().getId())).path("offices").path(String.valueOf(availability.getOffice().getId())).build();

        return dto;
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

    public URI getOffice() {
        return office;
    }

    public void setOffice(URI office) {
        this.office = office;
    }
}
