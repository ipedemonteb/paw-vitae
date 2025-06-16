package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentDao {

    Appointment create(LocalDateTime date, String status, String reason, Specialty specialty, Doctor doctor, Patient patient, String report, DoctorOffice doctorOffice, boolean allowFullHistory);
    boolean officeHasAppointments(long officeId);
    //make it look between a range back
    List<Appointment> getAppointmentsWithHistoryAllowedBefore(LocalDateTime dateTime);

    void cancelAppointment(long appointmentId);

    void completeAppointments();

    List<Appointment> getPastConfirmedAppointments();

    Optional<Appointment> getById(long appointmentId);

    List<Appointment> getAppointments(long userId, boolean isFuture, int page, int size, String filter);

    List<Appointment> getAppointmentsByUserAndDate(long userId, LocalDate date, Integer time);

    List<Appointment> getAppointmentsByDate(LocalDate today);

    List<Appointment> getFutureAppointmentsByUser(long userId); //Needs no pagination, we limit how far in advance one can schedule appointments.

    int countAppointments(long userId, boolean isFuture, String filter);

    void updateReport(long appointmentId, String report);

    List<Appointment> getAppointmentsByPatient(long patientId, int page, int size);

    int countAppointmentsByPatient(long patientId);

    int countAppointmentsByPatientWithFilesOrReport(long patientId);

    List<Appointment> getAppointmentsByPatientWithFilesOrReport(long patientId, int page, int size);

    boolean hasFullMedicalHistoryEnabled(long appointmentId, long doctorId);
//
//    List<Appointment> getAppointmentsWithFilesByPatient(long patientId, int page, int size);
//
//    int countAppointmentsWithFilesByPatient(long patientId);

}
