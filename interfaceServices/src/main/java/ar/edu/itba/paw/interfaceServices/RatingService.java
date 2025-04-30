package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingService {
    public Rating create(double rating, long doctorId, long patientId, long appointmentId, String comment, long id);
    public Optional<Rating> getRating(int id);
    public Optional<Rating> getRatingByAppointmentId(long appointmentId);
    public List<Rating> getRatingsByDoctorId(long doctorId);
    public List<Rating> getRatingsByPatientId(long patientId);
}
