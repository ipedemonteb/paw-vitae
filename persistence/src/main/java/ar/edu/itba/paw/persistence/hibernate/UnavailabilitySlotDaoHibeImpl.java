package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.UnavailabilitySlotsDao;
import ar.edu.itba.paw.models.UnavailabilitySlot;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Repository
public class UnavailabilitySlotDaoHibeImpl implements UnavailabilitySlotsDao {

    @PersistenceContext
    private EntityManager em;


    @Override
    public UnavailabilitySlot create(UnavailabilitySlot slot) {
        em.persist(slot);
        return slot;
    }

    @Override
    public void updateDoctorUnavailability(long doctorId, List<UnavailabilitySlot> unavailabilitySlots) {
        em.createQuery("DELETE FROM UnavailabilitySlot u WHERE u.doctor.id = :doctorId")
                .setParameter("doctorId", doctorId)
                .executeUpdate();

        for (UnavailabilitySlot slot : unavailabilitySlots) {
            em.persist(slot);
        }
    }

    @Override
    public List<UnavailabilitySlot> getUnavailabilityByDoctorId(long doctorId) {
        return em.createQuery("FROM UnavailabilitySlot u WHERE u.doctor.id = :doctorId ORDER BY u.id.startDate",
                        UnavailabilitySlot.class)
                .setParameter("doctorId", doctorId)
                .getResultList();
    }

    @Override
    public boolean isUnavailableAtDate(long doctorId, LocalDate date) {
        Long count = em.createQuery(
                        "SELECT COUNT(u) FROM UnavailabilitySlot u WHERE u.doctor.id = :doctorId AND :date BETWEEN u.id.startDate AND u.id.endDate", Long.class)
                .setParameter("doctorId", doctorId)
                .setParameter("date", date)
                .getSingleResult();
        return count > 0;
    }
}
