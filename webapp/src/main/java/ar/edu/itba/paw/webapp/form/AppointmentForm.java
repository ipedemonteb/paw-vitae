package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class AppointmentForm {

    @NotNull()
    @Size(min = 2, max = 50)
    private String name;

    @NotNull(message = "Last name is required")
    @Size(min = 2, max = 50)
    private String lastName;

    @NotNull(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(min = 6, max = 100)
    private String email;

    @NotNull(message = "Phone is required")
    @Size(min = 7, max = 20)
    private String phone;

    // Coverage selected by the user; the front end will supply the options.
    @NotNull(message = "Coverage selection is required")
    private Long coverageId;

    // Appointment specific fields
    @NotNull(message = "Doctor selection is required")
    private Long doctorId;

    @NotNull(message = "Appointment date/time is required")
    @Future(message = "The appointment must be scheduled for a future date/time")
    private LocalDateTime appointmentDateTime;

    @Size(max = 255)
    private String reason;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getCoverageId() {
        return coverageId;
    }

    public void setCoverageId(Long coverageId) {
        this.coverageId = coverageId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

