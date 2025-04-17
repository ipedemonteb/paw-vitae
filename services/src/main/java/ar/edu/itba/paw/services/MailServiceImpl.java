package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaceServices.ClientService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.MailService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Doctor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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

        Context context = new Context();
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("doctorName", doctor.getName() + " " + doctor.getLastName());
        templateModel.put("patientName", client.getName() + " " + client.getLastName());
        LocalDateTime date = appointment.getDate();
        templateModel.put("appointmentDate", date.toLocalDate().toString());
        templateModel.put("appointmentTime", date.getHour());
        templateModel.put("reason", appointment.getReason() != null ? appointment.getReason() : messageSource.getMessage("email.emptyReason", null, LocaleContextHolder.getLocale()));

        context.setVariables(templateModel);
        String htmlContentDoctor;
        String htmlContentClient;

        System.out.println("appointment.getStatus() = " + appointment.getStatus());

        if (appointment.getStatus().equals("pendiente")) { //TODO use enum maybe for types, not magic strings
            htmlContentDoctor = templateEngine.process("DoctorAppointmentRequest", context);
            htmlContentClient = templateEngine.process("PatientAppointmentRequest", context);
        } else if (appointment.getStatus().equals("confirmado")) {
            htmlContentDoctor = templateEngine.process("DoctorAppointmentConfirmation", context);
            htmlContentClient = templateEngine.process("PatientAppointmentConfirmation", context);
        } else {
            htmlContentDoctor = templateEngine.process("DoctorAppointmentCancellation", context);
            htmlContentClient = templateEngine.process("PatientAppointmentCancellation", context);
        }

        MimeMessage doctorMessage = mailSender.createMimeMessage();
        MimeMessage clientMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper doctorHelper = new MimeMessageHelper(doctorMessage, true, "UTF-8");
            MimeMessageHelper clientHelper = new MimeMessageHelper(clientMessage, true, "UTF-8");

            doctorHelper.setTo(doctor.getEmail());
            clientHelper.setTo(client.getEmail());

            doctorHelper.setSubject(subject);
            clientHelper.setSubject(subject);

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