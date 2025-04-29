package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validation.AppointmentFileValid;
import ar.edu.itba.paw.webapp.validation.AppointmentValidDate;
import ar.edu.itba.paw.webapp.validation.FileSize;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDate;
public class DoctorFileForm {

    @Size(max = 5, message = "{appointment.files.max}")
    @AppointmentFileValid(message = "{appointment.files.valid}")
    private MultipartFile[] patientFiles;

    public MultipartFile[] getFiles() {
        return patientFiles;
    }

    public void setFiles(MultipartFile[] files) {
        this.patientFiles = files;
    }
}

