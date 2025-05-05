package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingDao {

    public Rating create(long rating, long doctorId, long patientId, long appointmentId, String comment);

    public Optional<Rating> getRating(long id);

    public Optional<Rating> getRatingByAppointmentId(long appointmentId);

    public List<Rating> getRatingsByDoctorId(long doctorId);

    public List<Rating> getRatingsByPatientId(long patientId);
}
