package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validation.AppointmentExistence;
import ar.edu.itba.paw.webapp.validation.AppointmentValidDate;
import ar.edu.itba.paw.webapp.validation.AppointmentValidSpecialtyForDoctor;
import ar.edu.itba.paw.webapp.validation.OfficeAcceptsSpecialty;
import ar.edu.itba.paw.webapp.validation.OfficeAvailableAtDayAndTime;
import ar.edu.itba.paw.webapp.validation.OfficeOwnedByDoctor;
import ar.edu.itba.paw.webapp.validation.Specialty;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@OfficeOwnedByDoctor(message = "office.invalid")
@OfficeAcceptsSpecialty(officeId = "officeId", specialtyId = "specialtyId")
@OfficeAvailableAtDayAndTime(officeId = "officeId", date = "appointmentDate", appointmentHour = "appointmentHour")
@AppointmentExistence(userId = "doctorId", date = "appointmentDate", startTime = "appointmentHour")
@AppointmentExistence(userId = "patientId", date = "appointmentDate", startTime = "appointmentHour")
@AppointmentValidDate(date = "appointmentDate", startTime = "appointmentHour")
@AppointmentValidSpecialtyForDoctor(doctorId = "doctorId", specialtyId = "specialtyId")
public class AppointmentForm {

    @NotNull(message = "appointment.date.notnull")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

    @NotNull(message = "appointment.hour.notnull")
    @Min(value = 8, message = "$appointment.hour.invalid")
    @Max(value = 20, message = "$appointment.hour.invalid")
    private Integer appointmentHour;

    @Size(max = 255)
    private String reason;

    @NotNull
    @Specialty(message = "specialty.invalid")
    private long specialtyId;

    @NotNull
    private long officeId;

    @NotNull
    private long doctorId;

    @NotNull
    private long patientId;

    private boolean allowFullHistory = true;

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

    public long getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(long specialtyId) {
        this.specialtyId = specialtyId;
    }

    public long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(long officeId) {
        this.officeId = officeId;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public boolean isAllowFullHistory() {
        return allowFullHistory;
    }

    public void setAllowFullHistory(boolean allowFullHistory) {
        this.allowFullHistory = allowFullHistory;
    }
}
