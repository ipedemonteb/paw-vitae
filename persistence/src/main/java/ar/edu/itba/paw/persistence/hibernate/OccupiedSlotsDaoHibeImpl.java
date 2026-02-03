package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.OccupiedSlotsDao;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.OccupiedSlots;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class OccupiedSlotsDaoHibeImpl implements OccupiedSlotsDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public OccupiedSlots create(Doctor doctor, LocalDate slotDate, LocalTime startTime) {
        OccupiedSlots occupiedSlots = new OccupiedSlots(doctor, slotDate, startTime);
        em.persist(occupiedSlots);
        return occupiedSlots;
    }

    @Override
    public Optional<OccupiedSlots> getById(long id) {
        return Optional.ofNullable(em.find(OccupiedSlots.class, id));
    }

    @Override
    public List<OccupiedSlots> getByDoctorIdInDateRange(long doctorId, LocalDate startDate, LocalDate endDate) {
        final TypedQuery<OccupiedSlots> query = em.createQuery(
                "FROM OccupiedSlots os WHERE os.doctor.id = :doctorId AND os.slotDate BETWEEN :startDate AND :endDate",
                OccupiedSlots.class);
        query.setParameter("doctorId", doctorId);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    @Override
    public void delete(long slotId) {
        OccupiedSlots toDelete = em.find(OccupiedSlots.class, slotId);
        if (toDelete != null) {
            em.remove(toDelete);
        }
    }
}