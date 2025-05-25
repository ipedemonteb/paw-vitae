package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.DoctorOfficeDao;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorOffice;
import ar.edu.itba.paw.models.DoctorOfficeId;
import ar.edu.itba.paw.models.Neighborhood;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class DoctorOfficeDaoHibe implements DoctorOfficeDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public DoctorOffice create(DoctorOffice doctorOffice) {
        doctorOffice.setNeighborhood(em.getReference(Neighborhood.class, doctorOffice.getNeighborhood().getId()));
        em.persist(doctorOffice);
        return doctorOffice;
    }

    @Override
    public Optional<DoctorOffice> getById(long id) {
        return Optional.ofNullable(em.find(DoctorOffice.class, id));
    }

    @Override
    public List<DoctorOffice> getByDoctorId(long doctorId) {
        return em.createQuery("FROM DoctorOffice d WHERE d.doctor.id = :doctorId", DoctorOffice.class)
                .setParameter("doctorId", doctorId)
                .getResultList();
    }
}
