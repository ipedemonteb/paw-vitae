package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaceServices.NeighborhoodService;
import ar.edu.itba.paw.models.Neighborhood;
import ar.edu.itba.paw.webapp.dto.CoverageDTO;
import ar.edu.itba.paw.webapp.dto.NeighborhoodDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@Path("/neighborhoods")
@Component
public class RestNeighborhoodController {

    private final NeighborhoodService neighborhoodService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public RestNeighborhoodController(NeighborhoodService neighborhoodService){
        this.neighborhoodService = neighborhoodService;
    }

    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getAll() {
        final List<Neighborhood> neighborhoodList = neighborhoodService.getAll();
        final List<NeighborhoodDTO> dtos = neighborhoodList.stream()
                .map(n -> NeighborhoodDTO.fromNeighborhood(n, uriInfo))
                .collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<NeighborhoodDTO>>(dtos) {}).build();
    }


    @GET
    @Path("/{id:\\d+}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") final long id) {
        final Neighborhood neighborhood = neighborhoodService.getById(id)
                .orElseThrow(NotFoundException::new);

        return Response.ok(new GenericEntity<>(NeighborhoodDTO.fromNeighborhood(neighborhood, uriInfo)) {}).build();
    }

}
