package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.RatingDao;
import ar.edu.itba.paw.models.*;
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
       TypedQuery<Rating> query = em.createQuery("FROM Rating AS r WHERE r.appointment.id = :id", Rating.class);
        query.setParameter("id", appointmentId);
        List<Rating> ratings = query.getResultList();
        if (ratings.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ratings.getFirst());
    }

    @Override
    public Page<Rating> getRatingsByDoctorId(long doctorId, int page, int pageSize) {
        String jpql = "SELECT r FROM Rating AS r " +
                "JOIN FETCH r.patient " +
                "WHERE r.doctor.id = :id " +
                "ORDER BY r.rating DESC, r.id DESC";

        TypedQuery<Rating> query = em.createQuery(jpql, Rating.class);
        query.setParameter("id", doctorId);

        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);

        List<Rating> ratings = query.getResultList();

        TypedQuery<Long> countQuery = em.createQuery(
                "SELECT COUNT(r) FROM Rating AS r WHERE r.doctor.id = :id", Long.class);
        countQuery.setParameter("id", doctorId);

        Long totalItems = countQuery.getSingleResult();

        return new Page<>(ratings, page, pageSize, totalItems);
    }

    @Override
    public List<Rating> getRatingsByPatientId(long patientId) {
        TypedQuery<Rating> query = em.createQuery("FROM Rating AS r WHERE r.patient.id = :id", Rating.class);
        query.setParameter("id", patientId);
        return query.getResultList();
    }

    @Override
    public List<Rating> getFiveTopRatings() {
        TypedQuery<Rating> query = em.createQuery("FROM Rating AS r WHERE r.comment <> '' ORDER BY r.rating DESC", Rating.class);
        query.setMaxResults(5);
        return query.getResultList();
    }

    @Override
    public List<Rating> getFiveTopRatingsByDoctorId(long doctorId) {
        TypedQuery<Rating> query = em.createQuery(
                "FROM Rating AS r WHERE r.doctor.id = :doctorId AND r.comment <> '' ORDER BY r.rating DESC",
                Rating.class
        );
        query.setParameter("doctorId", doctorId);
        query.setMaxResults(5);
        return query.getResultList();
    }

    public Page<Rating> getAllRatings(int page, int pageSize) {

        String jpql = "SELECT r FROM Rating AS r ORDER BY r.rating DESC, r.id DESC";

        TypedQuery<Rating> query = em.createQuery(jpql, Rating.class);
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);
        List<Rating> ratings = query.getResultList();

        TypedQuery<Long> countQuery = em.createQuery("SELECT COUNT(r) FROM Rating AS r", Long.class);
        Long totalItems = countQuery.getSingleResult();

        return new Page<>(ratings, page, pageSize, totalItems);
    }
}
