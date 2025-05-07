package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.AppointmentFileService;
import ar.edu.itba.paw.models.AppointmentFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Controller
public class FileController {

    private final AppointmentFileService fileService;

    @Autowired
    public FileController(AppointmentFileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping("/appointment/{appointmentId}/file/{fileId}")
    public void downloadFile(@PathVariable long appointmentId,
                             @PathVariable long fileId,
                             HttpServletResponse response) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<AppointmentFile> fileOpt = fileService.getAuthorizedFile(fileId, appointmentId, username);
        if (fileOpt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        AppointmentFile file = fileOpt.get();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getFileName() + "\"");
        response.getOutputStream().write(file.getFileData());
        response.getOutputStream().flush();
    }

}
