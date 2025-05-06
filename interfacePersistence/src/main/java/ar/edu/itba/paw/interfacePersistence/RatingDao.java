package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingDao {

     Rating create(long rating, long doctorId, long patientId, long appointmentId, String comment);

     Optional<Rating> getRating(long id);

     Optional<Rating> getRatingByAppointmentId(long appointmentId);

     List<Rating> getRatingsByDoctorId(long doctorId);

     List<Rating> getRatingsByPatientId(long patientId);

     List<Rating> getFiveTopRatings();
}
