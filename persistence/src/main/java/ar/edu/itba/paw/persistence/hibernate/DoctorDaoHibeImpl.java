package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Specialty;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

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
        return em.createQuery("SELECT d FROM Doctor d JOIN d.specialtyList s WHERE s.id = :specialtyId", Doctor.class)
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

    @Override
    public List<Doctor> getWithFilters(long specialtyId, long coverageId, List<Integer> weekdays, String orderBy, String direction, int page, int pageSize) {
        return null;
    }

    @Override
    public int countWithFilters(long specialtyId, long coverageId, List<Integer> weekdays, String orderBy, String direction) {
        return 0;
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
