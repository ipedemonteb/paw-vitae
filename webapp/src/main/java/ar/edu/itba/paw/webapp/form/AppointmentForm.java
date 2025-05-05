package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validation.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDate;
@AppointmentExistence(doctorId = "doctorId", date = "appointmentDate", startTime = "appointmentHour", message = "{appointment.date.existance}")
@AppointmentValidDate(date = "appointmentDate", startTime = "appointmentHour", message = "{appointment.date.valid}")
public class AppointmentForm {


    @NotNull(message = "{appointment.date.notnull}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

    @NotNull(message = "{appointment.hour.notnull}")
    @Min(value = 8, message = "Hour must be between 8 and 20")
    @Max(value = 20, message = "Hour must be between 8 and 20")
    private Integer appointmentHour;

    @Size(max = 255)
    private String reason;

    @Size(max = 5, message = "{appointment.files.max}")
    @AppointmentFileValid(message = "{appointment.files.valid}")
    private MultipartFile[] patientFiles;

    @NotNull
    @Specialty(message = "{specialty.invalid}")
    private long specialtyId;

    @NotNull
    private final long doctorId;

    public AppointmentForm(long doctorId) {
        this.doctorId = doctorId;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public MultipartFile[] getPatientFiles() {
        return patientFiles;
    }

    public void setPatientFiles(MultipartFile[] patientFiles) {
        this.patientFiles = patientFiles;
    }

    public long getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(long specialtyId) {
        this.specialtyId = specialtyId;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Integer getAppointmentHour() {
        return appointmentHour;
    }

    public void setAppointmentHour(Integer appointmentHour) {
        this.appointmentHour = appointmentHour;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    public MultipartFile[] getFiles() {
        return patientFiles;
    }

    public void setFiles(MultipartFile[] files) {
        this.patientFiles = files;
    }
}

