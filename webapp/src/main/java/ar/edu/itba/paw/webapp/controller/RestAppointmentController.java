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



@Path("/appointments")
@Component
public class RestAppointmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestAppointmentController.class);

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
    @Path("/upcoming")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response listUpcoming(
            @QueryParam("doctorId") Long doctorId,
            @QueryParam("patientId") Long patientId,
            @QueryParam("filter") @DefaultValue("all") String filter,
            @QueryParam("page") @DefaultValue("1") @Min(1) int page,
            @QueryParam("pageSize") @DefaultValue("10") @Min(1) int pageSize
    ) {
        long userId = resolveUserId(doctorId, patientId);
        String sanitizedFilter = sanitizeFilter(filter, UPCOMING_FILTERS, "all");

        Page<Appointment> appointmentPage = appointmentService.getAppointments(userId, true, page, pageSize, sanitizedFilter);
        return Response.ok(new GenericEntity<>(AppointmentDTO.fromAppointment(appointmentPage.getContent(), uriInfo)) {}).build();
    }

    @GET
    @Path("/past")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response listPast(
            @QueryParam("doctorId") Long doctorId,
            @QueryParam("patientId") Long patientId,
            @QueryParam("filter") @DefaultValue("all") String filter,
            @QueryParam("page") @DefaultValue("1") @Min(1) int page,
            @QueryParam("pageSize") @DefaultValue("10") @Min(1) int pageSize
    ) {
        long userId = resolveUserId(doctorId, patientId);
        String sanitizedFilter = sanitizeFilter(filter, PAST_FILTERS, "all");

        Page<Appointment> appointmentPage = appointmentService.getAppointments(userId, false, page, pageSize, sanitizedFilter);
        return Response.ok(new GenericEntity<>(AppointmentDTO.fromAppointment(appointmentPage.getContent(), uriInfo)) {}).build();
    }

    @GET
    @Path("/{id:\\d+}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") final long id) {
        final Appointment appointment = this.appointmentService.getById(id).orElseThrow(AppointmentNotFoundException::new);
        return Response.ok(new GenericEntity<>(AppointmentDTO.fromAppointment(appointment, uriInfo)) {}).build();
    }

    private long resolveUserId(Long doctorId, Long patientId) {
        boolean doctorProvided = doctorId != null;
        boolean patientProvided = patientId != null;

        if (doctorProvided == patientProvided) {
            throw new BadRequestException("Must provide either doctorId or patientId");
        }
        return doctorProvided ? doctorId : patientId;
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
