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
            @QueryParam("userId") @NotNull Long userId,
            @QueryParam("collection") @DefaultValue("upcoming") String collection,
            @QueryParam("filter") @DefaultValue("all") String filter,
            @QueryParam("page") @DefaultValue("1") @Min(1) int page,
            @QueryParam("pageSize") @DefaultValue("10") @Min(1) int pageSize
    ) {
        Page<Appointment> appointmentPage = appointmentService.getAppointments(userId, "upcoming".equalsIgnoreCase(collection), page, pageSize, filter);
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
        return Response.created(location).build();
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
        if (!appointmentService.cancelAppointment(id, form.getUserId())) {
            throw new BadRequestException("Appointment can not be cancelled");
        }
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id:\\d+}/report")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateReport(@PathParam("id") final long id,
                                 @Valid @NotNull AppointmentReportForm form) {
        appointmentService.updateAppointmentReport(id, form.getReport());
        return Response.noContent().build();
    }

}
