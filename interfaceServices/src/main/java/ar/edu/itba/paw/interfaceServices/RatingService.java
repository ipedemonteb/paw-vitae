package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Rating;

import java.util.Optional;

public interface RatingService {
    public Rating create(double rating, int doctorId, int patientId, int appointmentId, String comment, long id);
    public Optional<Rating> getRating(int id);
    public Optional<Rating> getRatingByAppointmentId(int appointmentId);
}
