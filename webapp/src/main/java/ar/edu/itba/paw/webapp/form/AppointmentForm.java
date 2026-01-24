package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@OfficeOwnedByDoctor(message = "office.invalid")
@OfficeAcceptsSpecialty(officeId = "officeId", specialtyId = "specialtyId")
@OfficeAvailableAtDayAndTime(officeId = "officeId", slotId="slotId")
@AppointmentExistence(userId = "patientId", slotId="slotId")
@AppointmentValidDate(slotId="slotId")
@AppointmentValidSpecialtyForDoctor(doctorId = "doctorId", specialtyId = "specialtyId")
public class AppointmentForm {

    @SlotAvailable(message = "appointment.slot.unavailable")
    @NotNull
    private Long slotId;
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


    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
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
