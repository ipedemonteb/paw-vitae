package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaceServices.ClientService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.MailService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Doctor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final MessageSource messageSource;
    private final static String from_mail = "vitaepaw@gmail.com";

    private final DoctorService doctorService;
    private final ClientService clientService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    public MailServiceImpl(final JavaMailSender mailSender, final TemplateEngine templateEngine, final MessageSource messageSource, final DoctorService doctorService, final ClientService clientService) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.messageSource = messageSource;
        this.doctorService = doctorService;
        this.clientService = clientService;
    }

    @Async
    @Override
    public void sendAppointmentStatusEmail(String subject, Appointment appointment){

        Doctor doctor = doctorService.getById(appointment.getDoctorId()).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        Client client = clientService.getById(appointment.getClientId()).orElseThrow(() -> new IllegalArgumentException("Client not found"));

        Locale clientLocale = Locale.forLanguageTag(client.getLanguage());
        Locale doctorLocale = Locale.forLanguageTag(doctor.getLanguage());

        Context clientContext = new Context(clientLocale);
        Context doctorContext = new Context(doctorLocale);

        String doctorSubject = messageSource.getMessage(subject, null, doctorLocale);
        String clientSubject = messageSource.getMessage(subject, null, clientLocale);

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("doctorName", doctor.getName() + " " + doctor.getLastName());
        templateModel.put("patientName", client.getName() + " " + client.getLastName());
        LocalDateTime date = appointment.getDate();
        templateModel.put("appointmentDate", date.toLocalDate().toString());
        templateModel.put("appointmentTime", date.getHour());
        //@TODO: Check
        templateModel.put("reason", appointment.getReason() != null ? appointment.getReason() : "-");

        clientContext.setVariables(templateModel);
        doctorContext.setVariables(templateModel);
        String htmlContentDoctor;
        String htmlContentClient;

        System.out.println("appointment.getStatus() = " + appointment.getStatus());

        if (appointment.getStatus().equals(AppointmentStatus.PENDIENTE.getValue())) {
            htmlContentDoctor = templateEngine.process("DoctorAppointmentRequest", doctorContext);
            htmlContentClient = templateEngine.process("PatientAppointmentRequest", clientContext);
        } else if (appointment.getStatus().equals(AppointmentStatus.CONFIRMADO.getValue())) {
            htmlContentDoctor = templateEngine.process("DoctorAppointmentConfirmation", doctorContext);
            htmlContentClient = templateEngine.process("PatientAppointmentConfirmation", clientContext);
        } else {
            htmlContentDoctor = templateEngine.process("DoctorAppointmentCancellation", doctorContext);
            htmlContentClient = templateEngine.process("PatientAppointmentCancellation", clientContext);
        }

        MimeMessage doctorMessage = mailSender.createMimeMessage();
        MimeMessage clientMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper doctorHelper = new MimeMessageHelper(doctorMessage, true, "UTF-8");
            MimeMessageHelper clientHelper = new MimeMessageHelper(clientMessage, true, "UTF-8");

            doctorHelper.setTo(doctor.getEmail());
            clientHelper.setTo(client.getEmail());

            doctorHelper.setSubject(doctorSubject);
            clientHelper.setSubject(clientSubject);

            doctorHelper.setText(htmlContentDoctor, true);
            clientHelper.setText(htmlContentClient, true);

            doctorHelper.setFrom(from_mail);
            clientHelper.setFrom(from_mail);
        } catch (MessagingException e) {
            LOGGER.error("Error while sending email", e);
        }

        mailSender.send(doctorMessage);
        mailSender.send(clientMessage);
    }
}