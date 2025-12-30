package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.exception.AppointmentNotFoundException;
import ar.edu.itba.paw.webapp.dto.AppointmentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.*;
import java.util.Locale;
import java.util.Set;

import static ar.edu.itba.paw.webapp.utils.ResponseUtils.buildPaginationHeaders;
import static javax.ws.rs.core.Response.Status.OK;


@Path("/appointments")
@Component
public class RestAppointmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestAppointmentController.class);

    private static final Set<String> COLLECTIONS = Set.of("upcoming", "past");
    private static final Set<String> UPCOMING_FILTERS = Set.of("all", "today", "week", "month");
    private static final Set<String> PAST_FILTERS = Set.of("all", "completed", "cancelled");

    private final AppointmentService appointmentService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public RestAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response list(
            @QueryParam("userId") Long userId,
            @QueryParam("collection") @DefaultValue("upcoming") String collection,
            @QueryParam("filter") @DefaultValue("all") String filter,
            @QueryParam("page") @DefaultValue("1") @Min(1) int page,
            @QueryParam("pageSize") @DefaultValue("10") @Min(1) int pageSize
    ) {
        if (userId == null) {
            throw new BadRequestException("Missing userId");
        }

        String sanitizedCollection = sanitizeCollection(collection);
        boolean isUpcoming = sanitizedCollection.equals("upcoming");
        Set<String> allowedFilters = isUpcoming ? UPCOMING_FILTERS : PAST_FILTERS;
        String sanitizedFilter = sanitizeFilter(filter, allowedFilters, "all");

        Page<Appointment> appointmentPage = appointmentService.getAppointments(userId, isUpcoming, page, pageSize, sanitizedFilter);
        Response.ResponseBuilder rb = Response.ok(new GenericEntity<>(AppointmentDTO.fromAppointment(appointmentPage.getContent(), uriInfo)) {});
        return buildPaginationHeaders(rb, appointmentPage, uriInfo);
    }

    @GET
    @Path("/{id:\\d+}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") final long id) {
        final Appointment appointment = this.appointmentService.getById(id).orElseThrow(AppointmentNotFoundException::new);
        return Response.ok(new GenericEntity<>(AppointmentDTO.fromAppointment(appointment, uriInfo)) {}).build();
    }

    private String sanitizeCollection(String collection) {
        if (collection == null || collection.isBlank()) {
            return "upcoming";
        }
        String normalized = collection.trim().toLowerCase(Locale.ROOT);
        if (!COLLECTIONS.contains(normalized)) {
            throw new BadRequestException("Invalid collection value");
        }
        return normalized;
    }

    private String sanitizeFilter(String filter, Set<String> allowed, String defaultValue) {
        if (filter == null || filter.isBlank()) {
            return defaultValue;
        }
        String normalized = filter.trim().toLowerCase(Locale.ROOT);
        if (!allowed.contains(normalized)) {
            throw new BadRequestException("Invalid filter value");
        }
        return normalized;
    }
}
