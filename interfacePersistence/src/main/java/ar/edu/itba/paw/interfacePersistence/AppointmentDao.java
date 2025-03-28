package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Appointment;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AppointmentDao {

    Appointment create(String clientId, String doctorId, Date startDate, Date endDate, String status, String reason);

    Optional<List<Appointment>> findByClientId(long clientId);

    Optional<List<Appointment>> findByDoctorId(long doctorId);

}
