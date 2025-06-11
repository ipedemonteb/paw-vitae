package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.AppointmentFileService;
import ar.edu.itba.paw.models.AppointmentFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;

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
        AppointmentFile file = fileService.getAuthorizedFile(fileId, appointmentId, username).orElseThrow(FileNotFoundException::new);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getFileName() + "\"");
        response.getOutputStream().write(file.getFileData());
        response.getOutputStream().flush();
    }

    @RequestMapping("/appointment/{appointmentId}/file-view/{fileId}")
    public void viewFileInline(@PathVariable long appointmentId,
                               @PathVariable long fileId,
                               HttpServletResponse response) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppointmentFile file = fileService.getAuthorizedFile(fileId, appointmentId, username)
                .orElseThrow(FileNotFoundException::new);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getFileName() + "\"");
        response.getOutputStream().write(file.getFileData());
        response.getOutputStream().flush();
    }



}
