package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Neighborhood;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

public class NeighborhoodDTO {
    private String name;
    private URI self;

    public static NeighborhoodDTO fromNeighborhood(Neighborhood neighborhood, UriInfo uriInfo) {
        NeighborhoodDTO res = new NeighborhoodDTO();

        res.name = neighborhood.getName();
        res.self = uriInfo.getBaseUriBuilder().path("api").path("neighborhoods").path(String.valueOf(neighborhood.getId())).build();

        return res;
    }

    public static List<NeighborhoodDTO> fromNeighborhood(List<Neighborhood> neighborhoods, UriInfo uriInfo) {
        return neighborhoods.stream().map(n -> fromNeighborhood(n, uriInfo)).toList();
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
