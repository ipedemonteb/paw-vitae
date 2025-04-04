package ar.edu.itba.paw.interfaceServices;

import javax.mail.MessagingException;
import java.util.Map;

public interface MailService {
    public void sendEmail(String to, String subject, Map<String, Object> templateModel) throws MessagingException;
}
