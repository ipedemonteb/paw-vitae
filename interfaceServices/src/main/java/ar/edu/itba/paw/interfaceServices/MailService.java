package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Appointment;
import org.springframework.scheduling.annotation.Async;

public interface MailService {

    public void sendAppointmentStatusEmail(String subject, Appointment appointment);

    @Async
    void sendReminderEmail(String subject, Appointment appointment);
}
