package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaceServices.MailService;
import ar.edu.itba.paw.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${mail.username}")
    private static String from_mail;
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
        templateModel.put("appointmentId", appointment.getId());
        templateModel.put("reason", (appointment.getReason() != null && !appointment.getReason().isEmpty()) ? appointment.getReason() : "-");
        patientContext.setVariables(templateModel);
        doctorContext.setVariables(templateModel);
        String htmlContentDoctor;
        String htmlContentPatient;

        if (appointment.getStatus().equals(AppointmentStatus.CONFIRMADO.getValue())) {
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
            LOGGER.error("Error creating email message", e);
        }

        mailSender.send(doctorMessage);
        mailSender.send(patientMessage);
        LOGGER.info("Email sent to doctor: {}", doctor.getEmail());
        LOGGER.info("Email sent to patient: {}", patient.getEmail());
    }

    @Async
    @Override
    public void sendReminderEmail(Appointment appointment) {

        Doctor doctor = appointment.getDoctor();
        Patient patient = appointment.getPatient();

        Locale patientLocale = Locale.forLanguageTag(patient.getLanguage());

        Context patientContext = new Context(patientLocale);

        String patientSubject = messageSource.getMessage("email.reminder", null, patientLocale);

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("doctorName", doctor.getName() + " " + doctor.getLastName());
        templateModel.put("patientName", patient.getName() + " " + patient.getLastName());
        LocalDateTime date = appointment.getDate();
        templateModel.put("appointmentDate", date.toLocalDate().toString());
        templateModel.put("appointmentTime", date.getHour());
        templateModel.put("reason", appointment.getReason() != null ? appointment.getReason() : "-");

        patientContext.setVariables(templateModel);
        String htmlContentPatient;

        htmlContentPatient = templateEngine.process("PatientAppointmentReminder", patientContext);
        MimeMessage patientMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper patientHelper = new MimeMessageHelper(patientMessage, true, "UTF-8");
            patientHelper.setTo(patient.getEmail());
            patientHelper.setSubject(patientSubject);
            patientHelper.setText(htmlContentPatient, true);
            patientHelper.setFrom(from_mail);
        } catch (MessagingException e) {
            LOGGER.error("Error creating email message", e);
        }
        mailSender.send(patientMessage);
        LOGGER.info("Email sent to patient: {}", patient.getEmail());
    }

    @Async
    @Override
    public void sendRecoverPasswordEmail(User user, String resetLink) {

        Locale userLocale = Locale.forLanguageTag(user.getLanguage());


        Context context = new Context(userLocale);
        context.setVariable("resetUrl", resetLink);
        context.setVariable("userName", user.getName() + " " + user.getLastName());
        context.setVariable("expiryHours", 1);

        String htmlContent = templateEngine.process("RecoverPassword", context);

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(user.getEmail());
            helper.setSubject(messageSource.getMessage("recover.password.email.subject", null, userLocale));
            helper.setText(htmlContent, true);
            helper.setFrom(from_mail);

            mailSender.send(message);
            LOGGER.info("Recovery email sent to: {}", user.getEmail());
        } catch (MessagingException e) {
            LOGGER.error("Error sending password recovery email to {}", user.getEmail(), e);
        }
    }

    @Async
    @Override
    public void sendVerificationRegisterEmail(User user, String verificationLink) {
        Locale userLocale = Locale.forLanguageTag(user.getLanguage());
        Context context = new Context(userLocale);
        context.setVariable("verificationUrl", verificationLink);
        context.setVariable("userName", user.getName() + " " + user.getLastName()); // Replace with actual name if available

        String htmlContent = templateEngine.process("VerificationEmail", context);

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(user.getEmail());
            helper.setSubject(messageSource.getMessage("verification.register.email.title", null, userLocale));
            helper.setText(htmlContent, true);
            helper.setFrom(from_mail);

            mailSender.send(message);
            LOGGER.info("Verification email sent to: {}", user.getEmail());
        } catch (MessagingException e) {
            LOGGER.error("Error sending verification email to {}", user.getEmail(), e);
        }
    }

}