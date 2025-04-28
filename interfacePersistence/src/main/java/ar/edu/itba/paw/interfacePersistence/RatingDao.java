package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Rating;

import java.util.Optional;

public interface RatingDao {
    public Rating create(double rating, int doctorId, int patientId, int appointmentId, String comment, long id);
    public Optional<Rating> getRating(int id);
    public Optional<Rating> getRatingByAppointmentId(int appointmentId);
}
