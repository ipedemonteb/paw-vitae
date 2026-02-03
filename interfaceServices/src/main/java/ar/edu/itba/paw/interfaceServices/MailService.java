package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.*;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface MailService {

    void sendAppointmentStatusEmail(String subject, MailDTO dto);

    void sendReminderEmail(Appointment appointment);

    void sendRecoverPasswordEmail(User user, String token);

    void sendVerificationRegisterEmail(User user, String verificationLink);

    void sendRatingMail(Doctor doctor, Patient patient, Appointment appointment, long rating, String ratingMessage);

    void sendFileUploadMail(Doctor doctor, Patient patient, Appointment appointment, List<AppointmentFile> uploadedFiles);

    void sendReportAddedMail(Doctor doctor, Patient patient, Appointment appointment, String report);

    }
