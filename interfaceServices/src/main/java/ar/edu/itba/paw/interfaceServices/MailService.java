package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.User;
import org.springframework.scheduling.annotation.Async;

public interface MailService {

    public void sendAppointmentStatusEmail(String subject, Appointment appointment);

    @Async
    void sendReminderEmail(Appointment appointment);

    void sendRecoverPasswordEmail(User user, String token);
}
