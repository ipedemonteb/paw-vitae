package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Patient;

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

    Optional<Long> updateAppointmentReport(long appointmentId, String report);

    boolean hasHistoryAllowedByAppointmentId(long appointmentId, long doctorId);

    Patient getPatientByAppointmentId(long appointmentId);

    Page<Appointment> getAppointmentsForPatientWithFilesOrReport(long patientId, int page, int pageSize, String direction);

    boolean hasFullMedicalHistoryEnabled(long patientId, long doctorId);

    boolean officeHasAppointments(long officeId);

    void revokeHistoryPermissionForOldAppointments();
}
