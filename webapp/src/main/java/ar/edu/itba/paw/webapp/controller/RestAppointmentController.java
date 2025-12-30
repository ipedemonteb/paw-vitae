package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.exception.AppointmentNotFoundException;
import ar.edu.itba.paw.webapp.dto.AppointmentDTO;
import ar.edu.itba.paw.webapp.form.AppointmentForm;
import ar.edu.itba.paw.webapp.form.CancelAppointmentForm;
import ar.edu.itba.paw.webapp.form.AppointmentReportForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.*;
import java.net.URI;
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Valid @NotNull AppointmentForm form) {
        Appointment appointment = appointmentService.create(
                form.getPatientId(),
                form.getDoctorId(),
                form.getAppointmentDate(),
                form.getAppointmentHour(),
                form.getReason(),
                form.getSpecialtyId(),
                form.getOfficeId(),
                form.isAllowFullHistory()
        );

        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(appointment.getId())).build();
        GenericEntity<AppointmentDTO> entity = new GenericEntity<>(AppointmentDTO.fromAppointment(appointment, uriInfo)) {};
        return Response.created(location).entity(entity).build();
    }

    @GET
    @Path("/{id:\\d+}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") final long id) {
        final Appointment appointment = this.appointmentService.getById(id).orElseThrow(AppointmentNotFoundException::new);
        return Response.ok(new GenericEntity<>(AppointmentDTO.fromAppointment(appointment, uriInfo)) {}).build();
    }

    @PATCH
    @Path("/{id:\\d+}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response cancel(@PathParam("id") final long id, @Valid @NotNull CancelAppointmentForm form) {
        Boolean cancelled = appointmentService.cancelAppointment(id, form.getUserId());
        if (!Boolean.TRUE.equals(cancelled)) {
            LOGGER.warn("Failed to cancel appointment {} by user {}", id, form.getUserId());
            return Response.status(Response.Status.CONFLICT).build();
        }
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id:\\d+}/report")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateReport(@PathParam("id") final long id,
                                 @Valid @NotNull AppointmentReportForm form) {
        appointmentService.getById(id).orElseThrow(AppointmentNotFoundException::new);
        appointmentService.updateAppointmentReport(id, form.getReport());
        return Response.noContent().build();
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
