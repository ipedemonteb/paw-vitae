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
import java.util.List;
import java.util.Optional;

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
//        Query nativeQuery = em.createNativeQuery("SELECT d.doctor_id FROM doctors d " +
//                        "JOIN doctor_specialties ds ON d.doctor_id = ds.doctor_id " +
//                        "JOIN specialties s ON ds.specialty_id = s.id " +
//                        "JOIN doctor_coverages dc ON d.doctor_id = dc.doctor_id " +
//                        "JOIN coverages c ON dc.coverage_id = c.id " +
//                        "WHERE s.id = :specialtyId AND c.id = :coverageId")
//                .setParameter("specialtyId", specialtyId)
//                .setParameter("coverageId", coverageId)
////        Query nativeQuery = em.createNativeQuery("SELECT d.doctor_id FROM doctors d")
//                .setFirstResult((page - 1) * pageSize)
//                .setMaxResults(pageSize);
//        List<Long> filteredIds = ((List<Object>) nativeQuery.getResultList())
//                .stream()
//                .map(result -> ((Number) result).longValue())
//                .toList();
//        TypedQuery<Doctor> query = em.createQuery("FROM Doctor d WHERE d.id IN :ids", Doctor.class)
//                .setParameter("ids", filteredIds);
//        return query.getResultList();
        return List.of();
    }

    @Override
    public int countWithFilters(long specialtyId, long coverageId, List<Integer> weekdays, String orderBy, String direction) {
        return 3;
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
