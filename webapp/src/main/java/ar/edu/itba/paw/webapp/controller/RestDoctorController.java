package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.DoctorOfficeNotFoundException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.CustomMediaType;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.webapp.utils.CacheUtils;
import ar.edu.itba.paw.webapp.utils.FileUtils;
import ar.edu.itba.paw.webapp.utils.UriUtils;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.*;
import java.time.LocalDate;
import java.util.List;

import static ar.edu.itba.paw.webapp.utils.ResponseUtils.*;

@Path(UriUtils.DOCTORS)
@Component
public class RestDoctorController {


    private final DoctorService doctorService;
    private final SpecialtyService specialtyService;
    private final CoverageService coverageService;
    private final DoctorOfficeService doctorOfficeService;
    private final DoctorProfileService doctorProfileService;
    private final DoctorExperienceService doctorExperienceService;
    private final DoctorCertificationService doctorCertificationService;
    private final DoctorOfficeAvailabilityService doctorOfficeAvailabilityService;
    private final DoctorOfficeSpecialtyService doctorOfficeSpecialtyService;
    private final ImageService imageService;
    private final UnavailabilitySlotsService unavailabilitySlotsService;
    private final OccupiedSlotsService occupiedSlotsService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public RestDoctorController(DoctorService doctorService, SpecialtyService specialtyService, CoverageService coverageService, DoctorOfficeService doctorOfficeService, DoctorProfileService doctorProfileService, DoctorExperienceService doctorExperienceService, DoctorCertificationService doctorCertificationService, DoctorOfficeAvailabilityService doctorOfficeAvailabilityService, DoctorOfficeSpecialtyService doctorOfficeSpecialtyService, OccupiedSlotsService occupiedSlotsService, ImageService imageService, UnavailabilitySlotsService unavailabilitySlotsService) {
        this.doctorService = doctorService;
        this.specialtyService = specialtyService;
        this.coverageService = coverageService;
        this.doctorOfficeService = doctorOfficeService;
        this.doctorProfileService = doctorProfileService;
        this.doctorExperienceService = doctorExperienceService;
        this.doctorCertificationService = doctorCertificationService;
        this.doctorOfficeAvailabilityService = doctorOfficeAvailabilityService;
        this.doctorOfficeSpecialtyService = doctorOfficeSpecialtyService;
        this.imageService = imageService;
        this.unavailabilitySlotsService = unavailabilitySlotsService;
        this.occupiedSlotsService = occupiedSlotsService;
    }

    @GET
    @Path("/{id:\\d+}")
    @Produces(value = CustomMediaType.APPLICATION_DOCTOR)
    public Response getById(@PathParam("id") final long id, @Context final Request request) {
        final Doctor doctor = this.doctorService.getById(id).orElseThrow(DoctorOfficeNotFoundException::new);
        return CacheUtils.conditionalCacheETag(Response.ok(new GenericEntity<>(DoctorDTO.fromDoctor(doctor, uriInfo)) {}), request, doctor.hashCode()).build();
    }

    @GET
    @Produces(value = CustomMediaType.APPLICATION_DOCTOR_LIST)
    public Response list(
    @Valid @BeanParam final DoctorSearchForm doctorSearchForm
    ) {
        Page<Doctor> doctorPage = this.doctorService.getWithFilters(doctorSearchForm.getSpecialtyId(), doctorSearchForm.getCoverageId(), doctorSearchForm.getWeekdays(), doctorSearchForm.getKeyword(), doctorSearchForm.getOrderBy(), doctorSearchForm.getDirection(), doctorSearchForm.getPage(), doctorSearchForm.getPageSize());
        return buildPaginationHeaders(Response.ok(new GenericEntity<>(DoctorDTO.fromDoctor(doctorPage.getContent(), uriInfo)) {}), doctorPage, uriInfo);
    }

    @HEAD
    @Produces(value = CustomMediaType.APPLICATION_DOCTOR_LIST)
    public Response getDoctorCount(){
        long count = doctorService.getAllDoctorsDisplayCount();
        return Response.ok().header("X-Total-Count", count).build();
    }

    @GET
    @Path("/{id:\\d+}/specialties")
    @Produces(value = CustomMediaType.APPLICATION_SPECIALTY_LIST)
    public Response getDoctorSpecialties(@PathParam("id") final long id) {
        List<Specialty> specialties = this.specialtyService.getByDoctorId(id);
        return Response.ok(new GenericEntity<List<SpecialtyDTO>>(SpecialtyDTO.fromSpecialty(specialties, uriInfo)) {}).build();
    }

