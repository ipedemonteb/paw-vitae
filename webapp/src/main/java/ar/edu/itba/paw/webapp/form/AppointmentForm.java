package ar.edu.itba.paw.webapp.form;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AppointmentForm {

    @NotNull
    @Size(min = 2, max = 50)
    private String name;

    @NotNull()
    @Size(min = 2, max = 50)
    private String lastName;

    @NotNull()
    @Email()
    @Size(min = 6, max = 100)
    private String email;

    @NotNull()
    @Size(min = 7, max = 20)
    private String phone;

    // Coverage selected by the user; the front end will supply the options.
    @NotNull()
    private Long coverageId;

    @NotNull(message = "{appointment.date.notnull}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

    @NotNull(message = "{appointment.hour.notnull}")
    @Min(value = 8, message = "Hour must be between 8 and 18")
    @Max(value = 18, message = "Hour must be between 8 and 18")
    private Integer appointmentHour;

    @Size(max = 255)
    private String reason;

    @NotNull
    private Long doctorId;

    @NotNull
    private Long clientId;

    @NotNull
    private Long specialtyId;



    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(Long specialtyId) {
        this.specialtyId = specialtyId;
    }

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

//    public LocalDateTime getAppointmentDateTime() {
//        return appointmentDateTime;
//    }
//
//    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
//        this.appointmentDateTime = appointmentDateTime;
//    }

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
}

