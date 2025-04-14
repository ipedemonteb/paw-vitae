package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Client;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface AppointmentService {

    Appointment create(long clientId, long doctorId, LocalDate date, Integer time, String reason, long specialtyId);

    Optional<List<Appointment>> getByClientId(long clientId);

    Optional<List<Appointment>> getByDoctorId(long doctorId);

    Set<Integer> getBookedHoursByDoctorAndDate(long doctorId, LocalDate date);

    Optional<List<Appointment>> getAllFutureAppointments(long doctorId);

    Optional<String> getFutureAppointmentsPerDate(long doctorId);

    Map<Appointment, Client> getForDoctor(long doctorId);
}
