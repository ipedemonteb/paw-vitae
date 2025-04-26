package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    @Autowired
    public AppointmentServiceImpl(AppointmentDao appointmentDao, SpecialtyService specialtyService, MailService mailService) {
        this.appointmentDao = appointmentDao;
        this.specialtyService = specialtyService;
        this.mailService = mailService;
    }

    @Transactional
    @Override
    public Appointment create(long patientId, long doctorId, LocalDate date, Integer time, String reason, long specialtyId) {
        LocalDateTime localDateTime = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), time, 0, 0);
        Optional<Specialty> specialty = specialtyService.getById(specialtyId);

        Appointment appointment = appointmentDao.create(patientId, doctorId, localDateTime, reason, specialty.orElseThrow(() -> new IllegalArgumentException("Specialty not found")));
        mailService.sendAppointmentStatusEmail("email.newAppointment", appointment);

        return appointment;
    }

    @Override
    public Optional<List<Appointment>> getByPatientId(long patientId) {
        return  appointmentDao.getByPatientId(patientId);
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

        if (appt.get().getDoctor().getId() != userId && appt.get().getPatient().getId() != userId) {
            return false;
        }
        appointmentDao.cancelApointment(appointmentId);
        appt.get().setStatus(AppointmentStatus.CANCELADO.getValue());
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
        appt.get().setStatus(AppointmentStatus.CONFIRMADO.getValue());
        mailService.sendAppointmentStatusEmail("email.acceptedAppointment", appt.get());
        return true;
    }
    @Override
    public Optional<Appointment> getById(long appointmentId) {
        return appointmentDao.getById(appointmentId);
    }

    @Override
    public Map<Boolean, List<Appointment>> getByDoctorIdPartitionedByDate(long doctorId) {
        return  appointmentDao.getByDoctorId(doctorId).orElse(Collections.emptyList()).stream()
                .collect(Collectors.partitioningBy(appointment -> appointment.getDate().isBefore(LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")))));
    }

    @Override
    public Map<Boolean, List<Appointment>> getByPatientIdPartitionedByDate(long patientId) {
        return appointmentDao.getByPatientId(patientId).orElseThrow(() -> new IllegalArgumentException("Patient not found")).stream()
                .collect(Collectors.partitioningBy(appointment -> appointment.getDate().isBefore(LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")))));
    }
}