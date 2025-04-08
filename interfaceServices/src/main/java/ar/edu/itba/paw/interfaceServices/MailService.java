package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Appointment;

import javax.mail.MessagingException;

public interface MailService {
    public void sendEmail( String subject, Appointment appointment, long doctorId, long clientId) throws MessagingException;
}
