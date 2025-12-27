package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Specialty;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class SpecialtyDTO {
    private String name;
    private URI self;


    public static SpecialtyDTO fromSpecialty(Specialty specialty, UriInfo uriInfo) {
        SpecialtyDTO res = new SpecialtyDTO();
        res.name = specialty.getKey();
        res.self = uriInfo.getBaseUriBuilder().path("specialties").path(String.valueOf(specialty.getId())).build();
        return res;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}