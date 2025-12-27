package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.webapp.dto.CoverageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@Path("/coverages")
@Component
public class RestCoveragesController {

    CoverageService coverageService;

    @Autowired
    public RestCoveragesController(CoverageService coverageService) {
        this.coverageService = coverageService;
    }

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") final long id) {
        final Coverage coverage = coverageService.findById(id).orElseThrow(NotFoundException::new);
        return Response.ok(CoverageDTO.fromCoverage(coverage, uriInfo)).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        final List<Coverage> coverages = coverageService.getAll();

        final List<CoverageDTO> dtos = coverages.stream()
                .map(c -> CoverageDTO.fromCoverage(c, uriInfo))
                .collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<CoverageDTO>>(dtos) {}).build();
    }
}