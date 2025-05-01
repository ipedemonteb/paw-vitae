package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentDao appointmentDao;
    private final SpecialtyService specialtyService;
    private final MailService mailService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    @Autowired
    public AppointmentServiceImpl(AppointmentDao appointmentDao, SpecialtyService specialtyService, MailService mailService) {
        this.appointmentDao = appointmentDao;
        this.specialtyService = specialtyService;
        this.mailService = mailService;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Todos los días a las 00:00
    public void sendDailyReminders() {
        LocalDate today = LocalDate.now();
        List<Appointment> appointments = appointmentDao.getAppointmentsByDate(today);
        for (Appointment appointment : appointments) {
            mailService.sendReminderEmail(appointment);
        }
    }

    @Transactional
    @Override
    public Appointment create(long patientId, long doctorId, LocalDate date, Integer time, String reason, long specialtyId) {
        LocalDateTime localDateTime = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), time, 0, 0);
        Optional<Specialty> specialty = specialtyService.getById(specialtyId);

        Appointment appointment = appointmentDao.create(patientId, doctorId, localDateTime, reason, specialty.orElseThrow(() -> new IllegalArgumentException("Specialty not found")));
        mailService.sendAppointmentStatusEmail("email.newAppointment", appointment);

        LOGGER.debug("New appointment created: {}", appointment);

        return appointment;
    }

    @Override
    public List<Appointment> getByPatientId(long patientId) {
        return  appointmentDao.getByPatientId(patientId);
    }

    @Override
    public List<Appointment> getByDoctorId(long doctorId) {
        return appointmentDao.getByDoctorId(doctorId);
    }



    @Transactional
    @Override
    public Boolean cancelAppointment(long appointmentId,long userId) {
        Optional<Appointment> appt = getById(appointmentId);
        if (appt.isEmpty()) {
            LOGGER.debug("Appointment not found: {}", appointmentId);
            return false;
        }

        appointmentDao.cancelAppointment(appointmentId);
        appt.get().setStatus(AppointmentStatus.CANCELADO.getValue());
        mailService.sendAppointmentStatusEmail("email.cancelledAppointment", appt.get());
        LOGGER.debug("Appointment cancelled: {}", appt.get());
        return true;
    }

    @Transactional
    @Override
    public Boolean acceptAppointment(long appointmentId,long userId) {
        Optional<Appointment> appt = getById(appointmentId);
        if (appt.isEmpty()){
            LOGGER.debug("Appointment not found: {}", appointmentId);
            return false;
        }

        appointmentDao.acceptAppointment(appointmentId);
        appt.get().setStatus(AppointmentStatus.CONFIRMADO.getValue());
        mailService.sendAppointmentStatusEmail("email.acceptedAppointment", appt.get());
        LOGGER.debug("Appointment accepted: {}", appt.get());
        return true;
    }
    @Override
    public Optional<Appointment> getById(long appointmentId) {
        return appointmentDao.getById(appointmentId);
    }


    @Override
    public Page<Appointment> getPastDoctorAppointments(long doctorId, int page, int size, String dateRange, String status) {
        List<Appointment> appointments = appointmentDao.getPastDoctorAppointments(doctorId, page, size, dateRange, status);
        return new Page<>(appointments, page, size, appointmentDao.countPastDoctorAppointments(doctorId,status));
    }
    @Override
    public Page<Appointment> getFutureDoctorAppointments(long doctorId, int page, int size, String dateRange, String status) {
        List<Appointment> appointments = appointmentDao.getFutureDoctorAppointments(doctorId, page, size, dateRange, status);
        return new Page<>(appointments, page, size, appointmentDao.countFutureDoctorAppointments(doctorId,dateRange));
    }
    @Override
    public Page<Appointment> getFuturePatientAppointments(long patientId, int page, int size, String dateRange, String status) {
        List<Appointment> appointments = appointmentDao.getFuturePatientAppointments(patientId, page, size, dateRange, status);
        return new Page<>(appointments, page, size, appointmentDao.countFuturePatientAppointments(patientId,dateRange));
    }
    @Override
    public Page<Appointment> getPastPatientAppointments(long patientId, int page, int size,String dateRange, String status) {
        List<Appointment> appointments = appointmentDao.getPastPatientAppointments(patientId, page, size, dateRange, status);
        return new Page<>(appointments, page, size, appointmentDao.countPastPatientAppointments(patientId,status));
    }
}