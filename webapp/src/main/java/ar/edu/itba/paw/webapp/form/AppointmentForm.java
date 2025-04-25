package ar.edu.itba.paw.webapp.form;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

public class AppointmentForm {


    @NotNull(message = "{appointment.date.notnull}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;

    @NotNull(message = "{appointment.hour.notnull}")
    @Min(value = 8, message = "Hour must be between 8 and 18")
    @Max(value = 18, message = "Hour must be between 8 and 18")
    private Integer appointmentHour;

    @Size(max = 255)
    private String reason;


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

