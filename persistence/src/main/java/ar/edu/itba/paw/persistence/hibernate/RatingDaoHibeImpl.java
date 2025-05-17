package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.RatingDao;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Rating;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class RatingDaoHibeImpl implements RatingDao {

    @PersistenceContext
    private EntityManager em;


    @Override
    public Rating create(long rating, Doctor doctor, Patient patient, Appointment appointment, String comment) {
        Rating ratingObj = new Rating(rating, doctor, patient, appointment, comment);
        em.persist(ratingObj);
        return ratingObj;
    }

    @Override
    public Optional<Rating> getRating(long id) {
        return Optional.ofNullable(em.find(Rating.class, id));
    }

    @Override
    public Optional<Rating> getRatingByAppointmentId(long appointmentId) {
       TypedQuery<Rating> query = em.createQuery("FROM Rating AS r WHERE r.appointment.id = id", Rating.class);
        query.setParameter("id", appointmentId);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public List<Rating> getRatingsByDoctorId(long doctorId) {
        return null;
    }

    @Override
    public List<Rating> getRatingsByPatientId(long patientId) {
        return null;
    }

    @Override
    public List<Rating> getFiveTopRatings() {
        return null;
    }
}
