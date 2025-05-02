package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AppointmentService {

    Appointment create(long patientId, long doctorId, LocalDate date, Integer time, String reason, long specialtyId);

    List<Appointment> getByPatientId(long patientId);

    List<Appointment> getByDoctorId(long doctorId);



    Boolean cancelAppointment(long appointmentId,long userId);

    Optional<Appointment> getById(long appointmentId);
    Page<Appointment> getAppointments(long userId, boolean isFuture, int page, int size, String filter);


    void updateAppointmentReport(long appointmentId, String report);
}
