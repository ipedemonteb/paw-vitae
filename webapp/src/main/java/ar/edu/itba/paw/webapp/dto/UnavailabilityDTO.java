package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.UnavailabilitySlot;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class UnavailabilityDTO {
    private long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private URI doctor;
    private URI self;

    public static UnavailabilityDTO fromUnavailabilitySlot(UnavailabilitySlot slot, UriInfo uriInfo) {
        UnavailabilityDTO dto = new UnavailabilityDTO();
        dto.startDate = slot.getStartDate();
        dto.endDate = slot.getEndDate();
        dto.id = slot.getId();
        dto.doctor = uriInfo.getBaseUriBuilder().path("api")
                .path("doctors")
                .path(String.valueOf(slot.getDoctor().getId()))
                .build();

        String doctorId = String.valueOf(slot.getDoctor().getId());
        String slotId = String.valueOf(slot.getId());

        dto.self = uriInfo.getBaseUriBuilder().path("api")
                .path("doctors")
                .path(doctorId)
                .path("unavailability")
                .path(slotId)
                .build();

        return dto;
    }

    public static List<UnavailabilityDTO> fromUnavailabilitySlot(List<UnavailabilitySlot> slots, UriInfo uriInfo) {
        return slots.stream()
                .map(s -> fromUnavailabilitySlot(s, uriInfo))
                .collect(Collectors.toList());
    }
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public URI getDoctor() { return doctor; }
    public void setDoctor(URI doctor) { this.doctor = doctor; }

    public URI getSelf() { return self; }
    public void setSelf(URI self) { this.self = self; }

}