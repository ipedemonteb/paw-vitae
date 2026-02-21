package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Specialty;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

public class SpecialtyRefDTO {

    private URI self;

    public static SpecialtyRefDTO fromSpecialty(Specialty specialty, UriInfo uriInfo) {
        SpecialtyRefDTO dto = new SpecialtyRefDTO();
        dto.self = uriInfo.getBaseUriBuilder()
                .path("api")
                .path("specialties")
                .path(String.valueOf(specialty.getId()))
                .build();
        return dto;
    }

    public static List<SpecialtyRefDTO> fromSpecialtyList(List<Specialty> specialties, UriInfo uriInfo) {
        return specialties.stream()
                .map(s -> fromSpecialty(s, uriInfo))
                .collect(Collectors.toList());
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}