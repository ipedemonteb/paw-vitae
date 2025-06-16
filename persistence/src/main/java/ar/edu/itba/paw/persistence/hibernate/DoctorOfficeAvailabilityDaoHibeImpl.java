package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.DoctorOfficeAvailabilityDao;
import ar.edu.itba.paw.models.DoctorOffice;
import ar.edu.itba.paw.models.DoctorOfficeAvailability;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class DoctorOfficeAvailabilityDaoHibeImpl implements DoctorOfficeAvailabilityDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public DoctorOfficeAvailability create(DoctorOfficeAvailability slot) {
        em.persist(slot);
        return slot;
    }

    @Override
    public void update(DoctorOfficeAvailability slot) {
        slot.setOffice(em.getReference(DoctorOffice.class, slot.getOffice().getId())); //check if necessary (I am traumatized)
        em.merge(slot);
    }

    @Override
    public void delete(DoctorOfficeAvailability slot) {
        DoctorOfficeAvailability toDelete = em.find(DoctorOfficeAvailability.class, slot.getId());
        if (toDelete != null) {
            em.remove(toDelete);
        }
    }

    @Override
    public List<DoctorOfficeAvailability> getByOfficeId(long officeId) {
        return em.createQuery("FROM DoctorOfficeAvailability d WHERE d.office.id = :officeId ORDER BY d.office.officeName, d.dayOfWeek, d.startTime",
                DoctorOfficeAvailability.class)
                .setParameter("officeId", officeId)
                .getResultList();
    }

    @Override
    public List<DoctorOfficeAvailability> getActiveByOfficeId(long officeId) {
        return em.createQuery("SELECT d FROM DoctorOfficeAvailability d JOIN d.office o WHERE o.id = :officeId AND o.active = true AND o.removed IS NULL ORDER BY o.officeName, d.dayOfWeek, d.startTime",
                DoctorOfficeAvailability.class)
                .setParameter("officeId", officeId)
                .getResultList();
    }

    @Override
    public List<DoctorOfficeAvailability> getByDoctorId(long doctorId) {
        return em.createQuery("SELECT d FROM DoctorOfficeAvailability d JOIN d.office o WHERE o.doctor.id = :doctorId AND o.removed IS NULL ORDER BY o.officeName, d.dayOfWeek, d.startTime",
                DoctorOfficeAvailability.class)
                .setParameter("doctorId", doctorId)
                .getResultList();
    }

    @Override
    public List<DoctorOfficeAvailability> getActiveByDoctorId(long doctorId) {
        return em.createQuery("SELECT d FROM DoctorOfficeAvailability d JOIN d.office o WHERE o.doctor.id = :doctorId AND o.active = true AND o.removed IS NULL ORDER BY o.officeName, d.dayOfWeek, d.startTime",
                DoctorOfficeAvailability.class)
                .setParameter("doctorId", doctorId)
                .getResultList();
    }
}
