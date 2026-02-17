package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.UnavailabilitySlotsDao;
import ar.edu.itba.paw.models.UnavailabilitySlot;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

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
    public Optional<UnavailabilitySlot> getById(long unavailabilitySlotId) {
        return Optional.ofNullable(em.find(UnavailabilitySlot.class, unavailabilitySlotId));
    }

    @Override
    public void delete(UnavailabilitySlot slot) {
        if(slot != null) {
            em.remove(slot);
        }
    }


    @Override
    public boolean isUnavailableAtDate(long doctorId, LocalDate date) {
        Long count = em.createQuery(
                        "SELECT COUNT(u) FROM UnavailabilitySlot u WHERE u.doctor.id = :doctorId AND :date BETWEEN u.startDate AND u.endDate", Long.class)
                .setParameter("doctorId", doctorId)
                .setParameter("date", date)
                .getSingleResult();
        return count > 0;
    }


    @Override
    public boolean hasOverlap(long doctorId, LocalDate start, LocalDate end) {
        String query = "SELECT COUNT(u) FROM UnavailabilitySlot u " +
                "WHERE u.doctor.id = :doctorId " +
                "AND u.startDate <= :end " +
                "AND u.endDate >= :start";

        Long count = em.createQuery(query, Long.class)
                .setParameter("doctorId", doctorId)
                .setParameter("start", start)
                .setParameter("end", end)
                .getSingleResult();

        return count > 0;
    }

    @Override
    public List<UnavailabilitySlot> getUnavailabilityByDoctorIdInDateRange(long doctorId, LocalDate from, LocalDate to) {
        return em.createQuery("""
            FROM UnavailabilitySlot u 
            WHERE u.doctor.id = :doctorId 
              AND u.startDate <= :to 
              AND u.endDate >= :from
            ORDER BY u.startDate ASC
            """, UnavailabilitySlot.class)
                .setParameter("doctorId", doctorId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }

    @Override
    public List<UnavailabilitySlot> getUnavailabilityByDoctorIdPaginated(long doctorId, int page, int pageSize) {
        LocalDate now = LocalDate.now();
        return em.createQuery("FROM UnavailabilitySlot u WHERE u.doctor.id = :doctorId AND u.endDate >= :now ORDER BY u.startDate DESC", UnavailabilitySlot.class)
                .setParameter("doctorId", doctorId)
                .setParameter("now", now)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public int countUnavailabilityByDoctorId(long doctorId) {
        return em.createQuery("SELECT COUNT(u) FROM UnavailabilitySlot u WHERE u.doctor.id = :doctorId", Long.class)
                .setParameter("doctorId", doctorId)
                .getSingleResult()
                .intValue();
    }
}
