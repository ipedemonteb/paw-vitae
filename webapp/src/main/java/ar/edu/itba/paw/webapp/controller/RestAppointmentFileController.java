package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.AppointmentFileService;
import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.models.AppointmentFile;
import ar.edu.itba.paw.webapp.dto.AppointmentDTO;
import ar.edu.itba.paw.webapp.dto.AppointmentFileDTO;
import ar.edu.itba.paw.webapp.dto.CoverageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Context;
import java.util.List;


@Path("/appointments/{appointmentId}/files")
@Component
public class RestAppointmentFileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestAppointmentFileController.class);

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
    @Produces(MediaType.APPLICATION_JSON)
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

        return Response.ok(file.getFileData())
                .type(MediaType.APPLICATION_OCTET_STREAM) //TODO remove
                .header("Content-Disposition", "attachment; filename=\"" + file.getFileName() + "\"")
                .build();
    }

    @GET
    @Path("/{fileId:\\d+}/view")
    @Produces("application/pdf")
    public Response viewFileInline(
            @PathParam("appointmentId") final long appointmentId,
            @PathParam("fileId") final long fileId
    ) {

        AppointmentFile file = getAuthorizedFile(appointmentId, fileId);

        return Response.ok(file.getFileData())
                .type("application/pdf") //TODO remove
                .header("Content-Disposition", "inline; filename=\"" + file.getFileName() + "\"")
                .build();
    }

    private AppointmentFile getAuthorizedFile(long appointmentId, long fileId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return appointmentFileService
                .getAuthorizedFile(fileId, appointmentId, username)
                .orElseThrow(NotFoundException::new);
    }
}
