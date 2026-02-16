package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.exception.CoverageNotFoundException;
import ar.edu.itba.paw.webapp.CustomMediaType;
import ar.edu.itba.paw.webapp.dto.CoverageDTO;
import ar.edu.itba.paw.webapp.utils.CacheUtils;
import ar.edu.itba.paw.webapp.utils.UriUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Cache;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;


@Path(UriUtils.COVERAGES)
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
    @Produces(CustomMediaType.APPLICATION_COVERAGE)
    public Response getById(@PathParam("id") final long id, @Context final Request request) {
        final Coverage coverage = coverageService.findById(id).orElseThrow(CoverageNotFoundException::new);
        return CacheUtils.conditionalCacheETag(Response.ok(new GenericEntity<>(CoverageDTO.fromCoverage(coverage, uriInfo)) {}), request, coverage.hashCode()).build();
    }


    @GET
    @Produces(CustomMediaType.APPLICATION_COVERAGE_LIST)
    public Response getAll() {
        final List<Coverage> coverages = coverageService.getAll();
        return Response.ok(new GenericEntity<>(CoverageDTO.fromCoverage(coverages, uriInfo)) {}).build();
    }
}