package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Rating;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RatingService {

     Rating create(long rating,String email, long appointmentId, String comment);

     Optional<Rating> getRating(long id);

     Optional<Rating> getRatingByAppointmentId(long appointmentId);

     Map<Rating, Patient> getFiveTopRatings();

     Page<Rating> getAllRatings(int page, int pageSize,Long doctorId);
}
