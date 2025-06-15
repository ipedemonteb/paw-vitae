package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.AvailabilitySlotsDao;
import ar.edu.itba.paw.models.AvailabilitySlot;
import org.postgresql.core.NativeQuery;
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

    //@TODO: capaz se puede hacer desde service
    @Override
    public void updateDoctorAvailability(long id, List<AvailabilitySlot> availabilitySlots) {
       em.createQuery("DELETE FROM AvailabilitySlot a WHERE a.doctor.id = :doctorId")
                .setParameter("doctorId", id)
                .executeUpdate();
        for (AvailabilitySlot slot : availabilitySlots) {
                em.persist(slot);
        }
    }

    @Override
    public List<AvailabilitySlot> getAvailabilityByDoctorId(long doctorId) {
      return em.createQuery("FROM AvailabilitySlot a WHERE a.doctor.id = :doctorId ORDER BY a.id.dayOfWeek, a.id.startTime",
                 AvailabilitySlot.class).setParameter("doctorId", doctorId)
                .getResultList();
    }
}
