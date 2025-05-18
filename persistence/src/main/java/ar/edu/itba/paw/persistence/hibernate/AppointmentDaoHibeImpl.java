package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Specialty;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class AppointmentDaoHibeImpl implements AppointmentDao {

    @Override
    public Appointment create(LocalDateTime date, String status, String reason, Specialty specialty, Doctor doctor, Patient patient, String report) {
        return null;
    }

    @Override
    public void cancelAppointment(long appointmentId) {

    }

    @Override
    public void completeAppointments() {

    }

    @Override
    public Optional<Appointment> getById(long appointmentId) {
        return Optional.empty();
    }

    @Override
    public List<Appointment> getAppointments(long userId, boolean isFuture, int page, int size, String filter) {
        return List.of();
    }

    @Override
    public List<Appointment> getAppointmentsByUserAndDate(long userId, LocalDate date, Integer time) {
        return List.of();
    }

    @Override
    public List<Appointment> getAppointmentsByDate(LocalDate today) {
        return List.of();
    }

    @Override
    public List<Appointment> getFutureAppointmentsByUser(long userId) {
        return List.of();
    }

    @Override
    public int countAppointments(long userId, boolean isFuture, String filter) {
        return 0;
    }

    @Override
    public void updateReport(long appointmentId, String report) {

    }
}
