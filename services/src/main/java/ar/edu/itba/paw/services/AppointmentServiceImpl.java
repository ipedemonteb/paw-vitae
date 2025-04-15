package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Specialty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ClientService clientService;
    private final DoctorService doctorService;
    @Autowired
    public AppointmentServiceImpl(AppointmentDao appointmentDao, SpecialtyService specialtyService, MailService mailService, MessageSource messageSource, ClientService clientService,DoctorService doctorService) {
        this.appointmentDao = appointmentDao;
        this.specialtyService = specialtyService;
        this.mailService = mailService;
        this.messageSource = messageSource;
        this.doctorService = doctorService;
        this.clientService = clientService;
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

    @Override
    public Map<Appointment, Client> getForDoctor(long doctorId) {
        Optional<List<Appointment>> appointments = appointmentDao.getByDoctorId(doctorId);
        List<Client> clients = clientService.getByAppointments(appointments.orElse(Collections.emptyList()));
        Map<Appointment, Client> appointmentClientMap = new HashMap<>();

        if (appointments.isPresent()) {
            for (Appointment appointment : appointments.get()) {
                appointmentClientMap.put(appointment, clients.stream().filter(c -> c.getId() == appointment.getClientId()).findFirst().orElseThrow(() -> new IllegalArgumentException("Client not found")));
            }
        }

        return appointmentClientMap;
    }

    @Override
    public Map<Appointment, Doctor> getForClient(long clientId) {
        Optional<List<Appointment>> appointments = appointmentDao.getByClientId(clientId);
        List<Doctor> doctors = doctorService.getByAppointments(appointments.orElse(Collections.emptyList()));
        Map<Appointment,Doctor> appointmentDoctorMap = new HashMap<>();
        if (appointments.isPresent()) {
            for (Appointment appointment : appointments.get()) {
                appointmentDoctorMap.put(appointment, doctors.stream().filter(d -> d.getId() == appointment.getDoctorId()).findFirst().orElseThrow(() -> new IllegalArgumentException("Doctor not found")));
            }
        }
        return appointmentDoctorMap;
    }
    @Transactional
    @Override
    public void cancelAppointment(long appointmentId) {
        appointmentDao.cancelApointment(appointmentId);
    }

    @Transactional
    @Override
    public void acceptAppointment(long appointmentId) {
        appointmentDao.acceptAppointment(appointmentId);
    }
    @Override
    public Optional<Appointment> getById(long appointmentId) {
        return appointmentDao.getById(appointmentId);
    }
}