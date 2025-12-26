package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.exception.DoctorOfficeNotFoundException;
import ar.edu.itba.paw.webapp.dto.DoctorDTO;
import ar.edu.itba.paw.webapp.paging.ParamCustomizer;
import org.jvnet.hk2.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

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
        return Response.ok(new GenericEntity<>(DoctorDTO.fromDoctor(doctor, uriInfo)) {}).build();
    }

    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response list(
            @QueryParam("specialty")
            @DefaultValue("0")
            @Min(0)
            long specialtyId,

            @QueryParam("page")
            @DefaultValue("1")
            @Min(1)
            int page,

            @QueryParam("coverage")
            @DefaultValue("0")
            @Min(0)
            long coverageId,

            @QueryParam("weekdays")
            List<@Min(0) @Max(6) Integer> weekdays,

            @QueryParam("keyword")
            @DefaultValue("")
            String keyword,

            @QueryParam("orderBy")
            @DefaultValue("name")
            String orderBy,

            @QueryParam("direction")
            @DefaultValue("asc")
            String direction
    ) {
        Page<Doctor> doctorPage = this.doctorService.getWithFilters(specialtyId, coverageId, weekdays, keyword, orderBy, direction, page, 9);
        List<DoctorDTO> doctors = doctorPage.getContent().stream().map(d -> DoctorDTO.fromDoctor(d, uriInfo)).toList();
        return Response.ok(new GenericEntity<>(doctors) {})
                .build();
    }

}
