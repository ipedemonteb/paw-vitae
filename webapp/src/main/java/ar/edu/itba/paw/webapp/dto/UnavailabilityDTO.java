package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.UnavailabilitySlot;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class UnavailabilityDTO {

    private LocalDate startDate;
    private LocalDate endDate;
    private URI doctor;
    public static UnavailabilityDTO fromUnavailabilitySlot(UnavailabilitySlot slot, UriInfo uriInfo) {
        UnavailabilityDTO dto = new UnavailabilityDTO();
        dto.startDate = slot.getStartDate();
        dto.endDate = slot.getEndDate();

        dto.doctor = uriInfo.getBaseUriBuilder()
                .path("doctors")
                .path(String.valueOf(slot.getDoctor().getId()))
                .build();

        return dto;
    }

    public static List<UnavailabilityDTO> fromUnavailabilitySlot(List<UnavailabilitySlot> slots, UriInfo uriInfo) {
        return slots.stream()
                .map(s -> fromUnavailabilitySlot(s, uriInfo))
                .collect(Collectors.toList());
    }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public URI getDoctor() { return doctor; }
    public void setDoctor(URI doctor) { this.doctor = doctor; }

}