package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.DoctorOfficeDao;
import ar.edu.itba.paw.models.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class DoctorOfficeDaoHibe implements DoctorOfficeDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public DoctorOffice create(DoctorOffice doctorOffice) {
        doctorOffice.setNeighborhood(em.getReference(Neighborhood.class, doctorOffice.getNeighborhood().getId()));
        List<Specialty> specialtiesRefs = doctorOffice.getSpecialties().stream()
                .map(specialty -> em.getReference(Specialty.class, specialty.getId()))
                .toList();
        doctorOffice.setSpecialties(specialtiesRefs);
        em.persist(doctorOffice);
        return doctorOffice;
    }

    @Override
    public Optional<DoctorOffice> getById(long id) {
        return Optional.ofNullable(em.find(DoctorOffice.class, id));
    }

    @Override
    public List<DoctorOffice> getByDoctorId(long doctorId) {
        return em.createQuery("FROM DoctorOffice d WHERE d.doctor.id = :doctorId AND d.removed IS NULL", DoctorOffice.class)
                .setParameter("doctorId", doctorId)
                .getResultList();
    }

    @Override
    public List<DoctorOffice> getByDoctorIdWithAvailability(long doctorId) {
        return em.createQuery("SELECT DISTINCT d FROM DoctorOffice d LEFT JOIN FETCH d.doctorOfficeAvailability WHERE d.doctor.id = :doctorId AND d.removed IS NULL", DoctorOffice.class)
                .setParameter("doctorId", doctorId)
                .getResultList();
    }

    @Override
    public List<DoctorOffice> getActiveByDoctorId(long doctorId) {
        return em.createQuery("FROM DoctorOffice d WHERE d.doctor.id = :doctorId AND d.active = true AND d.removed IS NULL", DoctorOffice.class)
                .setParameter("doctorId", doctorId)
                .getResultList();
    }

    @Override
    public List<DoctorOffice> getInactiveByDoctorId(long doctorId) {
        return em.createQuery("FROM DoctorOffice d WHERE d.doctor.id = :doctorId AND d.active = false AND d.removed IS NULL", DoctorOffice.class)
                .setParameter("doctorId", doctorId)
                .getResultList();
    }

    @Override
    public DoctorOffice update(DoctorOffice o) {
        o.setNeighborhood(em.getReference(Neighborhood.class, o.getNeighborhood().getId()));
        List<Specialty> specialtiesRefs = o.getSpecialties().stream()
                .map(specialty -> em.getReference(Specialty.class, specialty.getId()))
                .collect(Collectors.toCollection(ArrayList::new));
        o.setSpecialties(specialtiesRefs);
        return em.merge(o);
    }

    @Override
    public List<DoctorOffice> getByNameAndNeighborhoodId(String officeName, long neighborhoodId, long doctorId){
        return em.createQuery(
                        "SELECT d FROM DoctorOffice d " +
                                "WHERE d.doctor.id = :doctorId " +
                                "AND d.neighborhood.id = :neighborhoodId " +
                                "AND LOWER(TRIM(d.officeName)) = :name", DoctorOffice.class)
                .setParameter("doctorId", doctorId)
                .setParameter("neighborhoodId", neighborhoodId)
                .setParameter("name", officeName.trim().toLowerCase())
                .getResultList();
    }
    @Override
    public void remove(long id) {
        DoctorOffice office = em.find(DoctorOffice.class, id);
        if (office != null) {
            em.remove(office);
        }
    }
}
