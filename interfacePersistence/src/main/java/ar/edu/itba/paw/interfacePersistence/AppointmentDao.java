package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Appointment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentDao {

    Appointment create(long clientId, long doctorId, LocalDateTime startDate, String reason);

    Optional<List<Appointment>> getByClientId(long clientId);

    Optional<List<Appointment>> getByDoctorId(long doctorId);

}
