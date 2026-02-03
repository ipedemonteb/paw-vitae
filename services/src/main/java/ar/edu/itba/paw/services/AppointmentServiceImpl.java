package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final AvailabilitySlotsService availabilitySlotsService;

    @Autowired
    public AppointmentServiceImpl(AppointmentDao appointmentDao, SpecialtyService specialtyService, MailService mailService,
                                  DoctorService doctorService, PatientService patientService, DoctorOfficeService doctorOfficeService, AvailabilitySlotsService availabilitySlotsService) {
        this.appointmentDao = appointmentDao;
        this.specialtyService = specialtyService;
        this.mailService = mailService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.doctorOfficeService = doctorOfficeService;
        this.availabilitySlotsService = availabilitySlotsService;
    }

    @Transactional(readOnly = true)
    @Scheduled(cron = "0 0 0 * * ?")
    @Async
    public void sendDailyReminders() {
        LocalDate today = LocalDate.now();
        List<Appointment> appointments = appointmentDao.getAppointmentsByDate(today);
        for (Appointment appointment : appointments) {
            mailService.sendReminderEmail(appointment);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Async
    @Transactional
    public void revokeHistoryPermissionForOldAppointments() {
        LocalDateTime oneWeekAgo = LocalDateTime.now(ZoneId.systemDefault()).minusWeeks(1);
        List<Appointment> oldAppointments = appointmentDao.getAppointmentsWithHistoryAllowedBefore(oneWeekAgo);
        for (Appointment appointment : oldAppointments) {
            appointment.setAllowFullHistory(false);
        }
        LOGGER.info("Revoked history permission for {} appointments older than one week", oldAppointments.size());
    }

    @Transactional
    @Override
    public Appointment create(long patientId, long doctorId, Long slotId, String reason, long specialtyId, long officeId, boolean allowFullHistory) {
        LOGGER.debug("Creating appointment for patientId: {}, doctorId: {}, reason: {}, specialtyId: {}", patientId, doctorId, reason, specialtyId);
        AvailabilitySlots slot = availabilitySlotsService.getById(slotId).orElseThrow(NoSuchElementException::new);
        LocalDate date = slot.getSlotDate();
        int time = slot.getStartTime().getHour();
        LocalDateTime localDateTime = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), time, 0, 0);
        Optional<Specialty> specialty = specialtyService.getById(specialtyId);
        DoctorOffice doctorOffice = doctorOfficeService.getById(officeId).orElseThrow(DoctorOfficeNotFoundException::new);
        Appointment appointment = appointmentDao.create(localDateTime, AppointmentStatus.CONFIRMADO.getValue(), reason, specialty.orElseThrow(SpecialtyNotFoundException::new ),doctorService.getById(doctorId).orElseThrow(UserNotFoundException::new) , patientService.getById(patientId).orElseThrow(UserNotFoundException::new), "", doctorOffice, allowFullHistory);
        availabilitySlotsService.setAvailabilitySlotUnavailable(slotId);
        MailDTO dto = new MailDTO(appointment);
        mailService.sendAppointmentStatusEmail("email.newAppointment", dto);

        LOGGER.info("New appointment created with id: {}", appointment.getId());

        return appointment;
    }

    @Override
    @Transactional
    @Async
    @Scheduled(cron = "0 0 * * * *")
    public void completeAppointments() {
        LOGGER.debug("Completing past appointments that are confirmed and have passed the date");
        List<Appointment> appointments = appointmentDao.getPastConfirmedAppointments();
        for (Appointment appointment : appointments) {
            LOGGER.debug("Completing appointment with id: {}", appointment.getId());
            appointment.setStatus(AppointmentStatus.COMPLETO.getValue());
        }
    }

    @Transactional
    @Override
    public Boolean cancelAppointment(long appointmentId, long userId) {
        LOGGER.debug("Attempting to cancel appointment with id: {} by user with id: {}", appointmentId, userId);
        Optional<Appointment> appt = getById(appointmentId);
        if (appt.isEmpty()) {
            throw new AppointmentNotFoundException();
        }

        Appointment appointment = appt.get();
        if (!appointment.getCancellable()) {
            throw new CancellableException();
        }

        appointment.setStatus(AppointmentStatus.CANCELADO.getValue());
        MailDTO dto = new MailDTO(appointment);
        mailService.sendAppointmentStatusEmail("email.cancelledAppointment", dto);
        LOGGER.info("Appointment cancelled: {}", appointmentId);
        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Appointment> getById(long appointmentId) {
        LOGGER.debug("Getting appointment with id: {}", appointmentId);
        Optional<Appointment> appointment = appointmentDao.getById(appointmentId);
        appointment.ifPresent(a -> {
            Boolean isCancellable = LocalDateTime.now(ZoneId.systemDefault()).plusHours(2).isBefore(a.getDate());
            a.setCancellable(isCancellable);
        });
        return appointment;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Appointment> getAppointments(long userId, boolean isFuture, int page, int size, String filter, String sort) {
        LOGGER.debug("Getting appointments for userId: {}, isFuture: {}, page: {}, size: {}, filter: {}, sort: {}", userId, isFuture, page, size, filter, sort);
        if (page < 1) {
            page = 1;
        }
        List<Appointment> appointments = appointmentDao.getAppointments(userId, isFuture, page, size, filter, sort);
        appointments.forEach(a -> {
            Boolean isCancellable = LocalDateTime.now(ZoneId.systemDefault()).plusHours(2).isBefore(a.getDate());
            a.setCancellable(isCancellable);
        });
        return new Page<>(appointments, page, size, appointmentDao.countAppointments(userId, isFuture, filter));
    }

    @Transactional
    @Override
    public Optional<Long> updateAppointmentReport(long appointmentId, String report) {
        LOGGER.debug("Updating appointment report for appointmentId: {}, report: {}", appointmentId, report);
        if (report == null ) {
            return Optional.empty();
        }
        Optional<Appointment> appointment = getById(appointmentId);
        if (appointment.isPresent()) {
            appointment.get().setReport(report);
            mailService.sendReportAddedMail(appointment.get().getDoctor(), appointment.get().getPatient(), appointment.get(), report);
            LOGGER.info("Report added to appointment: {}", appointment.get());
            return Optional.of(appointment.get().getId());
        } else {
            LOGGER.info("Appointment not found: {}", appointmentId);
        }
        return Optional.empty();
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
            Boolean isCancellable = LocalDateTime.now(ZoneId.systemDefault()).plusHours(2).isBefore(a.getDate());
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
        Appointment appointment = getById(appointmentId).orElseThrow(AppointmentNotFoundException::new);
        return appointment.getPatient();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Appointment> getAppointmentsForPatientWithFilesOrReport(long patientId, int page, int pageSize, String direction) {
        List<Appointment> appointments = appointmentDao.getAppointmentsByPatientWithFilesOrReport(patientId, page, pageSize, direction);
        int total = appointmentDao.countAppointmentsByPatientWithFilesOrReport(patientId);
        return new Page<>(appointments, page, pageSize, total);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasFullMedicalHistoryEnabled(long patientId, long doctorId) {
        LOGGER.debug("Checking if full medical history is enabled for patientId: {}, doctorId: {}", patientId, doctorId);
        return appointmentDao.hasFullMedicalHistoryEnabled(patientId, doctorId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean officeHasAppointments(long officeId) {
        return appointmentDao.officeHasAppointments(officeId);
    }


    @Transactional(readOnly = true)
    @Override
    public boolean hasAppointmentWithPatient(long doctorId, long patientId) {
        LOGGER.debug("Checking if doctorId: {} has appointment with patientId: {}", doctorId, patientId);
        return appointmentDao.hasAppointmentWithPatient(patientId, doctorId);
    }

}