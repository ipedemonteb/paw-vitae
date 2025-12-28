package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
//import ar.edu.itba.paw.models.QueryParam;
import ar.edu.itba.paw.models.exception.DoctorOfficeNotFoundException;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.paging.ParamCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.*;
import java.util.List;

import static ar.edu.itba.paw.webapp.utils.ResponseUtils.buildPaginationHeaders;

@Path("/doctors")
@Component
public class RestDoctorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestDoctorController.class);

    private final DoctorService doctorService;
    private final SpecialtyService specialtyService;
    private final CoverageService coverageService;
    private final DoctorOfficeService doctorOfficeService;
    private final DoctorProfileService doctorProfileService;
    private final DoctorExperienceService doctorExperienceService;
    private final DoctorCertificationService doctorCertificationService;
    private final DoctorOfficeAvailabilityService doctorOfficeAvailabilityService;
    private final DoctorOfficeSpecialtyService doctorOfficeSpecialtyService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public RestDoctorController(DoctorService doctorService, SpecialtyService specialtyService, CoverageService coverageService, DoctorOfficeService doctorOfficeService, DoctorProfileService doctorProfileService, DoctorExperienceService doctorExperienceService, DoctorCertificationService doctorCertificationService, DoctorOfficeAvailabilityService doctorOfficeAvailabilityService, DoctorOfficeSpecialtyService doctorOfficeSpecialtyService) {
        this.doctorService = doctorService;
        this.specialtyService = specialtyService;
        this.coverageService = coverageService;
        this.doctorOfficeService = doctorOfficeService;
        this.doctorProfileService = doctorProfileService;
        this.doctorExperienceService = doctorExperienceService;
        this.doctorCertificationService = doctorCertificationService;
        this.doctorOfficeAvailabilityService = doctorOfficeAvailabilityService;
        this.doctorOfficeSpecialtyService = doctorOfficeSpecialtyService;

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
            @Min(1) //TODO, these annotations wont work because of a missing jersey dependency. Validate in backend or idk
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
        Response.ResponseBuilder rb = Response.ok(new GenericEntity<>(doctors) {});

        return buildPaginationHeaders(rb, doctorPage, uriInfo);
    }


//    @GET
//    @Produces(value = MediaType.APPLICATION_JSON)
//    public Response list(
//            @ParamCustomizer(defaultValue = 0,paramName = "specialty") ar.edu.itba.paw.models.QueryParam specialtyId,
//            @ParamCustomizer( defaultValue = 1) ar.edu.itba.paw.models.QueryParam page,
//            @ParamCustomizer(defaultValue = 0,paramName = "coverage") ar.edu.itba.paw.models.QueryParam coverageId,
//            @ParamCustomizer(paramName = "weekdays") List<QueryParam> weekdays,
//            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
//            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
//            @RequestParam(value = "direction", defaultValue = "asc") String direction,
//            @RequestParam(value = "view", required = false, defaultValue = "grid") String view
//    ) {
//        Page<Doctor> doctorPage = doctorService.getWithFilters(specialtyId.getValue(), coverageId.getValue(), weekdays, keyword, orderBy, direction, (int) page.getValue(), 9);
//
//        List<DoctorDTO> doctors = doctorPage.getContent().stream().map(d -> DoctorDTO.fromDoctor(d, uriInfo)).toList();
//        Response.ResponseBuilder rb = Response.ok(new GenericEntity<>(doctors) {});
//
//        return buildPaginationHeaders(rb, doctorPage, uriInfo);
//    }

    @GET
    @Path("/{id}/specialties")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getDoctorSpecialties(@PathParam("id") final long id) {
        List<Specialty> specialties = this.specialtyService.getByDoctorId(id);
        List<SpecialtyDTO> specialtyDTOS = specialties.stream().map(s -> SpecialtyDTO.fromSpecialty(s, uriInfo)).toList();
        return Response.ok(new GenericEntity<>(specialtyDTOS) {}).build();
    }

    @GET
    @Path("/{id}/coverages")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getDoctorCoverages(@PathParam("id") final long id) {
        List<Coverage> coverages = this.coverageService.findByDoctorId(id);
        List<CoverageDTO> coverageDTOS = coverages.stream().map(c -> CoverageDTO.fromCoverage(c, uriInfo)).toList();
        return Response.ok(new GenericEntity<>(coverageDTOS) {}).build();
    }

    @GET
    @Path("/{id}/offices")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getDoctorOffices(@PathParam("id") final long id) {
        List<DoctorOffice> offices = this.doctorOfficeService.getAllByDoctorId(id);
        List<OfficeDTO> officeDTOS = offices.stream().map(o -> OfficeDTO.fromDoctorOffice(o, uriInfo)).toList();
        return Response.ok(new GenericEntity<>(officeDTOS) {}).build();
    }

    @GET
    @Path("/{id}/offices/{officeId}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getDoctorOffices(@PathParam("id") final long id, @PathParam("officeId") final long officeId) {
        DoctorOffice office = this.doctorOfficeService.getById(officeId).orElseThrow(DoctorOfficeNotFoundException::new);
        return Response.ok(new GenericEntity<>(OfficeDTO.fromDoctorOffice(office, uriInfo)) {}).build();
    }

    @GET
    @Path("/{id}/offices/{officeId}/availability")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getDoctorOfficeAvailability(@PathParam("id") final long id, @PathParam("officeId") final long officeId) {
        List<DoctorOfficeAvailability> availabilities = this.doctorOfficeAvailabilityService.getByOfficeId(officeId);
        List<AvailabilityDTO> availabilityDTOS = availabilities.stream().map(a -> AvailabilityDTO.fromDoctorOfficeAvailability(a, uriInfo)).toList();
        return Response.ok(new GenericEntity<>(availabilityDTOS) {}).build();
    }

    @GET
    @Path("/{id}/offices/{officeId}/specialties")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getDoctorOfficeSpecialties(@PathParam("id") final long id, @PathParam("officeId") final long officeId) {
        List<DoctorOfficeSpecialty> specialties = this.doctorOfficeSpecialtyService.getByOfficeId(officeId);
        List<OfficeSpecialtyDTO> specialtyDTOS = specialties.stream().map(s -> OfficeSpecialtyDTO.fromDoctorOfficeSpecialty(s, uriInfo)).toList();
        return Response.ok(new GenericEntity<>(specialtyDTOS) {}).build();
    }


    @GET
    @Path("/{id}/profile")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getDoctorProfile(@PathParam("id") final long id) {
        DoctorProfile profile = this.doctorProfileService.findByDoctorId(id);
        return Response.ok(new GenericEntity<>(ProfileDTO.fromDoctorProfile(profile, uriInfo)) {}).build();
    }

    @GET
    @Path("/{id}/experiences")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getDoctorExperiences(@PathParam("id") final long id) {
        List<DoctorExperience> experiences = this.doctorExperienceService.findByDoctorId(id);
        List<ExperienceDTO> experienceDTOS = experiences.stream().map(e -> ExperienceDTO.fromDoctorExperience(e, uriInfo)).toList();
        return Response.ok(new GenericEntity<>(experienceDTOS) {}).build();
    }

    @GET
    @Path("/{id}/certifications")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getDoctorCertifications(@PathParam("id") final long id) {
        List<DoctorCertification> certifications = this.doctorCertificationService.findByDoctorId(id);
        List<CertificationDTO> certificationDTOS = certifications.stream().map(c -> CertificationDTO.fromDoctorCertification(c, uriInfo)).toList();
        return Response.ok(new GenericEntity<>(certificationDTOS) {}).build();
    }

}
