package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.webapp.CustomMediaType;
import ar.edu.itba.paw.webapp.dto.SpecialtyDTO;
import ar.edu.itba.paw.webapp.utils.CacheUtils;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import ar.edu.itba.paw.webapp.utils.UriUtils;
import org.glassfish.jersey.server.Uri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Path(UriUtils.SPECIALTIES)
@Component
public class RestSpecialtyController {
    private final MessageSource messageSource;
    private final SpecialtyService specialtyService;

    @Autowired
    public RestSpecialtyController(SpecialtyService specialtyService, MessageSource messageSource) {
        this.specialtyService = specialtyService;
        this.messageSource = messageSource;
    }

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/{id:\\d+}")
    @Produces(CustomMediaType.APPLICATION_SPECIALTY)
    public Response getById(@PathParam("id") final long id, @Context final Request request,@Context HttpHeaders headers) {
        final Specialty specialty = specialtyService.getById(id).orElseThrow(NotFoundException::new);
        Locale locale = ResponseUtils.getLocaleFromHeaders(headers.getAcceptableLanguages());
        Response.ResponseBuilder responseBuilder = CacheUtils.conditionalCacheETag(
                Response.ok(new GenericEntity<>(SpecialtyDTO.fromSpecialty(specialty, uriInfo, messageSource, locale)) {}),
                request,
                Objects.hash(specialty.hashCode(), locale.toLanguageTag()));
        responseBuilder.header("Vary", "Accept-Language");
        return responseBuilder.build();
    }

    @GET
    @Produces(CustomMediaType.APPLICATION_SPECIALTY_LIST)
    public Response getAll(@Context HttpHeaders headers) {
        final List<Specialty> specialties = specialtyService.getAll();
        Response.ResponseBuilder response = Response.ok(new GenericEntity<>(SpecialtyDTO.fromSpecialty(specialties, uriInfo,messageSource, ResponseUtils.getLocaleFromHeaders(headers.getAcceptableLanguages()))) {});
        response.header("Vary", "Accept-Language");
        return response.build();
    }
}