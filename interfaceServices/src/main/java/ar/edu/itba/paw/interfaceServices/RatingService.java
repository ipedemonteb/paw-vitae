package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Rating;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RatingService {

     Rating create(long rating, long doctorId, long patientId, long appointmentId, String comment);

     Optional<Rating> getRating(long id);

     Optional<Rating> getRatingByAppointmentId(long appointmentId);

     List<Rating> getRatingsByDoctorId(long doctorId);

     List<Rating> getRatingsByPatientId(long patientId);
     Map<Rating, Patient> getFiveTopRatings();
}
