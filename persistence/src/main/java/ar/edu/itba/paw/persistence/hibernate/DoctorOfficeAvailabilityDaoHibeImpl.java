package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.DoctorOfficeAvailabilityDao;
import ar.edu.itba.paw.models.DoctorOffice;
import ar.edu.itba.paw.models.DoctorOfficeAvailabilitySlot;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class DoctorOfficeAvailabilityDaoHibeImpl implements DoctorOfficeAvailabilityDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public DoctorOfficeAvailabilitySlot create(DoctorOfficeAvailabilitySlot slot) {
        em.persist(slot);
        return slot;
    }

    @Override
    public void update(DoctorOfficeAvailabilitySlot slot) {
        slot.setOffice(em.getReference(DoctorOffice.class, slot.getOffice().getId())); //check if necessary (I am traumatized)
        em.merge(slot);
    }

    @Override
    public void delete(DoctorOfficeAvailabilitySlot slot) {
        DoctorOfficeAvailabilitySlot toDelete = em.find(DoctorOfficeAvailabilitySlot.class, slot.getId());
        if (toDelete != null) {
            em.remove(toDelete);
        }
    }

    @Override
    public List<DoctorOfficeAvailabilitySlot> getByOfficeId(long officeId) {
        return em.createQuery("FROM DoctorOfficeAvailabilitySlot d WHERE d.office.id = :officeId ORDER BY d.office.officeName, d.dayOfWeek, d.startTime",
                DoctorOfficeAvailabilitySlot.class)
                .setParameter("officeId", officeId)
                .getResultList();
    }

    @Override
    public List<DoctorOfficeAvailabilitySlot> getActiveByOfficeId(long officeId) {
        return em.createQuery("SELECT d FROM DoctorOfficeAvailabilitySlot d JOIN d.office o WHERE o.id = :officeId AND o.active = true AND o.removed IS NULL ORDER BY o.officeName, d.dayOfWeek, d.startTime",
                DoctorOfficeAvailabilitySlot.class)
                .setParameter("officeId", officeId)
                .getResultList();
    }

    @Override
    public List<DoctorOfficeAvailabilitySlot> getByDoctorId(long doctorId) {
        return em.createQuery("SELECT d FROM DoctorOfficeAvailabilitySlot d JOIN d.office o WHERE o.doctor.id = :doctorId AND o.removed IS NULL ORDER BY o.officeName, d.dayOfWeek, d.startTime",
                DoctorOfficeAvailabilitySlot.class)
                .setParameter("doctorId", doctorId)
                .getResultList();
    }
}
