package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    public Optional<List<Appointment>> getAllFutureAppointments(long doctorId) {
        return appointmentDao.getAllFutureAppointments(Collections.singletonList(doctorId));
    }

    @Transactional
    @Override
    public Boolean cancelAppointment(long appointmentId,long userId) {
        Optional<Appointment> appt = getById(appointmentId);
        if (appt.isEmpty()) {
            return false;}

        if (appt.get().getDoctor().getId() != userId && appt.get().getClient().getId() != userId) {
            return false;
        }
        appointmentDao.cancelApointment(appointmentId);
        mailService.sendAppointmentStatusEmail("email.cancelledAppointment", appt.get());
        return true;
    }

    @Transactional
    @Override
    public Boolean acceptAppointment(long appointmentId,long userId) {
        Optional<Appointment> appt = getById(appointmentId);
        if (appt.isEmpty()) return false;

        if (appt.get().getDoctor().getId() != userId) {
            return false;
        }
        appointmentDao.acceptAppointment(appointmentId);
        mailService.sendAppointmentStatusEmail("email.acceptedAppointment", appt.get());
        return true;
    }
    @Override
    public Optional<Appointment> getById(long appointmentId) {
        return appointmentDao.getById(appointmentId);
    }

    @Override
    public Optional<Map<Long, List<Appointment>>> getAllFutureAppointments(List<Doctor> doctors) {
        Map<Long, List<Appointment>> futureAppointmentsMap = new HashMap<>();
        List<Long> doctorIds = doctors.stream().map(Doctor::getId).collect(Collectors.toList());
        Optional<List<Appointment>> appointments = appointmentDao.getAllFutureAppointments(doctorIds);
        if (appointments.isPresent()) {
            for (Appointment appointment : appointments.get()) {
                futureAppointmentsMap.computeIfAbsent(appointment.getDoctorId(), k -> new ArrayList<>()).add(appointment);
            }
        }
        return Optional.of(futureAppointmentsMap);
    }

    @Override
    public Map<Boolean, List<Appointment>> getByDoctorIdPartitionedByDate(long doctorId) {
        return  appointmentDao.getByDoctorId(doctorId).orElseThrow(() -> new IllegalArgumentException("Doctor not found")).stream()
                .collect(Collectors.partitioningBy(appointment -> appointment.getDate().isBefore(LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")))));
    }

    @Override
    public Map<Boolean, List<Appointment>> getByClientIdPartitionedByDate(long clientId) {
        return appointmentDao.getByClientId(clientId).orElseThrow(() -> new IllegalArgumentException("Client not found")).stream()
                .collect(Collectors.partitioningBy(appointment -> appointment.getDate().isBefore(LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")))));
    }
}