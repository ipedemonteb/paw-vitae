package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.exception.AppointmentNotFoundException;
import ar.edu.itba.paw.webapp.dto.AppointmentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

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
    @Path("/{id:\\d+}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") final long id) {
        final Appointment appointment = this.appointmentService.getById(id).orElseThrow(AppointmentNotFoundException::new);
        return Response.ok(new GenericEntity<>(AppointmentDTO.fromAppointment(appointment, uriInfo)) {}).build();
    }
}
