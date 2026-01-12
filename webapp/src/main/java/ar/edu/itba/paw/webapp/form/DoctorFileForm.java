package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validation.AppointmentFileValid;
import ar.edu.itba.paw.webapp.validation.InvalidDoctorFileForm;
import ar.edu.itba.paw.webapp.validation.InvalidFileQuantity;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
@InvalidDoctorFileForm(message = "InvalidDoctorFileForm.message")
@InvalidFileQuantity(message = "InvalidFileQuantity.message}")
public class DoctorFileForm {

    @Size(max = 5, message = "appointment.files.max")
    @AppointmentFileValid(message = "appointment.files.valid")
    private MultipartFile[] patientFiles;

    @Size(max = 255)
    private String report;

    @NotNull
    private Long appointmentId;

    public MultipartFile[] getPatientFiles() {
        return patientFiles;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setPatientFiles(MultipartFile[] patientFiles) {
        this.patientFiles = patientFiles;
    }

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

