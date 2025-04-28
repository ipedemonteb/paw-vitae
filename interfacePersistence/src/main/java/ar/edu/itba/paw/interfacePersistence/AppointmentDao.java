package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Specialty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;



    public interface AppointmentDao {

        Appointment create(long patientId, long doctorId, LocalDateTime startDate, String reason, Specialty specialty);

        Optional<List<Appointment>> getByPatientId(long patientId);

        Optional<List<Appointment>> getByDoctorId(long doctorId);


        void cancelAppointment(long appointmentId);

        void acceptAppointment(long appointmentId);

        Optional<Appointment> getById(long appointmentId);

        Optional<List<Appointment>> getPastDoctorAppointments(long doctorId, int page, int size);

        Optional<List<Appointment>> getFutureDoctorAppointments(long doctorId, int page, int size);

        Optional<List<Appointment>> getFuturePatientAppointments(long patientId, int page, int size);

        Optional<List<Appointment>> getPastPatientAppointments(long patientId, int page, int size);

        List<Appointment> getAppointmentsByDate(LocalDate today);
        int countFuturePatientAppointments(long patientId);
        int countPastPatientAppointments(long patientId);
        int countFutureDoctorAppointments(long doctorId);
        int countPastDoctorAppointments(long doctorId);

}
