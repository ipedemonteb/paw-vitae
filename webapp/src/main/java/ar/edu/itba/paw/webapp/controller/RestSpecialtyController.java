package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.webapp.CustomMediaType;
import ar.edu.itba.paw.webapp.dto.SpecialtyDTO;
import ar.edu.itba.paw.webapp.utils.CacheUtils;
import ar.edu.itba.paw.webapp.utils.UriUtils;
import org.glassfish.jersey.server.Uri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path(UriUtils.SPECIALTIES)
@Component
public class RestSpecialtyController {

    SpecialtyService specialtyService;

    @Autowired
    public RestSpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/{id:\\d+}")
    @Produces(CustomMediaType.APPLICATION_SPECIALTY)
    public Response getById(@PathParam("id") final long id, @Context final Request request) {
        final Specialty specialty = specialtyService.getById(id).orElseThrow(NotFoundException::new);
        return CacheUtils.conditionalCacheETag(Response.ok(new GenericEntity<>(SpecialtyDTO.fromSpecialty(specialty, uriInfo)) {}), request, specialty.hashCode()).build();
      //  return Response.ok(new GenericEntity<>(SpecialtyDTO.fromSpecialty(specialty, uriInfo)) {}).build();
    }

    @GET
    @Produces(CustomMediaType.APPLICATION_SPECIALTY_LIST)
    public Response getAll() {
        final List<Specialty> specialties = specialtyService.getAll();
        return Response.ok(new GenericEntity<>(SpecialtyDTO.fromSpecialty(specialties, uriInfo)) {}).build();
    }
}