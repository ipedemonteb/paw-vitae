package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.exception.AppointmentNotFoundException;
import ar.edu.itba.paw.webapp.dto.AppointmentDTO;
import ar.edu.itba.paw.webapp.form.AppointmentForm;
import ar.edu.itba.paw.webapp.form.AppointmentReportForm;
import ar.edu.itba.paw.webapp.CustomMediaType;
import ar.edu.itba.paw.webapp.form.AppointmentSearchForm;
import ar.edu.itba.paw.webapp.utils.CacheUtils;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import ar.edu.itba.paw.webapp.utils.UriUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static ar.edu.itba.paw.webapp.utils.ResponseUtils.buildPaginationHeaders;


@Path(UriUtils.APPOINTMENTS)
@Component
public class RestAppointmentController {

    private final AppointmentService appointmentService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public RestAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GET
    @Produces(value = CustomMediaType.APPLICATION_APPOINTMENT_LIST)
    public Response list(@BeanParam @Valid AppointmentSearchForm form) {

        Page<Appointment> appointmentPage = appointmentService.getAppointments(
                form.getUserId(),
                "upcoming".equalsIgnoreCase(form.getCollection()),
                form.getPage(),
                form.getPageSize(),
                form.getFilter(),
                form.getSort()
        );
        Response.ResponseBuilder rb = Response.ok(new GenericEntity<>(AppointmentDTO.fromAppointment(appointmentPage.getContent(), uriInfo)) {});
        return buildPaginationHeaders(rb, appointmentPage, uriInfo);
    }

    @POST
    @Consumes(CustomMediaType.APPLICATION_APPOINTMENT)
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
    @Produces(value = CustomMediaType.APPLICATION_APPOINTMENT)
    public Response getById(@PathParam("id") final long id, @Context final Request request) {
        final Appointment appointment = this.appointmentService.getById(id).orElseThrow(AppointmentNotFoundException::new);
        return CacheUtils.conditionalCacheLastModified(Response.ok(new GenericEntity<>(AppointmentDTO.fromAppointment(appointment, uriInfo)) {}), request, appointment.getLastModified()).build();
    }

    @PATCH
    @Path("/{id:\\d+}")
    @Consumes(CustomMediaType.APPLICATION_APPOINTMENT_CANCEL)
    public Response cancel(@PathParam("id") final long id,
                           @NotNull @QueryParam("userId") final Long userId) {
        appointmentService.cancelAppointment(id, userId);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id:\\d+}/report")
    @Consumes(CustomMediaType.APPLICATION_APPOINTMENT_REPORT)
    public Response updateReport(@PathParam("id") final long id,
                                 @Valid @NotNull AppointmentReportForm form) {
        appointmentService.updateAppointmentReport(id, form.getReport());
        return Response.noContent().build();
    }

}
