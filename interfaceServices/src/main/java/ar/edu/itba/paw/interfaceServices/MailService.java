package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Appointment;

import javax.mail.MessagingException;
import java.util.Map;

public interface MailService {
    public void sendEmail(String subject, Map<String, Object> args, String toMail, String template) throws MessagingException;
}
