package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.PatientService;
import ar.edu.itba.paw.interfaceServices.UserService;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.CustomMediaType;
import ar.edu.itba.paw.webapp.dto.PatientDTO;
import ar.edu.itba.paw.webapp.form.ChangePasswordForm;
import ar.edu.itba.paw.webapp.form.PatientForm;
import ar.edu.itba.paw.webapp.form.RecoverPasswordForm;
import ar.edu.itba.paw.webapp.form.UpdatePatientForm;
import ar.edu.itba.paw.webapp.utils.UriUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path(UriUtils.PATIENTS)
@Component
public class RestPatientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestPatientController.class);
    private final PatientService patientService;

    @Context
    private UriInfo uriInfo;
    @Autowired
    public RestPatientController(PatientService patientService) {
        this.patientService = patientService;
    }


    @GET
    @Path("/{id:\\d+}")
    @Produces(value = CustomMediaType.APPLICATION_PATIENT)
    public Response getById(@PathParam("id") final long id) {
        final Patient patient = this.patientService.getById(id).orElseThrow(NotFoundException::new);
        return Response.ok(new GenericEntity<>(PatientDTO.fromPatient(patient, uriInfo)) {}).build();
    }

    @HEAD
    @Produces(value = CustomMediaType.APPLICATION_PATIENT_LIST)
    public Response getPatientCount() {

        long count = patientService.getAllPatientsDisplayCount();

        return Response.ok() //TODO: 204? (No Content)
                .header("X-Total-Count", count)
                .build();
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
    @Consumes(CustomMediaType.APPLICATION_PATIENT)
    public Response createPatient(
            @Valid @NotNull PatientForm patientForm,
            @Context HttpHeaders headers
    ){
        final  Patient patient = patientService.create(patientForm.getName(),patientForm.getLastName(),patientForm.getEmail(),patientForm.getPassword(),patientForm.getPhone(), headers.getAcceptableLanguages(),patientForm.getCoverage(),patientForm.getNeighborhoodId());
        return  Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(patient.getId())).build()).build();
    }

    @Path("/{id:\\d+}")
    @PATCH
    @Consumes(CustomMediaType.APPLICATION_PATIENT)
    public Response modifyPatient(
            @PathParam("id") final long id,
            @Valid @NotNull UpdatePatientForm updatePatientForm
    ){
        final Patient patient = this.patientService.getById(id).orElseThrow(UserNotFoundException::new);
        patientService.updatePatient(patient,updatePatientForm.getName(),updatePatientForm.getLastName(),updatePatientForm.getPhone(),updatePatientForm.getCoverage());
        return Response.noContent().build();
    }
    @PATCH
    @Path("/{id:\\d+}/")
    @Consumes(CustomMediaType.APPLICATION_USER_PASSWORD)
    public Response changePassword(
            @PathParam("id") final long id,
            @Valid @NotNull ChangePasswordForm changePasswordForm
    ){
        patientService.changePassword(id,changePasswordForm.getPassword());
        return Response.noContent().build();
    }

}
