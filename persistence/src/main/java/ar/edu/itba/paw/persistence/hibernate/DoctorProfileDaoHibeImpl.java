package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.DoctorProfileDao;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorProfile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class DoctorProfileDaoHibeImpl implements DoctorProfileDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public DoctorProfile create(Doctor doctor, String bio, String description) {
        DoctorProfile profile = new DoctorProfile(doctor, bio, description);
        em.persist(profile);
        return profile;
    }

    @Override
    public Optional<DoctorProfile> getByDoctorId(long id) {
        TypedQuery<DoctorProfile> query = em.createQuery("FROM DoctorProfile AS p WHERE p.doctor.id = :id", DoctorProfile.class);
        query.setParameter("id", id);
        if (query.getResultList().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(query.getResultList().getFirst());
    }
}
