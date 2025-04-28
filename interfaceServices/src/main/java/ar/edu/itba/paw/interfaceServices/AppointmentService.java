package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AppointmentService {

    Appointment create(long patientId, long doctorId, LocalDate date, Integer time, String reason, long specialtyId);

    Optional<List<Appointment>> getByPatientId(long patientId);

    Optional<List<Appointment>> getByDoctorId(long doctorId);



    Boolean cancelAppointment(long appointmentId,long userId);
    Boolean acceptAppointment(long appointmentId, long userId);

    Optional<Appointment> getById(long appointmentId);
    Page<Appointment> getPastDoctorAppointments(long doctorId, int page, int size);
    Page<Appointment> getFutureDoctorAppointments(long doctorId, int page, int size);
    Page<Appointment> getFuturePatientAppointments(long patientId,int page, int size);
    Page<Appointment> getPastPatientAppointments(long patientId, int page, int size);

}
