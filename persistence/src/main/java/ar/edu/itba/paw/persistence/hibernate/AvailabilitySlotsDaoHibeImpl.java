package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.AvailabilitySlotsDao;
import ar.edu.itba.paw.models.AvailabilitySlots;
import ar.edu.itba.paw.models.Doctor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class AvailabilitySlotsDaoHibeImpl implements AvailabilitySlotsDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public AvailabilitySlots create(AvailabilitySlots slot) {
        em.persist(slot);
        return slot;
    }

    @Override
    public Optional<AvailabilitySlots> getById(long id) {
        return Optional.ofNullable(em.find(AvailabilitySlots.class, id));
    }

    @Override
    public List<AvailabilitySlots> getByDoctorIdInDateRange(long doctorId, LocalDate startDate, LocalDate endDate) {
        final TypedQuery<AvailabilitySlots> query = em.createQuery(
                "SELECT s FROM AvailabilitySlots s " +
                        "WHERE s.doctor.id = :doctorId " +
                        "AND s.slotDate BETWEEN :startDate AND :endDate " +
                        "ORDER BY s.slotDate ASC, s.startTime ASC",
                AvailabilitySlots.class);

        query.setParameter("doctorId", doctorId);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        return query.getResultList();
    }

    @Override
    public boolean exists(long doctorId, LocalDate date, LocalTime time) {
        final TypedQuery<Long> query = em.createQuery(
                "SELECT count(s) FROM AvailabilitySlots s " +
                        "WHERE s.doctor.id = :docId AND s.slotDate = :date AND s.startTime = :time", Long.class);
        query.setParameter("docId", doctorId);
        query.setParameter("date", date);
        query.setParameter("time", time);
        return query.getSingleResult() > 0;
    }
    @Override
    public int deleteUnbookedSlots(long doctorId, LocalDate fromDate) {
        return em.createQuery("DELETE FROM AvailabilitySlots s " +
                        "WHERE s.doctor.id = :doctorId " +
                        "AND s.slotDate >= :fromDate " +
                        "AND s.status = 'AVAILABLE'")
                .setParameter("doctorId", doctorId)
                .setParameter("fromDate", fromDate)
                .executeUpdate();
    }


}