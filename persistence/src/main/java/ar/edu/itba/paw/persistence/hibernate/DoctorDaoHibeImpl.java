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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class DoctorDaoHibeImpl implements DoctorDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Doctor create(String name, String lastName, String email, String password, String phone, String language, Long imageId, List<Specialty> specialties, List<Coverage> coverages) {
        Doctor doctor = new Doctor(name, lastName, email, password, phone, language, imageId, false,specialties, coverages);
        em.persist(doctor);
        return doctor;
    }

    @Override
    public Optional<Doctor> getById(long id) {
        return Optional.ofNullable(em.find(Doctor.class, id));
    }

    @Override
    public List<Doctor> getBySpecialty(long specialtyId, int page, int pageSize) {
        Query nativeQuery = getNativeQuery(specialtyId, 0, null, "name", "asc",  false); //TODO aggalan said not to remove this unused method, therefore i need to pass placeholders as arguments, any objections you may have take them up with him
        nativeQuery.setFirstResult((page-1) * pageSize);
        nativeQuery.setMaxResults(pageSize);

        List<?> doctorIdsRaw = nativeQuery.getResultList();
        List<Long> doctorIds = doctorIdsRaw.stream().map(id -> ((Number) id).longValue()).collect(Collectors.toList());
        if (doctorIds.isEmpty()) {
            return new ArrayList<>();
        }
        TypedQuery<Doctor> query = em.createQuery("FROM Doctor d WHERE d.id IN (:doctorIds)", Doctor.class);
        query.setParameter("doctorIds", doctorIds);

        return query.getResultList();
    }

    @Override
    public int countBySpecialty(long specialtyId) {
        Query nativeQuery = getNativeQuery(specialtyId, 0, null, "name", "asc",true);
        return ((Number) nativeQuery.getSingleResult()).intValue();
    }

    @Override
    public Optional<Doctor> getByEmail(String email) {
        TypedQuery<Doctor> query = em.createQuery("FROM Doctor d WHERE d.email = :email", Doctor.class);
        query.setParameter("email", email);
        List<Doctor> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());
    }

    private String orderBySwitch(String orderBy) {
        return switch (orderBy) {
            case "last_name" -> "d.last_name";
            case "rating" -> "d.rating";
            case "email" -> "d.email";
            default -> "d.name";
        };
    }

    private String directionSwitch(String direction) {
        if (direction.equals("desc")) {
            return "DESC";
        }
        return "ASC";
    }

    @Override
    public List<Doctor> getWithFilters(long specialtyId, long coverageId, List<Integer> weekdays, String orderBy, String direction, int page, int pageSize) {
        Query nativeQuery = getNativeQuery(specialtyId, coverageId, weekdays, orderBy, direction, false);
        nativeQuery.setFirstResult((page-1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<?> doctorIdsRaw = nativeQuery.getResultList();
        List<Long> doctorIds = doctorIdsRaw.stream().map(id -> ((Number) id).longValue()).collect(Collectors.toList());
        TypedQuery<Doctor> query = em.createQuery("FROM Doctor d WHERE d.id IN (:doctorIds) order by " + orderBySwitch(orderBy) + " " + directionSwitch(direction), Doctor.class);
        query.setParameter("doctorIds", doctorIds);

        return query.getResultList();
    }

    @Override
    public int countWithFilters(long specialtyId, long coverageId, List<Integer> weekdays, String orderBy, String direction) {
        Query nativeQuery = getNativeQuery(specialtyId, coverageId, weekdays, orderBy, direction, true);
        return ((Number) nativeQuery.getSingleResult()).intValue();
    }


    private static StringBuilder getSql(
            long specialtyId,
            long coverageId,
            List<Integer> weekdays,
            String orderBy,
            String direction,
            boolean isCount
    ) {
        StringBuilder sql = new StringBuilder();

        if (isCount) {
            // just count distinct IDs
            sql.append("SELECT COUNT(DISTINCT d.doctor_id) ");
        } else {
            // return the one column you need
            sql.append("SELECT d.doctor_id ");
        }

        // your joins & filters
        sql.append("""
        FROM doctors d
        JOIN users u            ON d.doctor_id = u.id
        JOIN doctor_specialties ds ON d.doctor_id = ds.doctor_id
        JOIN doctor_coverages dc ON d.doctor_id = dc.doctor_id
        JOIN doctor_availability a ON d.doctor_id = a.doctor_id
        WHERE u.is_verified = true
        """);

        if (specialtyId > 0) {
            sql.append(" AND ds.specialty_id = :specialtyId");
        }
        if (coverageId > 0) {
            sql.append(" AND dc.coverage_id   = :coverageId");
        }
        if (weekdays != null && !weekdays.isEmpty()) {
            sql.append(" AND a.day_of_week   IN (:weekdays)");
        }

        if (!isCount) {
            // GROUP BY doctor_id to get exactly one row per doctor
            sql.append(" GROUP BY d.doctor_id")
                    .append(" ORDER BY ");

            // pick the right aggregate for your ORDER BY
            switch (orderBy) {
                case "last_name" -> sql.append("MIN(u.last_name) ");
                case "rating"    -> sql.append("MIN(d.rating) ");
                case "email"     -> sql.append("MIN(u.email) ");
                default          -> sql.append("MIN(u.name) ");
            }

            // direction
            if ("desc".equalsIgnoreCase(direction)) {
                sql.append("DESC");
            } else {
                sql.append("ASC");
            }
        }

        return sql;
    }

    private Query getNativeQuery(long specialtyId, long coverageId, List<Integer> weekdays, String orderBy, String direction, boolean isCount) {
        StringBuilder sql = getSql(specialtyId, coverageId, weekdays, orderBy, direction, isCount);
        Query nativeQuery = em.createNativeQuery(sql.toString());

        if (specialtyId > 0) {
            nativeQuery.setParameter("specialtyId", specialtyId);
        }
        if (coverageId > 0) {
            nativeQuery.setParameter("coverageId", coverageId);
        }
        if (weekdays != null && !weekdays.isEmpty()) {
            nativeQuery.setParameter("weekdays", weekdays.stream().filter(w -> w >= 0 && w < 7).collect(Collectors.toList()));
        }
        return nativeQuery;
    }

    @Override
    public Optional<Doctor> getByVerificationToken(String token) {
        TypedQuery<Doctor> query = em.createQuery("FROM Doctor d WHERE d.verificationToken = :token", Doctor.class);
        query.setParameter("token", token);
        List<Doctor> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());
    }

    @Override
    public Optional<Doctor> getByResetToken(String token) {
        TypedQuery<Doctor> query = em.createQuery("FROM Doctor d WHERE d.resetToken = :token", Doctor.class);
        query.setParameter("token", token);
        List<Doctor> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());
    }

    @Override
    public int countAll() {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(d) FROM Doctor d WHERE d.verified = true", Long.class);
        return query.getSingleResult().intValue();
    }


    // -------------------------------------
    //  DEPRECATED METHODS
    // -------------------------------------

    //DEPRECATED
    @Override
    public void UpdateDoctorRating(long id, long rating) {
    }

    //DEPRECATED
    @Override
    public void updateDoctor(long id, List<Long> specialties, List<Long> coverages) {
    }

    // DEPRECATED
    @Override
    public void updateImage(long id, Long imageId) {
    }

}
