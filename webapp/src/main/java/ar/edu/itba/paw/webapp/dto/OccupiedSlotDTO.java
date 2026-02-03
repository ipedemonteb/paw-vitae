package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.OccupiedSlots;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

public class OccupiedSlotDTO {
    private URI doctor;
    private URI self;
    private long id;
    private String date;
    private String startTime;

    public static OccupiedSlotDTO fromSlot(OccupiedSlots slot, UriInfo uriInfo) {
        final OccupiedSlotDTO dto = new OccupiedSlotDTO();
        dto.id = slot.getId();
        dto.date = slot.getSlotDate().toString();
        dto.startTime = slot.getStartTime().toString();
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

    public static List<OccupiedSlotDTO> fromList(List<OccupiedSlots> slots, UriInfo uriInfo) {
        return slots.stream().map(s -> fromSlot(s, uriInfo)).collect(Collectors.toList());
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public URI getSelf() { return self; }
    public void setSelf(URI self) { this.self = self; }
    public URI getDoctor() { return doctor; }
    public void setDoctor(URI doctor) { this.doctor = doctor; }
}