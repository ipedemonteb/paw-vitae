package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.AppointmentFileService;
import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.models.AppointmentFile;
import ar.edu.itba.paw.webapp.CustomMediaType;
import ar.edu.itba.paw.webapp.dto.AppointmentFileDTO;
import ar.edu.itba.paw.webapp.utils.CacheUtils;
import ar.edu.itba.paw.webapp.utils.FileUtils;
import ar.edu.itba.paw.webapp.utils.UriUtils;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.*;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Context;
import java.net.URI;
import java.util.List;


@Path(UriUtils.APPOINTMENT_FILES)
@Component
public class RestAppointmentFileController {


    private final AppointmentFileService appointmentFileService;
    private final AppointmentService appointmentService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public RestAppointmentFileController(AppointmentFileService appointmentFileService, AppointmentService appointmentService) {
        this.appointmentFileService = appointmentFileService;
        this.appointmentService = appointmentService;
    }

    @GET
    @Produces(CustomMediaType.APPLICATION_APPOINTMENT_FILE_LIST)
    public Response listFiles(@PathParam("appointmentId") final long appointmentId) {
        appointmentService.getById(appointmentId).orElseThrow(NotFoundException::new);
        List<AppointmentFile> files = appointmentFileService.getByAppointmentId(appointmentId);
        return Response.ok(new GenericEntity<>(AppointmentFileDTO.fromAppointmentFile(files, uriInfo)) {}).build();
    }

    @GET
    @Path("/{fileId:\\d+}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(@PathParam("appointmentId") final long appointmentId,
                                 @PathParam("fileId") final long fileId) {

        AppointmentFile file = getAuthorizedFile(appointmentId, fileId);

        ResponseBuilder response = Response.ok(file.getFileData())
                .type(MediaType.APPLICATION_OCTET_STREAM) //TODO remove
                .header("Content-Disposition", "attachment; filename=\"" + file.getFileName() + "\"");
        return CacheUtils.unconditionalCache(response, CacheUtils.FILE_MAX_AGE).build();
    }

    @GET
    @Path("/{fileId:\\d+}/view")
    @Produces("application/pdf")
    public Response viewFileInline(
            @PathParam("appointmentId") final long appointmentId,
            @PathParam("fileId") final long fileId
    ) {

        AppointmentFile file = getAuthorizedFile(appointmentId, fileId);

        ResponseBuilder response = Response.ok(file.getFileData())
                .type("application/pdf") //TODO remove
                .header("Content-Disposition", "inline; filename=\"" + file.getFileName() + "\"");

        return CacheUtils.unconditionalCache(response, CacheUtils.FILE_MAX_AGE).build();
    }

    private AppointmentFile getAuthorizedFile(long appointmentId, long fileId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return appointmentFileService
                .getAuthorizedFile(fileId, appointmentId, username)
                .orElseThrow(NotFoundException::new);
    }

    @POST
    @Path("/doctor")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadDoctorFiles(@PathParam("appointmentId") final long appointmentId,
                                     final FormDataMultiPart multipart) {

        MultipartFile file = FileUtils.requireSinglePdf(multipart, "file");
        AppointmentFile createdFile = appointmentFileService.create(file,"doctor" , appointmentId);
        return Response.created(buildLocation(appointmentId, createdFile)).build();
    }

    @POST
    @Path("/patient")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadPatientFiles(@PathParam("appointmentId") final long appointmentId,
                                final FormDataMultiPart multipart) {

        MultipartFile file = FileUtils.requireSinglePdf(multipart, "file");
        AppointmentFile createdFile = appointmentFileService.create(file,"patient" , appointmentId);
        return Response.created(buildLocation(appointmentId, createdFile)).build();
    }


    private URI buildLocation(long appointmentId, AppointmentFile createdFile) {
        long fileId = createdFile.getId();
        return uriInfo.getBaseUriBuilder().path("api")
                .path("appointments")
                .path(String.valueOf(appointmentId))
                .path("files")
                .path(String.valueOf(fileId))
                .build();
    }
}
