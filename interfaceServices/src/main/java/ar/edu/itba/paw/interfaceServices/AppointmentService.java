package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Appointment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {

    Appointment create(long clientId, long doctorId, LocalDate date, Integer time, String reason, long specialtyId);

    Optional<List<Appointment>> getByClientId(long clientId);

    Optional<List<Appointment>> getByDoctorId(long doctorId);
}
