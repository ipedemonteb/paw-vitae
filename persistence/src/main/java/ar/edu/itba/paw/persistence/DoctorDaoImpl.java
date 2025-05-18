//package ar.edu.itba.paw.persistence;
//
//import ar.edu.itba.paw.interfacePersistence.DoctorDao;
//import ar.edu.itba.paw.models.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
//import org.springframework.stereotype.Repository;
//
//import javax.sql.DataSource;
//import java.util.*;
//
//@Repository
//public class DoctorDaoImpl implements DoctorDao {
//
//    private final JdbcTemplate jdbcTemplate;
//    private final SimpleJdbcInsert jdbcInsertDoctor;
//    private final SimpleJdbcInsert jdbcInsertDoctorCoverage;
//    private final SimpleJdbcInsert jdbcInsertDoctorSpecialty;
//
//    private final static String BASE_SQL = "SELECT u.id AS doctor_id, u.name AS doctor_name, u.last_name AS doctor_last_name, u.email AS doctor_email, u.password AS doctor_password, u.phone AS doctor_phone, u.language AS doctor_language, u.is_verified AS doctor_verified, d.rating AS rating, d.rating_count AS rating_count, d.image_id AS image_id, s.id AS specialty_id, s.key AS specialty_key, c.id AS coverage_id, c.coverage_name AS coverage_name, a.day_of_week AS day_of_week, a.start_time AS start_time, a.end_time AS end_time FROM users u JOIN doctors d ON u.id = d.doctor_id LEFT JOIN doctor_specialties ds ON ds.doctor_id = u.id LEFT JOIN specialties s ON s.id = ds.specialty_id LEFT JOIN doctor_coverages dc ON dc.doctor_id = u.id LEFT JOIN coverages c ON c.id = dc.coverage_id LEFT JOIN doctor_availability a ON a.doctor_id = u.id ";
//
//    @Autowired
//    public DoctorDaoImpl(final DataSource dataSource) {
//        jdbcTemplate = new JdbcTemplate(dataSource);
//        jdbcInsertDoctor = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName("doctors")
//                .usingColumns("doctor_id", "image_id");
//        jdbcInsertDoctorCoverage = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName("doctor_coverages")
//                .usingColumns("doctor_id", "coverage_id");
//        jdbcInsertDoctorSpecialty = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName("doctor_specialties")
//                .usingColumns("doctor_id", "specialty_id");
//    }
//
//    private static List<Object> getObjects(long specialtyId, long coverageId, List<Integer> weekdays, StringBuilder sql) {
//        List<Object> params = new ArrayList<>();
//
//        if (specialtyId > 0) {
//            sql.append("AND u.id IN (SELECT doctor_id FROM doctor_specialties where specialty_id = ?) ");
//            params.add(specialtyId);
//        }
//
//        if (coverageId > 0) {
//            sql.append("AND u.id IN (SELECT doctor_id FROM doctor_coverages where coverage_id = ?) ");
//            params.add(coverageId);
//        }
//
//        if (!weekdays.isEmpty()) {
//            sql.append("AND u.id IN (SELECT doctor_id FROM doctor_availability WHERE ");
//            for (int i = 0; i < weekdays.size(); i++) {
//                sql.append("day_of_week = ? ");
//                if (i < weekdays.size() - 1) {
//                    sql.append("OR ");
//                } else {
//                    sql.append(") ");
//                }
//                params.add(weekdays.get(i));
//            }
//        }
//        return params;
//    }
//
//    @Override
//    public Doctor create(long id, String name, String lastName, String email, String password, String phone, String language, Long imageId, List<Long> specialties, List<Long> coverages) {
//        final Map<String, Object> argsDoctor = new HashMap<>();
//        argsDoctor.put("doctor_id", id);
//        argsDoctor.put("image_id", imageId);
//
//        jdbcInsertDoctor.execute(argsDoctor);
//
//        for (Long specialty : specialties) {
//            final Map<String, Object> args = new HashMap<>();
//            args.put("doctor_id", id);
//            args.put("specialty_id", specialty);
//            jdbcInsertDoctorSpecialty.execute(args);
//        }
//
//        for (Long coverage : coverages) {
//            final Map<String, Object> args = new HashMap<>();
//            args.put("doctor_id", id);
//            args.put("coverage_id", coverage);
//            jdbcInsertDoctorCoverage.execute(args);
//        }
//
//        return new Doctor(
//                name,
//                id,
//                lastName,
//                email,
//                password,
//                phone,
//                language,
//                imageId != null ? imageId : -1L,
//                false
//        );
//    }
//
//    @Override
//    public Optional<Doctor> getById(long id) {
//        StringBuilder sql = new StringBuilder(BASE_SQL);
//        sql.append("WHERE u.id = ?");
//        return Optional.ofNullable(jdbcTemplate.query(sql.toString(), new DoctorExtractor(), id)).orElse(Collections.emptyList()).stream().findFirst();
//    }
//
//    @Override
//    public Optional<Doctor> getByEmail(String email) {
//        StringBuilder sql = new StringBuilder(BASE_SQL);
//        sql.append("WHERE u.email = ?");
//        return Optional.ofNullable(jdbcTemplate.query(sql.toString(), new DoctorExtractor(), email)).orElse(Collections.emptyList()).stream().findFirst();
//    }
//
//    @Override
//    public void UpdateDoctorRating(long id, long newRating) {
//        StringBuilder sql = new StringBuilder();
//        sql.append("UPDATE doctors ")
//                .append("SET rating = ((COALESCE(rating, 0) * rating_count + ?) / (rating_count + 1)), ")
//                .append("    rating_count = rating_count + 1 ")
//                .append("WHERE doctor_id = ?");
//
//        jdbcTemplate.update(sql.toString(), newRating, id);
//    }
//
//    @Override
//    public List<Doctor> getBySpecialty(long specialtyId, int page, int pageSize) {
//        StringBuilder sql = new StringBuilder(BASE_SQL);
//        sql.append("WHERE u.id IN (SELECT doctor_id FROM users u JOIN doctor_specialties ds ON u.id = ds.doctor_id WHERE ds.specialty_id = ? AND u.is_verified = true LIMIT ? OFFSET ?)");
//        return jdbcTemplate.query(sql.toString(), new DoctorExtractor(), specialtyId, pageSize, (page - 1) * pageSize);
//    }
//
//    @Override
//    public void updateDoctor(long id, List<Long> specialties, List<Long> coverages) {
//
//        jdbcTemplate.update("DELETE FROM doctor_specialties WHERE doctor_id = ?", id);
//        StringBuilder specialtyBuilder = new StringBuilder("INSERT INTO doctor_specialties (doctor_id, specialty_id) VALUES ");
//        List<Object> specialtyParams = new ArrayList<>();
//        for (int i = 0; i < specialties.size(); i++) {
//            specialtyBuilder.append("(?, ?)");
//            if (i < specialties.size() - 1) {
//                specialtyBuilder.append(", ");
//            }
//            specialtyParams.add((int) id);
//            specialtyParams.add(specialties.get(i));
//        }
//        jdbcTemplate.update(specialtyBuilder.toString(), specialtyParams.toArray());
//
//        jdbcTemplate.update("DELETE FROM doctor_coverages WHERE doctor_id = ?", id);
//
//        StringBuilder coverageBuilder = new StringBuilder("INSERT INTO doctor_coverages (doctor_id, coverage_id) VALUES ");
//        List<Object> coverageParams = new ArrayList<>();
//        for (int i = 0; i < coverages.size(); i++) {
//            coverageBuilder.append("(?, ?)");
//            if (i < coverages.size() - 1) {
//                coverageBuilder.append(", ");
//            }
//            coverageParams.add((int) id);
//            coverageParams.add(coverages.get(i));
//        }
//        jdbcTemplate.update(coverageBuilder.toString(), coverageParams.toArray());
//    }
//
//    @Override
//    public int countBySpecialty(long specialtyId) {
//        return jdbcTemplate.queryForObject(
//                "SELECT COUNT(*) FROM doctor_specialties join users u on u.id = doctor_specialties.doctor_id WHERE specialty_id = ? AND u.is_verified = true",
//                Integer.class,
//                specialtyId
//        );
//    }
//
//    @Override
//    public List<Doctor> getWithFilters(long specialtyId, long coverageId, List<Integer> weekdays, String orderBy, String direction, int page, int pageSize) {
//        StringBuilder sql = new StringBuilder(BASE_SQL);
//
//        String orderByQuery = switch (orderBy) {
//            case "name" -> "name";
//            case "last_name" -> "last_name";
//            case "rating" -> "rating";
//            case "email" -> "email";
//            default -> "doctor_id";
//        };
//
//        String directionQuery = (direction.equals("desc")) ? "DESC" : "ASC";
//
//        long specialtyIdQuery = specialtyId > 0 ? specialtyId : 0;
//
//        long coverageIdQuery = coverageId > 0 ? coverageId : 0;
//
//        List<Integer> weekdaysQuery = new ArrayList<>();
//
//        for (Integer weekday : weekdays) {
//            if (weekday >= 0 && weekday < 7) {
//                weekdaysQuery.add(weekday);
//            }
//        }
//
//        sql.append("WHERE u.id IN (SELECT doctor_id FROM users u JOIN doctors d ON u.id = d.doctor_id WHERE u.is_verified = true ");
//        List<Object> params = getObjects(specialtyIdQuery, coverageIdQuery, weekdaysQuery, sql);
//        sql.append("ORDER BY ").append(orderByQuery).append(" ").append(directionQuery).append(" LIMIT ? OFFSET ?) ORDER BY ").append(orderByQuery).append(" ").append(directionQuery);
//        params.add(pageSize);
//        params.add((page - 1) * pageSize);
//        return jdbcTemplate.query(sql.toString(), new DoctorExtractor(), params.toArray());
//    }
//
//    @Override
//    public int countWithFilters(long specialtyId, long coverageId, List<Integer> weekdays, String orderBy, String direction) {
//        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM users u JOIN doctors d ON u.id = d.doctor_id WHERE 1=1 AND u.is_verified = true ");
//        List<Object> params = getObjects(specialtyId, coverageId, weekdays, sql);
//
//        return jdbcTemplate.queryForObject(sql.toString(), Integer.class, params.toArray());
//    }
//
//    @Override
//    public Optional<Doctor> getByVerificationToken(String token) {
//        StringBuilder sql = new StringBuilder(BASE_SQL);
//        sql.append("WHERE u.verification_token = ?");
//        return Optional.ofNullable(jdbcTemplate.query(sql.toString(), new DoctorExtractor(), token)).orElse(Collections.emptyList()).stream().findFirst();
//    }
//
//    @Override
//    public Optional<Doctor> getByResetToken(String token) {
//        StringBuilder sql = new StringBuilder(BASE_SQL);
//        sql.append("WHERE u.reset_token = ?");
//        return Optional.ofNullable(jdbcTemplate.query(sql.toString(), new DoctorExtractor(), token)).orElse(Collections.emptyList()).stream().findFirst();
//    }
//    @Override
//    public void updateImage(long id, Long imageId) {
//        jdbcTemplate.update("UPDATE doctors SET image_id = ? WHERE doctor_id = ?", imageId, id);
//    }
//
//    @Override
//    public int countAll() {
//        Integer count = jdbcTemplate.queryForObject(
//                "SELECT COUNT(*) FROM users u JOIN doctors d ON u.id = d.doctor_id WHERE u.is_verified = true",
//                Integer.class
//        );
//        return count != null ? count : 0;
//    }
//}
