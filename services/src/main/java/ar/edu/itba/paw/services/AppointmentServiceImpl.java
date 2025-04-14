package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.MailService;
import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Specialty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentDao appointmentDao;
    private final SpecialtyService specialtyService;
    private final MailService mailService;
    private final MessageSource messageSource;
    @Autowired
    public AppointmentServiceImpl(AppointmentDao appointmentDao, SpecialtyService specialtyService, MailService mailService, MessageSource messageSource) {
        this.appointmentDao = appointmentDao;
        this.specialtyService = specialtyService;
        this.mailService = mailService;
        this.messageSource = messageSource;
    }

    @Override
    public Appointment create(long clientId, long doctorId, LocalDate date, Integer time, String reason, long specialtyId) {
        LocalDateTime localDateTime = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), time, 0, 0);
        Optional<Specialty> specialty = specialtyService.getById(specialtyId);

        Appointment appointment = appointmentDao.create(clientId, doctorId, localDateTime, reason, specialty.orElseThrow(() -> new IllegalArgumentException("Specialty not found")));

        try {
            mailService.sendEmail(messageSource.getMessage("emil.newAppointment", null, LocaleContextHolder.getLocale()), appointment, appointment.getDoctorId(), appointment.getClientId());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return appointment;
    }

    @Override
    public Optional<List<Appointment>> getByClientId(long clientId) {
        return appointmentDao.getByClientId(clientId);
    }

    @Override
    public Optional<List<Appointment>> getByDoctorId(long doctorId) {
        return appointmentDao.getByDoctorId(doctorId);
    }

    @Override
    public Set<Integer> getBookedHoursByDoctorAndDate(long doctorId, LocalDate date) {
        Optional<List<Appointment>> appointments = getByDoctorId(doctorId);

        return appointments.map(appointmentList -> appointmentList.stream()
                .filter(appointment -> appointment.getDate().toLocalDate().equals(date))
                .map(appointment -> appointment.getDate().getHour())
                .collect(Collectors.toSet())).orElse(Collections.emptySet());

    }

    @Override
    public Optional<List<Appointment>> getAllFutureAppointments(long doctorId) {
        return appointmentDao.getAllFutureAppointments(doctorId);
    }

    @Override
    public Optional<String> getFutureAppointmentsPerDate(long doctorId) {
        Optional<List<Appointment>> futureAppointments = getAllFutureAppointments(doctorId);

        if (futureAppointments.isPresent()) {
            Map<LocalDate, List<Integer>> appointmentsByDate = new HashMap<>();
            for (Appointment appointment : futureAppointments.get()) {
                LocalDate date = appointment.getDate().toLocalDate();
                appointmentsByDate.computeIfAbsent(date, k -> new ArrayList<>()).add(appointment.getDate().getHour());
            }

            StringBuilder result = new StringBuilder();
            result.append("[");
            for (Map.Entry<LocalDate, List<Integer>> entry : appointmentsByDate.entrySet()) {
                LocalDate date = entry.getKey();
                List<Integer> appointments = entry.getValue();
                result.append("{").append("\"date\": ").append("\"").append(date).append("\"").append(", ").append("\"hours\": ").append(Arrays.toString(appointments.toArray())).append("},");
            }
            if (!appointmentsByDate.isEmpty()) {
                result.deleteCharAt(result.length() - 1); // Remove the trailing comma
            }
            result.append("]");
            return Optional.of(result.toString());
        } else {
            return Optional.empty();
        }
    }
}