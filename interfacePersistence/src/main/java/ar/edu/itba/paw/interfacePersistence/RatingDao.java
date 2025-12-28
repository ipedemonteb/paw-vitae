package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.*;

import java.util.List;
import java.util.Optional;

public interface RatingDao {

     Rating create(long rating, Doctor doctor, Patient patient, Appointment appointment, String comment);

     Optional<Rating> getRating(long id);

     Optional<Rating> getRatingByAppointmentId(long appointmentId);

     Page<Rating> getRatingsByDoctorId(long doctorId, int page, int pageSize);

     List<Rating> getRatingsByPatientId(long patientId);

     List<Rating> getFiveTopRatings();

     List<Rating> getFiveTopRatingsByDoctorId(long doctorId);

     Page<Rating> getAllRatings(int page, int pageSize);
}
