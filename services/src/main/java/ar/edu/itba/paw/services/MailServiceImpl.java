package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaceServices.ClientService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.MailService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final MessageSource messageSource;
    private final static String from_mail = "vitaepaw@gmail.com";

    private final DoctorService doctorService;
    private final ClientService clientService;

    @Autowired
    public MailServiceImpl(final JavaMailSender mailSender, final TemplateEngine templateEngine, final MessageSource messageSource, final DoctorService doctorService, final ClientService clientService) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.messageSource = messageSource;
        this.doctorService = doctorService;
        this.clientService = clientService;
    }

//    @Override
//    public void sendEmail(String subject, Appointment appointment, long doctorId, long clientId) throws MessagingException{
//
//        Optional<Doctor> doctor = doctorService.getById(doctorId);
//        Optional<Client> client = clientService.getById(clientId);
//
//        if (doctor.isEmpty() || client.isEmpty()) {
//            throw new IllegalArgumentException("Doctor or Client not found");
//        }
//
//        Context context = new Context();
//        Map<String, Object> templateModel = new HashMap<>();
//        templateModel.put("doctorName", doctor.get().getName() + " " + doctor.get().getLastName());
//        templateModel.put("patientName", client.get().getName() + " " + client.get().getLastName());
//        LocalDateTime date = appointment.getDate();
//        templateModel.put("appointmentDate", date.toLocalDate().toString());
//        templateModel.put("appointmentTime", date.getHour());
//        templateModel.put("reason", appointment.getReason() != null ? appointment.getReason() : messageSource.getMessage("email.emptyReason", null, LocaleContextHolder.getLocale()));
//
//        context.setVariables(templateModel);
//        String htmlContent = templateEngine.process("email", context);
//
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//        helper.setTo(doctor.get().getEmail());
//        helper.setSubject(subject);
//        helper.setText(htmlContent, true);
//        helper.setFrom(from_mail);
//
//        mailSender.send(message);
//    }


    @Override
    public void sendEmail(String subject, Map<String, Object> args, String toMail, String template) throws MessagingException {
        Context context = new Context();
        context.setVariables(args);
        String htmlContent = templateEngine.process(template, context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(toMail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.setFrom(from_mail);

        mailSender.send(message);
    }
}