package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.AvailabilitySlots;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

public class AvailabilitySlotDTO {
    private URI doctor;
    private URI self;
    private long id;
    private String date;
    private String startTime;
    private String status;

    public static AvailabilitySlotDTO fromSlot(AvailabilitySlots slot, UriInfo uriInfo) {
        final AvailabilitySlotDTO dto = new AvailabilitySlotDTO();
        dto.id = slot.getId();
        dto.date = slot.getSlotDate().toString();
        dto.startTime = slot.getStartTime().toString();
        dto.status = slot.getStatus().name();
        if (uriInfo != null) {
            dto.self = uriInfo.getBaseUriBuilder().path("api")
                    .path("doctors")
                    .path(String.valueOf(slot.getDoctor().getId()))
                    .path("slots")
                    .path(String.valueOf(slot.getId()))
                    .build();
            dto.doctor = uriInfo.getBaseUriBuilder().path("api").path("doctors").path(String.valueOf(slot.getDoctor().getId())).build();
        }
        return dto;
    }

    public static List<AvailabilitySlotDTO> fromList(List<AvailabilitySlots> slots, UriInfo uriInfo) {
        return slots.stream().map(s -> fromSlot(s, uriInfo)).collect(Collectors.toList());
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public URI getSelf() { return self; }
    public void setSelf(URI self) { this.self = self; }
    public URI getDoctor() { return doctor; }
    public void setDoctor(URI doctor) { this.doctor = doctor; }
}