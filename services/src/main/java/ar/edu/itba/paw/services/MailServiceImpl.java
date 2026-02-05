package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaceServices.MailService;
import ar.edu.itba.paw.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MailServiceImpl implements MailService {
    @Value("${mail.username}")
    private String from_mail;

    @Value("${app.base-url}")
    private String BASE_URL;

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
    public void sendAppointmentStatusEmail(String subject, MailDTO appointment) {

        //Doctor doctor = appointment.getDoctor();
        //Patient patient = appointment.getPatient();

        Locale patientLocale = Locale.forLanguageTag(appointment.getPatientLanguage());
        Locale doctorLocale = Locale.forLanguageTag(appointment.getDoctorLanguage());

        Context patientContext = new Context(patientLocale);
        Context doctorContext = new Context(doctorLocale);

        String doctorSubject = messageSource.getMessage(subject, null, doctorLocale);
        String patientSubject = messageSource.getMessage(subject, null, patientLocale);

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("doctorName", appointment.getDoctorName());
        templateModel.put("patientName", appointment.getPatientName());
        LocalDateTime date = appointment.getDate();
        templateModel.put("appointmentDate", date.toLocalDate().toString());
        templateModel.put("appointmentTime", date.getHour());
        templateModel.put("appointmentId", appointment.getId());
        templateModel.put("appointmentOffice", appointment.getOfficeName());
        templateModel.put("appointmentOfficeNeighborhood", appointment.getOfficeNeighborhood());
        templateModel.put("reason", (appointment.getReason() != null && !appointment.getReason().isEmpty()) ? appointment.getReason() : "-");
        templateModel.put("linkUrlPatient", BASE_URL + "/patient/dashboard/appointment-details/" + appointment.getId());
        templateModel.put("linkUrlDoctor", BASE_URL + "/doctor/dashboard/appointment-details/" + appointment.getId());
        templateModel.put("cancelLinkUrlPatient", BASE_URL + "/patient/dashboard/upcoming");
        templateModel.put("cancelLinkUrlDoctor", BASE_URL + "/doctor/dashboard/upcoming");

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

            doctorHelper.setTo(appointment.getDoctorEmail());
            patientHelper.setTo(appointment.getPatientEmail());

            doctorHelper.setSubject(doctorSubject + " #" + appointment.getId());
            patientHelper.setSubject(patientSubject + " #" + appointment.getId());

            doctorHelper.setText(htmlContentDoctor, true);
            patientHelper.setText(htmlContentPatient, true);

            doctorHelper.setFrom(from_mail);
            patientHelper.setFrom(from_mail);
            mailSender.send(doctorMessage);
            mailSender.send(patientMessage);
            LOGGER.info("Email sent to doctor: {}", appointment.getDoctorEmail());
            LOGGER.info("Email sent to patient: {}", appointment.getPatientEmail());
        } catch (MessagingException e) {
            LOGGER.error("Error creating email message", e);
        }
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
        templateModel.put("appointmentOffice", appointment.getDoctorOffice().getOfficeName());
        templateModel.put("appointmentOfficeNeighborhood", appointment.getDoctorOffice().getNeighborhood().getName());
        templateModel.put("reason", appointment.getReason() != null ? appointment.getReason() : "-");
        templateModel.put("linkUrl", BASE_URL + "/patient/dashboard/appointment-details/" + appointment.getId());


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
            mailSender.send(patientMessage);
            LOGGER.info("Email sent to patient: {}", patient.getEmail());
        } catch (MessagingException e) {
            LOGGER.error("Error creating email message", e);
        }
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
        context.setVariable("userName", user.getName() + " " + user.getLastName());

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

    @Async
    @Override
    public void sendRatingMail(Doctor doctor, Patient patient, Appointment appointment, long rating, String ratingMessage){
        Locale userLocale = Locale.forLanguageTag(doctor.getLanguage());
        Context context = new Context(userLocale);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        context.setVariable("appointmentDate", appointment.getDate().format(dateFormatter));
        context.setVariable("appointmentTime", appointment.getDate().format(timeFormatter));
        context.setVariable("appointmentOffice", appointment.getDoctorOffice().getOfficeName());
        context.setVariable("appointmentOfficeNeighborhood", appointment.getDoctorOffice().getNeighborhood().getName());
        context.setVariable("doctorName", doctor.getName() + " " + doctor.getLastName());
        context.setVariable("patientName", patient.getName() + " " + patient.getLastName());
        context.setVariable("rating", rating);
        context.setVariable("ratingMessage", ratingMessage);
        context.setVariable("doctor", doctor);
        context.setVariable("patient", patient);
        context.setVariable("appointment", appointment);
        context.setVariable("linkUrl", BASE_URL + "/doctor/dashboard/appointment-details/" + appointment.getId());


        String htmlContent = templateEngine.process("DoctorRatedNotification", context);

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(doctor.getEmail());
            helper.setSubject(messageSource.getMessage("doctor.rating.title", null, userLocale));
            helper.setText(htmlContent, true);
            helper.setFrom(from_mail);
            mailSender.send(message);
            LOGGER.info("Doctor rated notification email sent to: {}", doctor.getEmail());
        } catch (MessagingException e) {
            LOGGER.error("Error sending rated notification email to {}", doctor.getEmail(), e);
        }
    }

    @Async
    @Override
    public void sendFileUploadMail(MailDTO appointment, List<AppointmentFile> uploadedFiles) {
        Locale userLocale = Locale.forLanguageTag(appointment.getPatientLanguage());
        Context context = new Context(userLocale);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        context.setVariable("appointmentDate", appointment.getDate().format(dateFormatter));
        context.setVariable("appointmentTime", appointment.getDate().format(timeFormatter));
        context.setVariable("appointmentOffice", appointment.getOfficeName());
        context.setVariable("appointmentOfficeNeighborhood", appointment.getOfficeNeighborhood());

        context.setVariable("doctorName", appointment.getDoctorName());
        context.setVariable("patientName", appointment.getPatientName());
        context.setVariable("appointment", appointment);
        context.setVariable("linkUrl", BASE_URL + "/patient/dashboard/appointment-details/" + appointment.getId());

        String htmlContent = templateEngine.process("PatientFileUploadedNotification", context);

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(appointment.getPatientEmail());
            helper.setSubject(messageSource.getMessage("patient.file.upload.title", null, userLocale));
            helper.setText(htmlContent, true);
            helper.setFrom(from_mail);

            for (AppointmentFile file : uploadedFiles) {
                if (file.getFileData() != null && file.getFileName() != null) {
                    helper.addAttachment(file.getFileName(), new ByteArrayResource(file.getFileData()));
                }
            }
            mailSender.send(message);
            LOGGER.info("File uploaded notification email sent to: {}", appointment.getPatientEmail());
        } catch (MessagingException e) {
            LOGGER.error("Error sending file uploaded notification email to {}", appointment.getPatientEmail(), e);
        }
    }
    @Async
    @Override
    public void sendReportAddedMail(Doctor doctor, Patient patient, Appointment appointment, String report) {
        Locale userLocale = Locale.forLanguageTag(patient.getLanguage());
        Context context = new Context(userLocale);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        context.setVariable("appointmentDate", appointment.getDate().format(dateFormatter));
        context.setVariable("appointmentTime", appointment.getDate().format(timeFormatter));
        context.setVariable("appointmentOffice", appointment.getDoctorOffice().getOfficeName());
        context.setVariable("appointmentOfficeNeighborhood", appointment.getDoctorOffice().getNeighborhood().getName());
        context.setVariable("doctor", doctor);
        context.setVariable("patient", patient);
        context.setVariable("doctorName", doctor.getName() + " " + doctor.getLastName());
        context.setVariable("patientName", patient.getName() + " " + patient.getLastName());
        context.setVariable("appointment", appointment);
        context.setVariable("report", report);
        context.setVariable("linkUrl", BASE_URL + "/patient/dashboard/appointment-details/" + appointment.getId());


        String htmlContent = templateEngine.process("PatientReportAddedNotification", context);

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(patient.getEmail());
            helper.setSubject(messageSource.getMessage("patient.report.added.title", null, userLocale));
            helper.setText(htmlContent, true);
            helper.setFrom(from_mail);

            mailSender.send(message);
            LOGGER.info("Report uploaded notification email sent to: {}", patient.getEmail());
        } catch (MessagingException e) {
            LOGGER.error("Error sending report uploaded notification email to {}", patient.getEmail(), e);
        }
    }


}