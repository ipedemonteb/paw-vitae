package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Appointment;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {

    Appointment create(long clientId, long doctorId, Date startDate, Date endDate, String status, String reason);

    Optional<List<Appointment>> getByClientId(long clientId);

    Optional<List<Appointment>> getByDoctorId(long doctorId);
}
