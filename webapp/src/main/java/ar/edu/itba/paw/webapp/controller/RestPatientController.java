package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.PatientService;
import ar.edu.itba.paw.interfaceServices.UserService;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.webapp.dto.CreatePatientDto;
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

    private final UserService userService;
    @Context
    private UriInfo uriInfo;
    @Autowired
    public RestPatientController(PatientService patientService,UserService userService) {
        this.patientService = patientService;
        this.userService = userService;
    }


    @GET
    @Path("/{id}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") final long id) {
        final Patient patient = this.patientService.getById(id).orElseThrow(NotFoundException::new);
        return Response.ok(new GenericEntity<>(PatientDTO.fromPatient(patient, uriInfo)) {}).build();
    }

//    @GET
//    @Path("/{id}/coverages")
//    @Produces(value = MediaType.APPLICATION_JSON)
//    public Response getCoveragesByPatientId(@PathParam("id") final long id) {
//        final Patient patient = this.patientService.getById(id).orElseThrow(NotFoundException::new);
//        if (patient.getCoverage() == null) {
//            return Response.noContent().build();
//        }
//        return Response.ok(new GenericEntity<>(CoverageDTO.fromCoverage(patient.getCoverage(), uriInfo)) {}).build();
//
//    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPatient(final CreatePatientDto createPatientDto){
        final  Patient patient=patientService.create(createPatientDto.getName(),createPatientDto.getLastName(),createPatientDto.getEmail(),createPatientDto.getPassword(),createPatientDto.getPhone(),createPatientDto.getLanguage(),createPatientDto.getCoverage(),createPatientDto.getNeighborhood());
        userService.setVerificationToken(createPatientDto.getEmail());
        return  Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(patient.getId())).build()).build();
    }
    @Path("/{id}")
    @PATCH
    @Consumes({MediaType.APPLICATION_JSON})
    public Response modifyPatient(@PathParam("id") final long id,final CreatePatientDto createPatientDto){
        final Patient patient = this.patientService.getById(id).orElseThrow(NotFoundException::new);
        patientService.updatePatient(patient,createPatientDto.getName(),createPatientDto.getLastName(),createPatientDto.getPhone(),createPatientDto.getCoverage());
        return Response.ok(new GenericEntity<>(PatientDTO.fromPatient(patient, uriInfo)){}).build();
    }

}
