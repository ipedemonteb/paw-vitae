package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.UnavailabilitySlotsDao;
import ar.edu.itba.paw.models.UnavailabilitySlot;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.ZoneId;
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

    @Override
    public List<UnavailabilitySlot> getUnavailabilityByDoctorIdCurrentAndNextMonth(long doctorId) {
        LocalDate now = LocalDate.now();
        LocalDate startOfThisMonth = now.withDayOfMonth(1);
        LocalDate endOfNextMonth = now.plusMonths(1).withDayOfMonth(1).plusMonths(1).minusDays(1);

        return em.createQuery("""
            FROM UnavailabilitySlot u 
            WHERE u.doctor.id = :doctorId 
              AND (
                  u.id.startDate <= :endOfNextMonth 
                  AND u.id.endDate >= :startOfThisMonth
              )
        """, UnavailabilitySlot.class)
                .setParameter("doctorId", doctorId)
                .setParameter("startOfThisMonth", startOfThisMonth)
                .setParameter("endOfNextMonth", endOfNextMonth)
                .getResultList();
    }

    @Override
    public List<UnavailabilitySlot> getUnavailabilityByDoctorIdAndMonthAndYear(long doctorId, int month, int year) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        LocalDate now = LocalDate.now();
        return em.createQuery("FROM UnavailabilitySlot u WHERE u.doctor.id = :doctorId " +
                        "AND u.id.startDate <= :end AND u.id.endDate >= :start AND u.id.endDate > :now ", UnavailabilitySlot.class)
                .setParameter("doctorId", doctorId)
                .setParameter("start", start)
                .setParameter("end", end)
                .setParameter("now", now)
                .getResultList();
    }
}
