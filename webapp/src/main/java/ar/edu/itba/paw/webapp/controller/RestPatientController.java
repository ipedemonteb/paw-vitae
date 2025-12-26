package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.PatientService;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.webapp.dto.PatientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/patients")
@Component
public class RestPatientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestPatientController.class);

    private final PatientService patientService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public RestPatientController(PatientService patientService){
        this.patientService = patientService;
    }

    @GET
    @Path("/{id}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") final long id) {
        final Patient patient = this.patientService.getById(id).orElseThrow(NotFoundException::new);
        return Response.ok(new GenericEntity<>(PatientDTO.fromPatient(patient, uriInfo)) {}).build();
    }




}
