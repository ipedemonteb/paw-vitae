package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AppointmentService {

    Appointment create(long patientId, long doctorId, LocalDate date, Integer time, String reason, long specialtyId, long officeId, boolean allowFullHistory);

    void sendDailyReminders();

    void completeAppointments();

    Boolean cancelAppointment(long appointmentId, long userId);

    Optional<Appointment> getById(long appointmentId);

    Page<Appointment> getAppointments(long userId, boolean isFuture, int page, int size, String filter);

    List<Appointment> getAppointmentByUserAndDate(long userId, LocalDate date, Integer time);

    Map<LocalDate, List<Integer>> getFutureAppointmentsByUserAndDate(long userId);

    void updateAppointmentReport(long appointmentId, String report);

    boolean hasAllowedAppointmentBetweenDoctorAndPatient(long doctorId, long patientId);
}
