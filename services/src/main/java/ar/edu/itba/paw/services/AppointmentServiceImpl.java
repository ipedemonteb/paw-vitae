package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentServiceImpl.class);
    private final AppointmentDao appointmentDao;
    private final SpecialtyService specialtyService;
    private final MailService mailService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final DoctorOfficeService doctorOfficeService;

    @Autowired
    public AppointmentServiceImpl(AppointmentDao appointmentDao, SpecialtyService specialtyService, MailService mailService,
                                  DoctorService doctorService, PatientService patientService, DoctorOfficeService doctorOfficeService) {
        this.appointmentDao = appointmentDao;
        this.specialtyService = specialtyService;
        this.mailService = mailService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.doctorOfficeService = doctorOfficeService;
    }

    @Transactional(readOnly = true)
    @Scheduled(cron = "0 0 0 * * ?") // Todos los días a las 00:00
    @Async
    public void sendDailyReminders() {
        LocalDate today = LocalDate.now();
        List<Appointment> appointments = appointmentDao.getAppointmentsByDate(today);
        for (Appointment appointment : appointments) {
            mailService.sendReminderEmail(appointment);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Async// Every day at 01:00 AM
    @Transactional
    public void revokeHistoryPermissionForOldAppointments() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        List<Appointment> oldAppointments = appointmentDao.getAppointmentsWithHistoryAllowedBefore(oneWeekAgo);
        for (Appointment appointment : oldAppointments) {
            appointment.setAllowFullHistory(false);
        }
        LOGGER.info("Revoked history permission for {} appointments older than one week", oldAppointments.size());
    }

    @Transactional
    @Override
    public Appointment create(long patientId, long doctorId, LocalDate date, Integer time, String reason, long specialtyId, long officeId, boolean allowFullHistory) {
        LOGGER.debug("Creating appointment for patientId: {}, doctorId: {}, date: {}, time: {}, reason: {}, specialtyId: {}", patientId, doctorId, date, time, reason, specialtyId);

        LocalDateTime localDateTime = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), time, 0, 0);
        Optional<Specialty> specialty = specialtyService.getById(specialtyId);

        DoctorOffice doctorOffice = doctorOfficeService.getById(officeId).orElseThrow(() -> new IllegalArgumentException("Doctor office not found")); // TODO: MAKE CUSTOM EXCEPTION

        //Appointment appointment = appointmentDao.create(patientId, doctorId, localDateTime, reason, specialty.orElseThrow(() -> new IllegalArgumentException("Specialty not found")));
        Appointment appointment = appointmentDao.create(localDateTime, AppointmentStatus.CONFIRMADO.getValue(), reason, specialty.orElseThrow(() -> new IllegalArgumentException("Specialty not found")),doctorService.getById(doctorId).orElseThrow(UserNotFoundException::new) , patientService.getById(patientId).orElseThrow(UserNotFoundException::new), "", doctorOffice, allowFullHistory);
        mailService.sendAppointmentStatusEmail("email.newAppointment", appointment);

        LOGGER.info("New appointment created with id: {}", appointment.getId());

        return appointment;
    }

    @Override
    @Transactional
    @Async
    @Scheduled(cron = "0 0 * * * *")
    public void completeAppointments() {
        List<Appointment> appointments= appointmentDao.getPastConfirmedAppointments();
        for (Appointment appointment : appointments) {
            appointment.setStatus(AppointmentStatus.COMPLETO.getValue());
        }
    }

    @Transactional
    @Override
    public Boolean cancelAppointment(long appointmentId, long userId) {
        LOGGER.debug("Attempting to cancel appointment with id: {} by user with id: {}", appointmentId, userId);
        Optional<Appointment> appt = getById(appointmentId);
        if (appt.isEmpty()) {
            LOGGER.warn("Appointment not found: {}", appointmentId);
            return false;
        }

        Appointment appointment = appt.get();
        if (!appointment.getCancellable()) {
            LOGGER.warn("Cannot cancel appointment less than two hours in advance: {}", appointmentId);
            return false;
        }

        appointment.setStatus(AppointmentStatus.CANCELADO.getValue());
        mailService.sendAppointmentStatusEmail("email.cancelledAppointment", appointment);
        LOGGER.info("Appointment cancelled: {}", appointmentId);
        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Appointment> getById(long appointmentId) {
        LOGGER.debug("Getting appointment with id: {}", appointmentId);
        Optional<Appointment> appointment = appointmentDao.getById(appointmentId);
        appointment.ifPresent(a -> {
            Boolean isCancellable = LocalDateTime.now().plusHours(2).isBefore(a.getDate());
            a.setCancellable(isCancellable);
        });
        return appointment;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Appointment> getAppointments(long userId, boolean isFuture, int page, int size, String filter) {
        LOGGER.debug("Getting appointments for userId: {}, isFuture: {}, page: {}, size: {}, filter: {}", userId, isFuture, page, size, filter);
        if (page < 1) {
            page = 1;
        }
        List<Appointment> appointments = appointmentDao.getAppointments(userId, isFuture, page, size, filter);
        appointments.forEach(a -> {
            Boolean isCancellable = LocalDateTime.now().plusHours(2).isBefore(a.getDate());
            a.setCancellable(isCancellable);
        });
        return new Page<>(appointments, page, size, appointmentDao.countAppointments(userId, isFuture, filter));
    }

    @Transactional
    @Override
    public void updateAppointmentReport(long appointmentId, String report) {
        LOGGER.debug("Updating appointment report for appointmentId: {}, report: {}", appointmentId, report);
        if (report == null) {
            return;
        }
        Optional<Appointment> appointment = getById(appointmentId);
        if (appointment.isPresent()) {
            appointment.get().setReport(report);
            mailService.sendReportAddedMail(appointment.get().getDoctor(), appointment.get().getPatient(), appointment.get(), report);
            LOGGER.info("Report added to appointment: {}", appointment.get());
        } else {
            LOGGER.info("Appointment not found: {}", appointmentId);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Appointment> getAppointmentByUserAndDate(long userId, LocalDate date, Integer time) {
        LOGGER.debug("Getting appointments for userId: {}, date: {}, time: {}", userId, date, time);
        if (date == null || time == null) {
            return Collections.emptyList();
        }
        List<Appointment> appointments = appointmentDao.getAppointmentsByUserAndDate(userId, date, time);
        appointments.forEach(a -> {
            Boolean isCancellable = LocalDateTime.now().plusHours(2).isBefore(a.getDate());
            a.setCancellable(isCancellable);
        });
        return appointments;
    }

    @Transactional(readOnly = true)
    @Override
    public Map<LocalDate, List<Integer>> getFutureAppointmentsByUserAndDate(long userId) {
        LOGGER.debug("Getting future appointments for userId: {}", userId);
        List<Appointment> appointments = appointmentDao.getFutureAppointmentsByUser(userId);
        Map<LocalDate, List<Integer>> appointmentsByDate = new HashMap<>();
        for (Appointment appointment : appointments) {
            LocalDate date = appointment.getDate().toLocalDate();
            appointmentsByDate.computeIfAbsent(date, k -> new ArrayList<>()).add(appointment.getDate().getHour());
        }
        return appointmentsByDate;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean hasHistoryAllowedByAppointmentId(long appointmentId, long doctorId) {
        LOGGER.debug("Checking history permission for appointment with id: {}", appointmentId);
        Optional<Appointment> appointment = appointmentDao.getById(appointmentId);
        return appointment.isPresent() && appointment.get().isAllowFullHistory() && appointment.get().getDoctor().getId() == doctorId;
    }

    @Transactional(readOnly = true)
    @Override
    public Patient getPatientByAppointmentId(long appointmentId) {
        LOGGER.debug("Getting patient by appointmentId: {}", appointmentId);
        Appointment appointment = getById(appointmentId).orElseThrow(() -> new IllegalArgumentException("Appointment not found with id: " + appointmentId));
        return appointment.getPatient();
    }
}