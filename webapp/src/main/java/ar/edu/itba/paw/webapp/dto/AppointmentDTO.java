package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Appointment;

public class AppointmentDTO {
    private long id;

    public static AppointmentDTO fromAppointment(Appointment appointment) {
        AppointmentDTO res = new AppointmentDTO();
        res.id = appointment.getId();
        return res;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
