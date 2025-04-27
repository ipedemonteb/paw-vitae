package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaceServices.MailService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.Patient;
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
    private final static String from_mail = "vitaepaw@gmail.com";
    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final MessageSource messageSource;

    @Autowired
    public MailServiceImpl(final JavaMailSender mailSender, final TemplateEngine templateEngine, final MessageSource messageSource) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.messageSource = messageSource;
    }

    @Async
    @Override
    public void sendAppointmentStatusEmail(String subject, Appointment appointment) {

        Doctor doctor = appointment.getDoctor();
        Patient patient = appointment.getPatient();

        Locale patientLocale = Locale.forLanguageTag(patient.getLanguage());
        Locale doctorLocale = Locale.forLanguageTag(doctor.getLanguage());

        Context patientContext = new Context(patientLocale);
        Context doctorContext = new Context(doctorLocale);

        String doctorSubject = messageSource.getMessage(subject, null, doctorLocale);
        String patientSubject = messageSource.getMessage(subject, null, patientLocale);

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("doctorName", doctor.getName() + " " + doctor.getLastName());
        templateModel.put("patientName", patient.getName() + " " + patient.getLastName());
        LocalDateTime date = appointment.getDate();
        templateModel.put("appointmentDate", date.toLocalDate().toString());
        templateModel.put("appointmentTime", date.getHour());
        //@TODO: Check
        templateModel.put("reason", appointment.getReason() != null ? appointment.getReason() : "-");

        patientContext.setVariables(templateModel);
        doctorContext.setVariables(templateModel);
        String htmlContentDoctor;
        String htmlContentPatient;

        System.out.println("appointment.getStatus() = " + appointment.getStatus());

        if (appointment.getStatus().equals(AppointmentStatus.PENDIENTE.getValue())) {
            htmlContentDoctor = templateEngine.process("DoctorAppointmentRequest", doctorContext);
            htmlContentPatient = templateEngine.process("PatientAppointmentRequest", patientContext);
        } else if (appointment.getStatus().equals(AppointmentStatus.CONFIRMADO.getValue())) {
            htmlContentDoctor = templateEngine.process("DoctorAppointmentConfirmation", doctorContext);
            htmlContentPatient = templateEngine.process("PatientAppointmentConfirmation", patientContext);
        } else {
            htmlContentDoctor = templateEngine.process("DoctorAppointmentCancellation", doctorContext);
            htmlContentPatient = templateEngine.process("PatientAppointmentCancellation", patientContext);
        }

        MimeMessage doctorMessage = mailSender.createMimeMessage();
        MimeMessage patientMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper doctorHelper = new MimeMessageHelper(doctorMessage, true, "UTF-8");
            MimeMessageHelper patientHelper = new MimeMessageHelper(patientMessage, true, "UTF-8");

            doctorHelper.setTo(doctor.getEmail());
            patientHelper.setTo(patient.getEmail());

            doctorHelper.setSubject(doctorSubject);
            patientHelper.setSubject(patientSubject);

            doctorHelper.setText(htmlContentDoctor, true);
            patientHelper.setText(htmlContentPatient, true);

            doctorHelper.setFrom(from_mail);
            patientHelper.setFrom(from_mail);
        } catch (MessagingException e) {
            LOGGER.debug("Error creating email message", e);
        }

        mailSender.send(doctorMessage);
        mailSender.send(patientMessage);
        LOGGER.debug("Email sent to doctor: {}", doctor.getEmail());
        LOGGER.debug("Email sent to patient: {}", patient.getEmail());
    }

    @Async
    @Override
    public void sendReminderEmail(String subject, Appointment appointment) {

        Doctor doctor = appointment.getDoctor();
        Patient patient = appointment.getPatient();

        Locale patientLocale = Locale.forLanguageTag(patient.getLanguage());

        Context patientContext = new Context(patientLocale);

        String patientSubject = messageSource.getMessage(subject, null, patientLocale);

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("doctorName", doctor.getName() + " " + doctor.getLastName());
        templateModel.put("patientName", patient.getName() + " " + patient.getLastName());
        LocalDateTime date = appointment.getDate();
        templateModel.put("appointmentDate", date.toLocalDate().toString());
        templateModel.put("appointmentTime", date.getHour());
        templateModel.put("reason", appointment.getReason() != null ? appointment.getReason() : "-");

        patientContext.setVariables(templateModel);
        String htmlContentPatient;

        System.out.println("appointment.getStatus() = " + appointment.getStatus());
        htmlContentPatient = templateEngine.process("PatientAppointmentReminder", patientContext);
        MimeMessage patientMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper patientHelper = new MimeMessageHelper(patientMessage, true, "UTF-8");
            patientHelper.setTo(patient.getEmail());
            patientHelper.setSubject(patientSubject);
            patientHelper.setText(htmlContentPatient, true);
            patientHelper.setFrom(from_mail);
        } catch (MessagingException e) {
            LOGGER.debug("Error creating email message", e);
        }
        mailSender.send(patientMessage);
        LOGGER.debug("Email sent to patient: {}", patient.getEmail());
    }
}