    @GET
    @Path("/{id:\\d+}/coverages")
    @Produces(value = CustomMediaType.APPLICATION_COVERAGE_LIST)
    public Response getDoctorCoverages(@PathParam("id") final long id) {
        List<Coverage> coverages = this.coverageService.findByDoctorId(id);
        return Response.ok(new GenericEntity<>(CoverageDTO.fromCoverage(coverages, uriInfo)) {}).build();
    }

    @GET
    @Path("/{id:\\d+}/unavailability")
    @Produces(value = CustomMediaType.APPLICATION_UNAVAILABILITY_LIST)
    public Response getDoctorUnavailability(
            @PathParam("id") final long id,
            @BeanParam @Valid UnavailabilitySearchForm form
    ) {
        Page<UnavailabilitySlot> unavailabilitySlotPage = this.unavailabilitySlotsService.getUnavailabilityByDoctorId(
                id,
                form.getFrom(),
                form.getTo(),
                form.getPage(),
                form.getPageSize()
        );
        return buildPaginationHeaders(Response.ok(new GenericEntity<>(UnavailabilityDTO.fromUnavailabilitySlot(unavailabilitySlotPage.getContent(), uriInfo)) {}), unavailabilitySlotPage, uriInfo);
    }

    @GET
    @Path("/{id:\\d+}/offices")
    @Produces(value = CustomMediaType.APPLICATION_OFFICE_LIST)
    public Response getDoctorOffices(
            @PathParam("id") final long id,
            @BeanParam @Valid OfficeSearchForm form) {

        List<DoctorOffice> offices = this.doctorOfficeService.getByDoctorId(id, form.getStatus());
        return Response.ok(new GenericEntity<>(OfficeDTO.fromDoctorOffice(offices, uriInfo)) {}).build();
    }

    @GET
    @Path("/{id:\\d+}/offices/{officeId:\\d+}")
    @Produces(value = CustomMediaType.APPLICATION_OFFICE)
    public Response getDoctorOffices(@PathParam("id") final long id, @PathParam("officeId") final long officeId, @Context final Request request) {
        DoctorOffice office = this.doctorOfficeService.getById(officeId).orElseThrow(DoctorOfficeNotFoundException::new);
        return CacheUtils.conditionalCacheETag(Response.ok(new GenericEntity<>(OfficeDTO.fromDoctorOffice(office, uriInfo)) {}), request, office.hashCode()).build();
    }

    @GET
    @Path("/{id:\\d+}/availability")
    @Produces(value = CustomMediaType.APPLICATION_AVAILABILITY_LIST)
    public Response getDoctorOfficeAvailability(@PathParam("id") final long id, @BeanParam @Valid AvailabilityOfficeForm availabilityOfficeForm) {
        List<DoctorOfficeAvailability> availabilities = this.doctorOfficeAvailabilityService.getWithFilters(id,availabilityOfficeForm.getOfficeId());
        return Response.ok(new GenericEntity<>(AvailabilityDTO.fromDoctorOfficeAvailability(availabilities, uriInfo)) {}).build();
    }

    @GET
    @Path("/{id:\\d+}/offices/{officeId:\\d+}/specialties")
    @Produces(value = CustomMediaType.APPLICATION_OFFICE_SPECIALTY_LIST)
    public Response getDoctorOfficeSpecialties(@PathParam("id") final long id, @PathParam("officeId") final long officeId) {
        List<DoctorOfficeSpecialty> specialties = this.doctorOfficeSpecialtyService.getByOfficeId(officeId);
        return Response.ok(new GenericEntity<>(OfficeSpecialtyDTO.fromDoctorOfficeSpecialty(specialties, uriInfo)) {}).build();
    }

    @GET
    @Path("/{id:\\d+}/biography")
    @Produces(value = CustomMediaType.APPLICATION_DOCTOR_PROFILE)
    public Response getDoctorProfile(@PathParam("id") final long id, @Context final Request request) {
        DoctorProfile profile = this.doctorProfileService.findByDoctorId(id);
        return CacheUtils.conditionalCacheETag(Response.ok(new GenericEntity<>(ProfileDTO.fromDoctorProfile(profile, uriInfo)) {}), request, profile.hashCode()).build();
    }

