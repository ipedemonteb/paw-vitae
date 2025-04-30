package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Specialty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;



    public interface AppointmentDao {

        Appointment create(long patientId, long doctorId, LocalDateTime startDate, String reason, Specialty specialty);

        List<Appointment> getByPatientId(long patientId);

        List<Appointment> getByDoctorId(long doctorId);


        void cancelAppointment(long appointmentId);

        void acceptAppointment(long appointmentId);

        Optional<Appointment> getById(long appointmentId);

        List<Appointment> getPastDoctorAppointments(long doctorId, int page, int size, String dateRange, String status);
        List<Appointment> getFutureDoctorAppointments(long doctorId, int page, int size,String dateRange,String status);
        List<Appointment> getFuturePatientAppointments(long patientId,int page, int size,String dateRange,String status);
        List<Appointment> getPastPatientAppointments(long patientId, int page, int size,String dateRange,String status);

        List<Appointment> getAppointmentsByDate(LocalDate today);
        int countFuturePatientAppointments(long patientId,String dateRange);
        int countPastPatientAppointments(long patientId,String status);
        int countFutureDoctorAppointments(long doctorId,String dateRange);
        int countPastDoctorAppointments(long doctorId,String status);

}
