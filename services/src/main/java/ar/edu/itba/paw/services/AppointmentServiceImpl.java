package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ClientService clientService;
    private final DoctorService doctorService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    @Autowired
    public AppointmentServiceImpl(AppointmentDao appointmentDao, SpecialtyService specialtyService, MailService mailService, MessageSource messageSource, ClientService clientService,DoctorService doctorService) {
        this.appointmentDao = appointmentDao;
        this.specialtyService = specialtyService;
        this.mailService = mailService;
        this.messageSource = messageSource;
        this.doctorService = doctorService;
        this.clientService = clientService;
    }

    @Transactional
    @Override
    public Appointment create(long clientId, long doctorId, LocalDate date, Integer time, String reason, long specialtyId) {
        LocalDateTime localDateTime = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), time, 0, 0);
        Optional<Specialty> specialty = specialtyService.getById(specialtyId);

        Appointment appointment = appointmentDao.create(clientId, doctorId, localDateTime, reason, specialty.orElseThrow(() -> new IllegalArgumentException("Specialty not found")));
        mailService.sendAppointmentStatusEmail("email.newAppointment", appointment);

        return appointment;
    }

    @Override
    public Optional<List<Appointment>> getByClientId(long clientId) {
        return  appointmentDao.getByClientId(clientId);
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

    @Transactional
    @Override
    public void cancelAppointment(long appointmentId) {
        appointmentDao.cancelApointment(appointmentId);
        Appointment appointment = getById(appointmentId).orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        mailService.sendAppointmentStatusEmail("email.cancelledAppointment", appointment);
    }

    @Transactional
    @Override
    public void acceptAppointment(long appointmentId) {
        appointmentDao.acceptAppointment(appointmentId);
        Appointment appointment = getById(appointmentId).orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        mailService.sendAppointmentStatusEmail("email.acceptedAppointment", appointment);
    }
    @Override
    public Optional<Appointment> getById(long appointmentId) {
        return appointmentDao.getById(appointmentId);
    }
}