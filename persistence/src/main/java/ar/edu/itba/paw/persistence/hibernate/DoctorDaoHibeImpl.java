package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Specialty;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class DoctorDaoHibeImpl implements DoctorDao {
    @PersistenceContext
    private EntityManager em;


    @Override
    public Doctor create(String name, String lastName, String email, String password, String phone, String language, Long imageId, List<Specialty> specialties, List<Coverage> coverages) {
        Doctor doctor = new Doctor(name, lastName, email, password, phone, language, imageId, false);
        em.persist(doctor);
        return doctor;
    }

    @Override
    public Optional<Doctor> getById(long id) {
        return Optional.ofNullable(em.find(Doctor.class, id));
    }

    @Override
    public List<Doctor> getBySpecialty(long specialtyId, int page, int pageSize) {
        return em.createQuery("FROM Doctor d JOIN d.specialtyList s WHERE s.id = :specialtyId", Doctor.class)
                .setParameter("specialtyId", specialtyId)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public int countBySpecialty(long specialtyId) {

        return 0;
    }

    @Override
    public void updateImage(long id, Long imageId) {

    }

    @Override
    public Optional<Doctor> getByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public void UpdateDoctorRating(long id, long rating) {

    }

    @Override
    public void updateDoctor(long id, List<Long> specialties, List<Long> coverages) {

    }

    //@TODO: Fix
    @Override
    public List<Doctor> getWithFilters(long specialtyId, long coverageId, List<Integer> weekdays, String orderBy, String direction, int page, int pageSize) {
        String orderByQuery = switch (orderBy) {
            case "name" -> "name";
            case "last_name" -> "last_name";
            case "rating" -> "rating";
            case "email" -> "email";
            default -> "doctor_id";
        };

        String directionQuery = (direction.equals("desc")) ? "DESC" : "ASC";

        long specialtyIdQuery = specialtyId > 0 ? specialtyId : 0;

        long coverageIdQuery = coverageId > 0 ? coverageId : 0;

        List<Integer> weekdaysQuery = new ArrayList<>();

        for (Integer weekday : weekdays) {
            if (weekday >= 0 && weekday < 7) {
                weekdaysQuery.add(weekday);
            }
        }

        Query nativeQuery = em.createNativeQuery("SELECT d.doctor_id FROM doctors d " +
                                "JOIN users u ON d.doctor_id = u.id "+
                "JOIN doctor_specialties ds ON d.doctor_id = ds.doctor_id " +
                "JOIN doctor_coverages dc ON d.doctor_id = dc.doctor_id " +
                "JOIN doctor_availability a ON d.doctor_id = a.doctor_id " +
                "WHERE ds.specialty_id = :specialtyId AND dc.coverage_id = :coverageId AND a.day_of_week IN (:weekdays) AND u.is_verified = true " +
                "ORDER BY " + orderByQuery + " " + directionQuery)
                        .setParameter("specialtyId", specialtyIdQuery)
                        .setParameter("coverageId", coverageIdQuery)
                        .setParameter("weekdays", weekdaysQuery)
                        .setFirstResult((page-1) * pageSize)
                        .setMaxResults(pageSize);

        List<?> doctorIdsRaw = nativeQuery.getResultList();
        List<Long> doctorIds = doctorIdsRaw.stream().map(id -> ((Number) id).longValue()).collect(Collectors.toList());
        TypedQuery<Doctor> query = em.createQuery("FROM Doctor d WHERE d.id IN (:doctorIds)", Doctor.class);
        query.setParameter("doctorIds", doctorIds);
        System.out.println("restult" + query.getResultList());
        return query.getResultList();
    }

    @Override
    public int countWithFilters(long specialtyId, long coverageId, List<Integer> weekdays, String orderBy, String direction) {
        Query nativeQuery = em.createNativeQuery("SELECT COUNT(*) FROM doctors d " +
                "JOIN users u ON d.doctor_id = u.id "+
                "JOIN doctor_specialties ds ON d.doctor_id = ds.doctor_id " +
                "JOIN doctor_coverages dc ON d.doctor_id = dc.doctor_id " +
                "JOIN doctor_availability a ON d.doctor_id = a.doctor_id " +
                "WHERE ds.specialty_id = :specialtyId AND dc.coverage_id = :coverageId AND a.day_of_week IN (:weekdays) AND u.is_verified = true")
                        .setParameter("specialtyId", specialtyId)
                        .setParameter("coverageId", coverageId)
                .setParameter("weekdays", weekdays);
        return ((Number) nativeQuery.getSingleResult()).intValue();
    }

    @Override
    public Optional<Doctor> getByVerificationToken(String token) {
        return Optional.empty();
    }

    @Override
    public Optional<Doctor> getByResetToken(String token) {
        return Optional.empty();
    }

    @Override
    public int countAll() {
        return 0;
    }



}