    @GET
    @Path("/{id:\\d+}/experiences")
    @Produces(value = CustomMediaType.APPLICATION_DOCTOR_EXPERIENCE_LIST)
    public Response getDoctorExperiences(@PathParam("id") final long id) {
        List<DoctorExperience> experiences = this.doctorExperienceService.findByDoctorId(id);
        return Response.ok(new GenericEntity<>(ExperienceDTO.fromDoctorExperience(experiences, uriInfo)) {}).build();
    }

    @GET
    @Path("/{id:\\d+}/certifications")
    @Produces(value = CustomMediaType.APPLICATION_DOCTOR_CERTIFICATION_LIST)
    public Response getDoctorCertifications(@PathParam("id") final long id) {
        List<DoctorCertification> certifications = this.doctorCertificationService.findByDoctorId(id);
        return Response.ok(new GenericEntity<>(CertificationDTO.fromDoctorCertification(certifications, uriInfo)) {}).build();
    }


    @GET
    @Path("/{id:\\d+}/image")
    @Produces({"image/png", "image/jpeg", "image/jpg"})
    public Response getImage(@PathParam("id") final long id, @Context final Request request) {
        final Images image = doctorService.getDoctorImage(id).orElseThrow(NotFoundException::new);
        ResponseBuilder response =  Response.ok(image.getImage());
        return CacheUtils.conditionalCacheETag(response, request, image.hashCode()).build();
    }

    @POST
    @Consumes(value = CustomMediaType.APPLICATION_DOCTOR)
    public Response createDoctor(
            @Valid @NotNull DoctorForm doctorForm,
            @Context HttpHeaders headers
    ) {
        Doctor doctor = this.doctorService.create(doctorForm.getName(), doctorForm.getLastName(), doctorForm.getEmail(), doctorForm.getPassword(), doctorForm.getPhone(), headers.getAcceptableLanguages(), doctorForm.getSpecialties(), doctorForm.getCoverages());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(doctor.getId())).build()).build();
    }

    @PATCH
    @Consumes(value = CustomMediaType.APPLICATION_DOCTOR)
    @Path("/{id:\\d+}")
    public Response updateDoctor(
            @PathParam("id") final long id,
            @Valid @NotNull UpdateDoctorForm updateDoctorForm
    ) {
        Doctor doctor = this.doctorService.getById(id).orElseThrow(UserNotFoundException::new);
        this.doctorService.updateDoctor(doctor, updateDoctorForm.getName(),
                updateDoctorForm.getLastName(), updateDoctorForm.getPhone(), updateDoctorForm.getSpecialties(), updateDoctorForm.getCoverages(),updateDoctorForm.getPassword());
        return Response.noContent().build();
    }

    @PUT
    @Consumes(value = CustomMediaType.APPLICATION_DOCTOR_CERTIFICATION_LIST)
    @Path("/{id:\\d+}/certifications")
    public Response changeDoctorCertifications(
            @PathParam("id") final long id,
            @Valid @NotNull DoctorCertificatesForm certifications
    ) {
        Doctor doctor = this.doctorService.getById(id).orElseThrow(UserNotFoundException::new);
        this.doctorCertificationService.update(doctor, certifications.getCertificates());
        return Response.noContent().build();
    }

    @PUT
    @Consumes(value = CustomMediaType.APPLICATION_DOCTOR_EXPERIENCE_LIST)
    @Path("/{id:\\d+}/experiences")
    public Response changeDoctorExperiences(
            @PathParam("id") final long id,
            @Valid @NotNull DoctorExperiencesForm experiences
    ) {
        Doctor doctor = this.doctorService.getById(id).orElseThrow(UserNotFoundException::new);
        this.doctorExperienceService.update(doctor, experiences.getExperiences());
        return Response.noContent().build();
    }

    @PUT
    @Consumes(value = CustomMediaType.APPLICATION_DOCTOR_PROFILE)
    @Path("/{id:\\d+}/biography")
    public Response changeDoctorBiography(
            @PathParam("id") final long id,
            @Valid @NotNull DoctorBioForm doctorForm
    ) {
        Doctor doctor = this.doctorService.getById(id).orElseThrow(UserNotFoundException::new);
        this.doctorProfileService.update(doctor, doctorForm.getBiography(), doctorForm.getDescription());
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id:\\d+}/image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadImage(final FormDataMultiPart multipart,
                                @PathParam("id") final long id) {
        MultipartFile file = FileUtils.requireSingleImage(multipart, "file");
        long imageId = imageService.create(file, id);
        doctorService.setImage(id, imageId);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id:\\d+}/availability")
    @Consumes(CustomMediaType.APPLICATION_AVAILABILITY_LIST)
    public Response setDoctorAvailability(
            @PathParam("id") final long id,
            @Valid @NotNull DoctorAvailabilityForm availabilityForm
    ) {
        this.doctorOfficeAvailabilityService.update(availabilityForm.getDoctorOfficeAvailabilities(), id);
        return Response.noContent().build();
    }

