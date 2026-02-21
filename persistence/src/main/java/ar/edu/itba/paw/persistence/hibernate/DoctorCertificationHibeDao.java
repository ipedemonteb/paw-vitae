package ar.edu.itba.paw.persistence.hibernate;


import ar.edu.itba.paw.interfacePersistence.DoctorCertificationDao;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorCertification;
import ar.edu.itba.paw.models.DoctorExperience;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

@Repository
public class DoctorCertificationHibeDao implements DoctorCertificationDao {

    @PersistenceContext
    EntityManager em;

    @Override
    public DoctorCertification create(Doctor doctor, String certificateName, String issuingEntity, LocalDate issueDate) {
        DoctorCertification certification = new DoctorCertification(doctor, certificateName, issuingEntity, issueDate);
        em.persist(certification);
        return certification;
    }

    @Override
    public List<DoctorCertification> getByDoctorId(long doctorId) {
        TypedQuery<DoctorCertification> query = em.createQuery("FROM DoctorCertification AS c WHERE c.doctor.id = :id order by c.issueDate desc ", DoctorCertification.class);
        query.setParameter("id", doctorId);
        if (query.getResultList().isEmpty()) {
            return List.of();
        }
        return query.getResultList();
    }

    @Override
    public void delete(long id) {
        DoctorCertification certification = em.find(DoctorCertification.class, id);
        if (certification != null) {
            em.remove(certification);
        }
    }
}
