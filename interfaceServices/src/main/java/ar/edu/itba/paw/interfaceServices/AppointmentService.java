package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Appointment;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AppointmentService {

    Appointment create(long patientId, long doctorId, LocalDate date, Integer time, String reason, long specialtyId);

    Optional<List<Appointment>> getByPatientId(long patientId);

    Optional<List<Appointment>> getByDoctorId(long doctorId);

    Optional<List<Appointment>> getAllFutureAppointments(long doctorId);

    Boolean cancelAppointment(long appointmentId,long userId);
    Boolean acceptAppointment(long appointmentId, long userId);

    Optional<Appointment> getById(long appointmentId);
    List<Appointment> getPastDoctorAppointments(long doctorId);
    List<Appointment> getFutureDoctorAppointments(long doctorId);
    List<Appointment> getFuturePatientAppointments(long patientId);
    List<Appointment> getPastPatientAppointments(long patientId);

}