//    @PUT
//    @Path("/{id:\\d+}/unavailability")
//    @Consumes(CustomMediaType.APPLICATION_UNAVAILABILITY_LIST)
//    public Response setDoctorUnavailability(
//            @PathParam("id") final long id,
//            @Valid @NotNull DoctorUnavailabilityForm unavailabilityForm
//    ) {
//        Doctor doctor = this.doctorService.getById(id).orElseThrow(UserNotFoundException::new);
//        this.unavailabilitySlotsService.updateDoctorUnavailability(doctor,unavailabilityForm.getUnavailabilitySlots());
//        return Response.noContent().build();
//    }

    @POST
    @Path("/{id:\\d+}/unavailability")
    @Consumes(CustomMediaType.APPLICATION_UNAVAILABILITY)
    public Response createDoctorUnavailability(
            @PathParam("id") final long id,
            @Valid @NotNull UnavailabilitySlotForm slotForm
    ) {
        Doctor doctor = this.doctorService.getById(id).orElseThrow(UserNotFoundException::new);
        UnavailabilitySlot createdSlot = this.unavailabilitySlotsService.create(doctor, slotForm);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(createdSlot.getId())).build()).build();
    }

    @DELETE
    @Path("/{id:\\d+}/unavailability/{slotId:\\d+}")
    public Response deleteDoctorUnavailability(
            @PathParam("id") final long doctorId,
            @PathParam("slotId") final long slotId
    ) {
        this.unavailabilitySlotsService.deleteDoctorUnavailability(doctorId, slotId);
        return Response.noContent().build();
    }
    @POST
    @Path("/{id:\\d+}/offices")
    @Consumes(value = CustomMediaType.APPLICATION_OFFICE)
    public Response createDoctorOffice(
            @Valid @NotNull OfficeForm officeForm,
            @PathParam("id") final long id
    ) {
        Doctor doctor = this.doctorService.getById(id).orElseThrow(UserNotFoundException::new);
        DoctorOffice office = this.doctorOfficeService.create(doctor, officeForm.getDoctorOfficeForm());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(office.getId())).build()).build();
    }

    @PATCH
    @Path("/{id:\\d+}/offices/{officeId:\\d+}")
    @Consumes(value = CustomMediaType.APPLICATION_OFFICE)
    public Response updateDoctorOffice(
            @Valid @NotNull UpdateOfficeForm officeForm,
            @PathParam("id") final long id,
            @PathParam("officeId") final long officeId
    ){
        this.doctorOfficeService.update(officeId,officeForm.getDoctorOfficeForm(),id);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id:\\d+}/offices/{officeId:\\d+}")
    public Response deleteDoctorOffice(
            @PathParam("id") final long doctorId,
            @PathParam("officeId") final long officeId
    ) {
        this.doctorOfficeService.delete(officeId, doctorId);
        return Response.noContent().build();
    }


    @GET
    @Path("/{id:\\d+}/slots")
    @Consumes(value = CustomMediaType.APPLICATION_AVAILABILITY_SLOTS_LIST)
    public Response getDoctorAvailabilitySlots(
            @PathParam("id") final long id,
            @BeanParam @Valid OccupiedSlotsSearchForm form) {

        List<OccupiedSlots> slots = this.occupiedSlotsService.getByDoctorIdInDateRange(
                id, LocalDate.parse(form.getFrom()), LocalDate.parse(form.getTo())
        );
        return Response.ok(new GenericEntity<>(OccupiedSlotDTO.fromList(slots, uriInfo)) {}).build();
    }

    //todo: cache?
    @GET
    @Path("/{id:\\d+}/slots/{slotId:\\d+}")
    @Consumes(value = CustomMediaType.APPLICATION_AVAILABILITY_SLOT)
    public Response getDoctorAvailabilitySlotById(
            @PathParam("id") final long id,
            @PathParam("slotId") final long slotId
    ) {
        OccupiedSlots slot = this.occupiedSlotsService.getById(slotId).orElseThrow(DoctorOfficeNotFoundException::new);
        return Response.ok(new GenericEntity<>(OccupiedSlotDTO.fromSlot(slot, uriInfo)) {}).build();
    }

}