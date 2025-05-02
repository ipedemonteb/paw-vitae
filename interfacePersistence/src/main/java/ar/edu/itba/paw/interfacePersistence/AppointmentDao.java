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


        Optional<Appointment> getById(long appointmentId);



        List<Appointment> getAppointments(long userId, boolean isFuture, int page, int size, String filter);

        List<Appointment> getAppointmentsByDate(LocalDate today);

        int countAppointments(long userId, boolean isFuture, String filter);
    }
