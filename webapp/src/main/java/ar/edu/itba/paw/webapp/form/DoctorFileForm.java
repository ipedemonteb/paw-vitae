package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validation.AppointmentFileValid;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

public class DoctorFileForm {

    @Size(max = 5, message = "{appointment.files.max}")
    @AppointmentFileValid(message = "{appointment.files.valid}")
    private MultipartFile[] patientFiles;

    @Size(max = 255)
    private String report;

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public MultipartFile[] getFiles() {
        return patientFiles;
    }

    public void setFiles(MultipartFile[] files) {
        this.patientFiles = files;
    }
}

