package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentDao {

    Appointment create(LocalDateTime date, String status, String reason, Specialty specialty, Doctor doctor, Patient patient, String report, DoctorOffice doctorOffice, boolean allowFullHistory);

    boolean officeHasAppointments(long officeId);

    boolean hasAppointmentWithPatient(long patientId, long doctorId);

    List<Appointment> getAppointmentsWithHistoryAllowedBefore(LocalDateTime dateTime);

    List<Appointment> getPastConfirmedAppointments();

    Optional<Appointment> getById(long appointmentId);

    List<Appointment> getAppointments(long userId, boolean isFuture, int page, int size, String filter);

    List<Appointment> getAppointmentsByUserAndDate(long userId, LocalDate date, Integer time);

    List<Appointment> getAppointmentsByDate(LocalDate today);

    List<Appointment> getFutureAppointmentsByUser(long userId);

    int countAppointments(long userId, boolean isFuture, String filter);

    List<Appointment> getAppointmentsByPatient(long patientId, int page, int size);

    int countAppointmentsByPatient(long patientId);

    int countAppointmentsByPatientWithFilesOrReport(long patientId);

    List<Appointment> getAppointmentsByPatientWithFilesOrReport(long patientId, int page, int size, String direction);

    boolean hasFullMedicalHistoryEnabled(long appointmentId, long doctorId);
}
