package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.AppointmentFile;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

public class AppointmentFileDTO {

    private long id;
    private String fileName;
    private String uploaderRole;
    private URI download;
    private URI view;

    public static AppointmentFileDTO fromAppointmentFile(AppointmentFile file, UriInfo uriInfo) {
        AppointmentFileDTO dto = new AppointmentFileDTO();

        dto.id = file.getId();
        dto.fileName = file.getFileName();
        dto.uploaderRole = file.getUploaderRole();

        String appointmentId = String.valueOf(file.getAppointment().getId());
        String fileId = String.valueOf(file.getId());

        dto.download = uriInfo.getBaseUriBuilder().path("api")
                .path("appointments")
                .path(appointmentId)
                .path("files")
                .path(fileId)
                .build();

        dto.view = uriInfo.getBaseUriBuilder().path("api")
                .path("appointments")
                .path(appointmentId)
                .path("files")
                .path(fileId)
                .path("view")
                .build();

        return dto;
    }

    public static List<AppointmentFileDTO> fromAppointmentFile(List<AppointmentFile> appointmentFiles, UriInfo uriInfo) {
        return appointmentFiles.stream().map(a -> fromAppointmentFile(a, uriInfo)).toList();
    }

    public long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUploaderRole() {
        return uploaderRole;
    }

    public URI getDownload() {
        return download;
    }

    public URI getView() {
        return view;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setUploaderRole(String uploaderRole) {
        this.uploaderRole = uploaderRole;
    }

    public void setDownload(URI download) {
        this.download = download;
    }

    public void setView(URI view) {
        this.view = view;
    }
}

