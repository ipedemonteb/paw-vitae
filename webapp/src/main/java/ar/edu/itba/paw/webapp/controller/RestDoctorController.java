package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.exception.DoctorOfficeNotFoundException;
import ar.edu.itba.paw.webapp.dto.DoctorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.util.stream.Collectors;

@Path("/doctors")
@Component
public class RestDoctorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestDoctorController.class);

    private final DoctorService doctorService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public RestDoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GET
    @Path("/{id}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") final long id) {
        final Doctor doctor = this.doctorService.getById(id).orElseThrow(DoctorOfficeNotFoundException::new);
        return Response.ok(new GenericEntity<>(DoctorDTO.fromDoctor(doctor)) {}).build();
    }
}
