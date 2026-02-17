package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.PatientDao;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Neighborhood;
import ar.edu.itba.paw.models.Patient;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class PatientDaoHibeImpl implements PatientDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Patient> getById(long id) {
        return Optional.ofNullable(em.find(Patient.class, id));
    }

    @Override
    public Patient create(String name, String lastName, String email, String password, String phone, String language, Coverage coverage, Neighborhood neighborhood) {
        Patient patient = new Patient(name, lastName, email, password, phone, language, coverage, neighborhood, false);
        em.persist(patient);
        return patient;
    }

    @Override
    public Optional<Patient> getByEmail(String email) {
        TypedQuery<Patient> query = em.createQuery("FROM Patient p WHERE p.email = :email", Patient.class);
        query.setParameter("email", email);
        List<Patient> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());
    }

    @Override
    public Optional<Patient> getByVerificationToken(String token) {
        TypedQuery<Patient> query = em.createQuery("FROM Patient p WHERE p.verificationToken = :token", Patient.class);
        query.setParameter("token", token);
        List<Patient> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());
    }

    @Override
    public Optional<Patient> getByResetToken(String token) {
        TypedQuery<Patient> query = em.createQuery("FROM Patient p WHERE p.resetPasswordToken = :token", Patient.class);
        query.setParameter("token", token);
        List<Patient> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());
    }

    @Override
    public int countAll() {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(p) FROM Patient p", Long.class);
        return query.getSingleResult().intValue();
    }
}