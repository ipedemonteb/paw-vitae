package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Doctor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface AppointmentService {

    Appointment create(long clientId, long doctorId, LocalDate date, Integer time, String reason, long specialtyId);

    Optional<List<Appointment>> getByClientId(long clientId);

    Optional<List<Appointment>> getByDoctorId(long doctorId);

    Optional<List<Appointment>> getAllFutureAppointments(long doctorId);

    Boolean cancelAppointment(long appointmentId,long userId);
    Boolean acceptAppointment(long appointmentId, long userId);
    Optional<Map<Long, List<Appointment>>> getAllFutureAppointments(List<Doctor> doctors);

    Optional<Appointment> getById(long appointmentId);
    Map<Boolean,List<Appointment>> getByDoctorIdPartitionedByDate(long doctorId);
    Map<Boolean,List<Appointment>> getByClientIdPartitionedByDate(long clientId);

}
