package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.DoctorExperienceDao;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorExperience;
import ar.edu.itba.paw.models.DoctorProfile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class DoctorExperienceHibeDao implements DoctorExperienceDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public DoctorExperience create(Doctor doctor, String title, String orgName, LocalDate startDate, LocalDate endDate) {
        DoctorExperience experience = new DoctorExperience(doctor, title, orgName, startDate, endDate);
        em.persist(experience);
        return experience;
    }

    @Override
    public List<DoctorExperience> getByDoctorId(long doctorId) {
        TypedQuery<DoctorExperience> query = em.createQuery("FROM DoctorExperience AS e WHERE e.doctor.id = :id", DoctorExperience.class);
        query.setParameter("id", doctorId);
        if (query.getResultList().isEmpty()) {
            return List.of();
        }
        return query.getResultList();

    }
}
