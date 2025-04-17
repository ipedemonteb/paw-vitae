package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Appointment;

public interface MailService {

    public void sendAppointmentStatusEmail(String subject, Appointment appointment);
}
