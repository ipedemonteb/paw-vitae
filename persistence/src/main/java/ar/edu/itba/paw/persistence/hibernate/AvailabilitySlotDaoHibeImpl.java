package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.AvailabilitySlotsDao;
import ar.edu.itba.paw.models.AvailabilitySlot;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AvailabilitySlotDaoHibeImpl implements AvailabilitySlotsDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public AvailabilitySlot create(AvailabilitySlot slot) {
        em.persist(slot);
        return slot;
    }

    @Override //TODO CHANGE
    public void updateDoctorAvailability(long id, List<AvailabilitySlot> availabilitySlots) {
//        AvailabilitySlot slot = em.find(AvailabilitySlot.class, id);
//        if (slot != null) {
//            slot.setAvailabilitySlots(availabilitySlots);
//            em.merge(slot);
//        }
    }

    @Override
    public List<AvailabilitySlot> getAvailabilityByDoctorId(long doctorId) {
        return em.createQuery("FROM AvailabilitySlot a WHERE a.doctor.id = :doctorId", AvailabilitySlot.class)
                .setParameter("doctorId", doctorId)
                .getResultList();
    }
}
