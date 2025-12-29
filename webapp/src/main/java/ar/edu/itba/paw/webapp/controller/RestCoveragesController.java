package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.exception.CoverageNotFoundException;
import ar.edu.itba.paw.webapp.dto.CoverageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.webapp.utils.ResponseUtils.buildResponse;
import static javax.ws.rs.core.Response.Status.OK;

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
    @Path("/{id:\\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") final long id) {
        final Coverage coverage = coverageService.findById(id).orElseThrow(CoverageNotFoundException::new);
        return buildResponse(coverage, uriInfo, CoverageDTO::fromCoverage, OK);
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        final List<Coverage> coverages = coverageService.getAll();
        return buildResponse(coverages, uriInfo, CoverageDTO::fromCoverage, OK);
    }
}